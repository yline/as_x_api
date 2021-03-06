package com.yline.lottery.module.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yline.application.SDKManager;
import com.yline.base.BaseFragment;
import com.yline.http.callback.OnJsonCallback;
import com.yline.lottery.R;
import com.yline.lottery.http.OkHttpManager;
import com.yline.lottery.http.model.LottoHistoryModel;
import com.yline.lottery.module.reward.LottoRewardActivity;
import com.yline.lottery.module.type.LottoTypeActivity;
import com.yline.lottery.sp.SPManager;
import com.yline.lottery.sp.StaticManager;
import com.yline.lottery.sp.TypeManager;
import com.yline.lottery.view.LoadingView;
import com.yline.lottery.view.SwitchLottoTypeView;
import com.yline.lottery.view.TextCircleLayout;
import com.yline.utils.LogUtil;
import com.yline.view.recycler.adapter.AbstractHeadFootRecyclerAdapter;
import com.yline.view.recycler.callback.OnRecyclerItemClickListener;
import com.yline.view.recycler.holder.RecyclerViewHolder;
import com.yline.view.refresh.PullToRefreshLayout;
import com.yline.view.refresh.callback.OnLoadMoreListener;
import com.yline.view.refresh.callback.OnRefreshListener;

/**
 * 首页 - 历史
 * 默认：双色球(或上次切换的结果)，页数：50
 *
 * @author yline 2018/8/30 -- 15:39
 */
public class HistoryFragment extends BaseFragment {
    private static final int REQUEST_CODE_SWITCH = 1; // 切换类型
    private static final int PAGE_SIZE = 50; // 最大50，默认50

    private HistoryRecyclerAdapter mRecyclerAdapter;
    private SwitchLottoTypeView mHeaderView;
    private PullToRefreshLayout mRefreshLayout;

    private LoadingView mLoadingView;
    private TextView titleTextView;

    private int pageNum; // 居然是1开始，每次回来都需要加1；接口的奇葩啊

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        StaticManager.getInstance().setIsRefreshHistory(false);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (StaticManager.getInstance().isRefreshHistory()) {
            StaticManager.getInstance().setIsRefreshHistory(false);
            initData();
        }
    }

    private void initView(View view) {
        mRecyclerAdapter = new HistoryRecyclerAdapter(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.history_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mRecyclerAdapter);

        mHeaderView = new SwitchLottoTypeView(getActivity());
        mRecyclerAdapter.addHeadView(mHeaderView);

        mRefreshLayout = view.findViewById(R.id.history_refresh);
        mLoadingView = view.findViewById(R.id.history_loading);
        titleTextView = view.findViewById(R.id.history_title_content);

        initViewClick();
    }

    private void initViewClick() {
        // 切换彩种
        mHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LottoTypeActivity.launchForResult(HistoryFragment.this, REQUEST_CODE_SWITCH);
            }
        });

        // 首次进入重新加载
        mLoadingView.setOnReloadClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });

        // 下拉刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void refresh() {
                if (mRecyclerAdapter.getItemCount() != 0) { // 数据不为空，伪刷新
                    SDKManager.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SDKManager.toast("已经是最新了");
                            mRefreshLayout.finishRefresh();
                        }
                    }, 1000);
                } else { // 数据为空，重新加载
                    initData();
                }
            }
        });

        // 上拉加载
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                loadMoreData();
            }
        });

        // 点击item
        mRecyclerAdapter.setOnRecyclerItemClickListener(new OnRecyclerItemClickListener<LottoHistoryModel.HistoryDetail>() {
            @Override
            public void onItemClick(RecyclerViewHolder viewHolder, LottoHistoryModel.HistoryDetail historyDetail, int position) {
                String lottoId = historyDetail.getLottery_id();
                String lottoName = TypeManager.getTypeNameByLottoId(lottoId);

                LottoRewardActivity.launch(getActivity(), lottoId, historyDetail.getLottery_no(), lottoName);
            }
        });
    }

    /**
     * 首次加载
     */
    private void initData() {
        updateTitleAndHeader();
        mLoadingView.loading();

        pageNum = 1;
        String lottoId = SPManager.getInstance().getLastLottoId();
        OkHttpManager.lottoQueryHistory(lottoId, PAGE_SIZE, pageNum, new OnJsonCallback<LottoHistoryModel>() {
            @Override
            public void onFailure(int code, String msg) {
                mLoadingView.loadFailed();
            }

            @Override
            public void onResponse(LottoHistoryModel lottoHistoryModel) {
                if (null != lottoHistoryModel) {
                    mLoadingView.loadSuccess();

                    pageNum = lottoHistoryModel.getPage() + 1;
                    mRecyclerAdapter.setDataList(lottoHistoryModel.getLotteryResList(), true);
                } else {
                    mLoadingView.loadFailed();
                }
            }
        });
    }

    private void updateTitleAndHeader() {
        String lottoId = SPManager.getInstance().getLastLottoId();
        String lottoName = TypeManager.getTypeNameByLottoId(lottoId);

        titleTextView.setText(String.format("历史—%s", lottoName));
        mHeaderView.updateData(lottoName);
    }

    /**
     * 加载更多
     */
    private void loadMoreData() {
        String lottoId = SPManager.getInstance().getLastLottoId();
        OkHttpManager.lottoQueryHistory(lottoId, PAGE_SIZE, pageNum, new OnJsonCallback<LottoHistoryModel>() {
            @Override
            public void onFailure(int code, String msg) {
                // do nothing
                mRefreshLayout.finishLoadMore();
            }

            @Override
            public void onResponse(LottoHistoryModel lottoHistoryModel) {
                mRefreshLayout.finishLoadMore();

                if (null != lottoHistoryModel) {
                    pageNum = lottoHistoryModel.getPage() + 1;
                    mRecyclerAdapter.addAll(lottoHistoryModel.getLotteryResList(), true);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.v("requestCode = " + requestCode + ", resultCode = " + resultCode);
        if (requestCode == REQUEST_CODE_SWITCH) {
            if (resultCode == Activity.RESULT_OK) { // 切换成功，则重新加载数据
                initData();
            }
        }
    }

    private class HistoryRecyclerAdapter extends AbstractHeadFootRecyclerAdapter<LottoHistoryModel.HistoryDetail> {
        private OnRecyclerItemClickListener<LottoHistoryModel.HistoryDetail> listener;

        private HistoryRecyclerAdapter(Context context) {
            super(context);
        }

        @Override
        public int getItemRes() {
            return R.layout.item_fragment_history;
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, int position) {
            final LottoHistoryModel.HistoryDetail detailModel = get(position);

            holder.setText(R.id.item_history_term, String.format("第%s期", detailModel.getLottery_no()));
            holder.setText(R.id.item_history_time, detailModel.getLottery_date());

            TextCircleLayout circleLayout = holder.get(R.id.item_history_number);
            circleLayout.setText(detailModel.getLottery_res(), detailModel.getLottery_id());

            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        listener.onItemClick(holder, detailModel, holder.getAdapterPosition());
                    }
                }
            });
        }

        private void setOnRecyclerItemClickListener(OnRecyclerItemClickListener<LottoHistoryModel.HistoryDetail> listener) {
            this.listener = listener;
        }
    }
}
