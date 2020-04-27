package com.hanframework.kit.futrue;

import java.util.List;
import java.util.concurrent.*;

/**
 * 如何重新定义线程执行器,其实在JDK抽象执行器中已经给出了实例代码。
 * 只需要实现{RunnableFuture}接口。重写{newTaskFor}方法,即可。
 * 需要注意一点的是,Runnable会被适配成Callable一样,这样做的目的
 * 是为了是他们能享用同一套代码逻辑
 * <pre> {@code
 * public class CustomThreadPoolExecutor extends ThreadPoolExecutor {
 *
 *     static class CustomTask<V> implements RunnableFuture<V> {...}
 *
 *     protected <V> RunnableFuture<V> newTaskFor(Callable<V> c) {
 *         return new CustomTask<V>(c);
 *     }
 *     protected <V> RunnableFuture<V> newTaskFor(Runnable r, V v) {
 *         return new CustomTask<V>(r, v);
 *     }
 *     // ... add constructors, etc.
 * }}</pre>
 *
 * @author liuxin
 * @version Id: EnhanceExecutorService.java, v 0.1 2019-06-12 10:55
 * @see AbstractExecutorService
 * @see DefaultEnhancePromiseTask
 */
public class EnhanceExecutorService extends AbstractExecutorService {

    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 定时任务打印,当前线程状态
     */
    private ScheduledExecutorService scheduledLogPrintExecutorService = new ScheduledThreadPoolExecutor(1);

    private void openSchedulePrintLog() {
        scheduledLogPrintExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                int activeCount = threadPoolExecutor.getActiveCount();
                int corePoolSize = threadPoolExecutor.getCorePoolSize();
                long taskCount = threadPoolExecutor.getTaskCount();
                long keepAliveTime = threadPoolExecutor.getKeepAliveTime(TimeUnit.MILLISECONDS);
                int poolSize = threadPoolExecutor.getPoolSize();
                int largestPoolSize = threadPoolExecutor.getLargestPoolSize();
                int maximumPoolSize = threadPoolExecutor.getMaximumPoolSize();
                int size = threadPoolExecutor.getQueue().size();
                System.err.println(
                        String.format("活跃线程数:[%d],队列任务:[%d],固定核心线程数:[%d],已完成任务数:[%d],池大小:[%d],活跃时间:[%dms],触顶线程池大小:[%d],最大池:[%d]",
                                activeCount, size, corePoolSize, taskCount, poolSize, keepAliveTime, largestPoolSize, maximumPoolSize));
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public static EnhanceExecutorService ofWrapper(ThreadPoolExecutor threadPoolExecutor) {
        return new EnhanceExecutorService(threadPoolExecutor);
    }

    private EnhanceExecutorService(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
        openSchedulePrintLog();
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new DefaultEnhancePromiseTask<>(runnable, value);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new DefaultEnhancePromiseTask<>(callable);
    }

    @Override
    public <T> EnhanceFuture<T> submit(Callable<T> task) {
        return (EnhanceFuture<T>) super.submit(task);
    }

    @Override
    public EnhanceFuture<?> submit(Runnable task) {
        return (EnhanceFuture<?>) super.submit(task);
    }

    @Override
    public void shutdown() {
        threadPoolExecutor.shutdown();
        scheduledLogPrintExecutorService.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        scheduledLogPrintExecutorService.shutdownNow();
        return threadPoolExecutor.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return threadPoolExecutor.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return threadPoolExecutor.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return threadPoolExecutor.awaitTermination(timeout, unit);
    }

    /**
     * 如何解决线程池ThreadLocal的问题 一般使用ThreadLocal 父子线程使用InheritableThreadLocal
     *
     * @param command 运行命令
     * @see ThreadLocal
     * @see InheritableThreadLocal
     */
    @Override
    public void execute(Runnable command) {
        threadPoolExecutor.execute(command);
    }

    public void sync(EnhanceFuture... enhanceFutures) {
        for (EnhanceFuture enhanceFuture : enhanceFutures) {
            try {
                enhanceFuture.sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}