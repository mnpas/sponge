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

package org.openksavi.sponge.integration.tests.java.examples;

import java.time.Duration;

import org.openksavi.sponge.event.Event;
import org.openksavi.sponge.examples.util.CorrelationEventsLog;
import org.openksavi.sponge.java.JKnowledgeBase;
import org.openksavi.sponge.java.JRule;

/**
 * Sponge Knowledge Base. Using rules - synchronous and asynchronous.
 */
public class RulesSyncAsync extends JKnowledgeBase {

    @Override
    public void onInit() {
        // Variables for assertions only
        getSponge().setVariable("correlationEventsLog", new CorrelationEventsLog());
    }

    public static class RuleFFF extends JRule {

        @Override
        public void onConfigure() {
            withEvents("e1", "e2", "e3 :first").withSynchronous(true);
        }

        @Override
        public void onRun(Event event) {
            getLogger().debug("Running rule for event: {}", event != null ? event.getName() : null);
            getSponge().getVariable(CorrelationEventsLog.class, "correlationEventsLog").addEvents(getMeta().getName(), this);
        }
    }

    public static class RuleFFL extends JRule {

        @Override
        public void onConfigure() {
            withEvents("e1", "e2", "e3 :last").withDuration(Duration.ofSeconds(2)).withSynchronous(false);
        }

        @Override
        public void onRun(Event event) {
            getLogger().debug("Running rule for event: {}", event != null ? event.getName() : null);
            getSponge().getVariable(CorrelationEventsLog.class, "correlationEventsLog").addEvents(getMeta().getName(), this);
        }
    }

    @Override
    public void onStartup() {
        getSponge().event("e1").set("label", "1").send();
        getSponge().event("e2").set("label", "2").send();
        getSponge().event("e2").set("label", "3").send();
        getSponge().event("e2").set("label", "4").send();
        getSponge().event("e3").set("label", "5").send();
        getSponge().event("e3").set("label", "6").send();
        getSponge().event("e3").set("label", "7").send();
    }
}
