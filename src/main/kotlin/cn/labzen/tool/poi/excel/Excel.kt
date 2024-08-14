package cn.labzen.tool.poi.excel

class Excel<T> private constructor(private val clazz: Class<T>) {

  init {

  }

  fun write(data: List<T>) {

  }

  companion object {
    private val cachedMappingBeanClasses = mutableMapOf<Class<*>, Excel<*>>()

    @Suppress("UNCHECKED_CAST")
    fun <T> create(clazz: Class<T>): Excel<T> =
      cachedMappingBeanClasses.computeIfAbsent(clazz) {
        Excel(clazz)
      } as Excel<T>
  }
}

fun main() {
  val data = listOf(
    Bean("Dean", 40, 20),
    Bean("Kaboo", 30, 10),
    Bean("Sam", 50, 15),
  )

  val excel = Excel.create(Bean::class.java)
  excel.write(data)
}

data class Bean(val name: String = "", val age: Int = 0, val depth: Int = 0)
