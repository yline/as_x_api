package com.yline.lottery.bugly;

import android.content.Context;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.yline.application.SDKManager;

public class BuglyConfig {
	private static final String APP_ID = "79ea17e2ee";
	
	/**
	 * Application 中同步进行 设置
	 *
	 * @param context 全局的环境变量
	 */
	public static void init(Context context, boolean isDebug) {
		// CrashReport.initCrashReport(context, APP_ID, isDebug);
		Bugly.init(context, APP_ID, isDebug); // 异常上报 + 应用升级
	}
	
	/* ---------------------------------- 设置 其它参数 ------------------------------- */
	
	/**
	 * 对应不同页面，设置不同的tag; 即可知道bug 触发的界面
	 *
	 * @param tag 页面的标记
	 */
	public static void setUserSceneTag(int tag) {
		CrashReport.setUserSceneTag(SDKManager.getApplication(), tag);
	}
	
	/**
	 * 例如：设置设备信息、用户的id；进行用户的独立定位
	 *
	 * @param key   key限长50字节，必须匹配正则：[a-zA-Z[0-9]]+
	 * @param value value限长200字节，过长截断
	 */
	public static void setUserData(String key, String value) {
		CrashReport.putUserData(SDKManager.getApplication(), key, value);
	}
	
	/**
	 * 区别是否是，开发者设备，方便区分日志来源
	 *
	 * @param isDevelopmentDevice 是否是开发设备
	 */
	public static void setIsDevelopmentDevice(boolean isDevelopmentDevice) {
		CrashReport.setIsDevelopmentDevice(SDKManager.getApplication(), isDevelopmentDevice);
	}
}
