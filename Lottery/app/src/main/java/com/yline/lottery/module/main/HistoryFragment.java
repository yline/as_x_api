package com.yline.lottery.module.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yline.base.BaseFragment;
import com.yline.http.callback.OnJsonCallback;
import com.yline.lottery.R;
import com.yline.lottery.http.OkHttpManager;
import com.yline.lottery.http.model.LottoHistoryModel;
import com.yline.lottery.view.SwitchLottoTypeView;
import com.yline.lottery.module.type.LottoTypeActivity;
import com.yline.lottery.sp.SPManager;
import com.yline.lottery.view.LoadingView;
import com.yline.utils.LogUtil;
import com.yline.view.recycler.adapter.AbstractHeadFootRecyclerAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;

/**
 * 首页 - 历史
 * 默认：双色球(或上次切换的结果)，页数：50
 *
 * @author yline 2018/8/30 -- 15:39
 */
public class HistoryFragment extends BaseFragment {
	private static final int REQUEST_CODE_SWITCH = 1; // 切换类型
	private static final int PAGE_SIZE = 50; // 最大50，默认50
	
	private HistoryRecyclerAdapter mRecyclerAdapter;
	private SwitchLottoTypeView mHeaderView;
	private LoadingView mLoadingView;
	
	private int pageNum;
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_history, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		initView(view);
		initData();
	}
	
	private void initView(View view) {
		mRecyclerAdapter = new HistoryRecyclerAdapter(getContext());
		RecyclerView recyclerView = view.findViewById(R.id.history_recycler);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setAdapter(mRecyclerAdapter);
		
		mHeaderView = new SwitchLottoTypeView(getActivity());
		mRecyclerAdapter.addHeadView(mHeaderView);
		
		mLoadingView = view.findViewById(R.id.history_loading);
		
		initViewClick();
	}
	
	private void initViewClick() {
		// 切换彩种
		mHeaderView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LottoTypeActivity.launchForResult(HistoryFragment.this, REQUEST_CODE_SWITCH);
			}
		});
		
		// 首次进入重新加载
		mLoadingView.setOnReloadClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initData();
			}
		});
	}
	
	private void initData() {
		mLoadingView.loading();
		
		String lottoId = SPManager.getInstance().getUserLotteryId();
		lottoId = TextUtils.isEmpty(lottoId) ? SPManager.getInstance().getLottoTypeFirstId() : lottoId; // 查询太复杂
		
		pageNum = 0;
		OkHttpManager.lottoQueryHistory(lottoId, PAGE_SIZE, pageNum, new OnJsonCallback<LottoHistoryModel>() {
			@Override
			public void onFailure(int code, String msg) {
				mLoadingView.loadFailed();
			}
			
			@Override
			public void onResponse(LottoHistoryModel lottoHistoryModel) {
				if (null != lottoHistoryModel) {
					mLoadingView.loadSuccess();
					
					pageNum = lottoHistoryModel.getPage();
					mRecyclerAdapter.setDataList(lottoHistoryModel.getLotteryResList(), true);
				} else {
					mLoadingView.loadFailed();
				}
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.v("requestCode = " + requestCode + ", resultCode = " + resultCode);
		if (requestCode == REQUEST_CODE_SWITCH) {
			if (resultCode == Activity.RESULT_OK) { // 切换成功，则重新加载数据
				initData();
			}
		}
	}
	
	private class HistoryRecyclerAdapter extends AbstractHeadFootRecyclerAdapter<LottoHistoryModel.HistoryDetail> {
		private HistoryRecyclerAdapter(Context context) {
			super(context);
		}
		
		@Override
		public int getItemRes() {
			return R.layout.item_fragment_history;
		}
		
		@Override
		public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
			LottoHistoryModel.HistoryDetail detailModel = get(position);
			
			String lottoId = detailModel.getLottery_id();
			String lottoName = SPManager.getInstance().getLottoTypeNameByLottoId(lottoId);
			String info = String.format("%s  第%s期  开奖：%s", lottoName, detailModel.getLottery_no(), detailModel.getLottery_date());
			holder.setText(R.id.item_history_info, info);
			
			holder.setText(R.id.item_history_number, detailModel.getLottery_res());
		}
	}
}
