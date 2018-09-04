package com.yline.lottery.module.rule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

import com.yline.base.BaseActivity;
import com.yline.lottery.R;
import com.yline.lottery.module.type.LottoTypeActivity;
import com.yline.lottery.sp.SPManager;

import java.util.HashSet;

/**
 * 展示各个规则，暂时采用HTML方式，可修改性高
 *
 * @author yline 2018/8/31 -- 11:35
 */
public class LottoRuleActivity extends BaseActivity {
	private static final int REQUEST_CODE_SWITCH = 1;
	
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
	
	private WebView mWebView;
	private View mSwitchView;
	
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
		
		initView();
		initData(fileName);
	}
	
	private void initView() {
		mSwitchView = findViewById(R.id.rule_switch_type);
		mWebView = findViewById(R.id.rule_webview);
		
		initViewClick();
	}
	
	private void initViewClick() {
		mSwitchView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LottoTypeActivity.launchForResult(LottoRuleActivity.this, REQUEST_CODE_SWITCH);
			}
		});
	}
	
	private void initData(String fileName) {
		mWebView.loadUrl("file:///android_asset/rule/" + fileName + ".html");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_SWITCH) {
			if (resultCode == Activity.RESULT_OK) {
				String fileName = SPManager.getInstance().getUserLotteryId();
				if (!TextUtils.isEmpty(fileName)) {
					initData(fileName);
				}
			}
		}
	}
}
