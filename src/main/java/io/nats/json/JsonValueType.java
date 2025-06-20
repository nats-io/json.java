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

/**
 * Possible types of the underlying value
 */
public enum JsonValueType {
    /**
     * Type for a JsonValue of a Java String
     */
    STRING,

    /**
     * Type for a JsonValue of a Java boolean
     */
    BOOL,

    /**
     * Type for a JsonValue of a Java int
     */
    INTEGER,

    /**
     * Type for a JsonValue of a Java long
     */
    LONG,

    /**
     * Type for a JsonValue of a Java double
     */
    DOUBLE,

    /**
     * Type for a JsonValue of a Java float
     */
    FLOAT,

    /**
     * Type for a JsonValue of a Java BigDecimal
     */
    BIG_DECIMAL,

    /**
     * Type for a JsonValue of a Java BigInteger
     */
    BIG_INTEGER,

    /**
     * Type for a JsonValue of an object (map)
     */
    MAP,

    /**
     * Type for a JsonValue of an array
     */
    ARRAY,

    /**
     * Type for a JsonValue of a null
     */
    NULL;
}
