// Copyright 2015-2024 The NATS Authors
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

import io.ResourceUtils;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;

import static io.nats.json.DateTimeUtils.DEFAULT_TIME;
import static io.nats.json.JsonWriteUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public final class JsonWriteUtilsTests {

    @Test
    public void testBeginEnd() {
        StringBuilder sb = beginJson();
        addField(sb, "name", "value");
        endJson(sb);
        assertEquals("{\"name\":\"value\"}", sb.toString());

        sb = beginFormattedJson();
        addField(sb, "name", "value");
        String ended = endFormattedJson(sb);
        assertEquals("{\n    \"name\":\"value\"\n}", ended);
        assertEquals("{\n    \"name\":\"value\"\n}", sb.toString());

        sb = beginJsonPrefixed(null);
        assertEquals("{", sb.toString());

        sb = beginJsonPrefixed("pre");
        assertEquals("pre{", sb.toString());
    }

    @Test
    public void testAddFields() {
        StringBuilder sb = new StringBuilder();

        addField(sb, "n/a", (String) null);
        assertEquals(0, sb.length());

        addField(sb, "n/a", "");
        assertEquals(0, sb.length());

        addStrings(sb, "n/a", (String[]) null);
        assertEquals(0, sb.length());

        addStrings(sb, "n/a", new String[0]);
        assertEquals(0, sb.length());

        addStrings(sb, "n/a", (List<String>) null);
        assertEquals(0, sb.length());

        addField(sb, "n/a", (JsonSerializable) null);
        assertEquals(0, sb.length());

        addJsons(sb, "n/a", new ArrayList<>());
        assertEquals(0, sb.length());

        addJsons(sb, "n/a", null);
        assertEquals(0, sb.length());

        addDurations(sb, "n/a", null);
        assertEquals(0, sb.length());

        addDurations(sb, "n/a", new ArrayList<>());
        assertEquals(0, sb.length());

        addField(sb, "n/a", (Boolean) null);
        assertEquals(0, sb.length());

        addFldWhenTrue(sb, "n/a", null);
        assertEquals(0, sb.length());

        addFldWhenTrue(sb, "n/a", false);
        assertEquals(0, sb.length());

        addField(sb, "n/a", (Integer) null);
        assertEquals(0, sb.length());

        addField(sb, "n/a", (Long) null);
        assertEquals(0, sb.length());

        //noinspection UnnecessaryBoxing
        addField(sb, "iminusone", new Integer(-1));
        assertEquals(0, sb.length());

        addField(sb, "lminusone", new Long(-1));
        assertEquals(0, sb.length());

        addStrings(sb, "foo", new String[]{"bbb"});
        assertEquals(14, sb.length());

        addField(sb, "zero", 0);
        assertEquals(23, sb.length());

        addField(sb, "lone", 1);
        assertEquals(32, sb.length());

        addField(sb, "lmax", Long.MAX_VALUE);
        assertEquals(59, sb.length());

        addField(sb, "btrue", true);
        assertEquals(72, sb.length());

        addField(sb, "bfalse", false);
        assertEquals(87, sb.length());

        addFieldWhenGtZero(sb, "intnull", (Integer) null);
        assertEquals(87, sb.length());

        addFieldWhenGtZero(sb, "longnull", (Long) null);
        assertEquals(87, sb.length());

        //noinspection UnnecessaryBoxing
        addFieldWhenGtZero(sb, "intnotgt0", new Integer(0));
        assertEquals(87, sb.length());

        addFieldWhenGtZero(sb, "longnotgt0", 0L);
        assertEquals(87, sb.length());

        //noinspection UnnecessaryBoxing
        addFieldWhenGtZero(sb, "intgt0", new Integer(1));
        assertEquals(98, sb.length());

        addFieldWhenGtZero(sb, "longgt0", 1L);
        assertEquals(110, sb.length());

        addField(sb, "zdt", (ZonedDateTime)null);
        assertEquals(110, sb.length());

        addField(sb, "zdt", DEFAULT_TIME);
        assertEquals(110, sb.length());

        addField(sb, "zdt", DateTimeUtils.gmtNow());
        assertEquals(149, sb.length());

        addFieldWhenGreaterThan(sb, "xgt", null, 1);
        assertEquals(149, sb.length());

        addFieldWhenGreaterThan(sb, "xgt", 0L, 1);
        assertEquals(149, sb.length());

        addFieldWhenGreaterThan(sb, "xgt", 1L, 1);
        assertEquals(149, sb.length());

        addFieldWhenGreaterThan(sb, "xgt", 2L, 1);
        assertEquals(157, sb.length());

        addRawJson(sb, "n/a", null);
        assertEquals(157, sb.length());

        addRawJson(sb, "n/a", "");
        assertEquals(157, sb.length());

        addRawJson(sb, "raw", "raw");
        assertEquals(167, sb.length());

        addFieldEvenEmpty(sb, "ee1", null);
        assertEquals(176, sb.length());

        addFieldEvenEmpty(sb, "ee2", "");
        assertEquals(185, sb.length());

        addFieldWhenGteMinusOne(sb, "n/a", null);
        assertEquals(185, sb.length());

        addFieldWhenGteMinusOne(sb, "n/a", -2L);
        assertEquals(185, sb.length());

        addFieldWhenGteMinusOne(sb, "gtem1", -1L);
        assertEquals(196, sb.length());

        addFieldAsNanos(sb, "n/a", null);
        assertEquals(196, sb.length());

        addFieldAsNanos(sb, "n/a", Duration.ZERO);
        assertEquals(196, sb.length());

        addFieldAsNanos(sb, "n/a", Duration.ofNanos(-1));
        assertEquals(196, sb.length());

        addFieldAsNanos(sb, "fan", Duration.ofNanos(1000000));
        assertEquals(210, sb.length());

        addEnumWhenNot(sb, "n/a", null, JsonValue.Type.STRING);
        assertEquals(210, sb.length());

        addEnumWhenNot(sb, "n/a", JsonValue.Type.STRING, JsonValue.Type.STRING);
        assertEquals(210, sb.length());

        addEnumWhenNot(sb, "ewn", JsonValue.Type.STRING, JsonValue.Type.LONG);
        assertEquals(225, sb.length());

        //noinspection unchecked
        addField(sb, "n/a", (Map)null);
        assertEquals(225, sb.length());

        Map<String, String> map = new HashMap<>();
        addField(sb, "n/a", map);
        assertEquals(225, sb.length());

        map.put("mfoo", "mbar");
        addField(sb, "afmap", map);
        assertEquals(249, sb.length());

        addFldWhenTrue(sb, "whentrue", true);
        assertEquals(265, sb.length());

        _addList(sb, "al1", new ArrayList<>(), StringBuilder::append);
        assertEquals(274, sb.length());

        List<Integer> ilist = new ArrayList<>();
        _addList(sb, "al2", ilist, StringBuilder::append);
        assertEquals(283, sb.length());

        ilist.add(1);
        _addList(sb, "al3", ilist, StringBuilder::append);
        assertEquals(293, sb.length());

        ilist.add(2);
        _addList(sb, "al4", ilist, StringBuilder::append);
        assertEquals(305, sb.length());

        List<String> slist = new ArrayList<>();
        addStrings(sb, "n/a", slist);
        assertEquals(305, sb.length());

        slist.add("s");
        addStrings(sb, "slist", slist);
        assertEquals(319, sb.length());

        List<Duration> durs = new ArrayList<>();
        addDurations(sb, "dur1", durs);
        assertEquals(319, sb.length());

        durs.add(Duration.ofMillis(1));
        addDurations(sb, "dur2", durs);
        assertEquals(336, sb.length());

        addJsons(sb, "n/a", null);
        assertEquals(336, sb.length());

        List<JsonValue> jlist = new ArrayList<>();
        addJsons(sb, "n/a", jlist);
        assertEquals(336, sb.length());

        JsonValue jv = new JsonValue("jv");
        jlist.add(jv);
        addJsons(sb, "jsons", jlist);
        assertEquals(351, sb.length());
        System.out.println(sb);
        System.out.println(sb.length());
    }

    @Test
    public void testParseDateTime() {
        assertEquals(1611186068, DateTimeUtils.parseDateTime("2021-01-20T23:41:08.579594Z").toEpochSecond());
        assertEquals(1612293508, DateTimeUtils.parseDateTime("2021-02-02T11:18:28.347722551-08:00").toEpochSecond());
        assertEquals(-62135596800L, DateTimeUtils.parseDateTime("anything-not-valid").toEpochSecond());
    }

    @Test
    public void testParseLong() {
        assertEquals(-1, safeParseLong("18446744073709551615", -999));
        assertEquals(-2, safeParseLong("18446744073709551614", -999));
        assertEquals(-999, safeParseLong("18446744073709551616", -999));
        assertEquals(-999, safeParseLong(null, -999));
        assertEquals(-999, safeParseLong("notanumber", -999));
        assertEquals(1, safeParseLong("1"));
    }

    @Test
    public void testMiscCoverage() {
        String json = ResourceUtils.resourceAsString("StreamInfo.json");
        printFormatted(JsonParser.parseUnchecked(json));

        assertEquals("\"JsonWriteUtilsTests\":", toKey(this.getClass()));
    }

    @Test
    public void testMapEquals() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("foo", "bar");
        map1.put("bada", "bing");

        Map<String, String> map2 = new HashMap<>();
        map2.put("bada", "bing");
        map2.put("foo", "bar");

        Map<String, String> map3 = new HashMap<>();
        map3.put("foo", "bar");

        Map<String, String> map4 = new HashMap<>();
        map4.put("foo", "baz");

        Map<String, String> empty1 = new HashMap<>();
        Map<String, String> empty2 = new HashMap<>();

        assertTrue(mapEquals(null, null));
        assertFalse(mapEquals(map1, null));
        assertFalse(mapEquals(null, map1));
        assertFalse(mapEquals(null, empty1));
        assertFalse(mapEquals(empty1, null));

        assertTrue(mapEquals(map1, map2));
        assertFalse(mapEquals(map1, map3));
        assertFalse(mapEquals(map1, map4));
        assertFalse(mapEquals(map1, empty1));

        assertTrue(mapEquals(map2, map1));
        assertFalse(mapEquals(map2, map3));
        assertFalse(mapEquals(map2, map4));
        assertFalse(mapEquals(map2, empty1));

        assertFalse(mapEquals(map3, map1));
        assertFalse(mapEquals(map3, map2));
        assertFalse(mapEquals(map3, map4));
        assertFalse(mapEquals(map3, empty1));

        assertFalse(mapEquals(map4, map1));
        assertFalse(mapEquals(map4, map2));
        assertFalse(mapEquals(map4, map3));
        assertFalse(mapEquals(map4, empty1));

        assertFalse(mapEquals(empty1, map1));
        assertFalse(mapEquals(empty1, map2));
        assertFalse(mapEquals(empty1, map3));
        assertFalse(mapEquals(empty1, map4));
        assertTrue(mapEquals(empty1, empty2));

        assertFalse(mapEquals(empty2, map1));
        assertFalse(mapEquals(empty2, map2));
        assertFalse(mapEquals(empty2, map3));
        assertFalse(mapEquals(empty2, map4));
        assertTrue(mapEquals(empty2, empty1));
    }

    public static boolean mapEquals(Map<String, String> map1, Map<String, String> map2) {
        if (map1 == null) {
            return map2 == null;
        }
        if (map2 == null || map1.size() != map2.size()) {
            return false;
        }
        for (String key : map1.keySet()) {
            if (!Objects.equals(map1.get(key), map2.get(key))) {
                return false;
            }
        }
        return true;
    }
}
