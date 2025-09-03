package cn.labzen.tool.feature.call;

/**
 * 调用者信息，包括类class及方法名
 */
public record CallDetail(Class<?> type, String methodName) {

}
