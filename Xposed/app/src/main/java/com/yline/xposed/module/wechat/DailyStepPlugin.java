package com.yline.xposed.module.wechat;

import com.yline.xposed.IPlugin;
import com.yline.xposed.config.SPManager;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 每日，运动步数
 *
 * @author yline 2019/6/26 -- 10:50
 */
public class DailyStepPlugin implements IPlugin {
    private int mStep = -1;

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam, String versionName) throws Throwable {
        mStep = SPManager.getInstance().getWechatDailyStepNum(10);

        //4.4 nexus 通过
        Class clazz = XposedHelpers.findClass("android.hardware.SystemSensorManager$SensorEventQueue", lpparam.classLoader);
        XposedBridge.hookAllMethods(clazz, "dispatchSensorEvent", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (mStep > 1) {
                    ((float[]) param.args[1])[0] = (((float[]) param.args[1])[0]) * mStep;
                }
                super.beforeHookedMethod(param);
            }
        });
    }
}
