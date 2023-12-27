import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author bkunzh
 * @date 2023/12/27
 */
public class ExecuteShellTimer {
    private static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2, new MyThreadFactory("ScheduledThreadPoolExecutor1-"));

    public static void scheduleOnce(Runnable runnable, Long afterSecond) {
        scheduledThreadPoolExecutor.schedule(runnable, afterSecond, TimeUnit.SECONDS);
    }

    public static void shutdownScheduledThreadPoolExecutor() {
        scheduledThreadPoolExecutor.shutdown();
    }

    //public static void main(String[] args) {
    //    scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
    //        ExecuteShell.main(args);
    //    }, 0L, 2L, TimeUnit.SECONDS);
    //}

    public static class MyThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public MyThreadFactory(String prefix) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = prefix +
                    poolNumber.getAndIncrement();
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + "-" + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
