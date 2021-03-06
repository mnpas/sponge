/*
 * Copyright 2016-2020 The Sponge authors.
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

package org.openksavi.sponge.remoteapi.feature.converter;

import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.openksavi.sponge.remoteapi.feature.converter.unit.GeoMapFeatureUnitConverter;
import org.openksavi.sponge.remoteapi.feature.converter.unit.GeoPositionFeatureUnitConverter;
import org.openksavi.sponge.remoteapi.feature.converter.unit.IconFeatureUnitConverter;
import org.openksavi.sponge.remoteapi.feature.converter.unit.SubActionFeatureUnitConverter;
import org.openksavi.sponge.remoteapi.feature.converter.unit.SubActionFeaturesUnitConverter;

/**
 * A default feature converter.
 */
public class DefaultFeatureConverter extends BaseFeatureConverter {

    public DefaultFeatureConverter(ObjectMapper objectMapper) {
        super(objectMapper);

        // Register default unit converters.
        registerAll(Arrays.asList(new SubActionFeatureUnitConverter(), new SubActionFeaturesUnitConverter(), new IconFeatureUnitConverter(),
                new GeoMapFeatureUnitConverter(), new GeoPositionFeatureUnitConverter()));
    }
}
