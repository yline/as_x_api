package com.yline.xposed.config;

public class SPManager {
    private SPManager() {
    }

    private static SPManager sInstance;

    public static SPManager getInstance() {
        if (null == sInstance) {
            synchronized (SPManager.class) {
                if (null == sInstance) {
                    sInstance = new SPManager();
                }
            }
        }
        return sInstance;
    }

    private static class Key {

    }
}
