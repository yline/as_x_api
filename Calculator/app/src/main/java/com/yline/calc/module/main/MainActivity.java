package com.yline.calc.module.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yline.base.BaseActivity;
import com.yline.calc.R;
import com.yline.calc.module.count.TimeCountActivity;
import com.yline.view.recycler.adapter.AbstractRecyclerAdapter;
import com.yline.view.recycler.callback.OnRecyclerItemClickListener;
import com.yline.view.recycler.holder.RecyclerViewHolder;

public class MainActivity extends BaseActivity {
    private MainRecyclerAdapter mRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TimeCountActivity.launch(MainActivity.this);
        initView();
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.main_recycler);
        mRecyclerAdapter = new MainRecyclerAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(mRecyclerAdapter);

        initViewClick();
    }

    private void initViewClick() {
        mRecyclerAdapter.setOnItemClickListener(new OnRecyclerItemClickListener<String>() {
            @Override
            public void onItemClick(RecyclerViewHolder viewHolder, String s, int position) {
                // todo 点击事件
            }
        });
    }

    private static class MainRecyclerAdapter extends AbstractRecyclerAdapter<String> {
        private OnRecyclerItemClickListener<String> sOnItemClickListener;

        @Override
        public int getItemRes() {
            return R.layout.item_main;
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerViewHolder recyclerViewHolder, int i) {
            recyclerViewHolder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != sOnItemClickListener) {
                        int position = recyclerViewHolder.getAdapterPosition();
                        sOnItemClickListener.onItemClick(recyclerViewHolder, get(position), position);
                    }
                }
            });
        }

        public void setOnItemClickListener(OnRecyclerItemClickListener<String> listener) {
            this.sOnItemClickListener = listener;
        }
    }
}
