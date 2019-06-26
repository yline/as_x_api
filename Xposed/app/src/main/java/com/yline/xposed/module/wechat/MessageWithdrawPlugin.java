package com.yline.xposed.module.wechat;

import android.content.ContentValues;
import android.text.TextUtils;

import com.yline.utils.LogUtil;
import com.yline.xposed.IPlugin;
import com.yline.xposed.config.SPManager;
import com.yline.xposed.utils.VersionUtil;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 消息防测回
 * XposedWechatHelper - RevokeMsgHook
 *
 * @author yline 2019/6/21 -- 16:06
 */
public class MessageWithdrawPlugin implements IPlugin {
    private boolean mEnable;

    private HashMap<Long, Object> mMsgCacheMap = new HashMap<>();
    private String mInsertMethodName;
    private Object mInsertObject;

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam, String versionName) throws Throwable {
        final String dbClassName = VersionUtil.getDatabaseClassName(versionName);

        // updateWithOnConflict
        Class clazz = XposedHelpers.findClass(dbClassName, lpparam.classLoader);
        XposedHelpers.findAndHookMethod(clazz, "updateWithOnConflict",
                String.class, ContentValues.class, String.class, String[].class, int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);

                        LogUtil.v("param = " + Arrays.toString(param.args));
                        if (!"message".equals(param.args[0])) {
                            return;
                        }

                        ContentValues values = (ContentValues) param.args[1];
                        if (!isWithdrawMessage(values)) {
                            return;
                        }

                        // 防撤回是否开启
                        mEnable = SPManager.getInstance().isWechatMessageWithdraw(true);
                        if (mEnable) {
                            handleMessageWithdraw(values);
                            param.setResult(1);
                        }
                    }
                });

        // delete
        XposedHelpers.findAndHookMethod(clazz, "delete", String.class, String.class, String[].class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                LogUtil.v("param = " + Arrays.toString(param.args));
                String paramA = (String) param.args[0];

                String[] medias = {"ImgInfo2", "voiceinfo", "videoinfo2", "WxFileIndex2"};
                if (mEnable && Arrays.asList(medias).contains(paramA)) {
                    param.setResult(1);
                }
            }
        });

        // File, delete【这样写，真的好么？？？】
        XposedHelpers.findAndHookMethod(File.class, "delete", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                String path = ((File) param.thisObject).getAbsolutePath();
                boolean isLegal = path.contains("/image2/") || path.contains("/voice2/") || path.contains("/video/");

                LogUtil.v("isLegal = " + isLegal + ", mEnable = " + mEnable + ", path = " + path);
                if (mEnable && isLegal) {
                    param.setResult(true);
                }
            }
        });

        // 复现撤回的文案的那句
        Class insertClass = findInsertClass(lpparam.classLoader);
        if (null != insertClass) {
            mInsertMethodName = findInsertMethodName(insertClass);
            XposedBridge.hookAllMethods(insertClass, mInsertMethodName, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);

                    LogUtil.v("param = " + Arrays.toString(param.args));
                    mInsertObject = param.thisObject;
                    if (param.args.length == 2) {
                        Object msg = param.args[0];
                        if (null == msg) {
                            return;
                        }

                        long msgId = XposedHelpers.getLongField(msg, "field_msgId");
                        mMsgCacheMap.put(msgId, msg);
                    }
                }
            });
        }
    }


    private void handleMessageWithdraw(ContentValues values) {
        long msgId = values.getAsLong("msgId");
        Object msg = mMsgCacheMap.get(msgId);
        if (null != msg && null != mInsertMethodName && null != mInsertObject) {
            XposedHelpers.setIntField(msg, "field_type", values.getAsInteger("type"));
            XposedHelpers.setObjectField(msg, "field_content", values.getAsString("content") + "(已被阻止)");

            long createTime = XposedHelpers.getLongField(msg, "field_createTime");
            XposedHelpers.setLongField(msg, "field_createTime", createTime + 1L);

            XposedHelpers.callMethod(mInsertObject, mInsertMethodName, msg, false);
        }
    }

    private static Class findInsertClass(ClassLoader classLoader) throws Throwable {
        Class clazz = XposedHelpers.findClass("com.tencent.mm.plugin.messenger.foundation.PluginMessengerFoundation", classLoader);
        Field[] fields = clazz.getDeclaredFields();

        Field tempField = null;
        // fields 数量最多的
        for (Field field : fields) {
            if (field.getType().getName().startsWith("com.tencent.mm.plugin.messenger.foundation")) {
                if (tempField == null ||
                        tempField.getType().getDeclaredFields().length < field.getType().getDeclaredFields().length) {
                    tempField = field;
                }
            }
        }

        // fields 里构造方法只有一个且参数为三个的
        if (tempField != null) {
            Field[] fieldFields = tempField.getType().getDeclaredFields();
            for (Field fieldField : fieldFields) {
                Constructor[] constructors = fieldField.getType().getConstructors();
                if (constructors.length == 1 && constructors[0].getParameterTypes().length == 3) {
                    return fieldField.getType();
                }
            }
        }

        return null;
    }

    private static String findInsertMethodName(Class insertClass) {
        Method[] methods = insertClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getParameterTypes().length == 2 && method.getParameterTypes()[1] == boolean.class
                    && method.getReturnType() == long.class) {
                return method.getName();
            }
        }
        return "";
    }

    private static boolean isWithdrawMessage(ContentValues values) {
        if (null == values) {
            return false;
        }

        String content = values.getAsString("content");
        Integer typeInt = values.getAsInteger("type");
        int type = null == typeInt ? 0 : typeInt;

        LogUtil.v("type = " + type + ", content = " + content);
        if (TextUtils.isEmpty(content) || 10000 != type) {
            return false;
        }

        return content.contains("撤回了一条消息") || content.contains("recalled a message");
    }
}
