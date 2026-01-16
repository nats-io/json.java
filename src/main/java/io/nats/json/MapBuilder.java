// Copyright 2025-2026 Synadia Communications, Inc.
//
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

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to build a JsonValue for a map
 */
public class MapBuilder implements JsonSerializable {

    /**
     * The JsonValue backing this MapBuilder
     */
    @NonNull
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
    @NonNull
    public static MapBuilder instance() {
        return new MapBuilder();
    }

    /**
     * Put an object in the map. The value is converted to a JsonValue if it isn't one already.
     * @param key the key
     * @param value the value
     * @return the builder
     */
    @NonNull
    public MapBuilder put(@NonNull String key, @Nullable Object value) {
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
    @NonNull
    public MapBuilder putEntries(@Nullable Map<String, ?> map) {
        if (map != null) {
            for (Map.Entry<String, ?> entry : map.entrySet()) {
                String key = entry.getKey();
                //noinspection DataFlowIssue // NO ISSUE, WE KNOW jv.map is NOT NULL
                jv.map.put(key, JsonValue.instance(entry.getValue()));
                //noinspection DataFlowIssue // NO ISSUE, WE KNOW jv.mapOrder is NOT NULL
                jv.mapOrder.add(key);
            }
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public String toJson() {
        return jv.toJson();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NonNull
    public JsonValue toJsonValue() {
        return jv;
    }
}
