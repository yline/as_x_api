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

    public static void setParamModel(WechatParamModel paramModel) {
        sInstance = paramModel;
    }

    private String versionName;

    // 朋友圈广告
    private String circleAdClassName;
    private String circleAdMethodName;

    // 微信红包
    private String redPacketReceiveRequestClassName;
    private String redPacketReceiveRequestMethodName;

    private String redPacketRequestClassName;
    private String redPacketReceiveUIParamClassName;

    private String redPacketTransferClassName;

    private String redPacketNetRequestClassName;
    private String redPacketNetRequestMethodName;
    private String redPacketCallerMethodName;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getRedPacketCallerMethodName() {
        return redPacketCallerMethodName;
    }

    public void setRedPacketCallerMethodName(String redPacketCallerMethodName) {
        this.redPacketCallerMethodName = redPacketCallerMethodName;
    }

    public String getRedPacketNetRequestClassName() {
        return redPacketNetRequestClassName;
    }

    public void setRedPacketNetRequestClassName(String redPacketNetRequestClassName) {
        this.redPacketNetRequestClassName = redPacketNetRequestClassName;
    }

    public String getRedPacketNetRequestMethodName() {
        return redPacketNetRequestMethodName;
    }

    public void setRedPacketNetRequestMethodName(String redPacketNetRequestMethodName) {
        this.redPacketNetRequestMethodName = redPacketNetRequestMethodName;
    }

    public String getRedPacketRequestClassName() {
        return redPacketRequestClassName;
    }

    public void setRedPacketRequestClassName(String redPacketRequestClassName) {
        this.redPacketRequestClassName = redPacketRequestClassName;
    }

    public String getRedPacketTransferClassName() {
        return redPacketTransferClassName;
    }

    public void setRedPacketTransferClassName(String redPacketTransferClassName) {
        this.redPacketTransferClassName = redPacketTransferClassName;
    }

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
