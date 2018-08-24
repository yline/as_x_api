package com.yline.lottery.module.lotto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yline.http.callback.OnJsonCallback;
import com.yline.lottery.http.OkHttpManager;
import com.yline.lottery.module.lotto.model.LottoTypeModel;
import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

import java.util.List;

public class LottoActivity extends BaseTestActivity {
	private static final int LAST_NUM = 18098; // 最后一个日期，时间：2018-08-22
	
	public static void launch(Context context) {
		if (null != context) {
			Intent intent = new Intent();
			intent.setClass(context, LottoActivity.class);
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initData();
	}
	
	@Override
	public void testStart(View view, Bundle savedInstanceState) {
		addButton("查询支持的类型", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			
			}
		});
	}
	
	private void initData() {
		OkHttpManager.lottoType(new OnJsonCallback<List<LottoTypeModel>>() {
			@Override
			public void onFailure(int code, String msg) {
				finish();
			}
			
			@Override
			public void onResponse(List<LottoTypeModel> lottoTypeModels) {
				for (LottoTypeModel typeModel : lottoTypeModels) {
					LogUtil.v(typeModel.toString());
				}
			}
		});
	}
}
