package com.yline.lottery.module.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yline.application.SDKManager;
import com.yline.base.BaseFragment;
import com.yline.http.callback.OnJsonCallback;
import com.yline.lottery.R;
import com.yline.lottery.http.OkHttpManager;
import com.yline.lottery.http.model.LottoQueryModel;
import com.yline.lottery.module.main.model.ActualModel;
import com.yline.lottery.sp.SPManager;
import com.yline.lottery.view.LoadingView;
import com.yline.utils.LogUtil;
import com.yline.view.recycler.adapter.AbstractRecyclerAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;
import com.yline.view.refresh.PullToRefreshLayout;
import com.yline.view.refresh.callback.OnRefreshListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * 首页 - 最新
 * PS：接口受到限制，因此只能本地缓存；
 * 机制：每日更新一次，有数据且日期正确则不请求，无数据或日期不对则请求
 * 本地日期不准确也没办法，谁让接口有限制呢（本人穷）
 *
 * @author yline 2018/8/30 -- 15:36
 */
public class ActualFragment extends BaseFragment {
	private ActualRecyclerAdapter mRecyclerAdapter;
	private LoadingView mLoadingView;
	private PullToRefreshLayout mRefreshLayout;
	
	private int callbackCount;
	private List<ActualModel> mActualModelList = new ArrayList<>();
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_actual, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		initView(view);
		initData();
	}
	
	private void initView(View view) {
		mRecyclerAdapter = new ActualRecyclerAdapter();
		
		RecyclerView recyclerView = view.findViewById(R.id.actual_recycler);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setAdapter(mRecyclerAdapter);
		
		mRefreshLayout = view.findViewById(R.id.actual_refresh);
		mRefreshLayout.setCanLoadMore(false);
		mLoadingView = view.findViewById(R.id.actual_loading);
		
		initViewClick();
	}
	
	private void initViewClick() {
		// 重新加载
		mLoadingView.setOnReloadClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initData();
			}
		});
		
		// 下拉刷新
		mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void refresh() {
				int itemCount = mRecyclerAdapter.getItemCount();
				int valueCount = SPManager.getInstance().getLottoTypeCount();
				if (itemCount == valueCount) { // 数据够了
					SDKManager.getHandler().postDelayed(new Runnable() {
						@Override
						public void run() {
							SDKManager.toast("已经是最新了");
							mRefreshLayout.finishRefresh();
						}
					}, 1000);
				} else {
					initData(); // 重新加载
				}
			}
		});
	}
	
	/**
	 * 保证一天只请求一天
	 */
	private void initData() {
		mLoadingView.loading();
		
		callbackCount = 0;
		
		List<String> idList = SPManager.getInstance().getLottoTypeIdList();
		for (final String lottoId : idList) {
			long lastSaveTime = SPManager.getInstance().getActualModelTime(lottoId);
			boolean isSameDate = isSameDate(lastSaveTime);
			if (!isSameDate) {
				OkHttpManager.lottoQuery(lottoId, null, new OnJsonCallback<LottoQueryModel>() {
					@Override
					public void onFailure(int code, String msg) {
						updateData(null, null, null, null);
					}
					
					@Override
					public void onResponse(LottoQueryModel lottoQueryModel) {
						if (null != lottoQueryModel) {
							updateData(lottoId, lottoQueryModel.getLottery_res(), lottoQueryModel.getLottery_no(), lottoQueryModel.getLottery_date());
						} else {
							updateData(null, null, null, null);
						}
					}
				});
			} else {
				ActualModel todayActualModel = SPManager.getInstance().getActualModel(lottoId);
				updateData(lottoId, todayActualModel.getResult(), todayActualModel.getNumber(), todayActualModel.getDate());
			}
		}
	}
	
	private void updateData(String lottoId, String result, String number, String date) {
		callbackCount++;
		
		if (null != lottoId) {
			SPManager.getInstance().setActualModel(lottoId, result, number, date);
			LogUtil.v(result + " - " + number);
			mActualModelList.add(ActualModel.genActualModel(lottoId, result, number, date));
		}
		
		if (callbackCount == SPManager.getInstance().getLottoTypeCount()) {
			if (mActualModelList.isEmpty()) {
				mLoadingView.loadFailed();
				//mRefreshLayout.finishRefresh();
			} else {
				mLoadingView.loadSuccess();
				//mRefreshLayout.finishRefresh();
				mRecyclerAdapter.setDataList(mActualModelList, true);
			}
		}
	}
	
	private class ActualRecyclerAdapter extends AbstractRecyclerAdapter<ActualModel> {
		@Override
		public int getItemRes() {
			return R.layout.item_fragment_actual;
		}
		
		@Override
		public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
			ActualModel actualModel = get(position);
			
			String lottoId = actualModel.getLottoId();
			String lottoName = SPManager.getInstance().getLottoTypeNameByLottoId(lottoId);
			String info = String.format("%s  第%s期  开奖：%s", lottoName, actualModel.getNumber(), actualModel.getDate());
			holder.setText(R.id.item_actual_info, info);
			
			holder.setText(R.id.item_actual_number, actualModel.getResult());
		}
	}
	
	/**
	 * 判断日期是否相同
	 *
	 * @return true(相同)
	 */
	private static boolean isSameDate(long lastSaveTime) {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTimeInMillis(System.currentTimeMillis());
		int nowYear = calendar.get(Calendar.YEAR);
		int nowDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
		
		calendar.setTimeInMillis(lastSaveTime);
		int lastSaveYear = calendar.get(Calendar.YEAR);
		int lastSaveDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
		
		return (nowYear == lastSaveYear && nowDayOfYear == lastSaveDayOfYear);
	}
}
