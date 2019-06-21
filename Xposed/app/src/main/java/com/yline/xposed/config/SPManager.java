package com.yline.xposed.config;

import com.yline.application.SDKManager;
import com.yline.utils.SPUtil;

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

    public int getWechatFingerGuess(int defaultValue) {
        return (int) get(Key.WECHAT_FINGER_GUESS, defaultValue);
    }

    public void setWechatFingerGuess(int wechatFingerGuess) {
        put(Key.WECHAT_FINGER_GUESS, wechatFingerGuess);
    }

    public int getWechatDiceGame(int defaultValue) {
        return (int) get(Key.WECHAT_DICE_GAME, defaultValue);
    }

    public void setWechatDiceGame(int wechatDiceGame) {
        put(Key.WECHAT_DICE_GAME, wechatDiceGame);
    }

    public void setWechatMessageWithdraw(boolean enable) {
        put(Key.WECHAT_MESSAGE_WITHDRAW, enable);
    }

    public boolean isWechatMessageWithdraw(boolean enable) {
        return (boolean) get(Key.WECHAT_MESSAGE_WITHDRAW, enable);
    }

    private static class Key {
        private static final String WECHAT_FINGER_GUESS = "wechat_finger_guess"; // 微信猜拳
        private static final String WECHAT_DICE_GAME = "wechat_dice_game"; // 微信骰子点数

        private static final String WECHAT_MESSAGE_WITHDRAW = "wechat_message_withdraw"; // 微信，防撤回
    }

    private static void put(String key, Object value) {
        if (null == SDKManager.getApplication()) {
            return;
        }

        SPUtil.put(SDKManager.getApplication(), key, value);
    }

    private static Object get(String key, Object defaultObject) {
        if (null == SDKManager.getApplication()) {
            return defaultObject;
        }
        return SPUtil.get(SDKManager.getApplication(), key, defaultObject);
    }
}
