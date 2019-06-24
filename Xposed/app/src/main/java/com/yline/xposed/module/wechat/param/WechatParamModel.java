package com.yline.xposed.module.wechat.param;

import java.io.Serializable;

public class WechatParamModel implements Serializable {
    private static final long serialVersionUID = -7081620804556680999L;

    private WechatParamModel() {
    }

    private transient static WechatParamModel sInstance;

    public static WechatParamModel getInstance() {
        if (null == sInstance) {
            synchronized (WechatParamModel.class) {
                if (null == sInstance) {
                    sInstance = new WechatParamModel();
                }
            }
        }
        return sInstance;
    }

    // 朋友圈广告
    private String circleAdClassName;
    private String circleAdMethodName;

    // 微信红包
    private String redPacketReceiveRequestClassName;
    private String redPacketReceiveRequestMethodName;

    private String redPacketReceiveUIParamClassName;

    public String getRedPacketReceiveUIParamClassName() {
        return redPacketReceiveUIParamClassName;
    }

    public void setRedPacketReceiveUIParamClassName(String redPacketReceiveUIParamClassName) {
        this.redPacketReceiveUIParamClassName = redPacketReceiveUIParamClassName;
    }

    public String getRedPacketReceiveRequestClassName() {
        return redPacketReceiveRequestClassName;
    }

    public void setRedPacketReceiveRequestClassName(String redPacketReceiveRequestClassName) {
        this.redPacketReceiveRequestClassName = redPacketReceiveRequestClassName;
    }

    public String getRedPacketReceiveRequestMethodName() {
        return redPacketReceiveRequestMethodName;
    }

    public void setRedPacketReceiveRequestMethodName(String redPacketReceiveRequestMethodName) {
        this.redPacketReceiveRequestMethodName = redPacketReceiveRequestMethodName;
    }

    public String getCircleAdClassName() {
        return circleAdClassName;
    }

    public void setCircleAdClassName(String circleAdClassName) {
        this.circleAdClassName = circleAdClassName;
    }

    public String getCircleAdMethodName() {
        return circleAdMethodName;
    }

    public void setCircleAdMethodName(String circleAdMethodName) {
        this.circleAdMethodName = circleAdMethodName;
    }
}
