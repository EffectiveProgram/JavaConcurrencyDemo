package com.jack.demo.concurrency.geekbang.base;

/**
 * @description: 可见性问题demo
 * @author: Jack
 * @date: 2020/11/8
 */
public class VisibilityDemo {
    private static long count = 0;

    private static void add10K() {
        int idx = 0;
        while (idx++ < 10000) {
            count += 1;
        }
    }

    public static long calc() throws InterruptedException {

        // 创建两个线程，执行add操作
        Thread th1 = new Thread(() -> {
            add10K();
        });
        Thread th2 = new Thread(() -> {
            add10K();
        });
        th1.start();
        th2.start();

        th1.join();
        th2.join();
        System.out.println(count);
        return count;
    }

    private static void test1() {
        try {
            calc();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        test1();
    }
}

