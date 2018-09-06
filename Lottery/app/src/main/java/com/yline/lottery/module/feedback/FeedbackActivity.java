package com.yline.lottery.module.feedback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yline.application.SDKManager;
import com.yline.base.BaseAppCompatActivity;
import com.yline.lottery.R;
import com.yline.lottery.bugly.BuglyConfig;

import java.util.Map;

public class FeedbackActivity extends BaseAppCompatActivity {
	public static void launch(Context context) {
		if (null != context) {
			Intent intent = new Intent();
			intent.setClass(context, FeedbackActivity.class);
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	private TextView infoTextView;
	private TextView contactTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		
		initView();
		initData();
	}
	
	private void initView() {
		infoTextView = findViewById(R.id.feedback_info);
		contactTextView = findViewById(R.id.feedback_contact);
		
		initViewClick();
	}
	
	private void initViewClick() {
		// 反馈
		findViewById(R.id.feedback_send).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String info = infoTextView.getText().toString().trim();
				String contact = contactTextView.getText().toString().trim();
				
				if (TextUtils.isEmpty(info)) {
					SDKManager.toast("内容不能为空");
					return;
				}
				
				sendInfo(info, contact);
				SDKManager.toast("发送成功，感谢你的反馈");
				finish();
			}
		});
	}
	
	private void initData() {
	
	}
	
	private void sendInfo(String info, String contact) {
		String feedbackInfo = "info = " + info + "\ncontact = " + contact;
		BuglyConfig.sendFeedback(feedbackInfo);
	}
}
