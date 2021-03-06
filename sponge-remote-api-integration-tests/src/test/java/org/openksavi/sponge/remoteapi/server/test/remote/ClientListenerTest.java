/*
 * Copyright 2016-2018 The Sponge authors.
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

package org.openksavi.sponge.remoteapi.server.test.remote;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.RegExUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.openksavi.sponge.remoteapi.client.BaseSpongeClient;
import org.openksavi.sponge.remoteapi.client.DefaultSpongeClient;
import org.openksavi.sponge.remoteapi.client.SpongeClientConfiguration;
import org.openksavi.sponge.remoteapi.client.SpongeRequestContext;
import org.openksavi.sponge.remoteapi.model.request.GetVersionRequest;

@Execution(ExecutionMode.SAME_THREAD)
@net.jcip.annotations.NotThreadSafe
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { ClientListenerTest.TestConfig.class })
@DirtiesContext
public class ClientListenerTest extends BasicTestTemplate {

    protected BaseSpongeClient createlient() {
        return new DefaultSpongeClient(SpongeClientConfiguration.builder().url(String.format("http://localhost:%d", port)).build());
    }

    private String normalizeJson(String json) {
        return RegExUtils.removeAll(json, "\\s");
    }

    @Test
    public void testGlobalListeners() {
        final List<String> requestStringList = new ArrayList<>();
        final List<String> responseStringList = new ArrayList<>();

        try (BaseSpongeClient client = createlient()) {
            client.addOnRequestSerializedListener((request, requestString) -> requestStringList.add(requestString));
            client.addOnResponseDeserializedListener((request, response, responseString) -> responseStringList.add(responseString));

            client.getVersion();
            String version = client.getVersion();
            client.getVersion();

            // Works only if the API version is not set manually in the service.
            assertEquals(engine.getVersion(), version);

            assertEquals(3, requestStringList.size());
            assertEquals(3, responseStringList.size());
            assertEquals("{\"jsonrpc\":\"2.0\",\"method\":\"version\",\"params\":{},\"id\":\"1\"}",
                    normalizeJson(requestStringList.get(0)));
            assertTrue(normalizeJson(responseStringList.get(0)).matches(
                    "\\{\"jsonrpc\":\"2.0\",\"result\":\\{\"header\":\\{\"requestTime\":\".*\",\"responseTime\":\".*\"},\"value\":\".*\"\\},\"id\":\"1\"\\}"));
        }
    }

    @Test
    public void testOneRequestListeners() {
        final AtomicReference<String> requestStringHolder = new AtomicReference<>();
        final AtomicReference<String> responseStringHolder = new AtomicReference<>();

        try (BaseSpongeClient client = createlient()) {
            client.getVersion();

            SpongeRequestContext context = SpongeRequestContext.builder()
                    .onRequestSerializedListener((request, requestString) -> requestStringHolder.set(requestString))
                    .onResponseDeserializedListener((request, response, responseString) -> responseStringHolder.set(responseString))
                    .build();
            String version = client.getVersion(new GetVersionRequest(), context).getResult().getValue();

            // Works only if the API version is not set manually in the service.
            assertEquals(engine.getVersion(), version);

            client.getVersion();

            assertEquals("{\"jsonrpc\":\"2.0\",\"method\":\"version\",\"params\":{},\"id\":\"2\"}",
                    normalizeJson(requestStringHolder.get()));
            assertTrue(normalizeJson(responseStringHolder.get()).matches(
                    "\\{\"jsonrpc\":\"2.0\",\"result\":\\{\"header\":\\{\"requestTime\":\".*\",\"responseTime\":\".*\"},\"value\":\".*\"\\},\"id\":\"2\"\\}"));
        }
    }
}
