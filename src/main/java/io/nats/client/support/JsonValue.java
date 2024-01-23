// Copyright 2023-2024 The NATS Authors
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

package io.nats.client.support;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static io.nats.client.support.JsonEncoding.jsonEncode;

public class JsonValue implements JsonSerializable {

    public enum Type {
        STRING, BOOL, INTEGER, LONG, DOUBLE, FLOAT, BIG_DECIMAL, BIG_INTEGER, MAP, ARRAY, NULL;
    }

    private static final char QUOTE = '"';
    private static final char COMMA = ',';
    private static final String NULL_STR = "null";

    public static final JsonValue NULL = new JsonValue();
    public static final JsonValue TRUE = new JsonValue(true);
    public static final JsonValue FALSE = new JsonValue(false);
    public static final JsonValue EMPTY_MAP = new JsonValue(Collections.unmodifiableMap(new HashMap<>()));
    public static final JsonValue EMPTY_ARRAY = new JsonValue(Collections.unmodifiableList(new ArrayList<>()));

    public final String string;
    public final Boolean bool;
    public final Integer i;
    public final Long l;
    public final Double d;
    public final Float f;
    public final BigDecimal bd;
    public final BigInteger bi;
    public final Map<String, JsonValue> map;
    public final List<JsonValue> array;
    public final Type type;
    public final Object object;
    public final Number number;

    public final List<String> mapOrder;

    public JsonValue() {
        this(null, null, null, null, null, null, null, null, null, null);
    }

    public JsonValue(String string) {
        this(string, null, null, null, null, null, null, null, null, null);
    }

    public JsonValue(char c) {
        this("" + c, null, null, null, null, null, null, null, null, null);
    }

    public JsonValue(Boolean bool) {
        this(null, bool, null, null, null, null, null, null, null, null);
    }

    public JsonValue(int i) {
        this(null, null, i, null, null, null, null, null, null, null);
    }

    public JsonValue(long l) {
        this(null, null, null, l, null, null, null, null, null, null);
    }

    public JsonValue(double d) {
        this(null, null, null, null, d, null, null, null, null, null);
    }

    public JsonValue(float f) {
        this(null, null, null, null, null, f, null, null, null, null);
    }

    public JsonValue(BigDecimal bd) {
        this(null, null, null, null, null, null, bd, null, null, null);
    }

    public JsonValue(BigInteger bi) {
        this(null, null, null, null, null, null, null, bi, null, null);
    }

    public JsonValue(Map<String, JsonValue> map) {
        this(null, null, null, null, null, null, null, null, map, null);
    }

    public JsonValue(List<JsonValue> list) {
        this(null, null, null, null, null, null, null, null, null, list);
    }

    public JsonValue(JsonValue[] values) {
        this(null, null, null, null, null, null, null, null, null, values == null ? null : Arrays.asList(values));
    }

    private JsonValue(String string, Boolean bool, Integer i, Long l, Double d, Float f, BigDecimal bd, BigInteger bi, Map<String, JsonValue> map, List<JsonValue> array) {
        this.map = map;
        mapOrder = new ArrayList<>();
        this.array = array;
        this.string = string;
        this.bool = bool;
        this.i = i;
        this.l = l;
        this.d = d;
        this.f = f;
        this.bd = bd;
        this.bi = bi;
        if (i != null) {
            this.type = Type.INTEGER;
            number = i;
            object = number;
        }
        else if (l != null) {
            this.type = Type.LONG;
            number = l;
            object = number;
        }
        else if (d != null) {
            this.type = Type.DOUBLE;
            number = this.d;
            object = number;
        }
        else if (f != null) {
            this.type = Type.FLOAT;
            number = this.f;
            object = number;
        }
        else if (bd != null) {
            this.type = Type.BIG_DECIMAL;
            number = this.bd;
            object = number;
        }
        else if (bi != null) {
            this.type = Type.BIG_INTEGER;
            number = this.bi;
            object = number;
        }
        else {
            number = null;
            if (map != null) {
                this.type = Type.MAP;
                object = map;
            }
            else if (string != null) {
                this.type = Type.STRING;
                object = string;
            }
            else if (bool != null) {
                this.type = Type.BOOL;
                object = bool;
            }
            else if (array != null) {
                this.type = Type.ARRAY;
                object = array;
            }
            else {
                this.type = Type.NULL;
                object = null;
            }
        }
    }

    public String toString(Class<?> c) {
        return toString(c.getSimpleName());
    }

    public String toString(String key) {
        return QUOTE + key + QUOTE + ":" + toJson();
    }

    @Override
    public String toString() {
        return toJson();
    }

    @Override
    public JsonValue toJsonValue() {
        return this;
    }

    @Override
    public String toJson() {
        switch (type) {
            case STRING:      return valueString(string);
            case BOOL:        return valueString(bool);
            case MAP:         return valueString(map);
            case ARRAY:       return valueString(array);
            case INTEGER:     return i.toString();
            case LONG:        return l.toString();
            case DOUBLE:      return d.toString();
            case FLOAT:       return f.toString();
            case BIG_DECIMAL: return bd.toString();
            case BIG_INTEGER: return bi.toString();
            default:          return NULL_STR;
        }
    }

    private String valueString(String s) {
        return QUOTE + jsonEncode(s) + QUOTE;
    }

    private String valueString(boolean b) {
        return Boolean.toString(b).toLowerCase();
    }

    private String valueString(Map<String, JsonValue> map) {
        StringBuilder sbo = new StringBuilder("{");
        if (!mapOrder.isEmpty()) {
            for (String key : mapOrder) {
                addField(sbo, key, map.get(key));
            }
        }
        else {
            for (String key : map.keySet()) {
                addField(sbo, key, map.get(key));
            }
        }
        return endJson(sbo).toString();
    }

    private String valueString(List<JsonValue> list) {
        StringBuilder sba = beginArray();
        for (JsonValue v : list) {
            sba.append(v.toJson());
            sba.append(COMMA);
        }
        return endArray(sba).toString();
    }

    private static void addField(StringBuilder sb, String fname, JsonSerializable value) {
        if (value != null) {
            sb.append('"');
            jsonEncode(sb, fname);
            sb.append("\":").append(value.toJson()).append(COMMA);
        }
    }

    private static StringBuilder beginArray() {
        return new StringBuilder("[");
    }

    private static StringBuilder endJson(StringBuilder sb) {
        int lastIndex = sb.length() - 1;
        if (sb.charAt(lastIndex) == ',') {
            sb.setCharAt(lastIndex, '}');
            return sb;
        }
        sb.append("}");
        return sb;
    }

    private static StringBuilder endArray(StringBuilder sb) {
        int lastIndex = sb.length() - 1;
        if (sb.charAt(lastIndex) == ',') {
            sb.setCharAt(lastIndex, ']');
            return sb;
        }
        sb.append("]");
        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonValue jsonValue = (JsonValue) o;

        if (type != jsonValue.type) return false;
        if (!Objects.equals(map, jsonValue.map)) return false;
        if (!Objects.equals(array, jsonValue.array)) return false;
        if (!Objects.equals(string, jsonValue.string)) return false;
        if (!Objects.equals(bool, jsonValue.bool)) return false;
        if (!Objects.equals(i, jsonValue.i)) return false;
        if (!Objects.equals(l, jsonValue.l)) return false;
        if (!Objects.equals(d, jsonValue.d)) return false;
        if (!Objects.equals(f, jsonValue.f)) return false;
        if (!Objects.equals(bd, jsonValue.bd)) return false;
        return Objects.equals(bi, jsonValue.bi);
    }

    @Override
    public int hashCode() {
        int result = map != null ? map.hashCode() : 0;
        result = 31 * result + (array != null ? array.hashCode() : 0);
        result = 31 * result + (string != null ? string.hashCode() : 0);
        result = 31 * result + (bool != null ? bool.hashCode() : 0);
        result = 31 * result + (i != null ? i.hashCode() : 0);
        result = 31 * result + (l != null ? l.hashCode() : 0);
        result = 31 * result + (d != null ? d.hashCode() : 0);
        result = 31 * result + (f != null ? f.hashCode() : 0);
        result = 31 * result + (bd != null ? bd.hashCode() : 0);
        result = 31 * result + (bi != null ? bi.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
