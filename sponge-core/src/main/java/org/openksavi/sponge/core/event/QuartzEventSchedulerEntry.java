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

import org.quartz.JobKey;

import org.openksavi.sponge.event.Event;
import org.openksavi.sponge.event.EventSchedulerEntry;

public class QuartzEventSchedulerEntry implements EventSchedulerEntry {

    private static final long serialVersionUID = 7609940705260150180L;

    private JobKey id;

    private Event event;

    public QuartzEventSchedulerEntry(JobKey id, Event event) {
        this.id = id;
        this.event = event;
    }

    @Override
    public JobKey getId() {
        return id;
    }

    @Override
    public Event getEvent() {
        return event;
    }
}
