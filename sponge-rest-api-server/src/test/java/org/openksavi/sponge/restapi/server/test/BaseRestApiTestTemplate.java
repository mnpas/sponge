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

package org.openksavi.sponge.restapi.server.test;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import org.openksavi.sponge.core.util.SpongeUtils;
import org.openksavi.sponge.engine.SpongeEngine;
import org.openksavi.sponge.restapi.RestApiConstants;
import org.openksavi.sponge.restapi.client.ErrorResponseException;
import org.openksavi.sponge.restapi.client.IncorrectKnowledgeBaseVersionException;
import org.openksavi.sponge.restapi.client.SpongeRestClient;
import org.openksavi.sponge.restapi.model.RestActionMeta;
import org.openksavi.sponge.restapi.model.request.GetVersionRequest;
import org.openksavi.sponge.restapi.model.response.GetVersionResponse;
import org.openksavi.sponge.type.ActionType;
import org.openksavi.sponge.type.StringType;
import org.openksavi.sponge.type.TypeKind;

public abstract class BaseRestApiTestTemplate {

    @Inject
    protected SpongeEngine engine;

    @Inject
    @Named(PortTestConfig.PORT_BEAN_NAME)
    protected Integer port;

    protected abstract SpongeRestClient createRestClient();

    @Test
    public void testVersion() {
        try (SpongeRestClient client = createRestClient()) {
            assertEquals(engine.getVersion(), client.getVersion());
        }
    }

    @Test
    public void testVersionWithId() {
        try (SpongeRestClient client = createRestClient()) {
            client.getConfiguration().setUseRequestId(true);

            GetVersionRequest request = new GetVersionRequest();
            GetVersionResponse response = client.getVersion(request);

            assertEquals(null, response.getErrorCode());
            assertEquals(null, response.getErrorMessage());
            assertEquals(null, response.getDetailedErrorMessage());
            assertEquals(engine.getVersion(), response.getVersion());
            assertEquals("1", response.getId());
            assertEquals(response.getId(), request.getId());
        }
    }

    @Test
    public void testActions() {
        try (SpongeRestClient client = createRestClient()) {
            assertEquals(RestApiTestConstants.ANONYMOUS_ALL_ACTION_COUNT, client.getActions().size());
        }
    }

    @Test
    public void testActionsParamArgMetadataRequiredTrue() {
        try (SpongeRestClient client = createRestClient()) {
            assertEquals(RestApiTestConstants.ACTIONS_WITH_METADATA_COUNT, client.getActions(null, true).size());
        }
    }

    @Test
    public void testActionsParamArgMetadataRequiredFalse() {
        try (SpongeRestClient client = createRestClient()) {
            List<RestActionMeta> actions = client.getActions(null, false);

            assertEquals(RestApiTestConstants.ANONYMOUS_ALL_ACTION_COUNT, actions.size());
            RestActionMeta meta = actions.stream().filter(action -> action.getName().equals("UpperCase")).findFirst().get();
            assertEquals(TypeKind.STRING, meta.getArgsMeta().get(0).getType().getKind());
            assertTrue(meta.getArgsMeta().get(0).getType() instanceof StringType);
        }
    }

    @Test
    public void testActionsNameRegExp() {
        try (SpongeRestClient client = createRestClient()) {
            String nameRegExp = ".*Case";
            List<RestActionMeta> actions = client.getActions(nameRegExp);

            assertEquals(2, actions.size());
            assertTrue(actions.stream().allMatch(action -> action.getName().matches(nameRegExp)));
        }
    }

    @Test
    public void testActionsNameExact() {
        try (SpongeRestClient client = createRestClient()) {
            String name = "UpperCase";
            List<RestActionMeta> actions = client.getActions(name);

            assertEquals(1, actions.size());
            assertEquals(actions.get(0).getName(), name);
        }
    }

    @Test
    public void testGetActionMeta() {
        try (SpongeRestClient client = createRestClient()) {
            String actionName = "UpperCase";
            RestActionMeta actionMeta = client.getActionMeta(actionName);

            assertEquals(actionName, actionMeta.getName());
            assertEquals(1, actionMeta.getArgsMeta().size());
            assertTrue(actionMeta.getArgsMeta().get(0).getType() instanceof StringType);
            assertTrue(actionMeta.getResultMeta().getType() instanceof StringType);
        }
    }

    @Test
    public void testCall() {
        try (SpongeRestClient client = createRestClient()) {
            String arg1 = "test1";

            Object result = client.call("UpperCase", arg1);

            assertTrue(result instanceof String);
            assertEquals(arg1.toUpperCase(), result);

            await().atMost(30, TimeUnit.SECONDS).until(() -> engine.getOperations().getVariable(AtomicBoolean.class, "actionCalled").get());
            assertFalse(engine.isError());
        }
    }

    @Test(expected = IncorrectKnowledgeBaseVersionException.class)
    public void testCallWithWrongExpectedKnowledgeBaseVersion() {
        try (SpongeRestClient client = createRestClient()) {
            String arg1 = "test1";

            RestActionMeta actionMeta = client.getActionMeta("UpperCase");
            actionMeta.getKnowledgeBase().setVersion(2);

            try {
                client.call("UpperCase", arg1);
                fail("Exception expected");
            } finally {
                engine.clearError();
            }
        }
    }

    @Test
    public void testCallBinaryArgAndResult() throws IOException {
        try (SpongeRestClient client = createRestClient()) {
            byte[] image = IOUtils.toByteArray(getClass().getResourceAsStream("/image.png"));
            byte[] resultImage = client.call(byte[].class, "EchoImage", image);
            assertEquals(image.length, resultImage.length);
            assertArrayEquals(image, resultImage);
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCallWithActionTypeArg() {
        try (SpongeRestClient client = createRestClient()) {
            RestActionMeta actionMeta = client.getActionMeta("ActionTypeAction");
            List<String> values = client.call(List.class, ((ActionType) actionMeta.getArgsMeta().get(0).getType()).getActionName());
            assertEquals("value3", client.call("ActionTypeAction", values.get(values.size() - 1)));
        }
    }

    @Test
    public void testCallLanguageError() {
        try (SpongeRestClient client = createRestClient()) {
            try {
                client.call("LangErrorAction");
                fail("Exception expected");
            } catch (ErrorResponseException e) {
                assertEquals(RestApiConstants.DEFAULT_ERROR_CODE, e.getErrorCode());
                assertTrue(e.getErrorMessage().startsWith("NameError: global name 'throws_error' is not defined in"));
                assertTrue(e.getDetailedErrorMessage().startsWith(
                        "org.openksavi.sponge.engine.WrappedException: NameError: global name 'throws_error' is not defined in"));
            } catch (Throwable e) {
                fail("ResponseErrorSpongeException expected");
            } finally {
                engine.clearError();
            }
        }
    }

    @Test
    public void testCallKnowledgeBaseError() {
        try (SpongeRestClient client = createRestClient()) {
            try {
                client.call("KnowledgeBaseErrorAction");
                fail("Exception expected");
            } catch (ErrorResponseException e) {
                assertEquals(RestApiConstants.DEFAULT_ERROR_CODE, e.getErrorCode());
                assertTrue(e.getErrorMessage().startsWith("Exception: Knowledge base exception in"));
                assertTrue(e.getDetailedErrorMessage()
                        .startsWith("org.openksavi.sponge.engine.WrappedException: Exception: Knowledge base exception in"));
            } catch (Throwable e) {
                fail("ResponseErrorSpongeException expected");
            } finally {
                engine.clearError();
            }
        }
    }

    @Test
    public void testSend() {
        try (SpongeRestClient client = createRestClient()) {
            assertNotNull(client.send("alarm", SpongeUtils.immutableMapOf("attr1", "Test")));

            await().atMost(30, TimeUnit.SECONDS).until(() -> engine.getOperations().getVariable(AtomicBoolean.class, "eventSent").get());
            assertFalse(engine.isError());
        }
    }

    @Test
    public void testKnowledgeBases() {
        try (SpongeRestClient client = createRestClient()) {
            assertEquals(1, client.getKnowledgeBases().size());
        }
    }
}
