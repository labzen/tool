package cn.labzen.tool.poi.excel.annotation

/**
 * 设置Excel文件信息
 *
 * @property fileName 文件名
 * @property password 文件密码
 * @property headerStartRow 表头开始的行号，默认0，即第一行
 * @property disableDefaultValue 禁用表内单元格的默认数据，适用于导出模板（缺少数据，方便用户填写） todo 作为选项，不应该是注解
 * @property isXSSF 使用XSSF格式，即输出xlsx文件格式，默认true
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExcelSchema(
  val sheetName: String,
  val fileName: String = "",
  val password: String,
  val headerStartRow: Int = 0,
  val disableDefaultValue: Boolean = false,
  val isXSSF: Boolean = true,
)
