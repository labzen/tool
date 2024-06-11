package cn.labzen.tool.feature

import cn.labzen.tool.exception.MultiTryAllFailureException

/**
 * 多个try代码块的顺序尝试，直至成功
 *
 * 当有相近的多个功能代码块需要执行，但代码块会抛出异常；需求上几个代码块之间属于互斥，即第一个代码块执行成功，后续的代码块不需要再执行
 *
 * 例如，当需要对一个日期字符串进行解析时，因不知道字符串的日期格式，又想兼容多个格式，则需要进行很多已知格式的逐个尝试。但日期格式的解析会抛出异常，
 * 如果用常规的代码结构编写，则会出现很多的try-catch块，而且需要记录每次的执行结果，进行判断。使用本工具类，会简化代码结构
 *
 * ==============================
 *
 * 原有的代码结构：
 * ```java
 * String str = "2022-04-01 12:00:00";
 * LocalDateTime ldt = null;
 *
 * try {
 *   ldt = LocalDateTime.parse(str, DateTimeFormatter.ISO_DATE_TIME);
 * } catch (DateTimeParseException e) {}
 *
 * if (ldt == null) {
 *   try {
 *     ldt = LocalDateTime.parse(str, DateTimeFormatter.ISO_DATE);
 *   } catch (DateTimeParseException e) {}
 * }
 *
 * if (ldt == null) {
 *   try {
 *     ldt = LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
 *   } catch (DateTimeParseException e) {}
 * }
 * ```
 *
 * 使用本工具的代码结构：
 * ```java
 * String str = "2022-04-01 12:00:00";
 * MultiTry.Trier<String, LocalDateTime> mt = MultiTry.with(str);
 * LocalDateTime ldt = mt.attempt(s -> LocalDateTime.parse(s, DateTimeFormatter.ISO_DATE_TIME))
 *                       .attempt(s -> LocalDateTime.parse(s, DateTimeFormatter.ISO_DATE))
 *                       .attempt(s -> LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
 *                       .execute();
 * ```
 */
object MultiTry {
  // todo 未写测试

  /**
   * 根据某一个值，顺序执行多个代码块，直至第一个执行成功（未抛出异常的）代码块后，返回该代码块的结果
   */
  @JvmStatic
  fun <I, O> with(input: I) =
    Trier<I, O>(input)

  /**
   * 根据某一个值，顺序执行多个代码块，直至第一个执行成功（未抛出异常的）代码块后，结束
   */
  @JvmStatic
  fun <I> withoutReturn(input: I) =
    NoOutTrier<I>(input)

  /**
   * 顺序执行多个代码块，直至第一个执行成功（未抛出异常的）代码块后，返回该代码块的结果
   */
  @JvmStatic
  fun <O> withoutValue() =
    NoInTrier<O>()

  /**
   * 顺序执行多个代码块，直至第一个执行成功（未抛出异常的）代码块后，结束
   */
  @JvmStatic
  fun justTry() =
    JustTrier()

  class Trier<I, O> internal constructor(private val value: I) {

    private val attempts = mutableListOf<(I) -> O>()

    fun attempt(block: (I) -> O): Trier<I, O> {
      attempts.add(block)
      return this
    }

    @Throws(MultiTryAllFailureException::class)
    fun execute(): O? {
      for (attempt in attempts) {
        try {
          return attempt(value)
        } catch (e: Exception) {
          // do nothing
        }
      }

      return null
    }
  }

  class NoOutTrier<I> internal constructor(private val value: I) {

    private val attempts = mutableListOf<(I) -> Unit>()

    fun attempt(block: (I) -> Unit): NoOutTrier<I> {
      attempts.add(block)
      return this
    }

    @Throws(MultiTryAllFailureException::class)
    fun execute() {
      for (attempt in attempts) {
        try {
          attempt(value)
          break
        } catch (e: Exception) {
          // do nothing
        }
      }
    }
  }

  class NoInTrier<O> internal constructor() {

    private val attempts = mutableListOf<() -> O>()

    fun attempt(block: () -> O): NoInTrier<O> {
      attempts.add(block)
      return this
    }

    @Throws(MultiTryAllFailureException::class)
    fun execute(): O? {
      for (attempt in attempts) {
        try {
          return attempt()
        } catch (e: Exception) {
          // do nothing
        }
      }
      return null
    }
  }

  class JustTrier internal constructor() {

    private val attempts = mutableListOf<() -> Unit>()

    fun attempt(block: () -> Unit): JustTrier {
      attempts.add(block)
      return this
    }

    @Throws(MultiTryAllFailureException::class)
    fun execute() {
      for (attempt in attempts) {
        try {
          attempt()
          break
        } catch (e: Exception) {
          // do nothing
        }
      }
    }
  }
}
