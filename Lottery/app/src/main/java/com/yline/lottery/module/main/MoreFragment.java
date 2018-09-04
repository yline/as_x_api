package com.yline.lottery.module.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.yline.application.SDKManager;
import com.yline.http.callback.OnJsonCallback;
import com.yline.lottery.http.OkHttpManager;
import com.yline.lottery.module.type.LottoTypeActivity;
import com.yline.lottery.http.model.LottoBonusModel;
import com.yline.lottery.http.model.LottoHistoryModel;
import com.yline.lottery.http.model.LottoQueryModel;
import com.yline.lottery.http.model.LottoTypeModel;
import com.yline.lottery.module.rule.LottoRuleActivity;
import com.yline.lottery.sp.SPManager;
import com.yline.test.BaseTestFragment;
import com.yline.utils.LogUtil;

public class MoreFragment extends BaseTestFragment {
	
	@Override
	public void testStart(View view, Bundle savedInstanceState) {
		addButton("展示规则 - 双色球", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LottoRuleActivity.launch(getActivity(), "ssq");
			}
		});
		
		addButton("中奖计算器", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String lotteryId = SPManager.getInstance().getUserLotteryId();
				if (TextUtils.isEmpty(lotteryId)) {
					return;
				}
				
				String lotteryNum = "01,11,02,09,14,22,25@05,03";
				OkHttpManager.lottoBonus(lotteryId, null, lotteryNum, new OnJsonCallback<LottoBonusModel>() {
					@Override
					public void onFailure(int code, String msg) {
					
					}
					
					@Override
					public void onResponse(LottoBonusModel lottoBonusModel) {
					
					}
				});
			}
		});
		
		addButton("升级", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SDKManager.toast("升级");
			}
		});
		
		addButton("建议", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SDKManager.toast("建议");
			}
		});
	}
}
