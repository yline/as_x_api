package com.yline.lottery.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.yline.lottery.R;

/**
 * 加载动画
 * 加载中 - 加载失败 - 数据为空(直接当作加载失败处理)
 *
 * @author yline 2018/9/4 -- 9:30
 */
public class LoadingView extends RelativeLayout {
	private View loadingView;
	private View stateView;
	
	public LoadingView(Context context) {
		this(context, null);
	}
	
	public LoadingView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}
	
	public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		LayoutInflater.from(context).inflate(R.layout.view_loading, this, true);
		initView();
	}
	
	private void initView() {
		loadingView = findViewById(R.id.loading_view_rl_loading);
		stateView = findViewById(R.id.loading_view_rl_state);
		initViewClick();
	}
	
	private void initViewClick() {
	
	}
	
	public void loading() {
		this.setVisibility(VISIBLE);
		loadingView.setVisibility(VISIBLE);
		stateView.setVisibility(GONE);
	}
	
	public void loadSuccess() {
		this.setVisibility(GONE);
	}
	
	public void loadFailed() {
		this.setVisibility(VISIBLE);
		loadingView.setVisibility(GONE);
		stateView.setVisibility(VISIBLE);
	}
	
	public void setOnReloadClickListener(View.OnClickListener listener) {
		stateView.findViewById(R.id.loading_view_tv_reload).setOnClickListener(listener);
	}
}
