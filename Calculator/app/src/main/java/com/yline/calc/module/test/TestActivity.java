package com.yline.calc.module.test;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yline.calc.module.count.TimeCountActivity;
import com.yline.calc.utils.LunarCalendarUtils;
import com.yline.test.BaseTestActivity;
import com.yline.utils.LogUtil;

import java.util.Arrays;
import java.util.Calendar;

/**
 * 测试类
 *
 * @author yline 2019/6/18 -- 16:44
 */
public class TestActivity extends BaseTestActivity {
    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        final TextView tvLunarCalendar = addTextView("");
        addButton("测试 公历阴历 转换", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = launcherCalendar();
                tvLunarCalendar.setText(result);
            }
        });

        final TextView tvDistanceCalendar = addTextView("");
        addButton("距离截止日期还有几天", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = distanceCalendar();
                tvDistanceCalendar.setText(result);
            }
        });

        addButton("倒计时", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeCountActivity.launch(TestActivity.this, "Title", System.currentTimeMillis(), true);
            }
        });
    }

    /**
     * 计算距离截至日期的天数
     *
     * @return 仅仅计算日期
     */
    public String distanceCalendar() {
        // 计算 非闰月 日期
        int[] days = LunarCalendarUtils.lunarToSolar(2020, 4, 7, false);

        Calendar calendar = Calendar.getInstance();
        calendar.set(days[0], (days[1] - 1), days[2]);
        long aimMillis = calendar.getTimeInMillis(); // 获取到的时间，小时、分钟、秒都和获取的时间相同
        long diffMillis = aimMillis - System.currentTimeMillis();

        LogUtil.v("aimDay = " + Arrays.toString(days) + ", aimMillis = " + aimMillis + ", currentTimeMillis = " + System.currentTimeMillis());

        int diffDay;
        if (diffMillis > 0) {
            diffDay = (int) (diffMillis / 1000 / 60 / 60 / 24);
        } else {
            diffDay = (int) ((-diffMillis) / 1000 / 60 / 60 / 24);
        }
        return "diffDay = " + diffDay;
    }

    /**
     * 日历转换
     */
    public String launcherCalendar() {
        // 打印常量
        LogUtil.v("MIN_YEAR = " + LunarCalendarUtils.MIN_YEAR);
        LogUtil.v("MAX_YEAR = " + LunarCalendarUtils.MAX_YEAR + "\n");

        // 测试打印 月份天数
        for (int i = 1; i < 13; i++) {
            LogUtil.v("2017-" + i + ": = " + LunarCalendarUtils.daysInMonth(2017, i));
        }
        LogUtil.v("");

        // 测试正常转化:
        int[] day1 = LunarCalendarUtils.lunarToSolar(2017, 1, 10, false);
        LogUtil.v("2017, 1, 10 阳历 = " + Arrays.toString(day1)); // 2017, 1, 10 阳历 = [2017, 2, 6]

        int[] day2 = LunarCalendarUtils.solarToLunar(2017, 2, 6);
        LogUtil.v("2017, 2, 6 阴历 = " + Arrays.toString(day2) + "\n"); // 2017, 2, 6 阴历 = [2017, 1, 10, 0]

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("2020-4-7阴历,对应的阳历是:");

        // 测试不知是否是闰月时,转化 闰月
        int leapMonth = LunarCalendarUtils.leapMonth(2020); // 获取 润的月份
        int[] day3 = LunarCalendarUtils.lunarToSolar(2020, 4, 7, false); // 假设没有 润,获取 该月份
        boolean isLeap = (leapMonth == day3[1]);
        LogUtil.v(isLeap ? "当月是闰月" : "当月不是闰月");
        if (isLeap) {
            int[] day4 = LunarCalendarUtils.lunarToSolar(2020, 4, 7, true);
            LogUtil.v("day : " + Arrays.toString(day4));
            stringBuffer.append(Arrays.toString(day4));
        } else {
            LogUtil.v("day : " + Arrays.toString(day3));
            stringBuffer.append(Arrays.toString(day3));
        }

        return stringBuffer.toString();
    }
}
