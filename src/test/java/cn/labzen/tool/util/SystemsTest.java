package cn.labzen.tool.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SystemsTest {

  @Test
  void testInfo() {
    Assertions.assertNotNull(Systems.getOsName());
    Assertions.assertEquals(64, Systems.getOsArch());
  }

}
