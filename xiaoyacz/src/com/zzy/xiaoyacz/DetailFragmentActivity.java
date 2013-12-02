package com.zzy.xiaoyacz;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.zzy.xiaoyacz.data.TangShi;
import com.zzy.xiaoyacz.db.MyDB;

public class DetailFragmentActivity extends SherlockFragmentActivity{
	private ViewPager viewPager;
	private List<TangShi> tangshis;//唐诗列表
	private int currentIndex;//当前查看的序号
	private MyDB db;
	public static final String CURRENTINDEX="ci";
	public static final String TANGSHIS="tss";
	public static final String TITLE="title";
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.datail_activity);
		viewPager=(ViewPager) findViewById(R.id.viewpager);
		currentIndex=getIntent().getIntExtra(CURRENTINDEX, 0);
		tangshis=getIntent().getParcelableArrayListExtra(TANGSHIS);
		db=new MyDB(this);
		viewPager.setAdapter(new ViewPagerAdapter(this));
		viewPager.setCurrentItem(currentIndex);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		String title=getIntent().getStringExtra(TITLE);
		if(title!=null){
			getSupportActionBar().setTitle(title);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case android.R.id.home:
	        finish();
	        return true;
	    default: return super.onOptionsItemSelected(item);  
	    }
	}
	private class ViewPagerAdapter extends FragmentPagerAdapter{
		FragmentActivity mActivity;
		
		public ViewPagerAdapter(FragmentActivity mActivity) {
			super(mActivity.getSupportFragmentManager());
			this.mActivity=mActivity;
		}

		@Override
		public Fragment getItem(int index) {
			TangShi ts=tangshis.get(index);

			return DetailFragment.newInstance(ts, db);
		}

		@Override
		public int getCount() {
			return tangshis.size();
		}
		
	}
}
