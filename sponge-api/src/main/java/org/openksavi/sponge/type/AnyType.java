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

import org.openksavi.sponge.type.provided.ProvidedMeta;

/**
 * Any type. It may be used in situations when type is not important.
 */
public class AnyType extends DataType<Object> {

    public AnyType() {
        this(null);
    }

    public AnyType(String name) {
        super(DataTypeKind.ANY, name);
    }

    @Override
    public AnyType withName(String name) {
        return (AnyType) super.withName(name);
    }

    @Override
    public AnyType withLabel(String label) {
        return (AnyType) super.withLabel(label);
    }

    @Override
    public AnyType withDescription(String description) {
        return (AnyType) super.withDescription(description);
    }

    @Override
    public AnyType withAnnotated(boolean annotated) {
        return (AnyType) super.withAnnotated(annotated);
    }

    @Override
    public AnyType withAnnotated() {
        return (AnyType) super.withAnnotated();
    }

    @Override
    public AnyType withFormat(String format) {
        return (AnyType) super.withFormat(format);
    }

    @Override
    public AnyType withFeatures(Map<String, Object> features) {
        return (AnyType) super.withFeatures(features);
    }

    @Override
    public AnyType withFeature(String name, Object value) {
        return (AnyType) super.withFeature(name, value);
    }

    @Override
    public AnyType withDefaultValue(Object value) {
        return (AnyType) super.withDefaultValue(value);
    }

    @Override
    public AnyType withNullable(boolean nullable) {
        return (AnyType) super.withNullable(nullable);
    }

    @Override
    public AnyType withNullable() {
        return (AnyType) super.withNullable();
    }

    @Override
    public AnyType withReadOnly(boolean readOnly) {
        return (AnyType) super.withReadOnly(readOnly);
    }

    @Override
    public AnyType withReadOnly() {
        return (AnyType) super.withReadOnly();
    }

    @Override
    public AnyType withOptional() {
        return (AnyType) super.withOptional();
    }

    @Override
    public AnyType withProvided(ProvidedMeta provided) {
        return (AnyType) super.withProvided(provided);
    }
}
