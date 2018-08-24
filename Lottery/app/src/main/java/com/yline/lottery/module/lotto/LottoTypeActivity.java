package com.yline.lottery.module.lotto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yline.application.SDKManager;
import com.yline.base.BaseActivity;
import com.yline.http.callback.OnJsonCallback;
import com.yline.lottery.R;
import com.yline.lottery.http.OkHttpManager;
import com.yline.lottery.module.lotto.model.LottoTypeModel;
import com.yline.lottery.sp.SPManager;
import com.yline.view.recycler.adapter.AbstractRecyclerAdapter;
import com.yline.view.recycler.holder.Callback;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.List;

public class LottoTypeActivity extends BaseActivity {
	public static void launch(Context context) {
		if (null != context) {
			Intent intent = new Intent();
			intent.setClass(context, LottoTypeActivity.class);
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	private LottoTypeRecyclerAdapter mRecyclerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lotto_type);
		
		initView();
		initData();
	}
	
	private void initView() {
		RecyclerView recyclerView = findViewById(R.id.lotto_type_recycler);
		mRecyclerAdapter = new LottoTypeRecyclerAdapter();
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(mRecyclerAdapter);
		
		initViewClick();
	}
	
	private void initViewClick() {
		mRecyclerAdapter.setOnItemClickListener(new Callback.OnRecyclerItemClickListener<LottoTypeModel>() {
			@Override
			public void onItemClick(RecyclerViewHolder viewHolder, LottoTypeModel lottoTypeModel, int position) {
				SDKManager.toast("选择成功");
				
				SPManager.getInstance().setLotteryId(lottoTypeModel.getLottery_id());
				SPManager.getInstance().setLotteryType(lottoTypeModel);
				
				finish();
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
				if (null != lottoTypeModels && !lottoTypeModels.isEmpty()) {
					mRecyclerAdapter.setDataList(lottoTypeModels, true);
				} else {
					SDKManager.toast("请求数据为空，退出重新进入");
					finish();
				}
			}
		});
	}
	
	private class LottoTypeRecyclerAdapter extends AbstractRecyclerAdapter<LottoTypeModel> {
		private Callback.OnRecyclerItemClickListener<LottoTypeModel> itemClickListener;
		
		@Override
		public int getItemRes() {
			return android.R.layout.simple_list_item_1;
		}
		
		@Override
		public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int position) {
			final LottoTypeModel singleModel = get(position);
			
			holder.setText(android.R.id.text1, getLottoTypeString(singleModel));
			
			holder.getItemView().setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (null != itemClickListener) {
						itemClickListener.onItemClick(holder, singleModel, holder.getAdapterPosition());
					}
				}
			});
		}
		
		private void setOnItemClickListener(Callback.OnRecyclerItemClickListener<LottoTypeModel> itemClickListener) {
			this.itemClickListener = itemClickListener;
		}
	}
	
	public static String getLottoTypeString(LottoTypeModel singleModel) {
		StringBuilder stringBuilder = new StringBuilder();
		if (null != singleModel) {
			stringBuilder.append("名称：");
			stringBuilder.append(singleModel.getLottery_name());
			stringBuilder.append('\n');
			
			stringBuilder.append("类型：");
			stringBuilder.append((singleModel.getLottery_type_id() == LottoTypeModel.TYPE_WELFARE ? "福利彩票" : "体育彩票"));
			stringBuilder.append('\n');
			
			stringBuilder.append("开奖：");
			stringBuilder.append(singleModel.getRemarks());
		}
		return stringBuilder.toString();
	}
}
