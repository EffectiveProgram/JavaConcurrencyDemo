package com.jack.demo.concurrency.geekbang.base;

/**
 * @description: 双检锁demo
 * @author: Jack
 * @date: 2020/11/8
 */
public class Singleton {
    static volatile Singleton instance; // 单例对象要加volatile修饰
    static Singleton getInstance(){
        if (instance == null) {
            synchronized(Singleton.class) {
                if (instance == null)
                    instance = new Singleton();
            }
        }
        return instance;
    }
}