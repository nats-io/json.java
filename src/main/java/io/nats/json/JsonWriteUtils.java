// Copyright 2020-2025 The NATS Authors
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

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static io.nats.json.DateTimeUtils.DEFAULT_TIME;
import static io.nats.json.Encoding.jsonEncode;
import static io.nats.json.JsonValue.instance;

/**
 * Utility to help write correct JSON.
 */
public abstract class JsonWriteUtils {
    private static final String Q = "\"";
    private static final String QCOLONQ = "\":\"";
    private static final String QCOLON = "\":";
    private static final String QCOMMA = "\",";
    private static final String COMMA = ",";

    private JsonWriteUtils() {} /* ensures cannot be constructed */

    // ----------------------------------------------------------------------------------------------------
    // BUILD A STRING OF JSON
    // ----------------------------------------------------------------------------------------------------

    /**
     * Create StringBuilder with an open squiggly bracket {
     * @return the StringBuilder
     */
    @NotNull
    public static StringBuilder beginJson() {
        return new StringBuilder("{");
    }

    /**
     * Create StringBuilder with an open square bracket {
     * @return the StringBuilder
     */
    @NotNull
    public static StringBuilder beginArray() {
        return new StringBuilder("[");
    }

    /**
     * Create a StringBuilder with a custom prefix or an open squiggly bracket if a prefix is null
     * @param prefix the prefix
     * @return the StringBuilder
     */
    @NotNull
    public static StringBuilder beginJsonPrefixed(@Nullable String prefix) {
        return prefix == null ? beginJson()
            : new StringBuilder(prefix).append('{');
    }

    /**
     * End a JSON object string made with this utility by checking its current end characters
     * @param sb the StringBuilder
     * @return the StringBuilder
     */
    @NotNull
    public static StringBuilder endJson(@NotNull StringBuilder sb) {
        int lastIndex = sb.length() - 1;
        if (sb.charAt(lastIndex) == ',') {
            sb.setCharAt(lastIndex, '}');
            return sb;
        }
        sb.append("}");
        return sb;
    }


    /**
     * End a JSON array string made with this utility by checking its current end characters
     * @param sb the StringBuilder to append to
     * @return the StringBuilder
     */
    @NotNull
    public static StringBuilder endArray(@NotNull StringBuilder sb) {
        int lastIndex = sb.length() - 1;
        if (sb.charAt(lastIndex) == ',') {
            sb.setCharAt(lastIndex, ']');
            return sb;
        }
        sb.append("]");
        return sb;
    }

    /**
     * Create StringBuilder with an open squiggly bracket { and a system specific line separator
     * @return the StringBuilder
     */
    @NotNull
    public static StringBuilder beginFormattedJson() {
        return new StringBuilder("{" + System.lineSeparator() + "    ");
    }

    /**
     * End a StringBuilder of formatted json with the system specific line separator
     * @param sb the StringBuilder to append to
     * @return the string
     */
    @NotNull
    public static String endFormattedJson(@NotNull StringBuilder sb) {
        sb.setLength(sb.length()-1);
        return sb.append(System.lineSeparator())
            .append("}")
            .toString()
            .replaceAll(",", "," + System.lineSeparator() + "    ");
    }

    /**
     * Appends a field for a raw json value unless the json string is null or empty.
     * @param sb string builder
     * @param fieldName the field name
     * @param json raw JSON
     */
    public static void addRawJson(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable String json) {
        if (json != null && !json.isEmpty()) {
            sb.append(Q);
            jsonEncode(sb, fieldName);
            sb.append(QCOLON).append(json).append(COMMA);
        }
    }

    /**
     * Appends a JsonSerializable unless the value is null.
     * @param sb string builder
     * @param fieldName the field name
     * @param value JsonSerializable value
     */
    public static void addField(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable JsonSerializable value) {
        if (value != null) {
            sb.append(Q);
            jsonEncode(sb, fieldName);
            sb.append(QCOLON).append(value.toJson()).append(COMMA);
        }
    }

    /**
     * Appends a field for a String value unless the string is null or empty.
     * @param sb string builder
     * @param fieldName the field name
     * @param value field value
     */
    public static void addField(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable String value) {
        if (value != null && !value.isEmpty()) {
            sb.append(Q);
            jsonEncode(sb, fieldName);
            sb.append(QCOLONQ);
            jsonEncode(sb, value);
            sb.append(QCOMMA);
        }
    }

    /**
     * Appends a field for a String value. Empty and null string are added as value of empty string
     * @param sb string builder
     * @param fieldName the field name
     * @param value field value
     */
    public static void addFieldAlways(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable String value) {
        sb.append(Q);
        jsonEncode(sb, fieldName);
        sb.append(QCOLONQ);
        if (value != null && !value.isEmpty()) {
            jsonEncode(sb, value);
        }
        sb.append(QCOMMA);
    }

    /**
     * Appends a field for a Boolean value unless the value is null or false
     * @param sb string builder
     * @param fieldName the field name
     * @param value field value
     */
    public static void addField(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable Boolean value) {
        if (value != null && value) {
            sb.append(Q);
            jsonEncode(sb, fieldName);
            sb.append(QCOLON).append("true").append(COMMA);
        }
    }

    /**
     * Appends a field for a Boolean value. null is considered false
     * @param sb string builder
     * @param fieldName the field name
     * @param value field value
     */
    public static void addFieldAlways(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable Boolean value) {
        sb.append(Q);
        jsonEncode(sb, fieldName);
        sb.append(QCOLON).append(value != null && value ? "true" : "false").append(COMMA);
    }

    /**
     * Appends a field for an integer value unless the value is null or less than zero
     * @param sb string builder
     * @param fieldName the field name
     * @param value field value
     */
    public static void addField(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable Integer value) {
        if (value != null && value >= 0) {
            sb.append(Q);
            jsonEncode(sb, fieldName);
            sb.append(QCOLON).append(value).append(COMMA);
        }
    }

    /**
     * Appends a field for an integer value unless the value is null or less than one.
     * @param sb string builder
     * @param fieldName the field name
     * @param value field value
     */
    public static void addFieldWhenGtZero(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable Integer value) {
        if (value != null && value > 0) {
            sb.append(Q);
            jsonEncode(sb, fieldName);
            sb.append(QCOLON).append(value).append(COMMA);
        }
    }

    /**
     * Appends a field for an integer value unless the value is null or less than -1
     * @param sb string builder
     * @param fieldName the field name
     * @param value field value
     */
    public static void addFieldWhenGteMinusOne(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable Integer value) {
        if (value != null && value >= -1) {
            sb.append(Q);
            jsonEncode(sb, fieldName);
            sb.append(QCOLON).append(value).append(COMMA);
        }
    }

    /**
     * Appends a field for an integer value unless the value is less than or equal to the greater than value
     * @param sb string builder
     * @param fieldName the field name
     * @param value field value
     * @param gt the number the value must be greater than
     */
    public static void addFieldWhenGreaterThan(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable Integer value, int gt) {
        if (value != null && value > gt) {
            sb.append(Q);
            jsonEncode(sb, fieldName);
            sb.append(QCOLON).append(value).append(COMMA);
        }
    }

    /**
     * Appends a field for a long value unless the value is null or less than zero.
     * @param sb string builder
     * @param fieldName the field name
     * @param value field value
     */
    public static void addField(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable Long value) {
        if (value != null && value >= 0) {
            sb.append(Q);
            jsonEncode(sb, fieldName);
            sb.append(QCOLON).append(value).append(COMMA);
        }
    }

    /**
     * Appends a field for a long value unless the value is null or less than one.
     * @param sb string builder
     * @param fieldName the field name
     * @param value field value
     */
    public static void addFieldWhenGtZero(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable Long value) {
        if (value != null && value > 0) {
            sb.append(Q);
            jsonEncode(sb, fieldName);
            sb.append(QCOLON).append(value).append(COMMA);
        }
    }

    /**
     * Appends a field for a long value unless the value is null or less than -1
     * @param sb string builder
     * @param fieldName the field name
     * @param value field value
     */
    public static void addFieldWhenGteMinusOne(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable Long value) {
        if (value != null && value >= -1) {
            sb.append(Q);
            jsonEncode(sb, fieldName);
            sb.append(QCOLON).append(value).append(COMMA);
        }
    }

    /**
     * Appends a field for a long value unless the value is less than or equal to the greater than value
     * @param sb string builder
     * @param fieldName the field name
     * @param value field value
     * @param gt the number the value must be greater than
     */
    public static void addFieldWhenGreaterThan(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable Long value, long gt) {
        if (value != null && value > gt) {
            sb.append(Q);
            jsonEncode(sb, fieldName);
            sb.append(QCOLON).append(value).append(COMMA);
        }
    }

    /**
     * Appends a field for a Duration value unless the value is null, or is zero or negative, converting a Duration to nanoseconds
     * @param sb string builder
     * @param fieldName the field name
     * @param value duration value
     */
    public static void addFieldAsNanos(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable Duration value) {
        if (value != null && !value.isZero() && !value.isNegative()) {
            sb.append(Q);
            jsonEncode(sb, fieldName);
            sb.append(QCOLON).append(value.toNanos()).append(COMMA);
        }
    }

    /**
     * Appends a date/time as a rfc 3339 formatted field, unless the value is null or DEFAULT_TIME
     * @param sb string builder
     * @param fieldName the field name
     * @param zonedDateTime field value
     */
    public static void addField(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable ZonedDateTime zonedDateTime) {
        if (zonedDateTime != null && !DEFAULT_TIME.equals(zonedDateTime)) {
            sb.append(Q);
            jsonEncode(sb, fieldName);
            sb.append(QCOLONQ)
                .append(DateTimeUtils.toRfc3339(zonedDateTime))
                .append(QCOMMA);
        }
    }

    // ----------------------------------------------------------------------------------------------------
    // MAPS
    // ----------------------------------------------------------------------------------------------------
    /**
     * Appends a Map object unless the map is null or empty.
     * @param sb string builder
     * @param fieldName the field name
     * @param map the map
     */
    public static void addField(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable Map<String, ?> map) {
        if (map != null && !map.isEmpty()) {
            addField(sb, fieldName, instance(map));
        }
    }

    // ----------------------------------------------------------------------------------------------------
    // ARRAYS / LISTS
    // ----------------------------------------------------------------------------------------------------
    /**
     * Interface used when adding a generic list
     * @param <T> the type of list
     */
    public interface ListValueResolver<T> {
        /**
         * Whether the object in question is appendable. A null object would
         * not be appendable, hence the default implementation
         * @param t the object
         * @return true for appendable
         */
        default boolean appendable(T t) { return t != null; }

        /**
         * Append the object's json value representation
         * @param sb the target SringBuilder
         * @param t the object
         */
        void append(StringBuilder sb, T t);
    }

    /**
     * ListValueResolver implementation for a list of Strings
     */
    public static ListValueResolver<String> STRING_LIST_RESOLVER = new ListValueResolver<>() {
        @Override
        public boolean appendable(String s) {
            return s != null && !s.isEmpty();
        }

        @Override
        public void append(StringBuilder sb, String s) {
            sb.append(Q);
            jsonEncode(sb, s);
            sb.append(Q);
        }
    };

    /**
     * ListValueResolver implementation for a list of Integers
     */
    public static ListValueResolver<Integer> INT_LIST_RESOLVER = StringBuilder::append;

    /**
     * ListValueResolver implementation for a list of Longs
     */
    public static ListValueResolver<Long> LONG_LIST_RESOLVER = StringBuilder::append;

    /**
     * ListValueResolver implementation for a list of Durations
     */
    public static ListValueResolver<Duration> DURATION_LIST_RESOLVER = (sb, duration) -> sb.append(duration.toNanos());

    /**
     * Appends an array, unless the list is null or empty
     * @param sb string builder
     * @param fieldName the field name
     * @param collection the Collection
     * @param resolver the ListValueResolver
     * @param <T> the list type
     */
    public static <T> void addArray(@NotNull StringBuilder sb, @NotNull String fieldName, @NotNull Collection<T> collection, @NotNull JsonWriteUtils.ListValueResolver<T> resolver) {
        sb.append(Q);
        jsonEncode(sb, fieldName);
        sb.append("\":[");
        int i = 0;
        for (T t : collection) {
            if (resolver.appendable(t)) {
                if (i++ > 0) {
                    sb.append(COMMA);
                }
                resolver.append(sb, t);
            }
        }
        sb.append("],");
    }

    /**
     * Appends an array of strings as an array, unless the string array is null or empty
     * @param sb string builder
     * @param fieldName the field name
     * @param array field value
     */
    public static void addStrings(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable String... array) {
        if (array != null && array.length > 0) {
            addArray(sb, fieldName, Arrays.asList(array), STRING_LIST_RESOLVER);
        }
    }

    /**
     * Appends a list of strings as an array, unless the string list is null or empty
     * @param sb string builder
     * @param fieldName the field name
     * @param collection field value
     */
    public static void addStrings(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable Collection<String> collection) {
        if (collection != null && !collection.isEmpty()) {
            addArray(sb, fieldName, collection, STRING_LIST_RESOLVER);
        }
    }

    /**
     * Appends an array of Integers as an array, unless the list is null or empty
     * @param sb string builder
     * @param fieldName the field name
     * @param array field value
     */
    public static void addIntegers(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable Integer... array) {
        if (array != null && array.length > 0) {
            addArray(sb, fieldName, Arrays.asList(array), INT_LIST_RESOLVER);
        }
    }

    /**
     * Appends a list of Longs as an array, unless the list is null or empty
     * @param sb string builder
     * @param fieldName the field name
     * @param collection field value
     */
    public static void addIntegers(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable Collection<Integer> collection) {
        if (collection != null && !collection.isEmpty()) {
            addArray(sb, fieldName, collection, INT_LIST_RESOLVER);
        }
    }

    /**
     * Appends an array of Longs as an array, unless the list is null or empty
     * @param sb string builder
     * @param fieldName the field name
     * @param array field value
     */
    public static void addLongs(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable Long... array) {
        if (array != null && array.length > 0) {
            addArray(sb, fieldName, Arrays.asList(array), LONG_LIST_RESOLVER);
        }
    }

    /**
     * Appends a list of Longs as an array, unless the list is null or empty
     * @param sb string builder
     * @param fieldName the field name
     * @param collection field value
     */
    public static void addLongs(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable Collection<Long> collection) {
        if (collection != null && !collection.isEmpty()) {
            addArray(sb, fieldName, collection, LONG_LIST_RESOLVER);
        }
    }

    /**
     * Appends an array of JsonSerializable as an array, unless the list is null or empty
     * @param sb string builder
     * @param fieldName the field name
     * @param collection field value
     */
    public static void addJsons(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable Collection<? extends JsonSerializable> collection) {
        if (collection != null && !collection.isEmpty()) {
            addArray(sb, fieldName, collection, (sbs, js) -> sbs.append(js.toJson()));
        }
    }

    /**
     * Appends an array of Durations (as nanoseconds) as an array, unless the list is null or empty
     * @param sb string builder
     * @param fieldName the field name
     * @param durations list of durations
     */
    public static void addDurations(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable Collection<Duration> durations) {
        if (durations != null && !durations.isEmpty()) {
            addArray(sb, fieldName, durations, DURATION_LIST_RESOLVER);
        }
    }

    /**
     * Add the toString of an enum as the value of a field, unless the enum is null.
     * @param sb string builder
     * @param fieldName the field name
     * @param e the enum
     * @param <E> The enum type
     */
    public static <E extends Enum<E>> void addEnum(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable E e) {
        if (e != null) {
            addField(sb, fieldName, e.toString());
        }
    }

    /**
     * Add the toString of an enum as the value of a field, unless the enum is null
     * or the enum matches the supplied dontAddIfThis
     * @param sb string builder
     * @param fieldName the field name
     * @param e the enum
     * @param dontAddIfThis the enum not to match, may be null
     * @param <E> The enum type
     */
    public static <E extends Enum<E>> void addEnumWhenNot(@NotNull StringBuilder sb, @NotNull String fieldName, @Nullable E e, @Nullable E dontAddIfThis) {
        if (e != null && e != dontAddIfThis) {
            addField(sb, fieldName, e.toString());
        }
    }

    // ----------------------------------------------------------------------------------------------------
    // PRINT UTILS
    // ----------------------------------------------------------------------------------------------------

    /**
     * Get the key string portion of a json key value using the class simple name as the field name
     * @param c the class
     * @return the key string
     */
    @NotNull
    public static String toKey(@NotNull Class<?> c) {
        return Q + c.getSimpleName() + QCOLON;
    }

    /**
     * Get the key string portion of a json key value using the class simple name as the field name
     * @param fieldName the field name
     * @return the key string
     */
    @NotNull
    public static String toKey(@NotNull String fieldName) {
        return Q + fieldName + QCOLON;
    }

    private static final int INDENT_WIDTH = 4;
    private static final String INDENT = "                                        ";
    private static String indent(int level) {
        return level <= 0 ? "" : INDENT.substring(0, level * INDENT_WIDTH);
    }

    /**
     * Get a string formatted for easy reading. Assumes the object's toString returns valid json.
     * @param o the object
     * @return the formatted string
     */
    @NotNull
    public static String getFormatted(@NotNull Object o) {
        return getFormatted(o.toString());
    }

    /**
     * Get a string formatted for easy reading.
     * @param js a JsonSerializable object
     * @return the formatted string
     */
    @NotNull
    public static String getFormatted(@NotNull JsonSerializable js) {
        return getFormatted(js.toJson());
    }

    /**
     * Get a string formatted for easy reading. Assumes valid json
     * @param json the json string
     * @return the formatted string
     */
    @NotNull
    public static String getFormatted(@NotNull String json) {
        StringBuilder sb = new StringBuilder();
        boolean begin_quotes = false;

        boolean opened = false;
        int indentLevel = 0;
        String indent = "";
        for (int x = 0; x < json.length(); x++) {
            char c = json.charAt(x);

            if (c == '\"') {
                if (opened) {
                    sb.append(System.lineSeparator()).append(indent);
                    opened = false;
                }
                sb.append(c);
                begin_quotes = !begin_quotes;
                continue;
            }

            if (!begin_quotes) {
                switch (c) {
                    case '{':
                    case '[':
                        sb.append(c);
                        opened = true;
                        indent = indent(++indentLevel);
                        continue;
                    case '}':
                    case ']':
                        indent = indent(--indentLevel);
                        sb.append(System.lineSeparator()).append(indent);
                        sb.append(c);
                        opened = false;
                        continue;
                    case ':':
                        sb.append(c).append(" ");
                        continue;
                    case ',':
                        sb.append(c).append(System.lineSeparator()).append(indentLevel > 0 ? indent : "");
                        continue;
                    default:
                        if (Character.isWhitespace(c)) continue;
                        if (opened) {
                            sb.append(System.lineSeparator()).append(indent);
                            opened = false;
                        }
                }
            }

            sb.append(c).append(c == '\\' ? "" + json.charAt(++x) : "");
        }

        return sb.toString();
    }

    /**
     * Print the object to System.out, formatted for easy reading. Assumes the object's toString returns valid json.
     * @param o the object
     */
    public static void printFormatted(@NotNull Object o) {
        System.out.println(getFormatted(o));
    }

    /**
     * Print the json to System.out, formatted for easy reading
     * @param js a JsonSerializable object
     */
    public static void printFormatted(@NotNull JsonSerializable js) {
        System.out.println(getFormatted(js));
    }

    /**
     * Print the string to System.out, formatted for easy reading. Assumes valid json
     * @param json the json string
     */
    public static void printFormatted(@NotNull String json) {
        System.out.println(getFormatted(json));
    }

    // ----------------------------------------------------------------------------------------------------
    // SAFE NUMBER PARSING HELPERS
    // ----------------------------------------------------------------------------------------------------

    /**
     * Safely parse a long, returning the null if the string is null or does not parse
     * @param s the string to parse
     * @return the result
     */
    @Nullable
    public static Long safeParseLong(@Nullable String s) {
        try {
            return s == null ? null : Long.parseLong(s);
        }
        catch (Exception e1) {
            try {
                return Long.parseUnsignedLong(s);
            }
            catch (Exception e2) {
                return null;
            }
        }
    }

    /**
     * Safely parse a long, returning the default if the string is null or does not parse
     * @param s the string to parse
     * @param dflt the default value
     * @return the result
     */
    public static long safeParseLong(@Nullable String s, long dflt) {
        Long l = safeParseLong(s);
        return l == null ? dflt : l;
    }
}
