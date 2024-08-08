package cn.labzen.tool.util;

import cn.labzen.tool.util.assist.SimpleBean;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static cn.labzen.tool.util.Collections.*;
import static org.junit.jupiter.api.Assertions.*;

class CollectionsTest {

  @Test
  void testIsNullOrEmpty() {
    assertTrue(isNullOrEmpty(null));
    assertTrue(isNullOrEmpty(Lists.newArrayList()));
    assertFalse(isNullOrEmpty(Lists.newArrayList("")));
  }

  @Test
  void testRemoveNull() {
    List<String> source = Lists.newArrayList("1", "", "2", null, "3");
    List<String> expected = Lists.newArrayList("1", "", "2", "3");
    List<String> target = removeNullElement(source);
    assertNotNull(target);
    assertIterableEquals(expected, target);
  }

  @Test
  void testRemoveBlank() {
    List<String> source = Lists.newArrayList("1", "  ", "2", null, "3");
    List<String> expected = Lists.newArrayList("1", "2", "3");
    List<String> target = removeBlankElement(source);
    assertNotNull(target);
    assertIterableEquals(expected, target);
  }

  @Test
  void testAllEquals() {
    List<SimpleBean> collection = Lists.newArrayList();
    collection.add(new SimpleBean("a", 1, true));
    collection.add(new SimpleBean("b", 2, true));
    collection.add(new SimpleBean("c", 3, true));

    //Assertions.assertTrue(allEquals(collection, SimpleBean::getBooleanValue));
    //Assertions.assertFalse(allEquals(collection, SimpleBean::getStringValue));
    assertTrue(allSame(collection, SimpleBean::getBooleanValue));
    Assertions.assertFalse(allSame(collection, SimpleBean::getStringValue));
  }

  @Test
  void testInsureContains() {
    ArrayList<String> strings = Lists.newArrayList("a", "b", "c");
    String word = "x";

    strings = insureContains(strings, word);
    Assertions.assertEquals(4, strings.size());
    assertTrue(strings.contains(word));

    strings = insureContains(strings, word);
    Assertions.assertEquals(4, strings.size());
  }

  @Test
  void testOnValueAtLeast() {
    ArrayList<String> strings = Lists.newArrayList();
    String word = "x";

    strings = onValueAtLeast(strings, "x");
    Assertions.assertEquals(1, strings.size());

    strings = onValueAtLeast(strings, "x");
    Assertions.assertEquals(1, strings.size());
  }
}
