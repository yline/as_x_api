package com.yline.lottery.module.feedback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yline.application.SDKManager;
import com.yline.base.BaseActivity;
import com.yline.base.BaseAppCompatActivity;
import com.yline.lottery.R;
import com.yline.lottery.bugly.BuglyConfig;
import com.yline.utils.LogUtil;

import java.util.Map;

public class FeedbackActivity extends BaseActivity {
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
	
	private RadioGroup typeRadioGroup;
	private TextView infoTextView;
	private TextView contactTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		
		initView();
	}
	
	private void initView() {
		infoTextView = findViewById(R.id.feedback_info);
		contactTextView = findViewById(R.id.feedback_contact);
		typeRadioGroup = findViewById(R.id.feedback_type);
		
		initViewClick();
	}
	
	private void initViewClick() {
		// 返回
		findViewById(R.id.feedback_title_img).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		// 反馈
		findViewById(R.id.feedback_send).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String info = infoTextView.getText().toString().trim();
				String contact = contactTextView.getText().toString().trim();
				
				TextView contentTextView = findViewById(typeRadioGroup.getCheckedRadioButtonId());
				String feedbackType = contentTextView.getText().toString().trim();
				if (TextUtils.isEmpty(feedbackType)) {
					SDKManager.toast("请选择问题类型");
					return;
				}
				
				if (TextUtils.isEmpty(info)) {
					SDKManager.toast("内容不能为空");
					return;
				}
				
				sendInfo(feedbackType, info, contact);
				SDKManager.toast("发送成功，感谢你的反馈");
				finish();
			}
		});
	}
	
	private void sendInfo(String type, String info, String contact) {
		String feedbackInfo = "type = " + type + "\ninfo = " + info + "\ncontact = " + contact;
		BuglyConfig.sendFeedback(feedbackInfo);
	}
}
