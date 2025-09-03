package cn.labzen.tool.feature.id;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 高并发场景下System.currentTimeMillis()的性能问题的优化（参考 <a href="http://git.oschina.net/yu120/sequence">http://git.oschina.net/yu120/sequence</a>）
 * <p>
 * System.currentTimeMillis()的调用比new一个普通对象要耗时的多（具体耗时高出多少我还没测试过，有人说是100倍左右），<br/>
 * System.currentTimeMillis()之所以慢是因为去跟系统打了一次交道<br/>
 * 后台定时更新时钟，JVM退出时，线程自动回收
 * <p>
 * 1ms 粒度的缓存时钟：
 * <li>将 System.currentTimeMillis() 缓存在 AtomicLong，周期性刷新，降低高并发下频繁系统调用的成本。
 * <li>守护线程，JVM 退出不阻塞。
 */
public final class SystemClock {

  private static final long PERIOD_MS = 1L;
  private static final AtomicLong NOW = new AtomicLong(System.currentTimeMillis());

  static {
    ThreadFactory tf = r -> {
      Thread t = new Thread(r, "system-clock");
      t.setDaemon(true);
      t.setPriority(Thread.NORM_PRIORITY + 1);
      return t;
    };
    try (ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1, tf)) {
      exec.setRemoveOnCancelPolicy(true);
      exec.scheduleAtFixedRate(() -> NOW.set(System.currentTimeMillis()), PERIOD_MS, PERIOD_MS, TimeUnit.MILLISECONDS);
    }
  }

  private SystemClock() {
  }

  /**
   * 返回缓存的当前时间戳（毫秒）。
   */
  public static long now() {
    return NOW.get();
  }

  /**
   * 直接读取系统时间（用于应急校验）。
   */
  public static long realNow() {
    return System.currentTimeMillis();
  }
}
