package com.yline.calc.module.count;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.yline.application.SDKManager;
import com.yline.base.BaseActivity;
import com.yline.calc.R;
import com.yline.calc.module.count.view.TimeCountView;
import com.yline.calc.utils.LunarCalendarUtils;

/**
 * 时间计数；
 * 1）倒计时
 * 2）顺计时
 *
 * @author yline 2019/6/18 -- 16:59
 */
public class TimeCountActivity extends BaseActivity {
    public static void launch(Context context) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, TimeCountActivity.class);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private TimeCountView mTimeCountView;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_count);

        initView();
    }

    private void initView() {
        mTimeCountView = findViewById(R.id.time_count_view);

        updateData();
        dfs();
    }

    private void updateData() {
        if (null != mTimeCountView) {
            int[] days = LunarCalendarUtils.lunarToSolar(2020, 4, 7, true);
            mTimeCountView.setData(days[0], days[1], days[2], 8, 0, 0);
        }
    }

    private void dfs() {
        if (null == mHandler) {
            mHandler = new Handler(Looper.getMainLooper());
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateData();
                dfs();
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        super.onDestroy();
    }
}
