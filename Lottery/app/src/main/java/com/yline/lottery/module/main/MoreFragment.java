package com.yline.lottery.module.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yline.application.SDKManager;
import com.yline.base.BaseFragment;
import com.yline.http.callback.OnJsonCallback;
import com.yline.lottery.R;
import com.yline.lottery.bugly.BuglyConfig;
import com.yline.lottery.http.OkHttpManager;
import com.yline.lottery.http.model.LottoBonusModel;
import com.yline.lottery.module.feedback.FeedbackActivity;
import com.yline.lottery.module.main.view.MoreItemView;
import com.yline.lottery.module.rule.LottoRuleActivity;
import com.yline.lottery.module.upgrade.UpgradeActivity;
import com.yline.lottery.sp.SPManager;
import com.yline.lottery.view.TextCircleLayout;
import com.yline.test.BaseTestFragment;
import com.yline.utils.LogUtil;

import java.util.Random;

public class MoreFragment extends BaseFragment {
    private MoreItemView ruleItem, upgradeItem, helpItem;
    private TextCircleLayout mTextCircleLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_more, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initView(View view) {
        ruleItem = view.findViewById(R.id.more_rule);
        upgradeItem = view.findViewById(R.id.more_upgrade);
        helpItem = view.findViewById(R.id.more_help);
        mTextCircleLayout = view.findViewById(R.id.more_dlt_yline);
        initViewClick();
    }

    private void initViewClick() {
        ruleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LottoRuleActivity.launch(getActivity());
            }
        });

        upgradeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpgradeActivity.launch(getActivity());
            }
        });

        helpItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackActivity.launch(getActivity());
            }
        });
    }

    private void initData() {
        ruleItem.setData(R.drawable.more_item_rule, "规则详解");
        upgradeItem.setData(R.drawable.more_item_upgrade, "版本升级");
        helpItem.setData(R.drawable.more_item_help, "意见反馈");

        mTextCircleLayout.setText("05,07,09,19,30,02,10", "dlt");
    }
}
