package com.yline.lottery.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yline.lottery.R;
import com.yline.lottery.module.type.LottoTypeActivity;
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
	}
	
	private void initView() {
		nowTypeTextView = findViewById(R.id.view_switch_lotto_type_now);
	}
	
	public void updateData(String lottoName) {
		nowTypeTextView.setText(lottoName);
	}
}
