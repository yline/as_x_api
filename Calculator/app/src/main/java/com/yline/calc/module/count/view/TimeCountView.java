package com.yline.calc.module.count.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.yline.calc.R;
import com.yline.view.recycler.holder.ViewHolder;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeCountView extends LinearLayout {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat(",###");
    private ViewHolder mViewHolder;

    public TimeCountView(Context context) {
        this(context, null);
    }

    public TimeCountView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.time_count_view, this, true);
        initView();
    }

    private void initView() {
        mViewHolder = new ViewHolder(this);
    }

    public void setData(long diffMillis) {
        updateData(diffMillis > 0 ? diffMillis : -diffMillis);
    }

    public void setData(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.set(year, month, day, hour, minute, second);

        long diffMillis = calendar.getTimeInMillis() - System.currentTimeMillis();
        updateData(diffMillis > 0 ? diffMillis : -diffMillis);
    }

    private void updateData(long diffMillis) {
        long diffSecond = diffMillis / 1000;
        mViewHolder.setText(R.id.time_count_view_second_tv, DECIMAL_FORMAT.format(diffSecond));

        long diffMinute = diffSecond / 60;
        mViewHolder.setText(R.id.time_count_view_minute_tv, DECIMAL_FORMAT.format(diffMinute));

        long diffHour = diffMinute / 60;
        mViewHolder.setText(R.id.time_count_view_hour_tv, DECIMAL_FORMAT.format(diffHour));

        long diffDay = diffHour / 24;
        mViewHolder.setText(R.id.time_count_view_day_tv, DECIMAL_FORMAT.format(diffDay));

        long diffWeek = diffDay / 7;
        LinearLayout weekLinearLayout = mViewHolder.get(R.id.time_count_view_week);
        if (diffWeek > 10) {
            weekLinearLayout.setVisibility(View.VISIBLE);
            mViewHolder.setText(R.id.time_count_view_week_tv, DECIMAL_FORMAT.format(diffWeek));
        } else {
            weekLinearLayout.setVisibility(View.GONE);
        }

        long diffYear = diffDay / 365; // 允许误差
        LinearLayout yearLinearLayout = mViewHolder.get(R.id.time_count_view_year);
        if (diffYear >= 3) {
            yearLinearLayout.setVisibility(View.VISIBLE);
            mViewHolder.setText(R.id.time_count_view_year_tv, DECIMAL_FORMAT.format(diffYear));
        } else {
            yearLinearLayout.setVisibility(View.GONE);
        }
    }
}
