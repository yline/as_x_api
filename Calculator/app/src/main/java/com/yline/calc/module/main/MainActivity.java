package com.yline.calc.module.main;

import android.os.Bundle;

import com.yline.base.BaseActivity;
import com.yline.calc.R;
import com.yline.calc.module.count.TimeCountActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TimeCountActivity.launch(MainActivity.this);
    }
}
