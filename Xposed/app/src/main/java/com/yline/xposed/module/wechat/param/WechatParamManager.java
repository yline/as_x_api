package com.yline.xposed.module.wechat.param;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.yline.utils.LogUtil;
import com.yline.xposed.config.SPManager;
import com.yline.xposed.utils.ReflectionUtil;

import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.DexClass;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 微信参数管理【从APP包中获取信息】
 * 参考：WechatEnhancement
 *
 * @author yline 2019/6/21 -- 17:29
 */
public class WechatParamManager {


    public static void init(XC_LoadPackage.LoadPackageParam lpparam, String versionName) {
        boolean loadSuccess = loadParam(versionName);
        WechatParamModel paramModel = WechatParamModel.getInstance();
        LogUtil.v("loadSuccess = " + loadSuccess);

        if (!loadSuccess) {
            findParam(lpparam, paramModel);
            saveParam(paramModel, versionName);
        }
    }

    private static boolean loadParam(String versionName) {
        String paramString = SPManager.getInstance().getParamModel();
        WechatParamModel paramModel = JSON.parseObject(paramString, WechatParamModel.class);

        String oldVersionName = null == paramModel ? null : paramModel.getVersionName();
        if (versionName.equals(oldVersionName)) {
            WechatParamModel.setParamModel(paramModel);
            return true;
        } else {
            return false;
        }
    }

    private static void findParam(XC_LoadPackage.LoadPackageParam lpparam, WechatParamModel paramModel) {
        List<String> wechatClassList = parseWechatDex(lpparam.appInfo.sourceDir);
        ClassLoader classLoader = lpparam.classLoader;

        if (null == wechatClassList || wechatClassList.isEmpty()) {
            LogUtil.e("parseWechatDex failed, wechatClassList is empty");
            return;
        }

        // CircleADPlugin
        try {
            Class xmlParserClass = ReflectionUtil.findClassesFromPackage(classLoader, wechatClassList, "com.tencent.mm.sdk.platformtools", 0)
                    .filterByMethod(Map.class, String.class, String.class)
                    .firstOrNull();
            String adClassName = xmlParserClass.getName();
            String adMethodName = ReflectionUtil.findMethodsByExactParameters(xmlParserClass, Map.class, String.class, String.class).getName();

            // 设置参数
            paramModel.setCircleAdClassName(adClassName);
            paramModel.setCircleAdMethodName(adMethodName);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        // RedPacketPlugin
        try {
            // 设置参数
            Class receiveRequestClass = ReflectionUtil.findClassesFromPackage(classLoader, wechatClassList, "com.tencent.mm.plugin.luckymoney", 1)
                    .filterByField("msgType", "int")
                    .filterByMethod(void.class, int.class, String.class, JSONObject.class)
                    .firstOrNull();
            String receiveRequestClassName = receiveRequestClass.getName();
            String receiveRequestMethodName = ReflectionUtil.findMethodsByExactParameters(receiveRequestClass, void.class, int.class, String.class, JSONObject.class).getName();

            paramModel.setRedPacketReceiveRequestClassName(receiveRequestClassName);
            paramModel.setRedPacketReceiveRequestMethodName(receiveRequestMethodName);

            // 设置参数
            Class requestClass = ReflectionUtil.findClassesFromPackage(classLoader, wechatClassList, "com.tencent.mm.plugin.luckymoney", 1)
                    .filterByField("talker", "java.lang.String")
                    .filterByMethod(void.class, int.class, String.class, JSONObject.class)
                    .filterByMethod(int.class, "getType")
                    .filterByNoMethod(boolean.class)
                    .firstOrNull();
            String requestClassName = requestClass.getName();

            paramModel.setRedPacketRequestClassName(requestClassName);

            // 设置参数
            Class receiveUIParamNameClass = ReflectionUtil.findClassesFromPackage(classLoader, wechatClassList, "com.tencent.mm", 1)
                    .filterByMethod(String.class, "getInfo")
                    .filterByMethod(int.class, "getType")
                    .filterByMethod(void.class, "reset")
                    .firstOrNull();
            String receiveUIParamNameClassName = receiveUIParamNameClass.getName();

            paramModel.setRedPacketReceiveUIParamClassName(receiveUIParamNameClassName);

            // 设置参数
            Class transferClass = ReflectionUtil.findClassesFromPackage(classLoader, wechatClassList, "com.tencent.mm.plugin.remittance", 1)
                    .filterByField("java.lang.String")
                    .filterByNoField("int")
                    .filterByMethod(void.class, int.class, String.class, JSONObject.class)
                    .filterByMethod(String.class, "getUri")
                    .firstOrNull();
            String transferClassName = transferClass.getName();

            paramModel.setRedPacketTransferClassName(transferClassName);

            // 设置参数
            Class requestCallerClass = ReflectionUtil.findClassesFromPackage(classLoader, wechatClassList, "com.tencent.mm", 1)
                    .filterByField("foreground", "boolean")
                    .filterByMethod(void.class, int.class, String.class, int.class, boolean.class)
                    .filterByMethod(void.class, "cancel", int.class)
                    .filterByMethod(void.class, "reset")
                    .firstOrNull();
            String requestCallerMethodName = ReflectionUtil.findMethodsByExactParameters(requestCallerClass, void.class, requestCallerClass, int.class).getName();

            paramModel.setRedPacketCallerMethodName(requestCallerMethodName);

            Class networkRequestClass = ReflectionUtil.findClassesFromPackage(classLoader, wechatClassList, "com.tencent.mm", 1)
                    .filterByMethod(void.class, "unhold")
                    .filterByMethod(requestCallerClass)
                    .firstOrNull();
            String networkRequestClassName = networkRequestClass.getName();
            String networkRequestMethodName = ReflectionUtil.findMethodsByExactParameters(networkRequestClass, requestCallerClass).getName();

            paramModel.setRedPacketNetRequestClassName(networkRequestClassName);
            paramModel.setRedPacketNetRequestMethodName(networkRequestMethodName);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private static List<String> parseWechatDex(String wechatApkSource) {
        ApkFile apkFile = null;
        try {
            List<String> classList = new ArrayList<>();

            apkFile = new ApkFile(wechatApkSource);
            DexClass[] dexClassArray = apkFile.getDexClasses();
            for (DexClass dexClass : dexClassArray) {
                classList.add(parseClassName(dexClass));
            }
            return classList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (null != apkFile) {
                    apkFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String parseClassName(DexClass clazz) {
        String str = clazz.getClassType().replace('/', '.');
        return str.substring(1, str.length() - 1);
    }

    private static void saveParam(WechatParamModel paramModel, String versionName) {
        // 版本号
        paramModel.setVersionName(versionName);

        // 放入内容
        String paramString = JSON.toJSONString(paramModel);
        SPManager.getInstance().setParamModel(paramString);
    }
}
