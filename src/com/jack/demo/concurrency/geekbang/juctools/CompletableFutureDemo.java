package com.jack.demo.concurrency.geekbang.juctools;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CompletableFutureDemo {
    public static void main(String[] args) {
        test3();
    }

    private static void test3() {
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
            SleepUtils.sleep(20, TimeUnit.SECONDS);
            return "Hello World";
        });
        CompletableFuture<String> stringCompletableFuture1 = stringCompletableFuture.thenApply(s -> s + "QQ");
        CompletableFuture<String> stringCompletableFuture2 = stringCompletableFuture1.thenApply(String::toUpperCase);
        System.out.println(stringCompletableFuture2.join());
    }

    private static void test2() {
        CompletableFuture f0 = CompletableFuture
                .supplyAsync(() -> {
                    SleepUtils.sleep(20, TimeUnit.SECONDS);
                    return "Hello World";
                })
                .thenApply(s -> s + " QQ")
                .thenApply(String::toUpperCase);
        System.out.println(f0.join());
    }

    private static void test1() {
        // 任务1：洗水壶->烧开水
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
            System.out.println("T1:洗水壶...");
            SleepUtils.sleep(1, TimeUnit.SECONDS);

            System.out.println("T1:烧开水...");
            SleepUtils.sleep(15, TimeUnit.SECONDS);
        });

        // 任务2：洗茶壶->洗茶杯->拿茶叶
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("T2:洗茶壶...");
            SleepUtils.sleep(1, TimeUnit.SECONDS);
            System.out.println("T2:洗茶杯...");
            SleepUtils.sleep(2, TimeUnit.SECONDS);
            System.out.println("T2:拿茶叶...");
            SleepUtils.sleep(1, TimeUnit.SECONDS);
            return "龙井";
        });

        // 任务3：任务1和任务2完成后执行：泡茶
        CompletableFuture<String> f3 = f1.thenCombine(f2, (x, tf) -> {
            System.out.println("T1:拿到茶叶：" + tf);
            System.out.println("T1:泡茶...");
            return "上茶：" + tf;
        });

        // 等待任务3执行结果
        System.out.println(f3.join());
    }

}
