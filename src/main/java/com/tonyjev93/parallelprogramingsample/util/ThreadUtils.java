package com.tonyjev93.parallelprogramingsample.util;

public class ThreadUtils {

    private ThreadUtils() {
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
