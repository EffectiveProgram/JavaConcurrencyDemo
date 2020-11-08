package com.jack.demo.concurrency.geekbang.juctools;

import java.util.concurrent.TimeUnit;

public class SleepUtils {
    public static void sleep(int t, TimeUnit u) {
        try {
            u.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
