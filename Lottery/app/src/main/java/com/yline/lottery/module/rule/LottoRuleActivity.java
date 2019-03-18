package com.yline.lottery.module.rule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.yline.base.BaseActivity;
import com.yline.lottery.R;
import com.yline.lottery.module.type.LottoTypeActivity;
import com.yline.lottery.sp.SPManager;
import com.yline.lottery.sp.TypeManager;
import com.yline.lottery.view.SwitchLottoTypeView;

/**
 * 展示各个规则，暂时采用HTML方式，可修改性高
 *
 * @author yline 2018/8/31 -- 11:35
 */
public class LottoRuleActivity extends BaseActivity {
    private static final String KEY_LOTTO_ID = "lottoId";
    private static final int REQUEST_CODE_SWITCH = 1;

    public static void launch(Context context, String lottoId) {
        if (null != context) {
            Intent intent = new Intent();
            intent.setClass(context, LottoRuleActivity.class);
            intent.putExtra(KEY_LOTTO_ID, lottoId);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    private WebView mWebView;
    private SwitchLottoTypeView mSwitchView;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);

        // 获取 lottoId
        String lottoId = null;
        Intent intent = getIntent();
        if (null != intent) {
            lottoId = intent.getStringExtra(KEY_LOTTO_ID);
        }

        lottoId = TextUtils.isEmpty(lottoId) ? SPManager.getInstance().getLastLottoId() : lottoId;
        initView();
        initData(lottoId);
    }

    private void initView() {
        mSwitchView = findViewById(R.id.rule_switch_type);
        mWebView = findViewById(R.id.rule_webview);
        titleTextView = findViewById(R.id.rule_title_content);

        initViewClick();
    }

    private void initViewClick() {
        findViewById(R.id.rule_title_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSwitchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LottoTypeActivity.launchForResult(LottoRuleActivity.this, REQUEST_CODE_SWITCH);
            }
        });
    }

    private void initData(String lottoId) {
        String lottoName = TypeManager.getTypeNameByLottoId(lottoId);

        titleTextView.setText(String.format("规则详解—%s", lottoName));
        mSwitchView.updateData(lottoName);

        mWebView.loadUrl(TypeManager.getTypeFileNameByLottoId(lottoId));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SWITCH) {
            if (resultCode == Activity.RESULT_OK) {
                String fileName = SPManager.getInstance().getLastLottoId();
                if (!TextUtils.isEmpty(fileName)) {
                    initData(fileName);
                }
            }
        }
    }
}
