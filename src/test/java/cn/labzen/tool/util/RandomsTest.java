package cn.labzen.tool.util;

import cn.labzen.tool.bean.Pair;
import cn.labzen.tool.definition.Constants;
import cn.labzen.tool.definition.Numbers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static cn.labzen.tool.util.Randoms.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;

public class RandomsTest {

  @Test
  void testNumber() {
    for (int i = 0; i < 500; i++) {
      assertEquals(0, intNumber(2, 1000, Numbers.EVEN_NUMBER) % 2);
      assertEquals(1, longNumber(1, 1000, Numbers.ODD_NUMBER) % 2);
    }
  }

  @Test
  void testString() {
    assertNotNull(string(10));
    assertEquals(10, string(10, NUMBERS).length());
    assertFalse(string(10, LETTERS).contains("0"));
    assertFalse(string(10, NUMBERS_WITHOUT_ZERO).contains("0"));
  }

  @Test
  void testCollection() {
    ArrayList<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
    MatcherAssert.assertThat(element(list), Matchers.is(Matchers.in(list)));
    MatcherAssert.assertThat(element(list), not(is(6)));

    Map<String, Integer> map = Maps.newHashMap();
    map.put("a", 1);
    map.put("b", 2);
    map.put("c", 3);
    Pair<String, Integer> pair = element(map);
    assertNotNull(pair);
    assertTrue(map.containsKey(pair.getFirst()));
    assertTrue(map.containsValue(pair.getSecond()));
    assertEquals(pair.getSecond(), map.get(pair.getFirst()));
  }

  @Test
  void testColor() {
    Color color = rgbColor();
    assertNotNull(color);

    String colorHex = hexColor();
    assertNotNull(colorHex);
    assertEquals(7, colorHex.length());
    assertEquals('#', colorHex.charAt(0));
  }

  @Test
  void testDate() {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Constants.PATTERN_OF_DATE_TIME);
    LocalDateTime b = LocalDateTime.parse("2018-05-01 12:00:00", dtf);
    LocalDateTime e = LocalDateTime.parse("2018-10-01 12:00:00", dtf);

    Pair<LocalDateTime, LocalDateTime> pair1 = new Pair<>(b, e);
    Pair<Date, Date> pair2 = new Pair<>(DateTimes.toDate(b), DateTimes.toDate(e));

    LocalDateTime localDateTime = localDateTime(pair1);
    Date date = date(pair2);

    assertNotNull(localDateTime);
    assertNotNull(date);

    assertTrue(localDateTime.isAfter(b));
    assertTrue(localDateTime.isBefore(e));
  }
}
