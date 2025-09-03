package cn.labzen.tool.util;

import cn.labzen.tool.exception.StringException;
import cn.labzen.tool.structure.Pair;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cn.labzen.tool.util.Strings.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ConstantValue")
public class StringsTest {

  @Test
  void testValue() {
    assertNull(value(null, null));
    assertEquals("null", value(null, "null"));
    assertEquals("value", value("value", "null"));

    assertNull(value(null, "^non$", "non"));
    assertEquals("value", value("null", "null", "value"));
    assertEquals("a123456b", value("a123456b", "\\d+", "numbers"));

    assertEquals("1", value("", "1", true));
    assertEquals("2", value("2", "3", true));
    assertEquals("1", value("   ", "1", true));
  }

  @Test
  void testNullBlankEmpty() {
    assertTrue(isEmpty(""));
    assertTrue(isEmpty(null));
    assertFalse(isEmpty(" "));
    assertFalse(isEmpty("abc"));

    assertTrue(isBlank(""));
    assertTrue(isBlank(null));
    assertTrue(isBlank(" "));
    assertFalse(isBlank("abc"));

    assertFalse(isNotEmpty(""));
    assertFalse(isNotEmpty(null));
    assertTrue(isNotEmpty(" "));
    assertTrue(isNotEmpty("abc"));

    assertFalse(isNotBlank(""));
    assertFalse(isNotBlank(null));
    assertFalse(isNotBlank(" "));
    assertTrue(isNotBlank("abc"));

    assertNull(emptyToNull(""));
    assertNull(emptyToNull(null));
    assertNotNull(emptyToNull(" "));
    assertNotNull(emptyToNull("abc"));

    assertNull(blankToNull(""));
    assertNull(blankToNull(null));
    assertNull(blankToNull(" "));
    assertNotNull(blankToNull("abc"));

    assertTrue(isAnyBlank("string", null, "", "  "));
    assertTrue(isAnyBlank("string", "notNull", "  "));
    assertTrue(isAnyBlank("string", "notNull", ""));
    assertTrue(isAnyBlank(Lists.newArrayList("string", "notNull", null)));
    assertFalse(isAnyBlank(List.of("string", "notNull", "notBlank")));

    assertFalse(isAllBlank("string", null, "", "  "));
    assertTrue(isAllBlank("", "  "));
    assertFalse(isAllBlank(Lists.newArrayList("string", "notNull", null)));
    assertTrue(isAllBlank(Lists.newArrayList(null, "", "    ")));
  }

  @Test
  void testConcatAndJoin() {
    assertEquals("123", concat("1", "", "2", null, "3"));
    assertEquals("123", concat(Lists.newArrayList("1", "", "2", null, "3")));
    //assertEquals("1::2:3", join(":", "1", "", "2", null, "3"));
    //assertEquals("1::2:3", join(":", List.of()("1", "", "2", null, "3")));
    //assertEquals("1", join(":", List.of()("1", null)));
  }

  @Test
  void testTrim() {
    assertEquals("123", trim("===123===", "="));
    assertEquals("123===", trim("===123===", "=", Position.LEFT));
    assertEquals("===123", trim("===123===", "=", Position.RIGHT));
  }

  @Test
  void testPosition() {
    assertEquals('4', at("123456789", 3));
    assertEquals('6', at("123456789", -4));
    assertThrows(IllegalArgumentException.class, () -> at("123456789", -20));

    // ============

    assertEquals("", sub("0123456789", 0, 0));
    assertEquals("345", sub("0123456789", 3, 3));
    assertEquals("123", sub("0123456789", 3, -3));
    assertEquals("789", sub("0123456789", -3, 3));
    assertEquals("567", sub("0123456789", -3, -3));
    assertThrows(StringException.class, () -> sub("0123456789", 5, 8));
    assertThrows(StringException.class, () -> sub("0123456789", -5, -8));

    // ============

    assertIterableEquals(List.of("abc", "def"), between("[abc] xyz [def]", '[', ']'));

    // ============

    Pair<String, String> parts1 = cut("000::111", "::");
    assertNotNull(parts1);
    assertEquals("000", parts1.first());
    assertEquals("111", parts1.second());

    Pair<String, String> parts2 = cut("000::111::222", "::");
    assertNotNull(parts2);
    assertEquals("000", parts2.first());
    assertEquals("111::222", parts2.second());
  }

  @Test
  void testFormat() {
    assertEquals("a=1,b=2,a+b=3", format("a={},b={},a+b={}", List.of("1", "2", "3")));
    assertEquals("a=1,b=2,a+b=\\{3}", format("a={},b={},a+b=\\{{}}", List.of("1", "2", "3")));
    assertEquals("a=1,b=2,a+b={}3", format("a={},b={},a+b=\\{}{}", List.of("1", "2", "3")));
    assertEquals("a=1,b=2,a+b=\\3", format("a={},b={},a+b=\\\\{}", List.of("1", "2", "3")));
    assertEquals("a={},b={},a+b={}", format("a={},b={},a+b={}"));
    assertEquals("a=1,b=2,a+b=3", format("a={},b={},a+b={}", List.of("1", "2", "3", "4")));
    assertEquals("a=1,b=2,a+b={}", format("a={},b={},a+b={}", List.of("1", "2")));

    assertEquals("a=1,b=2,a+b=3", format("a={},b={},a+b={}", "1", "2", "3"));
    assertEquals("a=1,b=2,a+b=\\{3}", format("a={},b={},a+b=\\{{}}", "1", "2", "3"));
    assertEquals("a=1,b=2,a+b={}3", format("a={},b={},a+b=\\{}{}", "1", "2", "3"));
    assertEquals("a=1,b=2,a+b=\\3", format("a={},b={},a+b=\\\\{}", "1", "2", "3"));
    assertEquals("a={},b={},a+b={}", format("a={},b={},a+b={}"));
    assertEquals("a=1,b=2,a+b=3", format("a={},b={},a+b={}", "1", "2", "3", "4"));
    assertEquals("a=1,b=2,a+b={}", format("a={},b={},a+b={}", "1", "2"));
    assertEquals("a=1,b=[null],a+b={}", format("a={},b={},a+b={}", "1", null));
  }

  @Test
  void testTimes() {
    assertEquals(0, times("", ""));
    assertEquals(0, times("abc", "1"));
    assertEquals(1, times("aaa", "aa"));
    assertEquals(1, times("abc123xyz", "123"));
    assertEquals(2, times("xyz&abc&123", "&"));
    assertEquals(2, times("Abc&abc&ABC&abC", "ab"));
    assertEquals(4, times("Abc&abc&ABC&abC", "ab", false));
  }

  @Test
  void testSimplify() {
    assertEquals("sample english sentence.", simplify("  sample   english   sentence.   "));
  }

  @Test
  void testCase() {
    assertEquals("abc", toLowerCase("ABC", 0));
    assertEquals("ABC", toUpperCase("abc", 0));

    assertEquals("Abc", toLowerCase("ABC", 1));
    assertEquals("aBC", toUpperCase("abc", 1));

    assertEquals("AbC", toLowerCase("ABC", 1, 1));
    assertEquals("aBc", toUpperCase("abc", 1, 1));

    assertThrows(StringException.class, () -> toLowerCase("ABC", 1, 3));
    assertThrows(StringException.class, () -> toUpperCase("abc", 1, 3));

    // ======================================================

    assertEquals("ThereIsAWord", studlyCase("  there is a word"));
    assertEquals("ThereIsAWord", studlyCase("there_is_a_word  "));
    assertEquals("ThereIsAWord", studlyCase(" there-is-a-word "));
    assertEquals("", studlyCase("   "));

    assertEquals("thereIsAWord", camelCase("  ThereIsAWord  "));
    assertEquals("thereIsAWord", camelCase("  there is a word"));
    assertEquals("thereIsAWord", camelCase("there_is_a_word  "));
    assertEquals("thereIsAWord", camelCase(" there-is-a-word "));
    assertEquals("", camelCase("   "));

    assertEquals("there_is_a_word", snakeCase("  there is a word"));
    assertEquals("there_is_a_word", snakeCase("there_is_a_word  "));
    assertEquals("there_is_a_word", snakeCase(" there-is-a-word "));
    assertEquals("", snakeCase("   "));
    assertEquals("there_is_some_word", snakeCase("thereIsSomeWord", Cases.LOWERCASE));
    assertEquals("there_is_a_word", snakeCase("thereIsAWord", Cases.LOWERCASE));
    assertEquals("THERE_IS_A_WORD", snakeCase("ThereIsAWord", Cases.UPPERCASE));

    assertEquals("there-is-a-word", kebabCase("  there is a word", Cases.LOWERCASE));
    assertEquals("there-is-a-word", kebabCase("there_is_a_word  ", Cases.LOWERCASE));
    assertEquals("there-is-a-word", kebabCase(" there-is-a-word ", Cases.LOWERCASE));
    assertEquals("", kebabCase("   "));
    assertEquals("there-is-some-word", kebabCase("thereIsSomeWord", Cases.LOWERCASE));
    assertEquals("there-is-a-word", kebabCase("thereIsAWord", Cases.LOWERCASE));
    assertEquals("THERE-IS-A-WORD", kebabCase("ThereIsAWord", Cases.UPPERCASE));
  }

  @Test
  void testPad() {
    assertEquals("00000", repeatUntil("0", 5));
    assertEquals("01010", repeatUntil("01", 5));
    assertEquals("01201", repeatUntil("012", 5));

    // the string length is 25
    String sentence = "binary Numbers look like ";
    assertEquals(sentence, fill(sentence, ".", -20));
    assertEquals(">>>>>" + sentence, fill(sentence, ">", 30));
    assertEquals(sentence + "...", fill(sentence, ".", -28));
    assertEquals(sentence + "01010", fill(sentence, "01", -30));
    assertEquals("!@#!@#!" + sentence, fill(sentence, "!@#", 32));
  }

  @Test
  void testContains() {
    assertTrue(containsAll("abcDEF001", List.of("ab", "cD", "001")));
    assertTrue(containsAll("abcDEF001", List.of("ab", "bc", "cD")));
    assertTrue(containsAll("abcDEF001", false, true, List.of("ab", "bc", "cd")));
    assertTrue(containsAll("abcDEF001", false, false, List.of("ab", "cd", "ef")));

    assertFalse(containsAll("abcDEF001", List.of("ab", "cd", "001")));
    assertFalse(containsAll("abcDEF001", true, true, List.of("ab", "bc", "cd")));
    assertFalse(containsAll("abcDEF001", true, false, List.of("ab", "bc", "cD")));

    assertTrue(containsAny("abcDEF001", List.of("aba", "cD", "002")));
    assertTrue(containsAny("abcDEF001", false, List.of("aba", "cd", "002")));

    assertFalse(containsAny("abcDEF001", List.of("aba", "cdc", "002")));
  }

  @Test
  void testInsertAndRemove() {
    assertEquals("xyz123456", insert("123456", "xyz", 0));
    assertEquals("123xyz456", insert("123456", "xyz", 3));
    assertEquals("123456xyz", insert("123456", "xyz", 6));
    assertThrows(StringException.class, () -> insert("123456", "xyz", 7));

    assertEquals("1234xyz56", insert("123456", "xyz", -2));
    assertEquals("12xyz3456", insert("123456", "xyz", -4));

    assertEquals("123456", remove("123xyz456", 3, 6));
    assertEquals("123xyz", remove("123xyz456", 6, 9));
    assertThrows(StringException.class, () -> remove("123xyz456", 6, 10), "删除字符下标越界");
    assertThrows(StringException.class, () -> remove("123xyz456", -1, 3), "删除字符下标越界");
    assertThrows(StringException.class, () -> remove("123xyz456", 5, 4), "删除范围下标start不能大于end");

    assertEquals("xyz", remove("123xyz456", List.of("123", "456"), 0));
    assertEquals("", remove("123xyz456", List.of("123", "456", "xyz"), 0));
    assertEquals("_abc_123xyz456", remove("123xyz456_abc_123xyz456", List.of("123", "456", "xyz"), 1));
    assertEquals("123xyz456_abc_", remove("123xyz456_abc_123xyz456", List.of("123", "456", "xyz"), -1));
    assertEquals("123xyz456_abc_", remove("123xyz456_abc_123xyz456", List.of("123", "456", "XYZ"), -1, false));
  }

  @Test
  void testStartsAndEndsWith() {
    assertTrue(startsWith("123xyz456", "789", "456", "123"));
    assertFalse(startsWith("123xyz456", "abc", "xyz"));
    assertTrue(startsWith("abc123xyz", false, "123", "ABC"));

    assertTrue(endsWith("123xyz456", "789", "456", "123"));
    assertFalse(endsWith("123xyz456", "abc", "xyz"));
    assertTrue(endsWith("abc123xyz", false, "123", "XYZ"));

    assertTrue(startsWith("123xyz456", List.of("789", "456", "123")));
    assertFalse(startsWith("123xyz456", List.of("abc", "xyz")));
    assertTrue(startsWith("abc123xyz", false, List.of("123", "ABC")));

    assertTrue(endsWith("123xyz456", List.of("789", "456", "123")));
    assertFalse(endsWith("123xyz456", List.of("abc", "xyz")));
    assertTrue(endsWith("abc123xyz", false, List.of("123", "XYZ")));

    // ====================================================================================

    assertEquals("abcxyz", insureStartsWith("xyz", "abc", true));
    assertEquals("abcABCxyz", insureStartsWith("ABCxyz", "abc", true));
    assertEquals("ABCxyz", insureStartsWith("ABCxyz", "abc", false));

    assertEquals("abcxyz", insureEndsWith("abc", "xyz", true));
    assertEquals("abcXYZxyz", insureEndsWith("abcXYZ", "xyz", true));
    assertEquals("abcXYZ", insureEndsWith("abcXYZ", "xyz", false));
  }

  @Test
  void testBrief() {
    assertEquals("Install the plugi...", brief("Install the plugin; Restart Eclipse and go to Window", 20, "..."));
    assertEquals("默认逻辑是当表单验证失败时,把按钮...", brief("默认逻辑是当表单验证失败时,把按钮给变灰色", 20, "..."));
  }

  @Test
  void testWrap() {
    assertEquals("abc[123]xyz[123]", wrap("abc123xyz123", "123", "[", "]"));
  }

  @Test
  void testReverse() {
    assertEquals("321", reverse("123"));
    assertEquals("zyx_cba", reverse("abc_xyz"));
  }

  @Test
  void testLastUntil() {
    assertEquals("core", lastUntil("/root/dean/core", "/", false));
    assertEquals("/core", lastUntil("/root/dean/core", "/", true));
    assertEquals("/core", lastUntil("/root/dean/core", "/dean", false));
    assertEquals("/dean/core", lastUntil("/root/dean/core", "/dean", true));
    assertEquals("/root/dean/core", lastUntil("/root/dean/core", "=", false));
    assertEquals("/core", lastUntil("/root/dean/core", "/core", true));
    assertEquals("", lastUntil("/root/dean/core", "/core", false));
  }

  @Test
  void testFrontUntil() {
    assertEquals("C:", frontUntil("C:/windows/system32", "/", false));
    assertEquals("C:/", frontUntil("C:/windows/system32", "/", true));
    assertEquals("C:/windows/system32", frontUntil("C:/windows/system32", "=", false));
    assertEquals("C:", frontUntil("C:/windows/system32", "C:", true));
    assertEquals("", frontUntil("C:/windows/system32", "C:", false));
  }

  @Test
  void testEquals() {
    assertTrue(equalsAny("1", "1", "2", "3"));
    assertFalse(equalsAny("1", "2", "3", "4"));

    assertTrue(equalsAny("abc", false, "abc", "xyz", "123"));
    assertTrue(equalsAny("abc", false, "Abc", "xyz", "123"));
    assertFalse(equalsAny("abc", false, "xyz", "123"));
  }
}
