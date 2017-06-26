/*
 * Copyright 2016-2017 Softelnet.
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

package org.openksavi.sponge.core.spi;

import org.openksavi.sponge.core.engine.event.NullEventQueue;
import org.openksavi.sponge.core.engine.event.PriorityEventQueue;
import org.openksavi.sponge.core.engine.event.SynchroEventQueue;
import org.openksavi.sponge.engine.event.EventQueue;
import org.openksavi.sponge.spi.EventQueueProvider;

/**
 * Default EventQueueProvider.
 */
public class DefaultEventQueueProvider implements EventQueueProvider {

    @Override
    public EventQueue getInputQueue() {
        return new PriorityEventQueue("InputQueue");
    }

    @Override
    public EventQueue getMainQueue() {
        return new SynchroEventQueue("MainQueue");
    }

    @Override
    public EventQueue getOutputQueue() {
        return new NullEventQueue();
    }
}
