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

import org.openksavi.sponge.event.Event;
import org.openksavi.sponge.java.JKnowledgeBase;
import org.openksavi.sponge.java.JTrigger;

/**
 * Sponge Knowledge base. Hello world.
 */
public class TriggersHelloWorld extends JKnowledgeBase {

    public static class HelloWorld extends JTrigger {

        @Override
        public void onConfigure() {
            withEvent("helloEvent");
        }

        @Override
        public void onRun(Event event) {
            System.out.println(event.<String>get("say"));
        }
    }

    @Override
    public void onStartup() {
        getSponge().event("helloEvent").set("say", "Hello World!").send();
    }
}
