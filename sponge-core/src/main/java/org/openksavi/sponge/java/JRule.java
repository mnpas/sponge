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

package org.openksavi.sponge.java;

import java.util.function.Function;

import org.openksavi.sponge.core.rule.BaseRule;
import org.openksavi.sponge.core.rule.CompositeEventCondition;
import org.openksavi.sponge.core.rule.ReflectionEventCondition;
import org.openksavi.sponge.rule.EventCondition;

/**
 * Java-specific implementation of the rule. As a condition supports a method name or a direct instance of EventCondition.
 */
public abstract class JRule extends BaseRule {

    private static final Function<? super Object, ? extends EventCondition> MAPPER = javaObject -> {
        if (javaObject instanceof String) {
            return new ReflectionEventCondition((String) javaObject);
        } else if (javaObject instanceof EventCondition) {
            return (EventCondition) javaObject;
        } else {
            throw new IllegalArgumentException("Incorrect condition type: " + javaObject.getClass());
        }
    };

    /**
     * Adds event conditions.
     *
     * @param eventAlias event alias.
     * @param javaObjects conditions (e.g. method names or direct EventCondition).
     */
    public void addConditions(String eventAlias, Object... javaObjects) {
        addJavaConditions(eventAlias, new CompositeEventCondition(MAPPER, javaObjects));
    }

    /**
     * Adds event conditions for all events.
     *
     * @param javaObjects conditions (e.g. method names or direct EventCondition).
     */
    public void addAllConditions(Object... javaObjects) {
        addAllJavaConditions(new CompositeEventCondition(MAPPER, javaObjects));
    }

    /**
     * Adds an event condition.
     *
     * @param eventAlias an event alias.
     * @param javaObject an event condition in a general form.
     */
    public void addCondition(String eventAlias, Object javaObject) {
        addJavaCondition(eventAlias, MAPPER.apply(javaObject));
    }
}