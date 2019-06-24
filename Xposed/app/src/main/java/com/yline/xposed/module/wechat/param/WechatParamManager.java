package com.yline.xposed.module.wechat.param;

import com.yline.utils.LogUtil;
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


    public static void init(XC_LoadPackage.LoadPackageParam lpparam) {
        WechatParamModel paramModel = WechatParamModel.getInstance();

        boolean loadSuccess = loadParam();
        LogUtil.v("loadSuccess = " + loadSuccess);

        if (!loadSuccess) {
            findParam(lpparam, paramModel);
            saveParam();
        }
    }

    private static boolean loadParam() {
        return false;
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
            Class receiveRequestClass = ReflectionUtil.findClassesFromPackage(classLoader, wechatClassList, "com.tencent.mm.plugin.luckymoney", 1)
                    .filterByField("msgType", "int")
                    .filterByMethod(void.class, int.class, String.class, JSONObject.class)
                    .firstOrNull();
            String receiveRequestClassName = receiveRequestClass.getName();
            String receiveRequestMethodName = ReflectionUtil.findMethodsByExactParameters(receiveRequestClass, void.class, int.class, String.class, JSONObject.class).getName();

            // 设置参数
            paramModel.setRedPacketReceiveRequestClassName(receiveRequestClassName);
            paramModel.setRedPacketReceiveRequestMethodName(receiveRequestMethodName);

            Class receiveUIParamNameClass = ReflectionUtil.findClassesFromPackage(classLoader, wechatClassList, "com.tencent.mm", 1)
                    .filterByMethod(String.class, "getInfo")
                    .filterByMethod(int.class, "getType")
                    .filterByMethod(void.class, "reset")
                    .firstOrNull();
            String receiveUIParamNameClassName = receiveUIParamNameClass.getName();

            // 设置参数
            paramModel.setRedPacketReceiveUIParamClassName(receiveUIParamNameClassName);

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


    private static void saveParam() {

    }
}
