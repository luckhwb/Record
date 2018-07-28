package thread;

import java.util.concurrent.FutureTask;

/**
 * Created by Hua wb on 2018/5/3.
 */
public class Callable {
    private static void doTaskWithResultInWorker() {
        java.util.concurrent.Callable<Integer> callable = () -> {
            System.out.println("Task starts");
            Thread.sleep(1000);
            int result = 0;
            for (int i=0; i<=100; i++) {
                result += i;
            }
            System.out.println("Task finished and return result");
            return result;
        };

        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        new Thread(futureTask).start();
        try {
            System.out.println("Before futureTask.get()");
            System.out.println("Result: " + futureTask.get());
            System.out.println("After futureTask.get()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        doTaskWithResultInWorker();
    }
}
