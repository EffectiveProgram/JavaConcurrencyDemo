package com.jack.demo.concurrency.geekbang.base;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 避免死锁的demo
 * 发生死锁的4个条件
 * - 互斥，共享资源 X 和 Y 只能被一个线程占用；
 * - 占有且等待，线程 T1 已经取得共享资源 X，在等待共享资源 Y 的时候，不释放共享资源 X；
 * - 不可抢占，其他线程不能强行抢占线程 T1 占有的资源；
 * - 循环等待，线程 T1 等待线程 T2 占有的资源，线程 T2 等待线程 T1 占有的资源，就是循环等待。
 * @author: Jack
 * @date: 2020/11/8
 */
public class DeadLockDemo {

    public static void main(String[] args) {
        test1();
    }


    private static void test1() {
    }

    /**
     * 该类会死锁
     */
    private static class Account1 {
        private int balance;

        void transfer(Account1 target, int amt) {
            synchronized (this) {
                synchronized (target) {
                    if (this.balance > amt) {
                        this.balance -= amt;
                        target.balance += amt;
                    }
                }
            }
        }
    }

    /**
     * 破坏死锁的条件之一：占用且等待条件
     * 增加一个锁管理员（单例），由该管理员为线程一次性分配和释放所有的锁
     */
    private static class Account2 {
        private Allocator actr;
        private int balance;

        void transfer(Account2 target, int amt) {
            while(!actr.apply(this, target));

            try {
                synchronized (this) {
                    synchronized (target) {
                        if (this.balance > amt) {
                            this.balance -= amt;
                            target.balance += amt;
                        }
                    }
                }
            } finally {
                {
                    actr.free(this, target);
                }
            }
        }
    }

    private static class Account3 {
        private int id;
        private int balance;

        void transfer(Account3 target, int amt) {
            Account3 small = this;
            Account3 big = target;
            if (this.id > big.id) {
                small = target;
                big = this;
            }
            synchronized (small) {
                synchronized (big) {
                    if (this.balance > amt) {
                        this.balance -= amt;
                        target.balance += amt;
                    }
                }
            }
        }
    }

    /**
     * 破坏死锁的条件之一：占用且等待条件
     * 增加一个锁管理员（单例），由该管理员为线程一次性分配和释放所有的锁
     */
    static class Allocator {
        private List<Object> als = new ArrayList<>();

        // 一次性申请所有资源
        synchronized boolean apply(Object from, Object to) {
            if (als.contains(from) || als.contains(to)) {
                return false;
            } else {
                als.add(from);
                als.add(to);
            }
            return true;
        }

        // 归还资源
        synchronized void free(Object from, Object to) {
            als.remove(from);
            als.remove(to);
        }
    }
}
