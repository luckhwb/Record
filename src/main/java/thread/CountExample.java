package thread;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.Executors.newCachedThreadPool;

/**
 * Created by Hua wb on 2018/7/23.
 */
public class CountExample {
    // 请求总数
    public static int clientTotal = 1000;

    // 同时并发执行的线程数
    public static int threadTotal = 200;
    public static AtomicInteger integer = new AtomicInteger();

    public static Integer count = 0;
    public static Integer count2 = 0;
    public static Map map = new HashMap();

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < clientTotal; i++) {
            map.put(i,0);
        }
        Date date = new Date();
        ExecutorService executorService = newCachedThreadPool();
        //信号量，此处用于控制并发的线程数
        final Semaphore semaphore = new Semaphore(threadTotal);
        //闭锁，可实现计数器递减
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal ; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try {
                    //执行此方法用于获取执行许可，当总计未释放的许可数不超过200时，
                    //允许通行，否则线程阻塞等待，直到获取到许可。
                    //semaphore.acquire();
                    add(finalI);
                    //释放许可
                    //semaphore.release();
                } catch (Exception e) {
                    //log.error("exception", e);
                    e.printStackTrace();
                }
                //闭锁减一
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();//线程阻塞，直到闭锁值为0时，阻塞才释放，继续往下执行
        executorService.shutdown();
        Date date2 = new Date();
        long l = date2.getTime() - date.getTime();
        System.out.println(l);
        System.out.println(map);
    }

    private static void add(Integer a) {
        synchronized (a){
            int i = (int) map.get(a);
            try {
                if (i == 0) {
                    Thread.sleep(500);
                }else {
                    Thread.sleep(250);
                }
                if (i  == 1){
                    return;
                }
                Thread.sleep(250);
                i++;
                map.put(a,(int) map.get(a) + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
