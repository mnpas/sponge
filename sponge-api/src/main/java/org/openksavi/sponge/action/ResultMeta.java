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

package org.openksavi.sponge.action;

import org.openksavi.sponge.type.DataType;

/**
 * An action result metadata.
 */
@SuppressWarnings("rawtypes")
public class ResultMeta<T extends DataType> {

    /** The result data type. */
    private T type;

    /** The result label. */
    private String label;

    /** The result description. */
    private String description;

    public ResultMeta(T type) {
        this.type = type;
    }

    public ResultMeta<T> withLabel(String label) {
        this.label = label;
        return this;
    }

    public ResultMeta<T> withDescription(String description) {
        this.description = description;
        return this;
    }

    public T getType() {
        return type;
    }

    public void setType(T type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
