package com.yline.xposed.module.wechat;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.yline.application.SDKManager;
import com.yline.utils.LogUtil;
import com.yline.xposed.IPlugin;
import com.yline.xposed.config.SPManager;
import com.yline.xposed.module.wechat.param.WechatParamModel;
import com.yline.xposed.utils.VersionUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 抢红包
 *
 * @author yline 2019/6/24 -- 12:37
 */
public class RedPacketPlugin implements IPlugin {
    private Object mRequestCaller;
    private final List<LuckyMoneyModel> mLuckyModelList = new ArrayList<>();
    private boolean mHasTimingIdentifier;

    @Override
    public void hook(final XC_LoadPackage.LoadPackageParam lpparam, String versionName) throws Throwable {
        mHasTimingIdentifier = VersionUtil.redPacketHasTimingIdentifier(versionName);

        final String dbClassName = VersionUtil.getDatabaseClassName(versionName);
        final String chatroomInfoUIClassName = VersionUtil.redPacketChatroomInfoUIClassName(versionName);

        final WechatParamModel paramModel = WechatParamModel.getInstance();

        XposedHelpers.findAndHookMethod(dbClassName, lpparam.classLoader, "insert",
                String.class, String.class, ContentValues.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);

                        String tableName = (String) param.args[0];
                        ContentValues values = (ContentValues) param.args[2];
                        Integer type = values.getAsInteger("type");
                        LogUtil.v("tableName = " + tableName + ", second = " + param.args[1] + ", type = " + type);

                        if (null == type) {
                            // do nothing
                        } else if (type == 436207665 || type == 469762097) {
                            // 红包处理
                            handleLuckyMoney(values, lpparam, paramModel);
                        } else if (type == 419430449) {
                            // 转账处理
                            handleTransfer(values, lpparam);
                        }
                    }
                });

        String receiveRequestClassName = paramModel.getRedPacketReceiveRequestClassName();
        String receiveRequestMethodName = paramModel.getRedPacketReceiveRequestMethodName();
        XposedHelpers.findAndHookMethod(receiveRequestClassName, lpparam.classLoader, receiveRequestMethodName,
                int.class, String.class, JSONObject.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        if (!mHasTimingIdentifier) {
                            return;
                        }

                        if (mLuckyModelList.size() <= 0) {
                            return;
                        }

                        String timingIdentifier = ((JSONObject) (param.args[2])).getString("timingIdentifier");
                        if (TextUtils.isEmpty(timingIdentifier)) {
                            return;
                        }
                        LuckyMoneyModel moneyModel = mLuckyModelList.get(0);

                        Class luckyMoneyRequestClass = XposedHelpers.findClass(HookParams.getInstance().LuckyMoneyRequestClassName, lpparam.classLoader);
                        Object luckyMoneyRequest = XposedHelpers.newInstance(luckyMoneyRequestClass,
                                moneyModel.getMsgType(), moneyModel.getChannelId(), moneyModel.getSendId(), moneyModel.getNativeUrlString(), "", "", moneyModel.getTalker(), "v1.0", timingIdentifier);
                        XposedHelpers.callMethod(mRequestCaller, HookParams.getInstance().RequestCallerMethod, luckyMoneyRequest, getDelayMillis());
                        mLuckyModelList.remove(0);
                    }
                });

        Class receiveUIParamNameClass = XposedHelpers.findClass(HookParams.getInstance().ReceiveUIParamNameClassName, lpparam.classLoader);
        XposedHelpers.findAndHookMethod(HookParams.getInstance().LuckyMoneyReceiveUIClassName, lpparam.classLoader, HookParams.getInstance().ReceiveUIMethod,
                int.class, int.class, String.class, receiveUIParamNameClass, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        boolean isQuickOpen = SPManager.getInstance().isWechatRedPacketQuickOpen(true);
                        if (isQuickOpen) {
                            Button button = (Button) XposedHelpers.findFirstFieldByExactType(param.thisObject.getClass(), Button.class).get(param.thisObject);
                            if (button.isShown() && button.isClickable()) {
                                button.performClick();
                            }
                        }
                    }
                });

        String contactInfoUIClassName = "com.tencent.mm.plugin.profile.ui.ContactInfoUI";
        XposedHelpers.findAndHookMethod(contactInfoUIClassName, lpparam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (SPManager.getInstance().isWechatRedPacketShowId(true)) {
                    Activity activity = (Activity) param.thisObject;
                    ClipboardManager cmb = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                    String wechatId = activity.getIntent().getStringExtra("Contact_User");
                    cmb.setText(wechatId);
                    SDKManager.toast("微信ID:" + wechatId + "已复制到剪切板");
                }
            }
        });

        XposedHelpers.findAndHookMethod(chatroomInfoUIClassName, lpparam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (SPManager.getInstance().isWechatRedPacketShowId(true)) {
                    Activity activity = (Activity) param.thisObject;
                    String wechatId = activity.getIntent().getStringExtra("RoomInfo_Id");
                    ClipboardManager cmb = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(wechatId);
                    SDKManager.toast("微信ID:" + wechatId + "已复制到剪切板");
                }
            }
        });
    }

    private void handleLuckyMoney(ContentValues values, XC_LoadPackage.LoadPackageParam lpparam, WechatParamModel paramModel) throws Throwable {
        boolean enable = SPManager.getInstance().isWechatRedPacketEnable(true);
        int status = values.getAsInteger("status");
        String talker = values.getAsString("talker"); // 可以用于判断黑名单
        int isSend = values.getAsInteger("isSend"); // 发送者，0-自己

        LogUtil.v("enable = " + enable + ", status = " + status + ", talker = " + talker + ", isSend = " + isSend);

        boolean isInBlack = isInBlackList(talker); // 是否在，黑名单内
        boolean isGroupTalk = talker.endsWith("@chatroom"); // 是否，是群聊
        LogUtil.v("isInBlack =" + isInBlack + ", isGroupTalk = " + isGroupTalk);

        // 未开启、红包过期、黑名单内不抢【群聊、私聊、自己发的也可以不抢】
        if (!enable || 4 == status || isInBlack) {
            return;
        }

        String content = values.getAsString("content");
        if (!content.startsWith("<msg")) {
            content = content.substring(content.indexOf("<msg"));
        }

        JSONObject wcpayinfo = new XmlToJson.Builder(content).build()
                .getJSONObject("msg").getJSONObject("appmsg").getJSONObject("wcpayinfo");
        String senderTitle = wcpayinfo.getString("sendertitle");
        String notContainsWords = PreferencesUtils.notContains();
        if (!TextUtils.isEmpty(notContainsWords)) {
            for (String word : notContainsWords.split(",")) {
                if (senderTitle.contains(word)) {
                    return;
                }
            }
        }

        String nativeUrlString = wcpayinfo.getString("nativeurl");
        Uri nativeUrl = Uri.parse(nativeUrlString);
        int msgType = Integer.parseInt(nativeUrl.getQueryParameter("msgtype"));
        int channelId = Integer.parseInt(nativeUrl.getQueryParameter("channelid"));
        String sendId = nativeUrl.getQueryParameter("sendid");

        Class networkRequestClass = XposedHelpers.findClass(HookParams.getInstance().NetworkRequestClassName, lpparam.classLoader);
        mRequestCaller = XposedHelpers.callStaticMethod(networkRequestClass, HookParams.getInstance().GetNetworkByModelMethod);

        String receiveRequestClassName = paramModel.getRedPacketReceiveRequestClassName();
        Class receiveLuckyMoneyRequestClass = XposedHelpers.findClass(receiveRequestClassName, lpparam.classLoader);
        if (mHasTimingIdentifier) {
            XposedHelpers.callMethod(mRequestCaller, HookParams.getInstance().RequestCallerMethod, XposedHelpers.newInstance(receiveLuckyMoneyRequestClass, channelId, sendId, nativeUrlString, 0, "v1.0"), 0);
            mLuckyModelList.add(new LuckyMoneyModel(msgType, channelId, sendId, nativeUrlString, talker));
            return;
        }

        Class luckyMoneyRequestClass = XposedHelpers.findClass(HookParams.getInstance().LuckyMoneyRequestClassName, lpparam.classLoader);
        Object luckyMoneyRequest = XposedHelpers.newInstance(luckyMoneyRequestClass,
                msgType, channelId, sendId, nativeUrlString, "", "", talker, "v1.0");

        XposedHelpers.callMethod(mRequestCaller, HookParams.getInstance().RequestCallerMethod, luckyMoneyRequest, getDelayMillis());
    }

    private boolean isInBlackList(String talker) {
        List<String> blackList = Arrays.asList("1", "2", "3");
        for (String blackStr : blackList) {
            if (blackStr.equalsIgnoreCase(talker)) {
                return true;
            }
        }
        return false;
    }

    private void handleTransfer(ContentValues contentValues, XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!PreferencesUtils.receiveTransfer()) {
            return;
        }

        JSONObject wcpayinfo = new XmlToJson.Builder(contentValues.getAsString("content")).build()
                .getJSONObject("msg").getJSONObject("appmsg").getJSONObject("wcpayinfo");

        int paysubtype = wcpayinfo.getInt("paysubtype");
        if (paysubtype != 1) {
            return;
        }

        String transactionId = wcpayinfo.getString("transcationid");
        String transferId = wcpayinfo.getString("transferid");
        int invalidtime = wcpayinfo.getInt("invalidtime");

        if (null == mRequestCaller) {
            Class networkRequestClass = XposedHelpers.findClass(HookParams.getInstance().NetworkRequestClassName, lpparam.classLoader);
            mRequestCaller = XposedHelpers.callStaticMethod(networkRequestClass, HookParams.getInstance().GetNetworkByModelMethod);
        }

        String talker = contentValues.getAsString("talker");

        Class getTransferRequestClass = XposedHelpers.findClass(HookParams.getInstance().GetTransferRequestClassName, lpparam.classLoader);
        XposedHelpers.callMethod(mRequestCaller, HookParams.getInstance().RequestCallerMethod, XposedHelpers.newInstance(getTransferRequestClass, transactionId, transferId, 0, "confirm", talker, invalidtime), 0);
    }

    private int getDelayMillis() {
        return 10;
    }

    private static class LuckyMoneyModel {
        private int msgType;

        private int channelId;

        private String sendId;

        private String nativeUrlString;

        private String talker;

        public LuckyMoneyModel(int msgType, int channelId, String sendId, String nativeUrlString, String talker) {
            this.msgType = msgType;
            this.channelId = channelId;
            this.sendId = sendId;
            this.nativeUrlString = nativeUrlString;
            this.talker = talker;
        }

        public int getMsgType() {
            return msgType;
        }

        public int getChannelId() {
            return channelId;
        }

        public String getSendId() {
            return sendId;
        }

        public String getNativeUrlString() {
            return nativeUrlString;
        }

        public String getTalker() {
            return talker;
        }
    }
}
