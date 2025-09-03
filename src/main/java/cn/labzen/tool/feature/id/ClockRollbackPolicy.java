package cn.labzen.tool.feature.id;

/**
 * 时钟回退策略
 */
public enum ClockRollbackPolicy {
  /**
   * 轻微回退等恢复（<= maxRollbackMs），超过阈值抛错。
   */
  AUTO,
  /**
   * 发现回退立即抛错。
   */
  FAST_FAIL,
  /**
   * 无条件等待到上一时间点（可能造成长时间阻塞）。
   */
  WAIT
}
