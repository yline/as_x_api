package com.yline.lottery.module.main;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yline.application.SDKManager;
import com.yline.lottery.module.lotto.LottoTypeActivity;
import com.yline.lottery.module.lotto.model.LottoTypeModel;
import com.yline.lottery.sp.SPManager;
import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;
import com.yline.utils.SPUtil;

public class MainActivity extends BaseTestActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String lotteryId = SPManager.getInstance().getLotteryId();
		if (TextUtils.isEmpty(lotteryId)) {
			SDKManager.toast("请先选择彩票类型");
			LottoTypeActivity.launch(MainActivity.this);
		}
	}
	
	@Override
	public void testStart(View view, Bundle savedInstanceState) {
		addButton("选择彩票类型", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LottoTypeActivity.launch(MainActivity.this);
			}
		});
		
		addButton("查询当前彩票类型", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LottoTypeModel typeModel = SPManager.getInstance().getLotteryType();
				LogUtil.v(LottoTypeActivity.getLottoTypeString(typeModel));
			}
		});
	}
}
