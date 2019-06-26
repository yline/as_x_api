package com.yline.xposed.module.wechat;

import com.yline.xposed.IPlugin;
import com.yline.xposed.config.SPManager;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 修改，腾讯定位
 *
 * @author yline 2019/6/26 -- 11:19
 */
public class LocationPlugin implements IPlugin {
    private boolean mIsLocation;
    private String mLatitude; // 维度
    private String mLongitude; // 经度

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam, String versionName) throws Throwable {
        mIsLocation = SPManager.getInstance().isWechatLocation(true);
        mLatitude = "39.908860";
        mLongitude = "116.397390";

        Class managerClazz = XposedHelpers.findClass("com.tencent.map.geolocation.TencentLocationManager", lpparam.classLoader);
        XposedBridge.hookAllMethods(managerClazz, "requestLocationUpdates", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Object tencentLocationListener = param.args[1];
                for (Method method : tencentLocationListener.getClass().getDeclaredMethods()) {
                    if (method.getParameterTypes().length == 10) {
                        XposedBridge.hookAllMethods(tencentLocationListener.getClass(), method.getName(), new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                if (mIsLocation) {
                                    param.args[1] = Double.valueOf(mLatitude);
                                    param.args[2] = Double.valueOf(mLongitude);
                                }
                                super.beforeHookedMethod(param);
                            }
                        });
                    }
                }
                super.beforeHookedMethod(param);
            }
        });
    }
}
