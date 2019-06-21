package com.yline.xposed;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 插件统一规范
 */
public interface IPlugin {
    /**
     * 插件，接口
     *
     * @param lpparam     应用参数
     * @param versionName 版本号
     */
    void hook(XC_LoadPackage.LoadPackageParam lpparam, String versionName) throws Throwable;
}
