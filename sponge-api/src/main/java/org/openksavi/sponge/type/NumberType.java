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

package org.openksavi.sponge.type;

import java.util.Map;

public class NumberType extends Type<Number> {

    private Number minValue;

    private Number maxValue;

    private boolean exclusiveMin = false;

    private boolean exclusiveMax = false;

    public NumberType() {
        super(TypeKind.NUMBER);
    }

    @Override
    public NumberType format(String format) {
        return (NumberType) super.format(format);
    }

    @Override
    public NumberType tags(String... tags) {
        return (NumberType) super.tags(tags);
    }

    @Override
    public NumberType tag(String tag) {
        return (NumberType) super.tag(tag);
    }

    @Override
    public NumberType features(Map<String, Object> features) {
        return (NumberType) super.features(features);
    }

    @Override
    public NumberType feature(String name, Object value) {
        return (NumberType) super.feature(name, value);
    }

    @Override
    public NumberType defaultValue(Number value) {
        return (NumberType) super.defaultValue(value);
    }

    @Override
    public NumberType nullable(boolean nullable) {
        return (NumberType) super.nullable(nullable);
    }

    public NumberType minValue(Number minValue) {
        setMinValue(minValue);
        return this;
    }

    public NumberType maxValue(Number maxValue) {
        setMaxValue(maxValue);
        return this;
    }

    public NumberType exclusiveMin(boolean exclusiveMin) {
        setExclusiveMin(exclusiveMin);
        return this;
    }

    public NumberType exclusiveMax(boolean exclusiveMax) {
        setExclusiveMax(exclusiveMax);
        return this;
    }

    public Number getMinValue() {
        return minValue;
    }

    public void setMinValue(Number minValue) {
        this.minValue = minValue;
    }

    public Number getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Number maxValue) {
        this.maxValue = maxValue;
    }

    public boolean isExclusiveMin() {
        return exclusiveMin;
    }

    public void setExclusiveMin(boolean exclusiveMin) {
        this.exclusiveMin = exclusiveMin;
    }

    public boolean isExclusiveMax() {
        return exclusiveMax;
    }

    public void setExclusiveMax(boolean exclusiveMax) {
        this.exclusiveMax = exclusiveMax;
    }
}
