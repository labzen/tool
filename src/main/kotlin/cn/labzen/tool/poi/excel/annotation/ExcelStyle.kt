package cn.labzen.tool.poi.excel.annotation

import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors

/**
 * Excel样式定义，注解使用在类上时，样式将适用于整个excel sheet页；注解使用在字段上时，样式仅适用于该字段对应的列上（优先级高）
 *
 * @property styleWidth 列宽度 todo 确认 单位
 * @property styleAlignment 列对齐方式
 * @property styleHeaderBackground 列头背景色
 * @property styleHeaderFontColor 列头字体颜色
 * @property styleCellBackground 单元格背景色，返回颜色个数大于1，则将逐行从指定的多个颜色中循环采用，例如：返回两个颜色，则会有背景颜色交替
 * @property styleCellFontColor 单元格字体颜色，返回颜色个数大于1，则将逐行从指定的多个颜色中循环采用，例如：返回两个颜色，则会有字体颜色交替
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExcelStyle(
  val styleWidth: Int = 20,
  val styleAlignment: HorizontalAlignment = HorizontalAlignment.LEFT,
  val styleHeaderBackground: IndexedColors = IndexedColors.GREY_40_PERCENT,
  val styleHeaderFontColor: IndexedColors = IndexedColors.BLACK,
  val styleCellBackground: Array<IndexedColors> = [IndexedColors.WHITE],
  val styleCellFontColor: Array<IndexedColors> = [IndexedColors.BLACK],
)
