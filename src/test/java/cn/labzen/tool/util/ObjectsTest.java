package cn.labzen.tool.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static cn.labzen.tool.util.Objects.*;

public class ObjectsTest {

  @Test
  void testCanBeIntOrLong() {
    Object firstNonNull = firstNonNull(null, null, "1", "2");
    Assertions.assertNotNull(firstNonNull);
    Assertions.assertEquals("1", firstNonNull);

    Long long1 = canBeLong("123456");
    Assertions.assertNotNull(long1);
    Assertions.assertEquals(123456L, long1.longValue());

    Long long2 = canBeLong("-98765");
    Assertions.assertNotNull(long2);
    Assertions.assertEquals(-98765L, long2.longValue());

    Long long3 = canBeLong(null, 10L);
    Assertions.assertNotNull(long3);
    Assertions.assertEquals(10L, long3.longValue());

    Integer int1 = canBeInt("123");
    Assertions.assertNotNull(int1);
    Assertions.assertEquals(123, int1.intValue());

    Integer int2 = canBeInt("654");
    Assertions.assertNotNull(int2);
    Assertions.assertEquals(654, int2.intValue());

    Integer int3 = canBeInt("123.456", 10);
    Assertions.assertNotNull(int3);
    Assertions.assertEquals(10, int3.intValue());

    Integer int4 = canBeInt("2147483648");
    Assertions.assertNull(int4);
  }

  @Test
  void testAnyOrAll() {
    Assertions.assertTrue(isAllNull(null, null));
    Assertions.assertFalse(isAllNull(null, 1, 2));

    Assertions.assertTrue(isAllNotNull(1, 2));
    Assertions.assertFalse(isAllNotNull(1, 2, null));

    Assertions.assertTrue(isAllNullOrNot(1, 2));
    Assertions.assertTrue(isAllNullOrNot(null, null));
    Assertions.assertFalse(isAllNullOrNot(1, null));

    Assertions.assertTrue(isAnyNull(1, 2, null));
    Assertions.assertFalse(isAnyNull(1, 2));

    Assertions.assertTrue(isAnyNotNull(null, 1, 2));
    Assertions.assertFalse(isAnyNotNull(null, null));
  }

  @Test
  void testNull() {
    Assertions.assertTrue(isNullAt(1, 1, null));
    Assertions.assertFalse(isNullAt(1, 1, 2));

    Assertions.assertTrue(isLeftNull(null, 1));
    Assertions.assertFalse(isLeftNull(1, null));

    Assertions.assertTrue(isRightNull(1, null));
    Assertions.assertFalse(isRightNull(null, 1));

    Assertions.assertThrows(RuntimeException.class, () -> notNullOrThrow(null, () -> new RuntimeException("")));
    Assertions.assertDoesNotThrow(() -> notNullOrThrow(1, () -> new RuntimeException("")));
  }

  @Test
  void testEquals() {
    Assertions.assertThrows(RuntimeException.class, () -> equalsOrThrow(1, 2, () -> new RuntimeException("")));
    Assertions.assertDoesNotThrow(() -> equalsOrThrow(1, 1, () -> new RuntimeException("")));

    Object changed = equalsOrElse(1, 2, () -> true);
    Assertions.assertNotNull(changed);
    Assertions.assertEquals(Boolean.class, changed.getClass());
    Assertions.assertTrue((boolean) changed);
  }
}
