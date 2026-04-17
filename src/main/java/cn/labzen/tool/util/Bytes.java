package cn.labzen.tool.util;

import javax.annotation.Nonnull;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class Bytes {

  private static final char[] HEXES = "0123456789ABCDEF".toCharArray();

  private Bytes() {
  }

  /**
   * 十六进制串转化为 byte 数组
   */
  public static byte[] hexStringToBytes(String hex) {
    String it = hex.trim();
    if (it.length() % 2 != 0) {
      throw new IllegalArgumentException("Hex string length must be even.");
    }

    byte[] result = new byte[it.length() / 2];
    for (int i = 0; i < result.length; i++) {
      String sub = it.substring(i * 2, i * 2 + 2);
      result[i] = (byte) Integer.parseInt(sub, 16);
    }
    return result;
  }

  /**
   * 字节数组转换为十六进制字符串
   */
  public static String bytesToHexString(byte[] bytes, boolean uppercase) {
    StringBuilder sb = new StringBuilder(bytes.length * 2);
    for (byte b : bytes) {
      sb.append(HEXES[(b >> 4) & 0x0F]);
      sb.append(HEXES[b & 0x0F]);
    }
    String result = sb.toString();
    return uppercase ? result.toUpperCase() : result.toLowerCase();
  }

  public static String bytesToHexString(byte[] bytes) {
    return bytesToHexString(bytes, true);
  }

  /**
   * 十六进制字符串转二进制字符串
   */
  public static String hexStringToBinaryString(String hex) {
    StringBuilder sb = new StringBuilder();
    for (char c : hex.trim().toCharArray()) {
      int xx = Integer.valueOf(String.valueOf(c), 16);
      sb.append(Strings.fill(Integer.toBinaryString(xx), "0", 4));
    }
    return sb.toString();
  }

  /**
   * 字节数组转 ASCII 字符串
   */
  public static String bytesToAsciiString(byte[] bytes) {
    return new String(bytes, StandardCharsets.ISO_8859_1);
  }

  /**
   * ASCII 字符串转字节数组
   */
  public static byte[] asciiStringToBytes(String ascii) {
    ascii = ascii.trim();
    byte[] result = new byte[ascii.length()];
    for (int i = 0; i < ascii.length(); i++) {
      result[i] = (byte) ascii.charAt(i);
    }
    return result;
  }

  /**
   * 整型转字节数组
   */
  public static byte[] intToBytes(int number) {
    return new byte[]{(byte) (number & 0xff),
                      (byte) ((number >> 8) & 0xff),
                      (byte) ((number >> 16) & 0xff),
                      (byte) ((number >> 24) & 0xff)};
  }

  /**
   * 字节数组转整型
   */
  public static int bytesToInt(byte[] bytes) {
    int result = 0;
    for (int i = 0; i < bytes.length; i++) {
      result |= (bytes[i] & 0xff) << (i * 8);
    }
    return result;
  }

  /**
   * 大整形转字节数组（固定 32 字节）
   */
  public static byte[] bigIntToBytes(BigInteger number) {
    byte[] it = number.toByteArray();
    if (it.length == 33) {
      return Arrays.copyOfRange(it, 1, 33);
    } else if (it.length < 32) {
      byte[] data = new byte[32];
      System.arraycopy(it, 0, data, 32 - it.length, it.length);
      return data;
    } else {
      return it;
    }
  }

  /**
   * 字节数组转大整形
   */
  public static BigInteger bytesToBigInt(byte[] bytes) {
    if (bytes[0] < 0) {
      byte[] data = new byte[bytes.length + 1];
      System.arraycopy(bytes, 0, data, 1, bytes.length);
      return new BigInteger(data);
    } else {
      return new BigInteger(bytes);
    }
  }

  /**
   * 长整型转字节数组
   */
  public static byte[] longToBytes(long number) {
    return new byte[]{(byte) (number & 0xff),
                      (byte) ((number >> 8) & 0xff),
                      (byte) ((number >> 16) & 0xff),
                      (byte) ((number >> 24) & 0xff),
                      (byte) ((number >> 32) & 0xff),
                      (byte) ((number >> 40) & 0xff),
                      (byte) ((number >> 48) & 0xff),
                      (byte) ((number >> 56) & 0xff)};
  }

  /**
   * 字节数组转长整型
   */
  public static long bytesToLong(byte[] bytes) {
    long result = 0;
    for (int i = 0; i < bytes.length; i++) {
      result |= ((long) bytes[i] & 0xff) << (i * 8);
    }
    return result;
  }

  /**
   * 任意对象（Serializable) 转换为字节数组
   */
  public static byte[] objectToBytes(Object obj) {
    try (ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
         ObjectOutputStream objectOS = new ObjectOutputStream(byteOS)) {
      objectOS.writeObject(obj);
      return byteOS.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException("Object serialization failed", e);
    }
  }

  /**
   * 字节数组还原任意对象（Serializable)
   */
  @SuppressWarnings("unchecked")
  public static <T> T bytesToObject(byte[] bytes, @Nonnull Class<T> expectedType) {
    try (ByteArrayInputStream byteIS = new ByteArrayInputStream(bytes);
         ObjectInputStream objectIS = new ObjectInputStream(byteIS)) {

      Object obj = objectIS.readObject();

      // 类型验证
      if (!expectedType.isInstance(obj)) {
        throw new ClassCastException("Deserialized object type " +
                                     obj.getClass().getName() +
                                     " does not match expected type " +
                                     expectedType.getName());
      }

      return (T) obj;
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException("Object deserialization failed", e);
    }
  }

}
