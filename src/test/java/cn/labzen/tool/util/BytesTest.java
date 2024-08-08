package cn.labzen.tool.util;

import com.google.common.collect.Maps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Map;

import static cn.labzen.tool.util.Bytes.*;

public class BytesTest {

  @Test
  void testHex() {
    String original = "410f1b";
    byte[] bytes = hexStringToBytes(original);
    String hex = bytesToHexString(bytes, false);

    Assertions.assertEquals(original, hex);

    String binary = hexStringToBinaryString("03");
    Assertions.assertEquals("00000011", binary);
  }

  @Test
  void testAscii() {
    String original = "ascii0123";
    byte[] bytes = asciiStringToBytes(original);
    String ascii = bytesToAsciiString(bytes);

    Assertions.assertEquals(original, ascii);
  }

  @Test
  void testInt() {
    int original = 12498761;
    byte[] bytes = intToBytes(original);
    int integer = bytesToInt(bytes);

    Assertions.assertEquals(original, integer);
  }

  @Test
  void testBigInt() {
    BigInteger original = new BigInteger("4546928818324112");
    byte[] bytes = bigIntToBytes(original);
    BigInteger integer = bytesToBigInt(bytes);

    Assertions.assertEquals(original, integer);
  }

  @Test
  void testByteArray() {
    long number = Randoms.longNumber(Long.MAX_VALUE);
    byte[] bytes = longToBytes(number);
    long recovered = bytesToLong(bytes);

    Assertions.assertEquals(number, recovered);
  }

  @Test
  void testObjectArray() {
    Map<String, Integer> params = Maps.newHashMap();
    params.put("a", 1);
    params.put("b", 2);

    byte[] bytes = objectToBytes(params);
    Object restored = bytesToObject(bytes);

    Assertions.assertEquals(params, restored);

    Bean bean = new Bean("Dean", 18);
    byte[] beanBytes = objectToBytes(bean);
    Object restoredBean = bytesToObject(beanBytes);

    Assertions.assertNotNull(restoredBean);
    Assertions.assertEquals(Bean.class, restoredBean.getClass());
    Bean rb = (Bean) restoredBean;
    Assertions.assertEquals(bean.age, rb.age);
  }

  static class Bean implements Serializable {

    private final String name;
    private final int age;

    public Bean(String name, int age) {
      this.name = name;
      this.age = age;
    }

    public String getName() {
      return name;
    }

    public int getAge() {
      return age;
    }
  }
}
