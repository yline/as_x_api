package com.yline.lottery;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.yline.application.SDKConfig;
import com.yline.application.SDKManager;
import com.yline.http.OkHttpUtils;
import com.yline.lottery.bugly.BuglyConfig;
import com.yline.utils.LogUtil;

public class IApplication extends Application {
	
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		// Http
		OkHttpUtils.init(true);
		
		// LibSDK
		SDKConfig sdkConfig = new SDKConfig();
		sdkConfig.setUtilLog(true); // 是否调试
		SDKManager.init(this, sdkConfig);
		
		// bugly
		BuglyConfig.init(this, true);
	}
}
