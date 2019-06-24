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

    public void setWechatCircleAd(boolean enable) {
        put(Key.WECHAT_CIRCLE_AD, enable);
    }

    public boolean isWechatCircleAd(boolean enable) {
        return (boolean) get(Key.WECHAT_CIRCLE_AD, enable);
    }

    public void setWechatCircleWithdraw(boolean enable) {
        put(Key.WECHAT_CIRCLE_WITHDRAW, enable);
    }

    public boolean isWechatCircleWithdraw(boolean enable) {
        return (boolean) get(Key.WECHAT_CIRCLE_WITHDRAW, enable);
    }

    public void setWechatWindowAutoLogin(boolean enable) {
        put(Key.WECHAT_WINDOW_AUTO_LOGIN, enable);
    }

    public boolean isWechatWindowAutoLogin(boolean enable) {
        return (boolean) get(Key.WECHAT_WINDOW_AUTO_LOGIN, enable);
    }

    public void setWechatRedPacketShowId(boolean enable) {
        put(Key.WECHAT_RED_PACKET_SHOW_ID, enable);
    }

    public boolean isWechatRedPacketShowId(boolean enable) {
        return (boolean) get(Key.WECHAT_RED_PACKET_SHOW_ID, enable);
    }

    public void setWechatRedPacketQuickOpen(boolean enable) {
        put(Key.WECHAT_RED_PACKET_QUICK_OPEN, enable);
    }

    public boolean isWechatRedPacketQuickOpen(boolean enable) {
        return (boolean) get(Key.WECHAT_RED_PACKET_QUICK_OPEN, enable);
    }

    private static class Key {
        private static final String WECHAT_FINGER_GUESS = "wechat_finger_guess"; // 微信猜拳
        private static final String WECHAT_DICE_GAME = "wechat_dice_game"; // 微信骰子点数

        private static final String WECHAT_MESSAGE_WITHDRAW = "wechat_message_withdraw"; // 微信，防撤回

        private static final String WECHAT_CIRCLE_AD = "wechat_circle_ad"; // 微信，朋友圈广告

        private static final String WECHAT_CIRCLE_WITHDRAW = "wechat_circle_withdraw"; // 微信，朋友圈，防删除

        private static final String WECHAT_WINDOW_AUTO_LOGIN = "wechat_window_auto_login"; // 微信，电脑端，自动登录

        private static final String WECHAT_RED_PACKET_QUICK_OPEN = "wechat_red_packet_quick_open"; // 微信，抢红包，快速打开
        private static final String WECHAT_RED_PACKET_SHOW_ID = "wechat_red_packet_show_id"; // 微信，抢完红包，copy Wechat ID
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
