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

package org.openksavi.sponge.trigger;

import org.openksavi.sponge.EventProcessor;
import org.openksavi.sponge.event.Event;

/**
 * Trigger.
 */
public interface Trigger extends EventProcessor<TriggerAdapter>, TriggerOperations {

    /**
     * A callback method that informs whether the specified event is to be accepted.
     *
     * @param event event.
     * @return {@code true} if the specified event is to be accepted.
     */
    boolean onAccept(Event event);

    /**
     * A callback method that runs this trigger.
     *
     * @param event event.
     */
    void onRun(Event event);
}
