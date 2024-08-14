package cn.labzen.tool.poi.excel.annotation

import cn.labzen.tool.poi.excel.converter.CellDataConverter
import cn.labzen.tool.poi.excel.converter.CellDataNopConverter
import cn.labzen.tool.poi.excel.appender.ColumnAppender
import cn.labzen.tool.poi.excel.appender.ColumnNopAppender
import kotlin.reflect.KClass

/**
 * 设置Excel列信息
 *
 * @property headerName 列头标题
 * @property order 列排序
 * @property defaultValue 单元格数据为null（或空字符串）时的默认值
 * @property converter 单元格数据转换器，需要实现[CellDataConverter]接口，当指定了转换器后，原数据将会先经过转换器进行处理
 * @property converterLite 单元格数据简易转换表达式，适合对简单、可枚举的无意义数据进行转换，格式为 `[data]=[text](,[data]=[text])...`，例如`0=是,1=否`
 * @property appender 列尾追加器，可用于对整列数据的最后，追加一个数据，具体数据内容可根据整列数据进行计算，例如求和、求平均数等
 * @property formatOfPrefix 单元格数据附加前缀
 * @property formatOfSuffix 单元格数据附加后缀
 * @property formatOfDate 单元格日期数据的格式化，默认 yyyy-MM-dd HH:mm:ss
 * @property formatOfDecimals 单元格小数数据的格式化，默认 %.2f，可参考String.format()
 * @property formatOfEnum 单元格数据严格按指定的枚举类型进行格式，如果设置了枚举类，则会将原数据转换为枚举值后作为单元格数据
 * @property formatOfEnumOptions 单元格输出为下拉列表选项，可选值为[formatOfEnum]指定的枚举类，默认为false，如果不设置[formatOfEnum]则无效
 * @property formatOfImage 单元格数据格式化显示为图片，合法取值为 `[width]:[height]`，只要有合法值，则会将原数据转换为指定宽高的图片输出到excel中
 *
 * todo 考虑bean中有list map这种field，增加新的注解
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExcelColumn(
  val order: Int = 0,
  val headerName: String,
  val defaultValue: String = "",
  val converter: KClass<out CellDataConverter<*>> = CellDataNopConverter::class,
  val converterLite: String = "",
  val appender: KClass<out ColumnAppender> = ColumnNopAppender::class,
  val formatOfPrefix: String = "",
  val formatOfSuffix: String = "",
  val formatOfDate: String = "yyyy-MM-dd HH:mm:ss",
  val formatOfDecimals: String = "%.2f",
  val formatOfEnum: KClass<Enum<*>> = Enum::class,
  val formatOfEnumOptions: Boolean = false,
  val formatOfImage: String = "",
)
