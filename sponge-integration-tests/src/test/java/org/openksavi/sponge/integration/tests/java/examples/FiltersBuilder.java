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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.openksavi.sponge.event.Event;
import org.openksavi.sponge.java.JFilterBuilder;
import org.openksavi.sponge.java.JKnowledgeBase;
import org.openksavi.sponge.java.JTrigger;

/**
 * Sponge Knowledge Base. Using filter builders.
 */
public class FiltersBuilder extends JKnowledgeBase {

    @Override
    public void onInit() {
        // Variables for assertions only
        Map<String, AtomicInteger> eventCounter = Collections.synchronizedMap(new HashMap<>());
        eventCounter.put("blue", new AtomicInteger(0));
        eventCounter.put("red", new AtomicInteger(0));
        getSponge().setVariable("eventCounter", eventCounter);
    }

    public static class ColorTrigger extends JTrigger {

        @Override
        public void onConfigure() {
            withEvent("e1");
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onRun(Event event) {
            getLogger().debug("Received event {}", event);
            ((Map<String, AtomicInteger>) getSponge().getVariable("eventCounter")).get(event.get("color")).incrementAndGet();
        }
    }

    @Override
    public void onLoad() {
        getSponge().enable(new JFilterBuilder("ColorFilter").withEvent("e1").withOnAccept((filter, event) -> {
            getLogger().debug("Received event {}", event);
            String color = event.get("color", null);
            if (color == null || !color.equals("blue")) {
                getLogger().debug("rejected");
                return false;
            } else {
                getLogger().debug("accepted");
                return true;
            }
        }));
    }

    @Override
    public void onStartup() {
        getSponge().event("e1").send();
        getSponge().event("e1").set("color", "red").send();
        getSponge().event("e1").set("color", "blue").send();
    }
}
