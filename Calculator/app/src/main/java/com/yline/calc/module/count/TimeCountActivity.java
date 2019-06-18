package com.yline.calc.module.count;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.yline.base.BaseActivity;
import com.yline.calc.R;
import com.yline.calc.module.count.view.TimeCountView;
import com.yline.calc.utils.LunarCalendarUtils;

import java.util.Calendar;
import java.util.Locale;

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
            long diffMillis = getDiffMillisByLunar(2020, 4, 7, true);
            mTimeCountView.setData(diffMillis);
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

    private static long getDiffMillisByLunar(int year, int month, int monthDay, boolean isLeapMonth) {
        int[] days = LunarCalendarUtils.lunarToSolar(year, month, monthDay, isLeapMonth);
        return getDiffMillis(days[0], days[1], days[2]);
    }

    private static long getDiffMillis(int year, int month, int monthDay) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.set(year, month, monthDay, 8, 0, 0);

        return calendar.getTimeInMillis() - System.currentTimeMillis();
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
