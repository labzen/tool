package cn.labzen.tool.util

import cn.labzen.tool.util.Strings.fill
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.math.BigInteger
import java.nio.charset.StandardCharsets

object Bytes {

  private val hexes = charArrayOf(
    '0',
    '1',
    '2',
    '3',
    '4',
    '5',
    '6',
    '7',
    '8',
    '9',
    'A',
    'B',
    'C',
    'D',
    'E',
    'F'
  )


  /**
   * 十六进制串转化为byte数组
   */
  @JvmStatic
  fun hexStringToBytes(hex: String): ByteArray =
    hex.trim().let {
      require(it.length % 2 == 0)

      return ByteArray(it.length / 2) { i ->
        it.substring(i * 2, i * 2 + 2).toInt(16).toByte()
      }
    }

  /**
   * 字节数组转换为十六进制字符串
   */
  @JvmOverloads
  @JvmStatic
  fun bytesToHexString(bytes: ByteArray, uppercase: Boolean = true): String =
    bytes.joinToString("") {
      "${hexes[it.toInt() shr 4 and 0x0f]}${hexes[it.toInt() and 0x0f]}"
    }.let {
      if (uppercase) it.uppercase() else it.lowercase()
    }

  /**
   * 十六进制字符串转二进制字符串
   */
  @JvmStatic
  fun hexStringToBinaryString(hex: String) =
    hex.trim().toCharArray().joinToString("") { c ->
      val xx = Integer.valueOf(c.toString(), 16)
      fill(Integer.toBinaryString(xx), "0", 4)
    }

  /**
   * 字节数组转ASCII字符串
   */
  @JvmStatic
  fun bytesToAsciiString(bytes: ByteArray): String =
    String(bytes, StandardCharsets.ISO_8859_1)

  /**
   * ASCII字符串转字节数组
   */
  @JvmStatic
  fun asciiStringToBytes(ascii: String): ByteArray =
    ascii.trim().toCharArray().map { it.code.toByte() }.toByteArray()

  /**
   * 整型转字节数组
   */
  @JvmStatic
  fun intToBytes(number: Int): ByteArray =
    ByteArray(4) {
      (number shr (it * 8) and 0xff).toByte()
    }

  /**
   * 字节数组转整型
   */
  @JvmStatic
  fun bytesToInt(bytes: ByteArray): Int =
    bytes.mapIndexed { index, byte ->
      (byte.toInt() and 0xff) shl (index * 8)
    }.sum()

  /**
   * 大整形转字节数组
   */
  @JvmStatic
  fun bigIntToBytes(number: BigInteger): ByteArray =
    number.toByteArray().let {
      when {
        it.size == 33 -> {
          val data = ByteArray(32)
          System.arraycopy(it, 1, data, 0, 32)
          data
        }

        it.size < 32 -> {
          val data = ByteArray(32)
          for (i in 0 until 32 - it.size) {
            data[i] = 0
          }
          System.arraycopy(it, 0, data, 32 - it.size, it.size)
          data
        }

        else -> it
      }
    }

  /**
   * 字节数组转大整形
   */
  @JvmStatic
  fun bytesToBigInt(bytes: ByteArray): BigInteger =
    if (bytes[0] < 0) {
      val data = ByteArray(bytes.size + 1)
      data[0] = 0
      System.arraycopy(bytes, 0, data, 1, bytes.size)
      BigInteger(data)
    } else
      BigInteger(bytes)

  /**
   * 长整型转字节数组
   */
  @JvmStatic
  fun longToBytes(number: Long): ByteArray =
    ByteArray(8) {
      (number shr (it * 8) and 0xff).toByte()
    }

  /**
   * 字节数组转长整型
   */
  @JvmStatic
  fun bytesToLong(bytes: ByteArray): Long =
    bytes.mapIndexed { index, byte ->
      (byte.toLong() and 0xff) shl (index * 8)
    }.sum()

  /**
   * 任意对象（Serializable)转换为字节数组
   */
  @JvmStatic
  fun objectToBytes(obj: Any): ByteArray =
    ByteArrayOutputStream().use { byteOS ->
      ObjectOutputStream(byteOS).use { objectOS ->
        objectOS.writeObject(obj)
        byteOS.toByteArray()
      }
    }

  /**
   * 字节数组还原任意对象（Serializable)
   */
  @JvmStatic
  fun bytesToObject(bytes: ByteArray): Any =
    ByteArrayInputStream(bytes).use { byteIS ->
      ObjectInputStream(byteIS).use { objectIS ->
        objectIS.readObject()
      }
    }
}
