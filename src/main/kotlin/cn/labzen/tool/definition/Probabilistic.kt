package cn.labzen.tool.definition

/**
 * 表达概率性（主观评定）
 */
enum class Probabilistic {

  /**
   * 100%
   */
  DEFINITELY,

  /**
   * 70%
   */
  PROBABLY,

  /**
   * 50%
   */
  MAYBE,

  /**
   * 30%
   */
  PERHAPS,

  /**
   * 10%
   */
  POSSIBLY,

  /**
   * 0%
   */
  NEVER
}
