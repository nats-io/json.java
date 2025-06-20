// Copyright 2025 The NATS Authors
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at:
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package io.nats.json;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to build a JsonValue for a map
 */
public class MapBuilder implements JsonSerializable {

    /**
     * The JsonValue backing this MapBuilder
     */
    @NotNull
    public final JsonValue jv;

    /**
     * Get a new instance of MapBuilder
     */
    public MapBuilder() {
        jv = new JsonValue(new HashMap<>());
    }

    /**
     * Get an instance of MapBuilder
     * @return a MapBuilder instance
     */
    @NotNull
    public static MapBuilder instance() {
        return new MapBuilder();
    }

    /**
     * Put an object in the map. The value is converted to a JsonValue if it isn't one already.
     * @param key the key
     * @param value the value
     * @return the builder
     */
    @NotNull
    public MapBuilder put(@NotNull String key, @Nullable Object value) {
        //noinspection DataFlowIssue // NO ISSUE, WE KNOW jv.map is NOT NULL
        jv.map.put(key, JsonValue.instance(value));
        //noinspection DataFlowIssue // NO ISSUE, WE KNOW jv.mapOrder is NOT NULL
        jv.mapOrder.add(key);
        return this;
    }

    /**
     * Put all entries from the source map into the MapBuilder.
     * All values are converted to a JsonValue if they aren't one already.
     * @param map the map
     * @return the builder
     */
    @NotNull
    public MapBuilder putEntries(@Nullable Map<String, ?> map) {
        if (map != null) {
            for (String key : map.keySet()) {
                //noinspection DataFlowIssue // NO ISSUE, WE KNOW jv.map is NOT NULL
                jv.map.put(key, JsonValue.instance(map.get(key)));
                jv.mapOrder.add(key);
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public String toJson() {
        return jv.toJson();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public JsonValue toJsonValue() {
        return jv;
    }
}
