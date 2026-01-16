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

/**
 * Interface used when adding a generic list
 *
 * @param <T> the type of list
 */
public interface ListValueResolver<T> {
    /**
     * Whether the object in question is appendable. A null object would
     * not be appendable, hence the default implementation
     *
     * @param t the object
     * @return true for appendable
     */
    default boolean appendable(@Nullable T t) {
        return t != null;
    }

    /**
     * Append the object's JSON value representation
     *
     * @param sb the target StringBuilder
     * @param t  the object
     */
    void append(@NonNull StringBuilder sb, @Nullable T t);
}
