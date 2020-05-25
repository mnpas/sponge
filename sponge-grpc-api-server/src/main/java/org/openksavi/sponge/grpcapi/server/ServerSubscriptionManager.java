/*
 * Copyright 2016-2019 The Sponge authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openksavi.sponge.grpcapi.server;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.protobuf.Timestamp;

import io.grpc.StatusRuntimeException;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openksavi.sponge.core.util.SpongeUtils;
import org.openksavi.sponge.engine.SpongeEngine;
import org.openksavi.sponge.grpcapi.proto.Event;
import org.openksavi.sponge.grpcapi.proto.ObjectValue;
import org.openksavi.sponge.grpcapi.proto.ResponseHeader;
import org.openksavi.sponge.grpcapi.proto.SubscribeResponse;
import org.openksavi.sponge.remoteapi.feature.converter.FeaturesUtils;
import org.openksavi.sponge.remoteapi.server.RemoteApiService;
import org.openksavi.sponge.remoteapi.type.converter.TypeConverter;
import org.openksavi.sponge.type.RecordType;
import org.openksavi.sponge.util.SpongeApiUtils;

/**
 * A subscription manager.
 */
public class ServerSubscriptionManager {

    private static final Logger logger = LoggerFactory.getLogger(ServerSubscriptionManager.class);

    private SpongeEngine engine;

    private RemoteApiService remoteApiService;

    private AtomicLong currentSubscriptionId = new AtomicLong(0);

    private Map<Long, ServerSubscription> subscriptions = new ConcurrentHashMap<>();

    public ServerSubscriptionManager(SpongeEngine engine, RemoteApiService remoteApiService) {
        this.engine = engine;
        this.remoteApiService = remoteApiService;
    }

    public SpongeEngine getEngine() {
        return engine;
    }

    public void setEngine(SpongeEngine engine) {
        this.engine = engine;
    }

    public RemoteApiService getRemoteApiService() {
        return remoteApiService;
    }

    public void setRemoteApiService(RemoteApiService remoteApiService) {
        this.remoteApiService = remoteApiService;
    }

    public AtomicLong getCurrentSubscriptionId() {
        return currentSubscriptionId;
    }

    public Map<Long, ServerSubscription> getSubscriptions() {
        return subscriptions;
    }

    public long createNewSubscriptionId() {
        return currentSubscriptionId.incrementAndGet();
    }

    public ServerSubscription getSubscription(long subscriptionId) {
        return subscriptions.get(subscriptionId);
    }

    public void putSubscription(ServerSubscription subscription) {
        subscriptions.put(Validate.notNull(subscription.getId(), "The subscription should have the id"), subscription);
    }

    public ServerSubscription removeSubscription(long subscriptionId) {
        return subscriptions.remove(subscriptionId);
    }

    protected boolean eventMatchesSubscription(org.openksavi.sponge.event.Event event, ServerSubscription subscription) {
        boolean hasEventType = engine.hasEventType(event.getName());
        return subscription.isActive()
                && subscription.getEventNames().stream()
                        .anyMatch(eventNamePattern -> engine.getPatternMatcher().matches(eventNamePattern, event.getName()))
                && (!subscription.isRegisteredTypeRequired() || hasEventType)
                // Check subscribe privileges for the event instance.
                && remoteApiService.getAccessService().canSubscribeEvent(subscription.getUserContext(), event.getName());
    }

    public void pushEvent(org.openksavi.sponge.event.Event event) {
        subscriptions.values().forEach(subscription -> {
            if (eventMatchesSubscription(event, subscription)) {
                try {
                    synchronized (subscription.getResponseObserver()) {
                        subscription.getResponseObserver().onNext(createSubscribeResponse(subscription, event));
                    }
                } catch (StatusRuntimeException e) {
                    if (!e.getStatus().isOk()) {
                        logger.debug("Setting subscription {} as inactive because the status code is {}", subscription.getId(),
                                e.getStatus().getCode());
                        subscription.setActive(false);
                    } else {
                        logger.error("pushEvent() StatusRuntimeException", e);
                    }
                } catch (Throwable e) {
                    logger.error("pushEvent() error", e);
                }
            }
        });

        // Cleanup inactive subscriptions.
        // TODO Move cleanup somewhere else.
        List<Long> inactiveSubscriptionIds = subscriptions.values().stream().filter(subscription -> !subscription.isActive())
                .map(subscription -> subscription.getId()).collect(Collectors.toList());
        inactiveSubscriptionIds.forEach(subscriptions::remove);
    }

    protected SubscribeResponse createSubscribeResponse(ServerSubscription subscription, org.openksavi.sponge.event.Event event) {
        ResponseHeader.Builder headerBuilder = ResponseHeader.newBuilder();

        return SubscribeResponse.newBuilder().setHeader(headerBuilder.build()).setSubscriptionId(subscription.getId())
                .setEvent(createEvent(event)).build();
    }

    protected Event createEvent(org.openksavi.sponge.event.Event event) {
        Event.Builder eventBuilder = Event.newBuilder();

        if (event.getId() != null) {
            eventBuilder.setId(event.getId());
        }
        if (event.getName() != null) {
            eventBuilder.setName(event.getName());
        }
        eventBuilder.setPriority(event.getPriority());
        if (event.getTime() != null) {
            eventBuilder.setTime(Timestamp.newBuilder().setSeconds(event.getTime().getEpochSecond()).setNanos(event.getTime().getNano()));
        }
        if (event.getLabel() != null) {
            eventBuilder.setLabel(event.getLabel());
        }
        if (event.getDescription() != null) {
            eventBuilder.setDescription(event.getDescription());
        }

        Map<String, Object> attributes = event.getAll();
        if (attributes != null) {
            TypeConverter typeConverter = remoteApiService.getTypeConverter();
            ObjectValue.Builder attributesValueBuilder = ObjectValue.newBuilder();

            try {
                Map<String, Object> transportAttributes = attributes;

                // Marshal attributes if an event has a registered type.
                if (engine.hasEventType(event.getName())) {
                    RecordType eventType = engine.getEventType(event.getName());

                    transportAttributes = SpongeApiUtils.collectToLinkedMap(attributes, entry -> entry.getKey(),
                            entry -> typeConverter.marshal(eventType.getFieldType(entry.getKey()), entry.getValue()));
                }

                attributesValueBuilder.setValueJson(typeConverter.getObjectMapper().writeValueAsString(transportAttributes));
            } catch (JsonProcessingException e) {
                throw SpongeUtils.wrapException(e);
            }

            eventBuilder.setAttributes(attributesValueBuilder.build());
        }

        Map<String, Object> features = event.getFeatures();
        if (features != null) {
            TypeConverter typeConverter = remoteApiService.getTypeConverter();
            ObjectValue.Builder featuresValueBuilder = ObjectValue.newBuilder();

            try {
                featuresValueBuilder.setValueJson(typeConverter.getObjectMapper()
                        .writeValueAsString(FeaturesUtils.marshal(typeConverter.getFeatureConverter(), features)));
            } catch (JsonProcessingException e) {
                throw SpongeUtils.wrapException(e);
            }

            eventBuilder.setFeatures(featuresValueBuilder.build());
        }

        return eventBuilder.build();
    }
}
