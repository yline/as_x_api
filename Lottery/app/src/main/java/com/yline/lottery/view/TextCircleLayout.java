package com.yline.lottery.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yline.lottery.R;
import com.yline.utils.UIScreenUtil;

public class TextCircleLayout extends LinearLayout {
	private LinearLayout mContainerLayout;
	
	private int textWidth;
	private int textHeight;
	private int textInnerPadding;
	private int marginRight;
	
	private int textNormalColor;
	private int textSpecialColor;
	
	public TextCircleLayout(Context context) {
		this(context, null);
	}
	
	public TextCircleLayout(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, -1);
	}
	
	public TextCircleLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		textWidth = UIScreenUtil.dp2px(context, 26); // 宽度
		textHeight = UIScreenUtil.dp2px(context, 26); // 高度
		textInnerPadding = UIScreenUtil.dp2px(context, 4); // padding距离
		marginRight = UIScreenUtil.dp2px(context, 6); // 距离下一个距离
		
		textNormalColor = ContextCompat.getColor(context, R.color.colorAccent); // 正常情况的颜色
		textSpecialColor = ContextCompat.getColor(context, R.color.colorPrimary); // 特殊情况的颜色
		
		initLinearLayout();
	}
	
	private void initLinearLayout() {
		mContainerLayout = new LinearLayout(getContext(), null);
		mContainerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		mContainerLayout.setOrientation(LinearLayout.HORIZONTAL);
		addView(mContainerLayout);
	}
	
	/**
	 * 设置展示的数据，当前支持：
	 * 双色球
	 */
	public void setText(String inputString, String lottoId) {
		String[] numberArray = inputString.split(",");
		switch (lottoId) {
			case "ssq": // 双色球
			case "qlc": // 七乐彩
				mContainerLayout.removeAllViews();
				for (int i = 0; i < numberArray.length; i++) {
					if (i != numberArray.length - 1) {
						addCircleText(numberArray[i], textNormalColor);
					} else {
						addCircleText(numberArray[i], textSpecialColor);
					}
				}
				break;
			case "dlt": // 超级大乐透
				mContainerLayout.removeAllViews();
				for (int i = 0; i < numberArray.length; i++) {
					if (i < numberArray.length - 2) {
						addCircleText(numberArray[i], textNormalColor);
					} else {
						addCircleText(numberArray[i], textSpecialColor);
					}
				}
				break;
			default:
				mContainerLayout.removeAllViews();
				for (String number : numberArray) {
					addCircleText(number, textNormalColor);
				}
				break;
		}
	}
	
	/**
	 * 动态添加一个 视图
	 *
	 * @param content 内容
	 * @param color   颜色
	 */
	private void addCircleText(String content, int color) {
		TextCircleView circleView = new TextCircleView(getContext());
		
		LinearLayout.LayoutParams circleParam = new LinearLayout.LayoutParams(textWidth, textHeight);
		circleParam.rightMargin = marginRight;
		circleView.setLayoutParams(circleParam);
		circleView.setPadding(textInnerPadding, textInnerPadding, textInnerPadding, textInnerPadding);
		
		circleView.setText(content);
		circleView.setTextColor(color);
		
		mContainerLayout.addView(circleView);
	}
}
