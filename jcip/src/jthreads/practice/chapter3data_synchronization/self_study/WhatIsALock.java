package jthreads.practice.chapter3data_synchronization.self_study;

import java.util.Date;

/**
 * @author ackerley
 * @date 2019-05-08 23:34
 */
public class WhatIsALock {
    public static void main(String[] args) {
        ThreadSafeClass1 test1 = new ThreadSafeClass1();
        new Thread() {
            @Override
            public void run() {
                test1.method1();
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                test1.method2();
            }
        }.start();

    }
}

/**
 * one lock、two methods...
 */
class ThreadSafeClass1 {
    private final Object lockObj = new Object();

    public void method1() {
        synchronized (lockObj) {
            System.out.println("method1 will try to hold the object lock for 5s...time now: " + new Date());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException excp) {
                excp.printStackTrace();
            }
        }
    }

    public void method2() {
        synchronized (lockObj) {
            System.out.println("method2...                                        time now: " + new Date());
        }
    }

}
