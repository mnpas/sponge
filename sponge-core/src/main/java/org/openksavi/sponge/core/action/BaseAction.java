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

package org.openksavi.sponge.core.action;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.openksavi.sponge.action.Action;
import org.openksavi.sponge.action.ActionAdapter;
import org.openksavi.sponge.action.IsActionActiveContext;
import org.openksavi.sponge.action.ProvideArgsContext;
import org.openksavi.sponge.core.BaseProcessor;
import org.openksavi.sponge.type.DataType;
import org.openksavi.sponge.type.VoidType;

@SuppressWarnings("rawtypes")
public abstract class BaseAction extends BaseProcessor<ActionAdapter> implements Action {

    @Override
    public void onConfigure() {
        //
    }

    @Override
    public BaseActionMeta getMeta() {
        return (BaseActionMeta) super.getMeta();
    }

    @Override
    public final ActionAdapter createAdapter() {
        return new BaseActionAdapter(new BaseActionDefinition());
    }

    @Override
    public boolean onIsActive(IsActionActiveContext context) {
        return true;
    }

    @Override
    public void onProvideArgs(ProvideArgsContext context) {
        // The default implementation doesn't modify the provided map.
    }

    @Override
    public BaseAction withName(String name) {
        return (BaseAction) super.withName(name);
    }

    @Override
    public BaseAction withLabel(String label) {
        return (BaseAction) super.withLabel(label);
    }

    @Override
    public BaseAction withDescription(String description) {
        return (BaseAction) super.withDescription(description);
    }

    @Override
    public BaseAction withVersion(Integer version) {
        return (BaseAction) super.withVersion(version);
    }

    @Override
    public BaseAction withFeatures(Map<String, Object> features) {
        return (BaseAction) super.withFeatures(features);
    }

    @Override
    public BaseAction withFeature(String name, Object value) {
        return (BaseAction) super.withFeature(name, value);
    }

    @Override
    public BaseAction withCategory(String category) {
        return (BaseAction) super.withCategory(category);
    }

    public BaseAction withArgs(List<DataType> argTypes) {
        getMeta().addArgs(argTypes);
        return this;
    }

    public BaseAction withArg(DataType argType) {
        return withArgs(Arrays.asList(argType));
    }

    public BaseAction withNoArgs() {
        return withArgs(Collections.emptyList());
    }

    public BaseAction withResult(DataType resultType) {
        getMeta().setResult(resultType);
        return this;
    }

    public BaseAction withNoResult() {
        return withResult(new VoidType());
    }

    public BaseAction withCallable(boolean callable) {
        getMeta().setCallable(callable);
        if (!callable && getMeta().getResult() == null) {
            withNoResult();
        }
        return this;
    }

    public BaseAction withNonCallable() {
        return withCallable(false);
    }

    public BaseAction withActivatable() {
        getMeta().setActivatable(true);
        return this;
    }
}
