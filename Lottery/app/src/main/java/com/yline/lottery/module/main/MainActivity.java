package com.yline.lottery.module.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.yline.base.BaseFragmentActivity;
import com.yline.lottery.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 主页的整体
 *
 * @author yline 2018/8/30 -- 15:36
 */
public class MainActivity extends BaseFragmentActivity {
	public static void launch(Context context) {
		if (null != context) {
			Intent intent = new Intent();
			intent.setClass(context, MainActivity.class);
			if (!(context instanceof Activity)) {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}
			context.startActivity(intent);
		}
	}
	
	private ActualFragment mActualFragment;
	private HistoryFragment mHistoryFragment;
	private QueryFragment mQueryFragment;
	private MoreFragment mMoreFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//		String lotteryId = SPManager.getInstance().getLotteryId();
		//		if (TextUtils.isEmpty(lotteryId)) {
		//			SDKManager.toast("请先选择彩票类型");
		//			LottoTypeActivity.launch(MainActivity.this);
		//		}
		
		setContentView(R.layout.activity_main);
		initView();
		initData();
	}
	
	private void initView() {
		mActualFragment = new ActualFragment();
		mHistoryFragment = new HistoryFragment();
		mQueryFragment = new QueryFragment();
		mMoreFragment = new MoreFragment();
		
		ViewPager viewPager = findViewById(R.id.main_view_pager);
		viewPager.setOffscreenPageLimit(4);
		TabLayout tabLayout = findViewById(R.id.main_tab);
		
		MainPagerAdapter pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(pagerAdapter);
		tabLayout.setupWithViewPager(viewPager);
		
		pagerAdapter.setViewList(new String[]{"最新", "历史", "查询", "更多"},
				new Fragment[]{mActualFragment, mHistoryFragment, mQueryFragment, mMoreFragment});
		
		initViewClick();
	}
	
	private void initViewClick() {
	
	}
	
	private void initData() {
	
	}
	
	private class MainPagerAdapter extends FragmentStatePagerAdapter {
		private List<String> mTitleList = new ArrayList<>();
		private List<Fragment> mFragmentList = new ArrayList<>();
		
		private MainPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public Fragment getItem(int position) {
			return mFragmentList.get(position);
		}
		
		@Override
		public int getCount() {
			return mFragmentList.size();
		}
		
		@Nullable
		@Override
		public CharSequence getPageTitle(int position) {
			return mTitleList.get(position);
		}
		
		/**
		 * 设置数据，并更新界面
		 *
		 * @param titleArray    标题
		 * @param fragmentArray 内容
		 */
		private void setViewList(String[] titleArray, Fragment[] fragmentArray) {
			if (titleArray.length != fragmentArray.length || titleArray.length == 0) {
				throw new RuntimeException("首页，设置的参数错误！！！");
			}
			
			mTitleList = Arrays.asList(titleArray);
			mFragmentList = Arrays.asList(fragmentArray);
			notifyDataSetChanged();
		}
	}
}
