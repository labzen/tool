package cn.labzen.tool.feature.lib;

import cn.labzen.logger.Loggers;
import cn.labzen.logger.kernel.LabzenLogger;
import cn.labzen.logger.kernel.enums.Status;
import cn.labzen.tool.exception.LibraryLoadException;
import cn.labzen.tool.util.Strings;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 动态链接库加载器
 */
public final class LibraryLoader {

  private static final String EXTENSION_OF_FILE_DLL = ".dll";
  private static final String EXTENSION_OF_FILE_SO = ".so";
  private static final int MAX_RETRY_TIMES = 5;
  private static final String LOG_SCENE = "Library";
  private static final String PROJECT_ROOT_PATH;

  private static final LabzenLogger LOGGER = Loggers.getLogger(LibraryLoader.class);

  private final List<File> libTargets;
  private final LibraryLoadReport report = new LibraryLoadReport();

  private boolean executed = false;

  static {
    URL systemResource = ClassLoader.getSystemResource("");
    if (systemResource != null) {
      PROJECT_ROOT_PATH = systemResource.getPath();
    } else {
      URL resource = Thread.currentThread().getContextClassLoader().getResource("");
      if (resource != null) {
        PROJECT_ROOT_PATH = resource.getPath();
      } else {
        PROJECT_ROOT_PATH = "";
      }
    }
  }

  private LibraryLoader(List<File> libTargets) {
    this.libTargets = libTargets;
  }

  /**
   * 开始加载动态链接库
   */
  public void load() {
    load(null);
  }

  /**
   * 开始加载动态链接库
   *
   * @param reportConsumer 当所有动态链接库全部加载处理完成后，会提交一份加载报告供开发者消费
   */
  public void load(Consumer<LibraryLoadReport> reportConsumer) {
    if (executed) {
      throw new LibraryLoadException("憋反复加载，有意思？");
    }

    LOGGER.atInfo().scene(LOG_SCENE).status(Status.STARTING).log("开始加载动态链接库文件...");

    // 复制副本
    List<File> pending = new ArrayList<>(libTargets);
    int times = 0;
    while (!pending.isEmpty() && times < MAX_RETRY_TIMES) {
      // 创建遍历副本
      List<File> currentBatch = new ArrayList<>(pending);
      // 清空待处理列表
      pending.clear();

      for (File dir : currentBatch) {
        internalLoad(dir);
      }

      // 收集本次失败的，重新加入待处理队列
      List<File> failures = report.failureFiles();
      if (!failures.isEmpty()) {
        pending.addAll(failures);
      }
      times++;
    }

    executed = true;
    if (reportConsumer != null) {
      reportConsumer.accept(report);
    }
    LOGGER.atInfo().scene(LOG_SCENE).status(Status.COMPLETED).log("动态链接库文件加载完成.");
  }

  private void internalLoad(File target) {
    List<File> foundFiles = new ArrayList<>();
    findFiles(target, foundFiles);

    foundFiles.forEach(this::loadIn);
  }

  private void loadIn(File file) {
    try {
      System.load(file.getAbsolutePath());
      report.loaded(file);
    } catch (Exception e) {
      report.failed(file, e);
    }
  }

  private void findFiles(File target, List<File> files) {
    if (target.isDirectory()) {
      File[] subFiles = target.listFiles();
      if (subFiles != null) {
        for (File subFile : subFiles) {
          findFiles(subFile, files);
        }
      }
    } else {
      boolean found = Strings.endsWith(target.getName(), false, EXTENSION_OF_FILE_DLL, EXTENSION_OF_FILE_SO);
      if (found) {
        files.add(target);
      }
    }
  }

  /**
   * 创建动态链接库加载器
   *
   * @param paths 存放动态链接库文件地址或存放的目录地址集合
   */
  public static LibraryLoader from(List<String> paths) {
    List<File> targets = paths.stream().map(LibraryLoader::turn2File).toList();
    return new LibraryLoader(targets);
  }

  private static File turn2File(String path) {
    File file;
    if (path.startsWith("./")) {
      file = Paths.get(path).toFile();
    } else {
      file = new File(path);
    }

    if (file.exists()) {
      return file;
    }

    LOGGER.atWarn().scene(LOG_SCENE).log("找不到动态链接库目录: {}", path);
    return null;
  }
}
