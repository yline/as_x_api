package com.yline.lottery.module.splash;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.yline.application.SDKManager;
import com.yline.base.BaseActivity;
import com.yline.http.callback.OnJsonCallback;
import com.yline.lottery.R;
import com.yline.lottery.http.OkHttpManager;
import com.yline.lottery.http.model.LottoTypeModel;
import com.yline.lottery.module.main.MainActivity;
import com.yline.lottery.sp.SPManager;
import com.yline.test.StrConstant;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * 闪屏界面 + 初始化界面
 *
 * @author yline 2018/9/3 -- 15:11
 */
public class SplashActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		initView();
		initData();
	}
	
	private void initView() {
		ImageView imageView = findViewById(R.id.splash_view);
		int[] photoArray = {R.drawable.duling_1, R.drawable.duling_2, R.drawable.duling_3, R.drawable.duling_4, R.drawable.duling_4};
		imageView.setImageResource(photoArray[StrConstant.getIntRandom(Integer.MAX_VALUE) % 5]);
		
		initViewClick();
	}
	
	private void initViewClick() {
	
	}
	
	private void initData() {
		// 闪烁界面
		long lastSaveTime = SPManager.getInstance().getLottoTypeSaveTime();
		boolean isSameMonth = isSameMonth(lastSaveTime);
		if (isSameMonth) {
			SDKManager.getHandler().postDelayed(new Runnable() {
				@Override
				public void run() {
					finish();
					MainActivity.launch(SplashActivity.this);
				}
			}, 1000);
		} else {
			// 次数超时也是如此啊
			OkHttpManager.lottoType(new OnJsonCallback<List<LottoTypeModel>>() {
				@Override
				public void onFailure(int code, String msg) {
					// do nothing
					if (TextUtils.isEmpty(msg)) {
						SDKManager.toast("首次安装请打开网络");
					}
					finish();
					MainActivity.launch(SplashActivity.this);
				}
				
				@Override
				public void onResponse(List<LottoTypeModel> lottoTypeModels) {
					SPManager.getInstance().setLottoTypeList(lottoTypeModels);
					finish();
					MainActivity.launch(SplashActivity.this);
				}
			});
		}
	}
	
	/**
	 * 判断是否月份相同
	 *
	 * @param lastSaveTime 上次保存的时间戳
	 * @return true(相同)
	 */
	private boolean isSameMonth(long lastSaveTime) {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTimeInMillis(System.currentTimeMillis());
		
		int nowYear = calendar.get(Calendar.YEAR);
		int nowMonth = calendar.get(Calendar.MONTH);
		
		calendar.setTimeInMillis(lastSaveTime);
		int lastYear = calendar.get(Calendar.YEAR);
		int lastMonth = calendar.get(Calendar.MONTH);
		return (nowYear == lastYear && nowMonth == lastMonth);
	}
}
