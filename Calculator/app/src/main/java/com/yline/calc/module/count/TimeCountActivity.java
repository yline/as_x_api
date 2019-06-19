package com.yline.calc.module.count;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.yline.base.BaseActivity;
import com.yline.calc.R;
import com.yline.calc.module.count.view.TimeCountView;

/**
 * 时间计数；
 * 1）倒计时
 * 2）顺计时
 *
 * @author yline 2019/6/18 -- 16:59
 */
public class TimeCountActivity extends BaseActivity {
    private static final String KEY_TITLE = "title";
    private static final String KEY_TARGET_MILLIS = "target_millis";
    private static final String KEY_IS_FUTURE = "is_future";

    public static void launch(Context context, String title, long targetMillis, boolean isFuture) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, TimeCountActivity.class);
            intent.putExtra(KEY_TITLE, title);
            intent.putExtra(KEY_TARGET_MILLIS, targetMillis);
            intent.putExtra(KEY_IS_FUTURE, isFuture);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    // 传递过来的数据
    private String mTitle;
    private long mTargetMillis;
    private boolean mIsFuture; // true -> 倒计时；false -> 总计时

    private TextView mTitleTextView;
    private TimeCountView mTimeCountView;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_count);

        mTitle = getIntent().getStringExtra(KEY_TITLE);
        mTargetMillis = getIntent().getLongExtra(KEY_TARGET_MILLIS, System.currentTimeMillis());
        mIsFuture = getIntent().getBooleanExtra(KEY_IS_FUTURE, true);

        initView();
    }

    private void initView() {
        mTitleTextView = findViewById(R.id.time_count_title);
        mTimeCountView = findViewById(R.id.time_count_view);

        mTitleTextView.setText(mTitle);

        updateData();
        dfs();
    }

    private void updateData() {
        if (null != mTimeCountView) {
            long diffMillis = mTargetMillis - System.currentTimeMillis();
            if (mIsFuture && diffMillis < 0 && null != mTitleTextView) { // 如果已经超时，则显示超时
                mTitleTextView.setTextColor(ContextCompat.getColor(TimeCountActivity.this, android.R.color.holo_red_light));
                mTitleTextView.setText(String.format("%s【超时】", mTitle));
                mIsFuture = false; // 这样就不会重复修改了
            }
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

    @Override
    protected void onDestroy() {
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        super.onDestroy();
    }
}
