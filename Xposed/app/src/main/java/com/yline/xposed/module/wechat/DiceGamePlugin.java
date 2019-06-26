package com.yline.xposed.module.wechat;

import com.yline.utils.LogUtil;
import com.yline.xposed.IPlugin;
import com.yline.xposed.config.SPManager;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 猜拳、骰子游戏，随意指定点数
 * XposedWechatHelper - EmojiGameHook
 *
 * @author yline 2019/6/21 -- 11:38
 */
public class DiceGamePlugin implements IPlugin {

    @Override
    public void hook(final XC_LoadPackage.LoadPackageParam lpparam, String versionName) throws Throwable {
        XposedHelpers.findAndHookMethod(Random.class, "nextInt", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                LogUtil.v("method = " + Arrays.toString(param.args));
                if ((int) param.args[0] >= 10) {
                    return;
                }

                // 对调用nextInt的所有函数遍历，发现调用nextInt的函数，并且满足某些要求时；进入下一个hook
                StackTraceElement[] traceElements = Thread.currentThread().getStackTrace();
                for (StackTraceElement traceElement : traceElements) {
                    if (!traceElement.getClassName().startsWith("com.tencent.mm.sdk.platformtools.")) {
                        continue;
                    }

                    Method[] methods = XposedHelpers.findClass(traceElement.getClassName(), lpparam.classLoader).getMethods();
                    for (Method method : methods) {
                        if (!method.getName().equals(traceElement.getMethodName())) {
                            continue;
                        }

                        Class<?>[] types = method.getParameterTypes();
                        if (types.length == 2 && types[0] == int.class && types[1] == int.class && types[2] == int.class) {
                            LogUtil.v("className = " + traceElement.getClassName() + ", methodName = " + traceElement.getMethodName());
                            hookMethod(lpparam.classLoader, traceElement.getClassName(), traceElement.getMethodName());
                            break;
                        }
                    }
                }

                super.beforeHookedMethod(param);
            }
        });
    }

    /**
     * 对某个方法，进行hook
     */
    private void hookMethod(ClassLoader classLoader, String className, String methodName) throws Throwable {
        Class clazz = XposedHelpers.findClass(className, classLoader);
        XposedHelpers.findAndHookMethod(clazz, methodName, int.class, int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                int result = (int) param.getResult();
                int type = (int) param.args[0];
                LogUtil.v("preResult = " + result + ", type = " + type);

                switch (type) {
                    case 5: // 点数[0,1,2,3,4,5]
                        result = SPManager.getInstance().getWechatDiceGame(5);
                        break;
                    case 2: // 猜拳[0-剪刀,1-石头,2-布]
                        result = SPManager.getInstance().getWechatFingerGuess(0);
                        break;
                }

                LogUtil.v("result = " + result);
                if (result != -1) {
                    param.setResult(result);
                }
                super.afterHookedMethod(param);
            }
        });
    }
}
