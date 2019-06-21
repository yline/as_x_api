package com.yline.xposed.module.wechat;

import com.yline.xposed.IPlugin;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 骰子游戏，随意指定点数
 *
 * @author yline 2019/6/21 -- 11:38
 */
public class DiceGamePlugin implements IPlugin {

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam, String versionName) throws Throwable {

    }
}
