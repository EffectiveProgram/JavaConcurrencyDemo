package com.jack.demo.concurrency.geekbang;

import java.util.concurrent.*;

public class CompletionServiceDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        test2();
    }

    /**
     * CompletionService 的实现原理也是内部维护了一个阻塞队列，当任务执行结束就把任务的执行结果加入到阻塞队列中
     * CompletionService 是把任务执行结果的 Future 对象加入到阻塞队列
     */
    private static void test2() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        CompletionService<Integer> cs = new ExecutorCompletionService<>(executor);
        cs.submit(() -> Repo.getPrice1());
        cs.submit(() -> Repo.getPrice2());
        cs.submit(() -> Repo.getPrice3());

        for (int i = 0; i < 3; i++) {
            Future<Integer> future = cs.take();
            Integer r = future.get();
            executor.execute(() -> Repo.save(r));
        }
        executor.shutdown();
    }

    /**
     * 有瑕疵的方案：如果获取电商 S1 报价的耗时很长，那么即便获取电商 S2 报价的耗时很短，也无法让保存 S2 报价的操作先执行，
     * 因为这个主线程都阻塞在了 f1.get() 操作上
     */
    private static void test1() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 异步向电商S1询价
        Future<Integer> f1 = executor.submit(() -> Repo.getPrice1());

        // 异步向电商S2询价
        Future<Integer> f2 = executor.submit(() -> Repo.getPrice2());

        // 异步向电商S3询价
        Future<Integer> f3 = executor.submit(() -> Repo.getPrice3());

        int r1 = f1.get();
        executor.execute(() -> Repo.save(r1));
        System.out.println("1");
        int r2 = f2.get();
        executor.execute(() -> Repo.save(r2));
        System.out.println("2");
        int r3 = f3.get();
        executor.execute(() -> Repo.save(r3));
        System.out.println("3");
        executor.shutdown();
    }

    private static class Repo {
        public static int getPrice1() {
            SleepUtils.sleep(10, TimeUnit.SECONDS);
            System.out.println("getPrice1, take 10s, return 10");
            return 10;
        }

        public static int getPrice2() {
            SleepUtils.sleep(1, TimeUnit.SECONDS);
            System.out.println("getPrice2, take 1s, return 1");
            return 1;
        }

        public static int getPrice3() {
            SleepUtils.sleep(5, TimeUnit.SECONDS);
            System.out.println("getPrice3 take 5s, return 5");
            return 5;
        }

        public static void save(int price) {
            System.out.println("save success, price = " + price);
        }
    }
}
