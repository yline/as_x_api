package com.yline.xposed.module.wechat;

import android.app.Activity;
import android.widget.Button;

import com.yline.utils.LogUtil;
import com.yline.xposed.IPlugin;
import com.yline.xposed.config.SPManager;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 微信，电脑端，自动登录【比较危险，因为任意人员只要有你的微信，都能登录】
 *
 * @author yline 2019/6/24 -- 11:34
 */
public class WindowAutoLogin implements IPlugin {

    @Override
    public void hook(XC_LoadPackage.LoadPackageParam lpparam, String versionName) throws Throwable {
        XposedHelpers.findAndHookMethod(Activity.class, "onStart", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                boolean enable = SPManager.getInstance().isWechatWindowAutoLogin(true);
                LogUtil.v("enable = " + enable);

                if (!enable || !(param.thisObject instanceof Activity)) {
                    Class clazz = param.thisObject.getClass();
                    if ("com.tencent.mm.plugin.webwx.ui.ExtDeviceWXLoginUI".equalsIgnoreCase(clazz.getName())) {
                        Field field = XposedHelpers.findFirstFieldByExactType(clazz, Button.class);
                        Button button = (Button) field.get(clazz);
                        if (null != button) {
                            button.performClick();
                        }
                    }
                }
            }
        });
    }
}
