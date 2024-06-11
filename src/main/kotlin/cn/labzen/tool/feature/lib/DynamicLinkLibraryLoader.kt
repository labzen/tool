package cn.labzen.tool.feature.lib

import cn.labzen.logger.kernel.LabzenLogger
import cn.labzen.logger.kernel.enums.Status
import cn.labzen.tool.exception.DynamicLinkLibraryLoadException
import cn.labzen.tool.kotlin.runIf
import cn.labzen.tool.kotlin.throwRuntimeIf
import cn.labzen.tool.util.Strings
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Paths
import java.util.function.Consumer

class DynamicLinkLibraryLoader private constructor(private val libraries: List<File>) {

  private var loaded = false
  private val report = DynamicLinkLibraryLoadReport()
  private var callback: Consumer<DynamicLinkLibraryLoadReport> = Consumer<DynamicLinkLibraryLoadReport> { report ->
    val loadedFilePaths = report.loadedFilePaths()
    val failureFilePaths = report.failureFilePaths()
    if (logger is LabzenLogger) {
      logger.info().scene(LOG_SCENE).status(Status.DONE)
        .log(
          "加载信息统计：共处理了 ${report.totalNumber()} 个动态链接库文件 " +
              "[成功加载: ${loadedFilePaths.size}; 加载失败: ${failureFilePaths.size}]."
        )
    } else {
      logger.info(
        "加载信息统计：共处理了 ${report.totalNumber()} 个动态链接库文件 " +
            "[成功加载: ${loadedFilePaths.size}; 加载失败: ${failureFilePaths.size}]."
      )
    }

    loadedFilePaths.forEach {
      if (logger is LabzenLogger) {
        logger.info().scene(LOG_SCENE).status(Status.SUCCESS).log("已加载文件: $it")
      } else {
        logger.info("已加载文件: $it")
      }
    }

    failureFilePaths.forEach {
      if (logger is LabzenLogger) {
        logger.warn().scene(LOG_SCENE).status(Status.WRONG).log("failed file: ${it.key}, cause ${it.value}")
      } else {
        logger.warn("failed file: ${it.key}, cause ${it.value}")
      }
    }
  }

  fun whenLoaded(consumer: Consumer<DynamicLinkLibraryLoadReport>) =
    this.apply { callback = consumer }

  fun load() {
    loaded.throwRuntimeIf { DynamicLinkLibraryLoadException("憋反复加载，有意思？") }
    if (logger is LabzenLogger) {
      logger.info().scene(LOG_SCENE).status(Status.STARTING).log("开始加载动态链接库文件..")
    } else {
      logger.info("开始加载动态链接库文件..")
    }

    var pending = libraries
    var times = 0
    while (pending.isNotEmpty() && times < MAX_RETRY_TIMES) {
      pending.forEach(::internalLoad)
      pending = report.failureFiles()
      times++
    }

    loaded = true
    callback.accept(report)
    if (logger is LabzenLogger) {
      logger.info().scene(LOG_SCENE).status(Status.COMPLETED).log("动态链接库文件加载成功.")
    } else {
      logger.info("动态链接库文件加载成功.")
    }
  }

  private fun internalLoad(file: File) {
    val files = mutableListOf<File>()
    foundFiles(file, files)

    files.forEach(::internalLoadIn)
  }

  private fun internalLoadIn(file: File) =
    try {
      System.load(file.path)
      report.loaded(file)
      // true
    } catch (t: Throwable) {
      report.failure(file, t)
      // false
    }

  private fun foundFiles(file: File, files: MutableList<File>) {
    if (file.isDirectory) {
      val subFiles: Array<File>? = file.listFiles()
      subFiles?.forEach { foundFiles(it, files) }
    } else {
      Strings.endsWith(file.name, false, EXTENSION_OF_FILE_DLL, EXTENSION_OF_FILE_SO).runIf {
        files.add(file)
      }
    }
  }

  companion object {
    private const val EXTENSION_OF_FILE_DLL = ".dll"
    private const val EXTENSION_OF_FILE_SO = ".so"
    private const val MAX_RETRY_TIMES = 5
    private const val LOG_SCENE = "Library"

    private val logger = LoggerFactory.getLogger(DynamicLinkLibraryLoader::class.java)

    private val projectRootPath by lazy {
      val systemResource = ClassLoader.getSystemResource("")
      if (systemResource != null) {
        systemResource.path
      } else {
        val resource = Thread.currentThread().contextClassLoader.getResource("")
        if (resource != null) {
          resource.path
        } else {
          ""
        }
      }
    }

    @JvmStatic
    fun from(vararg files: String): DynamicLinkLibraryLoader {
      val libraryRoots = files.mapNotNull { toDirectory(it) }
      return DynamicLinkLibraryLoader(libraryRoots)
    }

    private fun toDirectory(path: String): File? {
      val file = path.takeIf { it.startsWith("./") }?.let {
        Paths.get(projectRootPath, it).toFile()
      } ?: File(path)

      return if (file.exists()) {
        file
      } else {
        if (logger is LabzenLogger) {
          logger.warn().scene(LOG_SCENE).log("动态链接库路径找不到：$file")
        } else {
          logger.warn("动态链接库路径找不到：$file")
        }
        null
      }
    }
  }
}
