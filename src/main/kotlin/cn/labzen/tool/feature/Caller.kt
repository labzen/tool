@file:Suppress("unused")

package cn.labzen.tool.feature

/**
 * 获取当前线程的调用者信息
 */
object Caller {

  private const val OFFSET_START_AT_STACK_TRACE = 1
  private val CALLER_NAME = Caller.javaClass.name

  /**
   * 获取调用者信息，默认获取最近的调用者
   * @param frameDepth Int 调用者栈帧深度，默认1，即获取当前方法最近的调用者。取值0时，获取到的是本方法
   * @return CallerDetail? 调用者信息
   */
  @JvmStatic
  @JvmOverloads
  fun get(frameDepth: Int = 1): Detail? {
    val stackTrace = Thread.currentThread().stackTrace
    if (stackTrace.isEmpty()) return null

    val offset = offset(stackTrace)
    val index = offset + frameDepth
    if (index >= stackTrace.size) return null

    val stackTraceElement = stackTrace[index]
    return with(stackTraceElement) { Detail(Class.forName(className), methodName) }
  }

  /**
   * 判断当前方法的调用者是否为给出的类（及方法）
   * @param suspectClass Class<*> 疑似调用者类
   * @param suspectMethod String? 疑似调用者方法
   * @return Boolean true如果调用者就是给出的类（及方法）
   */
  @JvmStatic
  @JvmOverloads
  fun `is`(suspectClass: Class<*>, suspectMethod: String? = null) = get()?.let {
    it.classType == suspectClass && (suspectMethod?.equals(it.methodName) ?: true)
  } ?: false

  /**
   * 判断当前方法的调用者栈中是否存在给出的类（及方法），也就是判断当前方法是否被给出的类（及方法）间接的调用
   * @param suspectClass Class<*> 疑似调用者类
   * @param suspectMethod String? 疑似调用者方法
   * @return Boolean true如果调用者栈中包含给出的类（及方法）
   */
  @JvmStatic
  @JvmOverloads
  fun from(suspectClass: Class<*>, suspectMethod: String? = null): Boolean {
    val stackTrace = Thread.currentThread().stackTrace
    if (stackTrace.isEmpty()) return false

    // TODO 代理类的判断
    stackTrace.forEach {
      val hit = suspectClass.name === it.className
          && (suspectMethod?.equals(it.methodName) ?: true)
      if (hit) return true
    }
    return false
  }

  /**
   * 找出偏移量，用于定位调用本类方法的最近方法，即使用该类的方法栈帧在栈中的下标
   */
  private fun offset(stackTrace: Array<StackTraceElement>): Int {
    var start = OFFSET_START_AT_STACK_TRACE
    while (true) {
      if (stackTrace[++start].className !== CALLER_NAME) break
    }
    return start
  }

  /**
   * 调用者信息，包括类class及方法名
   * @param classType Class<*> 调用者类型
   * @param methodName String 调用者方法名
   */
  data class Detail(val classType: Class<*>, val methodName: String)

}

