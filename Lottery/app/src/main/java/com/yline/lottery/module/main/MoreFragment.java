package com.yline.lottery.module.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.yline.http.callback.OnJsonCallback;
import com.yline.lottery.http.OkHttpManager;
import com.yline.lottery.module.lotto.LottoTypeActivity;
import com.yline.lottery.module.lotto.model.LottoBonusModel;
import com.yline.lottery.module.lotto.model.LottoHistoryModel;
import com.yline.lottery.module.lotto.model.LottoQueryModel;
import com.yline.lottery.module.lotto.model.LottoTypeModel;
import com.yline.lottery.sp.SPManager;
import com.yline.test.BaseTestFragment;
import com.yline.utils.LogUtil;

public class MoreFragment extends BaseTestFragment {
	
	@Override
	public void testStart(View view, Bundle savedInstanceState) {
		addButton("选择彩票类型", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LottoTypeActivity.launch(getActivity());
			}
		});
		
		addButton("查询当前彩票类型", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LottoTypeModel typeModel = SPManager.getInstance().getLotteryType();
				LogUtil.v(LottoTypeActivity.getLottoTypeString(typeModel));
			}
		});
		
		addButton("开奖结果查询", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String lotteryId = SPManager.getInstance().getLotteryId();
				if (TextUtils.isEmpty(lotteryId)) {
					return;
				}
				
				OkHttpManager.lottoQuery(lotteryId, null, new OnJsonCallback<LottoQueryModel>() {
					@Override
					public void onFailure(int code, String msg) {
					
					}
					
					@Override
					public void onResponse(LottoQueryModel lottoQueryModel) {
					
					}
				});
			}
		});
		
		addButton("历史查询，最近50条", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String lotteryId = SPManager.getInstance().getLotteryId();
				if (TextUtils.isEmpty(lotteryId)) {
					return;
				}
				
				OkHttpManager.lottoQueryHistory(lotteryId, 50, 1, new OnJsonCallback<LottoHistoryModel>() {
					@Override
					public void onFailure(int code, String msg) {
					
					}
					
					@Override
					public void onResponse(LottoHistoryModel lottoHistoryModel) {
					
					}
				});
			}
		});
		
		addButton("中奖计算器", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String lotteryId = SPManager.getInstance().getLotteryId();
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
	}
}
