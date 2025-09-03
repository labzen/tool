package cn.labzen.tool.feature.call;

/**
 * 获取当前线程的调用者信息
 */
public final class Caller {

  private static final int OFFSET_START_AT_STACK_TRACE = 1;
  private static final String CALLER_NAME = Caller.class.getName();

  private Caller() {
  }

  /**
   * 获取调用者信息，默认获取最近的调用者，默认获取堆栈深度为1
   *
   * @return 调用者信息
   * @see Caller#get(int)
   */
  public static CallDetail get() {
    return get(1);
  }

  /**
   * 获取调用者信息，默认获取最近的调用者
   *
   * @param frameDepth 调用者栈帧深度，即获取当前方法最近的调用者。取值0时，获取到的是本方法
   * @return 调用者信息
   */
  public static CallDetail get(int frameDepth) {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    if (stackTrace.length == 0) {
      return null;
    }

    int offset = offset(stackTrace);
    int index = offset + frameDepth;
    if (index >= stackTrace.length) {
      return null;
    }

    StackTraceElement element = stackTrace[index];
    try {
      return new CallDetail(Class.forName(element.getClassName()), element.getMethodName());
    } catch (ClassNotFoundException e) {
      return null;
    }
  }

  /**
   * 找出偏移量，用于定位调用本类方法的最近方法，即使用该类的方法栈帧在栈中的下标
   */
  private static int offset(StackTraceElement[] stackTrace) {
    int start = OFFSET_START_AT_STACK_TRACE;
    while (true) {
      if (stackTrace[start++].getClassName().equals(CALLER_NAME)) {
        break;
      }
    }
    return start;
  }

  /**
   * 判断当前方法的调用者是否为给出的类
   */
  public static boolean is(Class<?> suspectClass) {
    CallDetail callDetail = get();
    if (callDetail == null) {
      return false;
    }

    return callDetail.getClass().equals(suspectClass);
  }

  /**
   * 判断当前方法的调用者是否为给出的类（及方法）
   */
  public static boolean is(Class<?> suspectClass, String suspectMethodName) {
    CallDetail callDetail = get();
    if (callDetail == null) {
      return false;
    }

    return callDetail.getClass().equals(suspectClass) && callDetail.methodName().equals(suspectMethodName);
  }

  /**
   * 判断当前方法的调用者栈中是否存在给出的类，也就是判断当前方法是否被给出的类间接的调用
   *
   * @return 如果调用者栈中包含给出的类，返回true
   */
  public static boolean from(Class<?> suspectClass) {
    return from(suspectClass, null);
  }

  /**
   * 判断当前方法的调用者栈中是否存在给出的类（及方法），也就是判断当前方法是否被给出的类（及方法）间接的调用
   *
   * @return 如果调用者栈中包含给出的类，返回true
   */
  public static boolean from(Class<?> suspectClass, String suspectMethodName) {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    if (stackTrace.length == 0) {
      return false;
    }

    String suspectClassName = suspectClass.getName();
    for (StackTraceElement element : stackTrace) {
      if (element.getClassName().equals(suspectClassName) &&
          (suspectMethodName == null || element.getMethodName().equals(suspectMethodName))) {
        return true;
      }
    }
    return false;
  }
}
