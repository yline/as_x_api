package com.yline.lottery.module.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yline.base.BaseFragment;
import com.yline.http.callback.OnJsonCallback;
import com.yline.lottery.R;
import com.yline.lottery.http.OkHttpManager;
import com.yline.lottery.module.lotto.model.LottoQueryModel;
import com.yline.lottery.module.main.model.TypeEnum;
import com.yline.view.recycler.adapter.AbstractRecyclerAdapter;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 首页 - 最新
 *
 * @author yline 2018/8/30 -- 15:36
 */
public class ActualFragment extends BaseFragment {
	private ActualRecyclerAdapter mRecyclerAdapter;
	
	private int callbackCount;
	private List<LottoQueryModel> mQueryModelList;
	
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
		
		initViewClick();
	}
	
	private void initViewClick() {
	
	}
	
	private void initData() {
		callbackCount = 0;
		for (final TypeEnum typeEnum : TypeEnum.values()) {
			OkHttpManager.lottoQuery(typeEnum.getId(), null, new OnJsonCallback<LottoQueryModel>() {
				@Override
				public void onFailure(int code, String msg) {
					updateData(typeEnum, null);
				}
				
				@Override
				public void onResponse(LottoQueryModel lottoQueryModel) {
					updateData(typeEnum, lottoQueryModel);
				}
			});
		}
	}
	
	private void updateData(TypeEnum typeEnum, LottoQueryModel lottoQueryModel) {
		callbackCount++;
		if (null != lottoQueryModel) {
			mQueryModelList.add(lottoQueryModel);
		}
		
		if (callbackCount == TypeEnum.values().length) {
			mRecyclerAdapter.setDataList(mQueryModelList, true);
		}
	}
	
	private class ActualRecyclerAdapter extends AbstractRecyclerAdapter<LottoQueryModel> {
		
		@Override
		public int getItemRes() {
			return 0;
		}
		
		@Override
		public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
		
		}
	}
}
