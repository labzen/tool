package cn.labzen.tool.poi.excel.converter

class CellDataNopConverter : CellDataConverter<Any> {

  override fun convert(data: Any): Any = data

  companion object {

    val INSTANCE = CellDataNopConverter()
  }
}
