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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A data type. Used for example in action arguments metadata.
 */
public class Type<T> {

    private TypeKind kind;

    private String format;

    private Map<String, Object> features = new LinkedHashMap<>();

    private T defaultValue;

    /** Tells if a value of this type may be null. The default is that a value must not be null, i.e. it is <b>not nullable</b>. */
    private boolean nullable = false;

    @SuppressWarnings("unused")
    private Type() {
        //
    }

    protected Type(TypeKind kind) {
        this.kind = kind;
    }

    public Type<T> format(String format) {
        setFormat(format);
        return this;
    }

    public Type<T> features(Map<String, Object> features) {
        this.features.putAll(features);
        return this;
    }

    public Type<T> feature(String name, Object value) {
        features.put(name, value);
        return this;
    }

    public Type<T> defaultValue(T value) {
        setDefaultValue(value);
        return this;
    }

    public Type<T> nullable(boolean nullable) {
        setNullable(nullable);
        return this;
    }

    public TypeKind getKind() {
        return kind;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Map<String, Object> getFeatures() {
        return features;
    }

    public void setFeatures(Map<String, Object> features) {
        this.features = features;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
}
