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

package org.openksavi.sponge.core.trigger;

import org.openksavi.sponge.core.BaseEventProcessorAdapter;
import org.openksavi.sponge.core.BaseProcessorDefinition;
import org.openksavi.sponge.engine.ProcessorType;
import org.openksavi.sponge.trigger.Trigger;
import org.openksavi.sponge.trigger.TriggerAdapter;

/**
 * Base trigger adapter.
 */
public class BaseTriggerAdapter extends BaseEventProcessorAdapter<Trigger> implements TriggerAdapter {

    public BaseTriggerAdapter(BaseProcessorDefinition definition) {
        super(definition);
    }

    @Override
    public ProcessorType getType() {
        return ProcessorType.TRIGGER;
    }

    @Override
    public BaseTriggerMeta getMeta() {
        return (BaseTriggerMeta) super.getMeta();
    }
}
