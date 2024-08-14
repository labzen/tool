package cn.labzen.tool.poi.excel.converter

interface CellDataConverter<R> {

  fun convert(data: Any): R
}
