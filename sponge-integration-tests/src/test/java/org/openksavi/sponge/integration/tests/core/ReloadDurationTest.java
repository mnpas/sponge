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

package org.openksavi.sponge.integration.tests.core;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

import org.openksavi.sponge.core.engine.DefaultSpongeEngine;
import org.openksavi.sponge.engine.SpongeEngine;

public class ReloadDurationTest {

    @Test
    public void testReloadDuration() throws InterruptedException {
        SpongeEngine engine = DefaultSpongeEngine.builder().knowledgeBase("kb", "examples/core/reload_duration.py").build();
        engine.startup();

        try {
            await().atMost(30, TimeUnit.SECONDS).until(() -> engine.getOperations().getVariable(AtomicBoolean.class, "endTest").get());

            assertFalse(engine.getOperations().getVariable(AtomicBoolean.class, "ruleAFired").get());
            assertFalse(engine.getOperations().getVariable(AtomicBoolean.class, "ruleBFired").get());
            assertTrue(engine.getOperations().getVariable(AtomicBoolean.class, "ruleCFired").get());

            assertFalse(engine.isError());
        } finally {
            engine.shutdown();
        }
    }
}
