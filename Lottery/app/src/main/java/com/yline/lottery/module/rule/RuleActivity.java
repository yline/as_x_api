package com.yline.lottery.module.rule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.yline.base.BaseActivity;
import com.yline.lottery.R;
import com.yline.lottery.http.manager.TypeEnum;

/**
 * 展示各个规则，暂时采用HTML方式，可修改性高
 * @author yline 2018/8/31 -- 11:35
 */
public class RuleActivity extends BaseActivity {
	private static final String RULE_NAME = "rule_html";
	private static final String LOTTO_TYPE = "lotto_type";
	
	/**
	 * @param ruleEnum 介绍的对象
	 */
	public static void launch(Context context, TypeEnum ruleEnum){
		if (null != context){
			Intent intent = new Intent();
			intent.setClass(context, RuleActivity.class);
			if ( null != ruleEnum) {
				intent.putExtra(RULE_NAME, ruleEnum.getRuleFile());
				intent.putExtra(LOTTO_TYPE, ruleEnum.getName());
			}
			if (!(context instanceof Activity)){
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rule);
		
		String fileName = TypeEnum.SSQ.getRuleFile();
		Intent intent = getIntent();
		if (null != intent){
			fileName = intent.getStringExtra(RULE_NAME);
		}
		
		WebView webView = findViewById(R.id.rule_webview);
		webView.loadUrl("file:///android_asset/rule/" + fileName);
	}
}
