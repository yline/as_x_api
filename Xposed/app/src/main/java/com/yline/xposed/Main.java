package com.yline.xposed;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import com.yline.utils.AppUtil;
import com.yline.utils.LogUtil;
import com.yline.xposed.module.wechat.CircleADPlugin;
import com.yline.xposed.module.wechat.CircleWithDrawPlugin;
import com.yline.xposed.module.wechat.DiceGamePlugin;
import com.yline.xposed.module.wechat.MessageWithdrawPlugin;
import com.yline.xposed.module.wechat.RedPacketPlugin;
import com.yline.xposed.module.wechat.WindowAutoLogin;
import com.yline.xposed.module.wechat.param.WechatParamManager;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Xposed入口，
 * dex被加载的时候，就调用了
 *
 * @author yline 2019/6/21 -- 10:06
 */
public class Main implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (null == lpparam) {
            Log.e("xxx-", "handleLoadPackage lpparam is null");
            throw new NullPointerException("handleLoadPackage lpparam is null");
        }

        // 入口
        final String packageName = lpparam.packageName;
        final String processName = lpparam.processName;
        Log.v("xxx-", "handleLoadPackage, packageName = " + packageName + ", processName = " + processName);

        try {
            loadWechatPlugins(lpparam);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void loadWechatPlugins(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if ("com.tencent.mm".equalsIgnoreCase(lpparam.packageName)) {
            XposedHelpers.findAndHookMethod(ContextWrapper.class, "attachBaseContext", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);

                    boolean isMainProcess = "com.tencent.mm".equalsIgnoreCase(lpparam.processName) || "com.tencent.mm:tools".equalsIgnoreCase(lpparam.processName);
                    if (!isMainProcess) {
                        return;
                    }

                    Context context = (Context) param.args[0];
                    String versionName = AppUtil.getVersionName(context);
                    LogUtil.v("versionName = " + versionName);

                    WechatParamManager.init(lpparam);
                    finishAttach(lpparam, versionName,
                            new DiceGamePlugin(), new MessageWithdrawPlugin(), new CircleADPlugin(),
                            new CircleWithDrawPlugin(), new WindowAutoLogin(), new RedPacketPlugin());
                }
            });
        }
    }

    private static void finishAttach(XC_LoadPackage.LoadPackageParam lpparam, String versionName, IPlugin... pluginArray) {
        StringBuilder sBuilder = new StringBuilder();
        for (IPlugin plugin : pluginArray) {
            try {
                plugin.hook(lpparam, versionName);
                sBuilder.append(plugin.getClass().getName());
                sBuilder.append(',');
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        LogUtil.v("attach plugins = " + sBuilder);
    }
}
