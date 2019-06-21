package com.yline.xposed.module.wechat;

import com.yline.xposed.IPlugin;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 朋友圈去广告
 *
 * @author yline 2019/6/21 -- 17:22
 */
public class CircleADPlugin implements IPlugin {

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam, String versionName) throws Throwable {
        // XposedHelpers.findAndHookMethod()
    }
}
