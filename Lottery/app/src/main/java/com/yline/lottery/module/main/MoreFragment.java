package com.yline.lottery.module.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yline.application.SDKManager;
import com.yline.base.BaseFragment;
import com.yline.http.callback.OnJsonCallback;
import com.yline.lottery.R;
import com.yline.lottery.bugly.BuglyConfig;
import com.yline.lottery.http.OkHttpManager;
import com.yline.lottery.http.model.LottoBonusModel;
import com.yline.lottery.module.feedback.FeedbackActivity;
import com.yline.lottery.module.main.view.MoreItemView;
import com.yline.lottery.module.rule.LottoRuleActivity;
import com.yline.lottery.sp.SPManager;
import com.yline.test.BaseTestFragment;

public class MoreFragment extends BaseFragment {
	private MoreItemView ruleItem, upgradeItem, helpItem;
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_more, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		initView(view);
		initData();
	}
	
	private void initView(View view) {
		ruleItem = view.findViewById(R.id.more_rule);
		upgradeItem = view.findViewById(R.id.more_upgrade);
		helpItem = view.findViewById(R.id.more_help);
		initViewClick(view);
	}
	
	private void initViewClick(View view) {
		ruleItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LottoRuleActivity.launch(getActivity());
			}
		});
		
		upgradeItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SDKManager.toast("升级");
				BuglyConfig.checkUpgrade();
			}
		});
		
		helpItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FeedbackActivity.launch(getActivity());
			}
		});
	}
	
	private void initData() {
//		ruleItem.setData(); // 规则
//		upgradeItem.setData(); // 升级
//		helpItem.setData(); // 帮助与反馈
	}
}
