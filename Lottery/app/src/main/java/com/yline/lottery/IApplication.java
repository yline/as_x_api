package com.yline.lottery;

import android.app.Application;

import com.yline.application.SDKManager;
import com.yline.http.OkHttpUtils;

public class IApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		OkHttpUtils.init(true);
		SDKManager.init(this, null);
	}
}
