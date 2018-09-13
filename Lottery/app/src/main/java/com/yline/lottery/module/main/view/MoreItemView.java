package com.yline.lottery.module.main.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.yline.lottery.R;
import com.yline.view.recycler.holder.ViewHolder;

/**
 * 更多界面的，Item
 *
 * @author yline 2018/9/13 -- 18:05
 */
public class MoreItemView extends RelativeLayout {
	private ViewHolder mViewHolder;
	
	public MoreItemView(Context context) {
		this(context, null);
	}
	
	public MoreItemView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}
	
	public MoreItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		LayoutInflater.from(context).inflate(R.layout.view_more_item, this, true);
		initView();
	}
	
	private void initView() {
		mViewHolder = new ViewHolder(this);
	}
	
	public void setData(int iconRes, String content) {
		setData(iconRes, content, null, true);
	}
	
	public void setData(int iconRes, String content, String subContent, boolean isArrow) {
		mViewHolder.setImageResource(R.id.view_more_item_img, iconRes);
		
		mViewHolder.setText(R.id.view_more_item_content, content);
		
		if (TextUtils.isEmpty(subContent)) {
			mViewHolder.get(R.id.view_more_item_sub_content).setVisibility(View.GONE);
		} else {
			mViewHolder.setText(R.id.view_more_item_sub_content, subContent).setVisibility(View.VISIBLE);
		}
		
		mViewHolder.get(R.id.view_more_item_arrow).setVisibility(isArrow ? View.VISIBLE : View.GONE);
	}
}
