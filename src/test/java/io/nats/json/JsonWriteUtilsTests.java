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
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
        String newline = System.lineSeparator();
        assertEquals("{" + newline + "    \"name\":\"value\"" + newline + "}", ended);
        assertEquals("{" + newline + "    \"name\":\"value\"" + newline + "}", sb.toString());

        sb = beginJsonPrefixed(null);
        assertEquals("{", sb.toString());

        sb = beginJsonPrefixed("pre");
        assertEquals("pre{", sb.toString());
    }


    private static final String FN = "fn";
    private static final int ADDED_BASE_LENGTH = 6; // "fn":<>,
    // static String _last = "";
    private static void checkLength(StringBuilder sb, AtomicInteger cur, int newAddition) {
        if (newAddition > 0) {
            cur.addAndGet(ADDED_BASE_LENGTH + newAddition);
        }
        // System.out.println(cur.get() + " |" + sb.substring(_last.length()) + "|");
        // _last = sb.toString();
        assertEquals(cur.get(), sb.length());
    }

    @Test
    public void testAddFields() {
        StringBuilder sb = new StringBuilder();
        AtomicInteger cur = new AtomicInteger();

        addRawJson(sb, FN, null);
        checkLength(sb, cur, -1);

        addRawJson(sb, FN, "");
        checkLength(sb, cur, -1);

        addRawJson(sb, FN, "raw");
        checkLength(sb, cur, 3); // 3 b/c no quotes

        JsonValue jv = null;
        addField(sb, FN, jv);
        checkLength(sb, cur, -1);

        jv = new JsonValue("foo");
        addField(sb, FN, jv);
        checkLength(sb, cur, 5);

        addField(sb, FN, (String) null);
        checkLength(sb, cur, -1);

        addField(sb, FN, "");
        checkLength(sb, cur, -1);

        addField(sb, FN, "not-raw");
        checkLength(sb, cur, 9); // includes quotes around string value

        addFieldAlways(sb, FN, (String) null);
        checkLength(sb, cur, 2);

        addFieldAlways(sb, FN, "");
        checkLength(sb, cur, 2);

        addFieldAlways(sb, "fn", "always");
        checkLength(sb, cur, 8);

        addField(sb, FN, (Boolean) null);
        checkLength(sb, cur, -1);

        addField(sb, FN, true);
        checkLength(sb, cur, 4);

        addField(sb, FN, false);
        checkLength(sb, cur, -1);

        addFieldAlways(sb, FN, (Boolean) null);
        checkLength(sb, cur, 5);

        addFieldAlways(sb, FN, true);
        checkLength(sb, cur, 4);

        addFieldAlways(sb, FN, false);
        checkLength(sb, cur, 5);

        addField(sb, FN, (Integer) null);
        checkLength(sb, cur, -1);

        addField(sb, FN, -1);
        checkLength(sb, cur, -1);

        addField(sb, FN, 0);
        checkLength(sb, cur, 1);

        addField(sb, FN, 42);
        checkLength(sb, cur, 2);

        addFieldWhenGtZero(sb, FN, (Integer) null);
        checkLength(sb, cur, -1);

        addFieldWhenGtZero(sb, FN, -1);
        checkLength(sb, cur, -1);

        addFieldWhenGtZero(sb, FN, 0);
        checkLength(sb, cur, -1);

        addFieldWhenGtZero(sb, FN, 42);
        checkLength(sb, cur, 2);

        addFieldWhenGteMinusOne(sb, FN, (Integer) null);
        checkLength(sb, cur, -1);

        addFieldWhenGteMinusOne(sb, FN, -2);
        checkLength(sb, cur, -1);

        addFieldWhenGteMinusOne(sb, FN, -1);
        checkLength(sb, cur, 2);

        addFieldWhenGteMinusOne(sb, FN, 0);
        checkLength(sb, cur, 1);

        addFieldWhenGteMinusOne(sb, FN, 42);
        checkLength(sb, cur, 2);

        addFieldWhenGreaterThan(sb, FN, (Integer) null, -2);
        checkLength(sb, cur, -1);

        addFieldWhenGreaterThan(sb, FN, -2, -2);
        checkLength(sb, cur, -1);

        addFieldWhenGreaterThan(sb, FN, -1, -2);
        checkLength(sb, cur, 2);

        addFieldWhenGreaterThan(sb, FN, 0, -2);
        checkLength(sb, cur, 1);

        addFieldWhenGreaterThan(sb, FN, 42, -2);
        checkLength(sb, cur, 2);

        addField(sb, FN, (Long) null);
        checkLength(sb, cur, -1);

        addField(sb, FN, -1L);
        checkLength(sb, cur, -1);

        addField(sb, FN, 0L);
        checkLength(sb, cur, 1);

        addField(sb, FN, 42L);
        checkLength(sb, cur, 2);

        addFieldWhenGtZero(sb, FN, (Long) null);
        checkLength(sb, cur, -1);

        addFieldWhenGtZero(sb, FN, -1L);
        checkLength(sb, cur, -1);

        addFieldWhenGtZero(sb, FN, 0L);
        checkLength(sb, cur, -1);

        addFieldWhenGtZero(sb, FN, 42L);
        checkLength(sb, cur, 2);

        addFieldWhenGteMinusOne(sb, FN, (Long) null);
        checkLength(sb, cur, -1);

        addFieldWhenGteMinusOne(sb, FN, -2L);
        checkLength(sb, cur, -1);

        addFieldWhenGteMinusOne(sb, FN, -1L);
        checkLength(sb, cur, 2);

        addFieldWhenGteMinusOne(sb, FN, 0L);
        checkLength(sb, cur, 1);

        addFieldWhenGteMinusOne(sb, FN, 42L);
        checkLength(sb, cur, 2);

        addFieldWhenGreaterThan(sb, FN, null, -2L);
        checkLength(sb, cur, -1);

        addFieldWhenGreaterThan(sb, FN, -2L, -2L);
        checkLength(sb, cur, -1);

        addFieldWhenGreaterThan(sb, FN, -1L, -2L);
        checkLength(sb, cur, 2);

        addFieldWhenGreaterThan(sb, FN, 0L, -2L);
        checkLength(sb, cur, 1);

        addFieldWhenGreaterThan(sb, FN, 42L, -2L);
        checkLength(sb, cur, 2);

        addFieldAsNanos(sb, FN, null);
        checkLength(sb, cur, -1);

        addFieldAsNanos(sb, FN, Duration.ZERO);
        checkLength(sb, cur, -1);

        addFieldAsNanos(sb, FN, Duration.ofNanos(-1));
        checkLength(sb, cur, -1);

        addFieldAsNanos(sb, FN, Duration.ofSeconds(42));
        checkLength(sb, cur, 11);

        addField(sb, FN, (ZonedDateTime) null);
        checkLength(sb, cur, -1);

        addField(sb, FN, DEFAULT_TIME);
        checkLength(sb, cur, -1);

        ZonedDateTime zdt = ZonedDateTime.now();
        String temp = DateTimeUtils.toRfc3339(zdt);
        addField(sb, FN, zdt);
        checkLength(sb, cur, temp.length() + 2);

        Map<String, String> smap = new HashMap<>();
        addField(sb, FN, (Map<String, String>)null);
        checkLength(sb, cur, -1);

        addField(sb, FN, smap);
        checkLength(sb, cur, -1);

        smap.put("foo", "bar");
        smap.put("bar", "baz");
        addField(sb, FN, smap);
        checkLength(sb, cur, 25);

        Map<String, Long> lmap = new HashMap<>();
        lmap.put("foo", 1L);
        lmap.put("bar", 2L);
        addField(sb, FN, lmap);
        checkLength(sb, cur, 17);

        Map<String, JsonSerializable> jsmap = new HashMap<>();
        jsmap.put("foo", JsonValue.instance("bar"));
        jsmap.put("bar", JsonValue.instance(2L));
        addField(sb, FN, jsmap);
        checkLength(sb, cur, 21);

        addStrings(sb, FN);
        checkLength(sb, cur, -1);

        String[] sa = null;
        addStrings(sb, FN, sa);
        checkLength(sb, cur, -1);

        sa = new String[0];
        addStrings(sb, FN, sa);
        checkLength(sb, cur, -1);

        addStrings(sb, FN, "foo", "bar");
        checkLength(sb, cur, 13);

        sa = new String[]{"foo", "bar"};
        addStrings(sb, FN, sa);
        checkLength(sb, cur, 13);

        List<String> slist = null;
        addStrings(sb, FN, slist);
        checkLength(sb, cur, -1);

        slist = new ArrayList<>();
        addStrings(sb, FN, slist);
        checkLength(sb, cur, -1);

        slist.add("foo");
        slist.add("bar");
        slist.add(null);
        slist.add("");
        addStrings(sb, FN, slist);
        checkLength(sb, cur, 13);

        addIntegers(sb, FN);
        checkLength(sb, cur, -1);

        Integer[] ia = null;
        addIntegers(sb, FN, ia);
        checkLength(sb, cur, -1);

        ia = new Integer[0];
        addIntegers(sb, FN, ia);
        checkLength(sb, cur, -1);

        addIntegers(sb, FN, -1, 0, 1);
        checkLength(sb, cur, 8);

        ia = new Integer[]{-1, 0, 1};
        addIntegers(sb, FN, ia);
        checkLength(sb, cur, 8);

        List<Integer> ilist = null;
        addIntegers(sb, FN, ilist);
        checkLength(sb, cur, -1);

        ilist = new ArrayList<>();
        addIntegers(sb, FN, ilist);
        checkLength(sb, cur, -1);

        ilist.add(-1);
        ilist.add(0);
        ilist.add(1);
        ilist.add(null);
        addIntegers(sb, FN, ilist);
        checkLength(sb, cur, 8);

        addLongs(sb, FN);
        checkLength(sb, cur, -1);

        Long[] la = null;
        addLongs(sb, FN, la);
        checkLength(sb, cur, -1);

        la = new Long[0];
        addLongs(sb, FN, la);
        checkLength(sb, cur, -1);

        addLongs(sb, FN, -1L, 0L, 1L);
        checkLength(sb, cur, 8);

        la = new Long[]{-1L, 0L, 1L};
        addLongs(sb, FN, la);
        checkLength(sb, cur, 8);

        List<Long> llist = null;
        addLongs(sb, FN, llist);
        checkLength(sb, cur, -1);

        llist = new ArrayList<>();
        addLongs(sb, FN, llist);
        checkLength(sb, cur, -1);

        llist.add(-1L);
        llist.add(0L);
        llist.add(1L);
        llist.add(null);
        addLongs(sb, FN, llist);
        checkLength(sb, cur, 8);

        addJsons(sb, FN, null);
        checkLength(sb, cur, -1);

        addJsons(sb, FN, new ArrayList<>());
        checkLength(sb, cur, -1);

        addJsons(sb, FN, jsmap.values());
        checkLength(sb, cur, 9);

        class ExtendsJs implements JsonSerializable {
            final String s;

            public ExtendsJs(String s) {
                this.s = s;
            }

            @Override
            public @NotNull String toJson() {
                return "\"" + FN + "\":\"" + s + "\"";
            }
        }

        List<ExtendsJs> elist = new ArrayList<>();
        elist.add(new ExtendsJs("foo"));
        elist.add(new ExtendsJs("bar"));
        addJsons(sb, FN, elist);
        checkLength(sb, cur, 23);

        List<Duration> dlist = null;
        addDurations(sb, FN, dlist);
        checkLength(sb, cur, -1);

        dlist = new ArrayList<>();
        addDurations(sb, FN, dlist);
        checkLength(sb, cur, -1);

        dlist.add(Duration.ofNanos(-1));
        dlist.add(Duration.ZERO);
        dlist.add(Duration.ofNanos(1));
        dlist.add(null);
        addDurations(sb, FN, dlist);
        checkLength(sb, cur, 8);

        addEnum(sb, FN, (TestEnum)null);
        checkLength(sb, cur, 0);

        addEnum(sb, FN, TestEnum.FOO);
        checkLength(sb, cur, 5);

        addEnumWhenNot(sb, FN, null, TestEnum.FOO);
        checkLength(sb, cur, 0);

        addEnumWhenNot(sb, FN, TestEnum.FOO, TestEnum.BAR);
        checkLength(sb, cur, 5);

        addEnumWhenNot(sb, FN, TestEnum.BAR, TestEnum.BAR);
        checkLength(sb, cur, 0);
    }

    enum TestEnum {
        FOO, BAR
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
