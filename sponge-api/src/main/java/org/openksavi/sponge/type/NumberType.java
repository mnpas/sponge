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

/**
 * A number type, that include both integer and floating-point numbers.
 */
public class NumberType extends DataType<Number> {

    /** The minimum value (optional). */
    private Number minValue;

    /** The maximum value (optional). */
    private Number maxValue;

    /** Tells if the minimum value should be exclusive. Defaults to {@code false}. */
    private boolean exclusiveMin = false;

    /** Tells if the maximum value should be exclusive. Defaults to {@code false}. */
    private boolean exclusiveMax = false;

    public NumberType() {
        super(DataTypeKind.NUMBER);
    }

    @Override
    public NumberType withFormat(String format) {
        return (NumberType) super.withFormat(format);
    }

    @Override
    public NumberType withFeatures(Map<String, Object> features) {
        return (NumberType) super.withFeatures(features);
    }

    @Override
    public NumberType withFeature(String name, Object value) {
        return (NumberType) super.withFeature(name, value);
    }

    @Override
    public NumberType withDefaultValue(Number value) {
        return (NumberType) super.withDefaultValue(value);
    }

    @Override
    public NumberType withNullable(boolean nullable) {
        return (NumberType) super.withNullable(nullable);
    }

    public NumberType withMinValue(Number minValue) {
        setMinValue(minValue);
        return this;
    }

    public NumberType withMinValue(Number minValue, boolean exclusiveMin) {
        setMinValue(minValue);
        setExclusiveMin(exclusiveMin);
        return this;
    }

    public NumberType withMaxValue(Number maxValue) {
        setMaxValue(maxValue);
        return this;
    }

    public NumberType withMaxValue(Number maxValue, boolean exclusiveMax) {
        setMaxValue(maxValue);
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
