package com.yline.lottery.module.rule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.yline.base.BaseActivity;
import com.yline.lottery.R;

import java.util.HashSet;

/**
 * 展示各个规则，暂时采用HTML方式，可修改性高
 *
 * @author yline 2018/8/31 -- 11:35
 */
public class LottoRuleActivity extends BaseActivity {
	private static final String SSQ = "ssq"; // 双色球
	private static final String DLT = "dlt"; // 超级大乐透
	private static final String QLC = "qlc"; // 七乐彩
	private static final String FCSD = "fcsd"; // 福彩3D
	private static final String QXC = "qxc"; // 七星彩
	private static final String PLS = "pls"; // 排列3
	private static final String PLW = "plw"; // 排列5
	
	private static final String RULE_NAME = "rule_html";
	// 为了增加遍历速度
	private static final HashSet<String> hasSet = new HashSet<>();
	static {
		hasSet.add(SSQ);
		hasSet.add(DLT);
		hasSet.add(QLC);
		hasSet.add(FCSD);
		hasSet.add(QXC);
		hasSet.add(PLS);
		hasSet.add(PLW);
	}
	
	/**
	 * @param lottoId 介绍的对象
	 */
	public static void launch(Context context, String lottoId) {
		if (null != context) {
			Intent intent = new Intent();
			intent.setClass(context, LottoRuleActivity.class);
			if (null != lottoId) {
				intent.putExtra(RULE_NAME, lottoId);
			}
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rule);
		
		// 获取 lottoId
		String fileName = SSQ;
		Intent intent = getIntent();
		if (null != intent) {
			String lottoId = intent.getStringExtra(RULE_NAME);
			// 如果传入的lottoId支持
			if (hasSet.contains(lottoId)) {
				fileName = lottoId;
			}
		}
		
		WebView webView = findViewById(R.id.rule_webview);
		webView.loadUrl("file:///android_asset/rule/" + fileName + ".html");
	}
}
