package com.yline.xposed.module.wechat;

import com.yline.utils.LogUtil;
import com.yline.xposed.IPlugin;
import com.yline.xposed.config.SPManager;
import com.yline.xposed.module.wechat.param.WechatParamModel;

import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 朋友圈去广告
 * WechatEnhancement - ADBlock
 *
 * @author yline 2019/6/21 -- 17:22
 */
public class CircleADPlugin implements IPlugin {

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam, String versionName) throws Throwable {
        WechatParamModel paramModel = WechatParamModel.getInstance();
        String adClassName = paramModel.getCircleAdClassName();
        String adMethodName = paramModel.getCircleAdMethodName();

        LogUtil.v("adClassName = " + adClassName + ", adMethodName = " + adMethodName);
        XposedHelpers.findAndHookMethod(adClassName, lpparam.classLoader, adMethodName, String.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                boolean enable = SPManager.getInstance().isWechatCircleAd(true);
                LogUtil.v("param = " + Arrays.toString(param.args) + ", enable = " + enable);

                if (enable && "ADInfo".equalsIgnoreCase(String.valueOf(param.args[1]))) {
                    param.setResult(null);
                }
            }
        });
    }
}
