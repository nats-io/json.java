// Copyright 2025-2026 The NATS Authors
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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonUnicodeParsingTest {

    @Test
    void testParseU_BasicASCII() throws JsonParseException {
        // Test basic ASCII character 'A' (U+0041)
        char[] json = "\"\\u0041\"".toCharArray();
        JsonParser testParser = new JsonParser(json);
        
        JsonValue result = testParser.parse();
        assertEquals("A", result.string);
    }
    
    @Test
    void testParseU_BasicASCII_Lowercase() throws JsonParseException {
        // Test with lowercase hex digits 'B' (U+0042)
        char[] json = "\"\\u0042\"".toCharArray();
        JsonParser testParser = new JsonParser(json);
        
        JsonValue result = testParser.parse();
        assertEquals("B", result.string);
    }
    
    @Test
    void testParseU_MixedCase() throws JsonParseException {
        // Test mixed case hex digits 'H' (U+0048)
        char[] json = "\"\\u0048\"".toCharArray();
        JsonParser testParser = new JsonParser(json);
        
        JsonValue result = testParser.parse();
        assertEquals("H", result.string);
    }
    
    @Test
    void testParseU_NonASCII() throws JsonParseException {
        // Test Japanese character '日' (U+65E5)
        char[] json = "\"\\u65e5\"".toCharArray();
        JsonParser testParser = new JsonParser(json);
        
        JsonValue result = testParser.parse();
        assertEquals("日", result.string);
    }
    
    @Test
    void testParseU_HighSurrogate() throws JsonParseException {
        // Test high surrogate for emoji (U+D83D)
        // Note: This creates an incomplete surrogate pair, but tests the parsing
        char[] json = "\"\\uD83D\\uDE00\"".toCharArray(); // 😀 emoji
        JsonParser testParser = new JsonParser(json);
        
        JsonValue result = testParser.parse();
        assertEquals("😀", result.string);
    }
    
    @Test
    void testParseU_AllZeros() throws JsonParseException {
        // Test null character (U+0000)
        char[] json = "\"\\u0000\"".toCharArray();
        JsonParser testParser = new JsonParser(json);
        
        JsonValue result = testParser.parse();
        assertEquals("\u0000", result.string);
    }
    
    @Test
    void testParseU_AllFs() throws JsonParseException {
        // Test maximum 4-digit hex value (U+FFFF)
        char[] json = "\"\\uFFFF\"".toCharArray();
        JsonParser testParser = new JsonParser(json);
        
        JsonValue result = testParser.parse();
        assertEquals("\uFFFF", result.string);
    }
    
    @Test
    void testParseU_MultipleEscapes() throws JsonParseException {
        // Test multiple Unicode escapes in one string
        char[] json = "\"\\u0041\\u0042\\u0043\"".toCharArray(); // "ABC"
        JsonParser testParser = new JsonParser(json);
        
        JsonValue result = testParser.parse();
        assertEquals("ABC", result.string);
    }
    
    @Test
    void testParseU_InvalidHexDigit_G() {
        // Test invalid hex digit 'G'
        char[] json = "\"\\u004G\"".toCharArray();
        JsonParser testParser = new JsonParser(json);
        
        assertThrows(JsonParseException.class, testParser::parse);
    }
    
    @Test
    void testParseU_InvalidHexDigit_SpecialChar() {
        // Test invalid hex digit '@'
        char[] json = "\"\\u004@\"".toCharArray();
        JsonParser testParser = new JsonParser(json);
        
        assertThrows(JsonParseException.class, testParser::parse);
    }
    
    @Test
    void testParseU_TruncatedEscape() {
        // Test truncated Unicode escape (only 3 digits)
        char[] json = "\"\\u004\"".toCharArray();
        JsonParser testParser = new JsonParser(json);
        
        assertThrows(JsonParseException.class, testParser::parse);
    }
    
    @Test
    void testParseU_EmptyAfterEscape() {
        // Test end of input after slash u
        char[] json = "\"\\u".toCharArray();
        JsonParser testParser = new JsonParser(json);
        
        assertThrows(JsonParseException.class, testParser::parse);
    }
    
    @Test
    void testParseU_PartialEscape() {
        // Test partial escape sequence
        char[] json = "\"\\u00".toCharArray();
        JsonParser testParser = new JsonParser(json);
        
        assertThrows(JsonParseException.class, testParser::parse);
    }
    
    @Test
    void testParseU_WithRegularText() throws JsonParseException {
        // Test Unicode escape mixed with regular text
        char[] json = "\"Hello \\u0041 World\"".toCharArray(); // "Hello A World"
        JsonParser testParser = new JsonParser(json);
        
        JsonValue result = testParser.parse();
        assertEquals("Hello A World", result.string);
    }
    
    @SuppressWarnings("UnnecessaryUnicodeEscape")
    @Test
    void testParseU_EdgeValues() throws JsonParseException {
        // Test various edge values
        String[] testCases = {
            "\"\\u0001\"", // U+0001
            "\"\\u007F\"", // U+007F (DEL)
            "\"\\u0080\"", // U+0080 (first non-ASCII)
            "\"\\u00FF\"", // U+00FF
            "\"\\u0100\"", // U+0100
            "\"\\u1000\"", // U+1000
            "\"\\uFFFE\"", // U+FFFE
        };
        
        char[][] expectedChars = {
            {'\u0001'}, {'\u007F'}, {'\u0080'}, 
            {'\u00FF'}, {'\u0100'}, {'\u1000'}, {'\uFFFE'}
        };
        
        for (int i = 0; i < testCases.length; i++) {
            JsonParser testParser = new JsonParser(testCases[i].toCharArray());
            JsonValue result = testParser.parse();
            assertEquals(new String(expectedChars[i]), result.string,
                "Failed for test case: " + testCases[i]);
        }
    }
}