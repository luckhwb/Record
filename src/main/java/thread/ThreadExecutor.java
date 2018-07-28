package thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hua wb on 2018/5/4.
 */
public class ThreadExecutor {
    public static void main(String[] args) {
        TimeUnit timeUnit = TimeUnit.MICROSECONDS;
        ThreadPoolExecutor poolExecutor = cachedThreadPool();
        for (int i = 0; i < 30; i++) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程:" + finalI +
                        "线程池中线程数目："+poolExecutor.getPoolSize()+
                        "查询等待："+ poolExecutor.getQueue().size()+"，已执行玩别的任务数目："+poolExecutor.getCompletedTaskCount());
            });
            poolExecutor.execute(thread);
        }
        poolExecutor.shutdown();
    }

    /**
     可缓存线程池：
     线程数无限制
     有空闲线程则复用空闲线程，若无空闲线程则新建线程
     一定程序减少频繁创建/销毁线程，减少系统开销
     * @return
     */
    public static ThreadPoolExecutor cachedThreadPool(){
       return (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    /**
     *定长线程池：
     可控制线程最大并发数（同时执行的线程数）
     超出的线程会在队列中等待
     * @return
     */
    public static ThreadPoolExecutor fixedThreadPool (){
        return (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
    }

    /**
     * 定长线程池：
     支持定时及周期性任务执行。
     * @return
     */
    public static ThreadPoolExecutor scheduledThreadPool (){
        return (ThreadPoolExecutor) Executors.newScheduledThreadPool(17);
    }
}
