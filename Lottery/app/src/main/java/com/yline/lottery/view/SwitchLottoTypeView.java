package com.yline.lottery.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yline.lottery.R;
import com.yline.lottery.sp.SPManager;

/**
 * 切换彩种 界面
 *
 * @author yline 2018/9/3 -- 17:51
 */
public class SwitchLottoTypeView extends RelativeLayout {
	private TextView nowTypeTextView;
	
	public SwitchLottoTypeView(Context context) {
		this(context, null);
	}
	
	public SwitchLottoTypeView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}
	
	public SwitchLottoTypeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		LayoutInflater.from(context).inflate(R.layout.view_switch_lotto_type, this, true);
		
		initView();
		initData();
	}
	
	private void initView() {
		nowTypeTextView = findViewById(R.id.view_switch_lotto_type_now);
	}
	
	private void initData() {
		String nowLottoName = SPManager.getInstance().getUserLotteryName();
		nowTypeTextView.setText(String.format("当前类型：%s", nowLottoName));
	}
	
	/**
	 * 重新更新显示内容
	 * 需要给SP重新设置值
	 */
	public void updateData() {
		initData();
	}
}
