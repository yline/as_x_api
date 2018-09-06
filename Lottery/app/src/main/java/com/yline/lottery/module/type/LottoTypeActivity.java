package com.yline.lottery.module.type;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yline.application.SDKManager;
import com.yline.base.BaseActivity;
import com.yline.base.BaseFragment;
import com.yline.http.callback.OnJsonCallback;
import com.yline.lottery.R;
import com.yline.lottery.http.OkHttpManager;
import com.yline.lottery.http.model.LottoTypeModel;
import com.yline.lottery.sp.SPManager;
import com.yline.lottery.view.LoadingView;
import com.yline.view.recycler.adapter.AbstractRecyclerAdapter;
import com.yline.view.recycler.callback.OnRecyclerItemClickListener;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.util.List;

/**
 * 用户选择，彩票类型
 *
 * @author yline 2018/9/3 -- 17:23
 */
public class LottoTypeActivity extends BaseActivity {
	private static final String LAUNCH_FROM = "launch_from";
	public static final int FROM_HISTORY = 1;
	public static final int FROM_RULE = 2;
	
	public static void launchForResult(Activity activity, int requestCode, int from) {
		if (null != activity) {
			Intent intent = new Intent();
			intent.setClass(activity, LottoTypeActivity.class);
			intent.putExtra(LAUNCH_FROM, from);
			activity.startActivityForResult(intent, requestCode);
		}
	}
	
	public static void launchForResult(BaseFragment fragment, int requestCode, int from) {
		if (null != fragment) {
			Intent intent = new Intent();
			intent.setClass(fragment.requireContext(), LottoTypeActivity.class);
			intent.putExtra(LAUNCH_FROM, from);
			fragment.startActivityForResult(intent, requestCode);
		}
	}
	
	private LottoTypeRecyclerAdapter mRecyclerAdapter;
	private LoadingView mLoadingView;
	private int from;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lotto_type);
		
		from = getIntent().getIntExtra(LAUNCH_FROM, FROM_HISTORY);
		
		initView();
		initData();
	}
	
	private void initView() {
		RecyclerView recyclerView = findViewById(R.id.lotto_type_recycler);
		mRecyclerAdapter = new LottoTypeRecyclerAdapter();
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(mRecyclerAdapter);
		
		mLoadingView = findViewById(R.id.lotto_type_loading);
		
		initViewClick();
	}
	
	private void initViewClick() {
		mRecyclerAdapter.setOnItemClickListener(new OnRecyclerItemClickListener<LottoTypeModel>() {
			@Override
			public void onItemClick(RecyclerViewHolder viewHolder, LottoTypeModel lottoTypeModel, int position) {
				SDKManager.toast("选择成功");
				
				switch (from) {
					case FROM_HISTORY:
						SPManager.getInstance().setHistoryLotteryId(lottoTypeModel.getLottery_id());
						SPManager.getInstance().setHistoryLotteryName(lottoTypeModel.getLottery_name());
						SPManager.getInstance().setHistoryLotteryType(lottoTypeModel);
						break;
					case FROM_RULE:
						SPManager.getInstance().setRuleLotteryId(lottoTypeModel.getLottery_id());
						SPManager.getInstance().setRuleLotteryName(lottoTypeModel.getLottery_name());
						SPManager.getInstance().setRuleLotteryType(lottoTypeModel);
						break;
					default:
						break;
				}
				
				setResult(Activity.RESULT_OK);
				finish();
			}
		});
	}
	
	private void initData() {
		mLoadingView.loading();
		OkHttpManager.lottoType(new OnJsonCallback<List<LottoTypeModel>>() {
			@Override
			public void onFailure(int code, String msg) {
				mLoadingView.loadFailed();
			}
			
			@Override
			public void onResponse(List<LottoTypeModel> lottoTypeModels) {
				if (null != lottoTypeModels && !lottoTypeModels.isEmpty()) {
					mLoadingView.loadSuccess();
					mRecyclerAdapter.setDataList(lottoTypeModels, true);
				} else {
					mLoadingView.loadFailed();
				}
			}
		});
	}
	
	private class LottoTypeRecyclerAdapter extends AbstractRecyclerAdapter<LottoTypeModel> {
		private OnRecyclerItemClickListener<LottoTypeModel> itemClickListener;
		
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
		
		private void setOnItemClickListener(OnRecyclerItemClickListener<LottoTypeModel> itemClickListener) {
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
