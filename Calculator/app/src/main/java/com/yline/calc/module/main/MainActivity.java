package com.yline.calc.module.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yline.base.BaseActivity;
import com.yline.calc.R;
import com.yline.calc.module.count.TimeCountActivity;
import com.yline.calc.utils.LunarCalendarUtils;
import com.yline.view.recycler.adapter.AbstractRecyclerAdapter;
import com.yline.view.recycler.callback.OnRecyclerItemClickListener;
import com.yline.view.recycler.holder.RecyclerViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity {
    private MainRecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.main_recycler);
        mRecyclerAdapter = new MainRecyclerAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(mRecyclerAdapter);

        initViewClick();
    }

    private void initViewClick() {
        mRecyclerAdapter.setOnItemClickListener(new OnRecyclerItemClickListener<ItemModel>() {
            @Override
            public void onItemClick(RecyclerViewHolder viewHolder, ItemModel model, int position) {
                TimeCountActivity.launch(MainActivity.this, model.name, model.targetMills, model.isFuture);
            }
        });
    }

    private void initData() {
        List<ItemModel> modelList = new ArrayList<>();
        modelList.add(new ItemModel("生命计算器", getMillisByLunar(1994, 4, 7, false), false));
        modelList.add(new ItemModel("2020倒计时", getMillisByLunar(2020, 4, 7, true), true));
        mRecyclerAdapter.setDataList(modelList, true);
    }

    private static long getMillisByLunar(int year, int month, int monthDay, boolean isLeapMonth) {
        int[] days = LunarCalendarUtils.lunarToSolar(year, month, monthDay, isLeapMonth);
        return getMillis(days[0], days[1], days[2]);
    }

    private static long getMillis(int year, int month, int monthDay) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.set(year, month, monthDay, 8, 0, 0);

        return calendar.getTimeInMillis();
    }

    private static class MainRecyclerAdapter extends AbstractRecyclerAdapter<ItemModel> {
        private OnRecyclerItemClickListener<ItemModel> sOnItemClickListener;

        @Override
        public int getItemRes() {
            return R.layout.item_main;
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerViewHolder recyclerViewHolder, int i) {
            final ItemModel model = get(i);
            recyclerViewHolder.setText(R.id.item_main_content, model.name);

            recyclerViewHolder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != sOnItemClickListener) {
                        int position = recyclerViewHolder.getAdapterPosition();
                        sOnItemClickListener.onItemClick(recyclerViewHolder, model, position);
                    }
                }
            });
        }

        private void setOnItemClickListener(OnRecyclerItemClickListener<ItemModel> listener) {
            this.sOnItemClickListener = listener;
        }
    }

    private static class ItemModel implements Serializable {
        private static final long serialVersionUID = 1650968213367288702L;

        private String name; // 名称
        private long targetMills; // 目标时间
        private boolean isFuture; // true -> 倒计时；false -> 总计时

        public ItemModel(String name, long targetMills, boolean isFuture) {
            this.name = name;
            this.targetMills = targetMills;
            this.isFuture = isFuture;
        }
    }
}
