package com.yline.lottery.sp;

import java.util.HashMap;

/**
 * 静态量
 *
 * @author yline 2019/3/18 -- 22:48
 * @version 1.0.0
 */
public class StaticManager {
    private static StaticManager mInstance;
    private static final HashMap<String, Boolean> hashMap = new HashMap<>();

    private StaticManager() {
    }

    public static StaticManager getInstance() {
        if (null == mInstance) {
            synchronized (StaticManager.class) {
                mInstance = new StaticManager();
            }
        }
        return mInstance;
    }

    public boolean isRefreshHistory() {
        Boolean isRefresh = hashMap.get(Key.REFRESH_MAIN_HISTORY);
        return (null != isRefresh && isRefresh);
    }

    public void setIsRefreshHistory(boolean isRefresh) {
        hashMap.put(Key.REFRESH_MAIN_HISTORY, isRefresh);
    }

    private static class Key {
        private static final String REFRESH_MAIN_HISTORY = "refresh_main_history"; // 首页，更新历史信息
    }
}
