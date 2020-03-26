/*
 * Copyright 2016-2017 The Sponge authors.
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

package org.openksavi.sponge.core.event;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.openksavi.sponge.SpongeException;
import org.openksavi.sponge.core.util.SpongeUtils;
import org.openksavi.sponge.event.Event;
import org.openksavi.sponge.event.EventClonePolicy;

/**
 * Base event.
 */
public abstract class BaseEvent implements Event {

    private static final long serialVersionUID = -2269323477538266057L;

    /** Default event priority. Equals to {@code 0}. */
    public static final int DEFAULT_PRIORITY = 0;

    /** Event ID. */
    protected String id;

    /** Event creation time. */
    protected Instant time;

    /** Event name. */
    protected String name;

    /** Event clone policy. */
    protected EventClonePolicy clonePolicy;

    /** Event label. */
    protected String label;

    /** Event description. */
    protected String description;

    /** Event features as a map of names to values. */
    private Map<String, Object> features = Collections.synchronizedMap(new LinkedHashMap<>());

    /**
     * Creates a new event that has no ID and time set (both are 0).
     *
     * @param name an event name.
     * @param clonePolicy an event clone policy.
     */
    protected BaseEvent(String name, EventClonePolicy clonePolicy) {
        if (name == null) {
            throw new IllegalArgumentException("Event name cannot be null");
        }
        this.name = name;
        this.clonePolicy = clonePolicy;
    }

    /**
     * Returns event name.
     *
     * @return event name.
     */
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns event time.
     *
     * @return event time.
     */
    @Override
    public Instant getTime() {
        return time;
    }

    /**
     * Sets event time.
     *
     * @param time event time.
     */
    @Override
    public void setTime(Instant time) {
        this.time = time;
    }

    /**
     * Returns event ID.
     *
     * @return event ID.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Sets event ID.
     *
     * @param id event ID.
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns event priority. This is always {@code 0} for standard events and cannot be changed.
     *
     * @return event priority.
     */
    @Override
    public int getPriority() {
        return DEFAULT_PRIORITY;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    protected abstract <T> T doGet(String name, boolean useDefault, T defaultValue);

    protected final <T> T getDefaultAttributeValue(String name, boolean useDefault, T defaultValue) {
        if (useDefault) {
            return defaultValue;
        } else {
            throw new IllegalArgumentException("Unknown attribute " + name);
        }
    }

    /**
     * Returns the attribute value or throws {@code IllegalArgumentException} if it does't exist.
     *
     * @param name attribute name.
     * @return attribute value.
     * @param <T> attribute.
     */
    @Override
    public final <T> T get(String name) {
        return doGet(name, false, null);
        // return doGet(name, () -> {
        // throw new IllegalArgumentException("Unknown attribute " + name);
        // });
    }

    @Override
    public final <T> T get(Class<T> cls, String name) {
        return doGet(name, false, null);
    }

    @Override
    public final <T> T get(String name, T defaultValue) {
        return doGet(name, true, defaultValue);
    }

    @Override
    public Event set(Map<String, Object> attributes) {
        if (attributes != null) {
            // First, validate all attributes.
            attributes.forEach((name, value) -> validateAttribute(name, value));

            attributes.forEach((name, value) -> set(name, value));
        }
        return this;
    }

    /**
     * Compares events by ID.
     *
     * @param event compared event.
     * @return {@code true} if both events are equal by ID.
     */
    @Override
    public boolean equalsById(Event event) {
        return event != null && Objects.equals(id, event.getId());
    }

    /**
     * Compares events by attributes.
     *
     * @param event compared event.
     * @return {@code true} if both events are equal by attributes.
     */
    @Override
    public boolean equalsByAttributes(Event event) {
        if (event == null) {
            return false;
        }

        if (event == this) {
            return true;
        }

        return Objects.equals(name, event.getName()) && getAll().equals(event.getAll());
    }

    protected void validateAttribute(String name, Object value) {
        if (clonePolicy == EventClonePolicy.DEEP && !(value instanceof Serializable)) {
            throw new SpongeException("Not serializable attribute can't be deep cloned.");
        }
    }

    @Override
    public Map<String, Object> getFeatures() {
        return features;
    }

    @Override
    public void setFeatures(Map<String, Object> features) {
        if (features != null) {
            features.forEach((name, value) -> validateFeature(name, value));
        }

        this.features = features != null ? Collections.synchronizedMap(new LinkedHashMap<>(features)) : null;
    }

    @Override
    public void addFeature(String name, Object value) {
        validateFeature(name, value);

        features.put(name, value);
    }

    @Override
    public void addFeatures(Map<String, Object> features) {
        if (features != null) {
            features.forEach((name, value) -> validateFeature(name, value));
            features.putAll(features);
        }
    }

    protected void validateFeature(String name, Object value) {
        if (clonePolicy == EventClonePolicy.DEEP && !(value instanceof Serializable)) {
            throw new SpongeException("Not serializable feature can't be deep cloned.");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public BaseEvent clone() {
        try {
            BaseEvent cloned = (BaseEvent) super.clone();

            if (features != null) {
                switch (clonePolicy) {
                case SHALLOW:
                    cloned.setFeatures(features);
                    break;
                case DEEP:
                    cloned.setFeatures((Map<String, Object>) SpongeUtils.deepClone((Serializable) features));
                    break;
                default:
                    throw new SpongeException("Unsupported clone policy: " + clonePolicy);
                }
            }

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new SpongeException(e);
        }
    }

    protected final String toStringBase() {
        return name + ", id=" + id + ", time=" + time + ", priority=" + getPriority();
    }

    @Override
    public String toString() {
        return toStringBase() + ", attributes=" + getAll();
    }
}
