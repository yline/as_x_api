package com.yline.lottery;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.yline.application.SDKManager;
import com.yline.http.OkHttpUtils;
import com.yline.lottery.bugly.BuglyConfig;

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
		SDKManager.init(this, null);
		
		// bugly
		BuglyConfig.init(this, true);
	}
}
