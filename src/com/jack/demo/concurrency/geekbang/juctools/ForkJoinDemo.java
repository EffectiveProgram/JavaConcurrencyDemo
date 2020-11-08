package com.jack.demo.concurrency.geekbang.juctools;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinDemo {
    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        Fibonacci fibonacci = new Fibonacci(30);
        Integer result = forkJoinPool.invoke(fibonacci);
        System.out.println(result);

    }

    private static class Fibonacci extends RecursiveTask<Integer> {
        final int n;

        public Fibonacci(int n) {
            this.n = n;
        }

        @Override
        protected Integer compute() {
            if (n <= 1) {
                return n;
            }
            Fibonacci fibonacci1 = new Fibonacci(n - 1);
            fibonacci1.fork();
            Fibonacci fibonacci2 = new Fibonacci(n - 2);
            return fibonacci2.compute() + fibonacci1.join();
        }
    }
}
