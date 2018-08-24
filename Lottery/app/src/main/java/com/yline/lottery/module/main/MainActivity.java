package com.yline.lottery.module.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yline.lottery.R;
import com.yline.lottery.module.lotto.LottoActivity;
import com.yline.test.BaseTestActivity;

public class MainActivity extends BaseTestActivity {
	
	@Override
	public void testStart(View view, Bundle savedInstanceState) {
		addButton("", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LottoActivity.launch(MainActivity.this);
			}
		});
	}
}
