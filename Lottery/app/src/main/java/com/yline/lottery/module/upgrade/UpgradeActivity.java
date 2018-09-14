package com.yline.lottery.module.upgrade;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yline.base.BaseActivity;
import com.yline.lottery.R;
import com.yline.lottery.bugly.BuglyConfig;
import com.yline.utils.AppUtil;

public class UpgradeActivity extends BaseActivity {
	public static void launch(Context context) {
		if (null != context) {
			Intent intent = new Intent();
			intent.setClass(context, UpgradeActivity.class);
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	private TextView versionTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upgrade);
		
		initView();
		initData();
	}
	
	private void initView() {
		versionTextView = findViewById(R.id.upgrade_app_version);
		initViewClick();
	}
	
	private void initViewClick() {
		// 返回
		findViewById(R.id.upgrade_title_img).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		// 检查新版本
		findViewById(R.id.upgrade_check_version).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BuglyConfig.checkUpgrade();
			}
		});
	}
	
	private void initData() {
		versionTextView.setText(String.format("%s %s", getString(R.string.app_name), AppUtil.getVersionName(this)));
	}
}
