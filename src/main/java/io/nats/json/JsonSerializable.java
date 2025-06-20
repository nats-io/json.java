// Copyright 2021-2025 The NATS Authors
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

import java.nio.charset.StandardCharsets;

/**
 * Interface for objects that can automatically render as JSON
 */
public interface JsonSerializable {

    /**
     * Get the String version of the JSON object
     * @return the string
     */
    @NotNull
    String toJson();

    /**
     * Get the byte[] version of the JSON object
     * The built-in default implementation uses the toJson() and converts it to a string.
     * @return the byte array
     */
    default byte @NotNull [] serialize() {
        return toJson().getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Get the JsonValue version of the JSON object
     * The built-in default implementation uses the toJson() and parse it to a JsonValue.
     * It assumes that you have valid JSON
     * @return the JsonValue
     */
    @NotNull
    default JsonValue toJsonValue() {
        return JsonParser.parseUnchecked(toJson());
    }
}
