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

package org.openksavi.sponge.features.model.geo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openksavi.sponge.SpongeException;
import org.openksavi.sponge.util.HasFeatures;

/**
 * A map.
 */
public class GeoMap implements HasFeatures, Serializable, Cloneable {

    private static final long serialVersionUID = 659974545540192399L;

    private GeoPosition center;

    private Double zoom;

    private Double minZoom;

    private Double maxZoom;

    private GeoCrs crs;

    private List<GeoLayer> layers = new ArrayList<>();

    private Map<String, Object> features = new LinkedHashMap<>();

    public GeoMap() {
    }

    public GeoMap withCenter(GeoPosition center) {
        setCenter(center);
        return this;
    }

    public GeoMap withZoom(Double zoom) {
        setZoom(zoom);
        return this;
    }

    public GeoMap withMinZoom(Double minZoom) {
        setMinZoom(minZoom);
        return this;
    }

    public GeoMap withMaxZoom(Double maxZoom) {
        setMaxZoom(maxZoom);
        return this;
    }

    public GeoMap withCrs(GeoCrs crs) {
        setCrs(crs);
        return this;
    }

    public GeoMap withLayers(List<GeoLayer> layers) {
        this.layers.addAll(layers);
        return this;
    }

    public GeoMap withLayer(GeoLayer layer) {
        layers.add(layer);
        return this;
    }

    public GeoMap withFeatures(Map<String, Object> features) {
        this.features.putAll(features);
        return this;
    }

    public GeoMap withFeature(String name, Object value) {
        features.put(name, value);
        return this;
    }

    public GeoPosition getCenter() {
        return center;
    }

    public void setCenter(GeoPosition center) {
        this.center = center;
    }

    public Double getZoom() {
        return zoom;
    }

    public void setZoom(Double zoom) {
        this.zoom = zoom;
    }

    public Double getMinZoom() {
        return minZoom;
    }

    public void setMinZoom(Double minZoom) {
        this.minZoom = minZoom;
    }

    public Double getMaxZoom() {
        return maxZoom;
    }

    public void setMaxZoom(Double maxZoom) {
        this.maxZoom = maxZoom;
    }

    public GeoCrs getCrs() {
        return crs;
    }

    public void setCrs(GeoCrs crs) {
        this.crs = crs;
    }

    public List<GeoLayer> getLayers() {
        return layers;
    }

    public void setLayers(List<GeoLayer> layers) {
        this.layers = layers;
    }

    @Override
    public Map<String, Object> getFeatures() {
        return features;
    }

    @Override
    public void setFeatures(Map<String, Object> features) {
        this.features = features;
    }

    @Override
    public GeoMap clone() {
        try {
            GeoMap cloned = (GeoMap) super.clone();
            cloned.center = center != null ? center.clone() : null;
            cloned.crs = crs != null ? crs.clone() : null;
            cloned.layers = layers != null ? layers.stream().map(layer -> layer.clone()).collect(Collectors.toList()) : null;
            cloned.features = features != null ? new LinkedHashMap<>(features) : null;

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new SpongeException(e);
        }
    }
}
