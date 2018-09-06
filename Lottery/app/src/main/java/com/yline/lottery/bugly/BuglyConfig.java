package com.yline.lottery.bugly;

import android.content.Context;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.crashreport.CrashReport;
import com.yline.application.SDKManager;

public class BuglyConfig {
	private static final String APP_ID = "9d049822eb";
	
	/**
	 * Application 中同步进行 设置
	 *
	 * @param context 全局的环境变量
	 */
	public static void init(Context context, boolean isDebug) {
		// CrashReport.initCrashReport(context, APP_ID, isDebug);
		Bugly.init(context, APP_ID, isDebug); // 异常上报 + 应用升级
		
		initBeta();
	}
	
	/**
	 * 自动更新初始化
	 */
	private static void initBeta() {
		Beta.autoDownloadOnWifi = true;
		/*
		Beta.autoInit = true; // true表示app启动自动初始化升级模块; false不会自动初始化
		Beta.autoCheckUpgrade = true; // true表示初始化时自动检查升级; false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法
		Beta.upgradeCheckPeriod = 60 * 1000; // 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
		Beta.initDelay = 1000; // 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
		Beta.largeIconId = R.mipmap.ic_launcher; // largeIconId为项目中的图片资源;
		Beta.smallIconId = R.mipmap.ic_launcher; // smallIconId为项目中的图片资源id
		Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS); // 后续更新资源会保存在此目录
		Beta.showInterruptedStrategy = true; // 设置点击过确认的弹窗在App下次启动自动检查更新时会再次显示。
		Beta.canShowUpgradeActs.add(MainActivity.class); // 例如，只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗; 如果不设置默认所有activity都可以显示弹窗。
		Beta.upgradeDialogLayoutId = R.layout.activity_main; // 设置自定义升级对话框UI布局
		Beta.tipsDialogLayoutId = R.layout.activity_main; // 设置自定义tip弹窗UI布局
		// Beta.upgradeCheckPeriod = new UILifecycleListener<UpgradeInfo>();// 设置升级对话框生命周期回调接口
		Beta.enableNotification = true; // 如果你不想在通知栏显示下载进度，你可以将这个接口设置为false，默认值为true。
		Beta.autoDownloadOnWifi = false; // 设置Wifi下自动下载
		Beta.canShowApkInfo = true; // 如果你使用我们默认弹窗是会显示apk信息的，如果你不想显示可以将这个接口设置为false。
		Beta.enableHotfix = true; // 升级SDK默认是开启热更新能力的，如果你不需要使用热更新，可以将这个接口设置为false。默认不更新
		*/
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
	
	public static void sendFeedback(String feedbackInfo) {
		CrashReport.postCatchedException(new FeedbackThrowable(feedbackInfo));
	}
	
	/* -----------------------------更新相关--------------------------- */
	public static void checkUpgrade() {
		Beta.checkUpgrade();
	}
	
	/**
	 * @param isManual  用户手动点击检查，非用户点击操作请传false
	 * @param isSilence 是否显示弹窗等交互，[true:没有弹窗和toast] [false:有弹窗或toast]
	 */
	public static void check(boolean isManual, boolean isSilence) {
		Beta.checkUpgrade(isManual, isSilence);
	}
	
	public static UpgradeInfo getUpgradeInfo() {
		return Beta.getUpgradeInfo();
	}
}
