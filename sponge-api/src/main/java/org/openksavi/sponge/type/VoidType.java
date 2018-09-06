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

public class VoidType extends Type<Object> {

    public VoidType() {
        super(TypeKind.VOID);
    }

    @Override
    public VoidType format(String format) {
        return (VoidType) super.format(format);
    }

    @Override
    public VoidType tags(String... tags) {
        return (VoidType) super.tags(tags);
    }

    @Override
    public VoidType tag(String tag) {
        return (VoidType) super.tag(tag);
    }

    @Override
    public VoidType features(Map<String, Object> features) {
        return (VoidType) super.features(features);
    }

    @Override
    public VoidType feature(String name, Object value) {
        return (VoidType) super.feature(name, value);
    }

    @Override
    public VoidType defaultValue(Object value) {
        return (VoidType) super.defaultValue(value);
    }

    @Override
    public VoidType nullable(boolean nullable) {
        return (VoidType) super.nullable(nullable);
    }
}
