// Copyright 2020-2024 The NATS Authors
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

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.nats.json.DateTimeUtils.DEFAULT_TIME;
import static io.nats.json.Encoding.jsonEncode;
import static io.nats.json.JsonValueUtils.instance;

public abstract class JsonWriteUtils {
    public static final String Q = "\"";
    public static final String QCOLONQ = "\":\"";
    public static final String QCOLON = "\":";
    public static final String QCOMMA = "\",";
    public static final String COMMA = ",";

    private JsonWriteUtils() {} /* ensures cannot be constructed */

    // ----------------------------------------------------------------------------------------------------
    // BUILD A STRING OF JSON
    // ----------------------------------------------------------------------------------------------------
    public static StringBuilder beginJson() {
        return new StringBuilder("{");
    }

    public static StringBuilder beginArray() {
        return new StringBuilder("[");
    }

    public static StringBuilder beginJsonPrefixed(String prefix) {
        return prefix == null ? beginJson()
            : new StringBuilder(prefix).append('{');
    }

    public static StringBuilder endJson(StringBuilder sb) {
        int lastIndex = sb.length() - 1;
        if (sb.charAt(lastIndex) == ',') {
            sb.setCharAt(lastIndex, '}');
            return sb;
        }
        sb.append("}");
        return sb;
    }

    public static StringBuilder endArray(StringBuilder sb) {
        int lastIndex = sb.length() - 1;
        if (sb.charAt(lastIndex) == ',') {
            sb.setCharAt(lastIndex, ']');
            return sb;
        }
        sb.append("]");
        return sb;
    }

    public static StringBuilder beginFormattedJson() {
        return new StringBuilder("{\n    ");
    }

    public static String endFormattedJson(StringBuilder sb) {
        sb.setLength(sb.length()-1);
        sb.append("\n}");
        return sb.toString().replace(",", ",\n    ");
    }

    /**
     * Appends a json field to a string builder.
     * @param sb string builder
     * @param fname fieldname
     * @param json raw json
     */
    public static void addRawJson(StringBuilder sb, String fname, String json) {
        if (json != null && !json.isEmpty()) {
            sb.append(Q);
            jsonEncode(sb, fname);
            sb.append(QCOLON);
            sb.append(json);
            sb.append(COMMA);
        }
    }

    /**
     * Appends a json field to a string builder.
     * @param sb string builder
     * @param fname fieldname
     * @param value field value
     */
    public static void addField(StringBuilder sb, String fname, String value) {
        if (value != null && !value.isEmpty()) {
            sb.append(Q);
            jsonEncode(sb, fname);
            sb.append(QCOLONQ);
            jsonEncode(sb, value);
            sb.append(QCOMMA);
        }
    }

    /**
     * Appends a json field to a string builder. Empty and null string are added as value of empty string
     * @param sb string builder
     * @param fname fieldname
     * @param value field value
     */
    public static void addFieldEvenEmpty(StringBuilder sb, String fname, String value) {
        if (value == null) {
            value = "";
        }
        sb.append(Q);
        jsonEncode(sb, fname);
        sb.append(QCOLONQ);
        jsonEncode(sb, value);
        sb.append(QCOMMA);
    }

    /**
     * Appends a json field to a string builder.
     * @param sb string builder
     * @param fname fieldname
     * @param value field value
     */
    public static void addField(StringBuilder sb, String fname, Boolean value) {
        if (value != null) {
            sb.append(Q);
            jsonEncode(sb, fname);
            sb.append(QCOLON).append(value ? "true" : "false").append(COMMA);
        }
    }

    /**
     * Appends a json field to a string builder.
     * @param sb string builder
     * @param fname fieldname
     * @param value field value
     */
    public static void addFldWhenTrue(StringBuilder sb, String fname, Boolean value) {
        if (value != null && value) {
            addField(sb, fname, true);
        }
    }

    /**
     * Appends a json field to a string builder.
     * @param sb string builder
     * @param fname fieldname
     * @param value field value
     */
    public static void addField(StringBuilder sb, String fname, Integer value) {
        if (value != null && value >= 0) {
            sb.append(Q);
            jsonEncode(sb, fname);
            sb.append(QCOLON).append(value).append(COMMA);
        }
    }

    /**
     * Appends a json field to a string builder.
     * @param sb string builder
     * @param fname fieldname
     * @param value field value
     */
    public static void addFieldWhenGtZero(StringBuilder sb, String fname, Integer value) {
        if (value != null && value > 0) {
            sb.append(Q);
            jsonEncode(sb, fname);
            sb.append(QCOLON).append(value).append(COMMA);
        }
    }

    /**
     * Appends a json field to a string builder.
     * @param sb string builder
     * @param fname fieldname
     * @param value field value
     */
    public static void addField(StringBuilder sb, String fname, Long value) {
        if (value != null && value >= 0) {
            sb.append(Q);
            jsonEncode(sb, fname);
            sb.append(QCOLON).append(value).append(COMMA);
        }
    }

    /**
     * Appends a json field to a string builder.
     * @param sb string builder
     * @param fname fieldname
     * @param value field value
     */
    public static void addFieldWhenGtZero(StringBuilder sb, String fname, Long value) {
        if (value != null && value > 0) {
            sb.append(Q);
            jsonEncode(sb, fname);
            sb.append(QCOLON).append(value).append(COMMA);
        }
    }

    /**
     * Appends a json field to a string builder.
     * @param sb string builder
     * @param fname fieldname
     * @param value field value
     */
    public static void addFieldWhenGteMinusOne(StringBuilder sb, String fname, Long value) {
        if (value != null && value >= -1) {
            sb.append(Q);
            jsonEncode(sb, fname);
            sb.append(QCOLON).append(value).append(COMMA);
        }
    }

    /**
     * Appends a json field to a string builder.
     * @param sb string builder
     * @param fname fieldname
     * @param value field value
     * @param gt the number the value must be greater than
     */
    public static void addFieldWhenGreaterThan(StringBuilder sb, String fname, Long value, long gt) {
        if (value != null && value > gt) {
            sb.append(Q);
            jsonEncode(sb, fname);
            sb.append(QCOLON).append(value).append(COMMA);
        }
    }

    /**
     * Appends a json field to a string builder.
     * @param sb string builder
     * @param fname fieldname
     * @param value duration value
     */
    public static void addFieldAsNanos(StringBuilder sb, String fname, Duration value) {
        if (value != null && !value.isZero() && !value.isNegative()) {
            sb.append(Q);
            jsonEncode(sb, fname);
            sb.append(QCOLON).append(value.toNanos()).append(COMMA);
        }
    }

    /**
     * Appends a json object to a string builder.
     * @param sb string builder
     * @param fname fieldname
     * @param value JsonSerializable value
     */
    public static void addField(StringBuilder sb, String fname, JsonSerializable value) {
        if (value != null) {
            sb.append(Q);
            jsonEncode(sb, fname);
            sb.append(QCOLON).append(value.toJson()).append(COMMA);
        }
    }

    public static void addField(StringBuilder sb, String fname, Map<String, String> map) {
        if (map != null && !map.isEmpty()) {
            addField(sb, fname, instance(map));
        }
    }

    @SuppressWarnings("rawtypes")
    public static void addEnumWhenNot(StringBuilder sb, String fname, Enum e, Enum dontAddIfThis) {
        if (e != null && e != dontAddIfThis) {
            addField(sb, fname, e.toString());
        }
    }

    public interface ListAdder<T> {
        void append(StringBuilder sb, T t);
    }

    /**
     * Appends a json field to a string builder.
     * @param <T> the list type
     * @param sb string builder
     * @param fname fieldname
     * @param list value list
     * @param adder implementation to add value, including its quotes if required
     */
    public static <T> void _addList(StringBuilder sb, String fname, List<T> list, ListAdder<T> adder) {
        sb.append(Q);
        jsonEncode(sb, fname);
        sb.append("\":[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(COMMA);
            }
            adder.append(sb, list.get(i));
        }
        sb.append("],");
    }

    /**
     * Appends a json field to a string builder.
     * @param sb string builder
     * @param fname fieldname
     * @param strings field value
     */
    public static void addStrings(StringBuilder sb, String fname, String[] strings) {
        if (strings != null && strings.length > 0) {
            _addStrings(sb, fname, Arrays.asList(strings));
        }
    }

    /**
     * Appends a json field to a string builder.
     * @param sb string builder
     * @param fname fieldname
     * @param strings field value
     */
    public static void addStrings(StringBuilder sb, String fname, List<String> strings) {
        if (strings != null && !strings.isEmpty()) {
            _addStrings(sb, fname, strings);
        }
    }

    private static void _addStrings(StringBuilder sb, String fname, List<String> strings) {
        _addList(sb, fname, strings, (sbs, s) -> {
            sb.append(Q);
            jsonEncode(sb, s);
            sb.append(Q);
        });
    }

    /**
     * Appends a json field to a string builder.
     * @param sb string builder
     * @param fname fieldname
     * @param jsons field value
     */
    public static void addJsons(StringBuilder sb, String fname, List<? extends JsonSerializable> jsons) {
        if (jsons != null && !jsons.isEmpty()) {
            _addList(sb, fname, jsons, (sbs, s) -> sbs.append(s.toJson()));
        }
    }

    /**
     * Appends a json field to a string builder.
     * @param sb string builder
     * @param fname fieldname
     * @param durations list of durations
     */
    public static void addDurations(StringBuilder sb, String fname, List<Duration> durations) {
        if (durations != null && !durations.isEmpty()) {
            _addList(sb, fname, durations, (sbs, dur) -> sbs.append(dur.toNanos()));
        }
    }

    /**
     * Appends a date/time to a string builder as a rfc 3339 formatted field.
     * @param sb string builder
     * @param fname fieldname
     * @param zonedDateTime field value
     */
    public static void addField(StringBuilder sb, String fname, ZonedDateTime zonedDateTime) {
        if (zonedDateTime != null && !DEFAULT_TIME.equals(zonedDateTime)) {
            sb.append(Q);
            jsonEncode(sb, fname);
            sb.append(QCOLONQ)
                .append(DateTimeUtils.toRfc3339(zonedDateTime))
                .append(QCOMMA);
        }
    }

    // ----------------------------------------------------------------------------------------------------
    // PRINT UTILS
    // ----------------------------------------------------------------------------------------------------
    public static String toKey(Class<?> c) {
        return "\"" + c.getSimpleName() + "\":";
    }

    private static final String INDENT = "                                        ";
    private static String indent(int level) {
        return level == 0 ? "" : INDENT.substring(0, level * 4);
    }

    /**
     * This isn't perfect but good enough for debugging
     * @param o the object
     * @return the formatted string
     */
    public static String getFormatted(Object o) {
        StringBuilder sb = new StringBuilder();
        int level = 0;
        int arrayLevel = 0;
        boolean lastWasClose = false;
        boolean indentNext = true;
        String indent = "";
        String s = o.toString();
        for (int x = 0; x < s.length(); x++) {
            char c = s.charAt(x);
            if (c == '{') {
                if (arrayLevel > 0 && lastWasClose) {
                    sb.append(indent);
                }
                sb.append(c).append('\n');
                indent = indent(++level);
                indentNext = true;
                lastWasClose = false;
            }
            else if (c == '}') {
                indent = indent(--level);
                sb.append('\n').append(indent).append(c);
                lastWasClose = true;
            }
            else if (c == ',') {
                sb.append(",\n");
                indentNext = true;
            }
            else {
                if (c == '[') {
                    arrayLevel++;
                }
                else if (c == ']') {
                    arrayLevel--;
                }
                if (indentNext) {
                    if (c != ' ') {
                        sb.append(indent).append(c);
                        indentNext = false;
                    }
                }
                else {
                    sb.append(c);
                }
                lastWasClose = lastWasClose && Character.isWhitespace(c);
            }
        }
        return sb.toString();
    }

    public static void printFormatted(Object o) {
        System.out.println(getFormatted(o));
    }

    // ----------------------------------------------------------------------------------------------------
    // SAFE NUMBER PARSING HELPERS
    // ----------------------------------------------------------------------------------------------------
    public static Long safeParseLong(String s) {
        try {
            return Long.parseLong(s);
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

    public static long safeParseLong(String s, long dflt) {
        Long l = safeParseLong(s);
        return l == null ? dflt : l;
    }
}
