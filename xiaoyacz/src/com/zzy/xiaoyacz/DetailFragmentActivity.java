package com.zzy.xiaoyacz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.zzy.xiaoyacz.data.TangShi;
import com.zzy.xiaoyacz.db.MyDB;

public class DetailFragmentActivity extends FragmentActivity{
	private ViewPager viewPager;
	private long[] tangshiIds;//用ID代替唐诗对象，能减少内存占用，提高速度
	private int currentIndex;//当前查看的序号
	private MyDB db;
	public static final String CURRENTINDEX="ci";
	public static final String TANGSHIS="tss";
	public static final String TANGSHIIDS="tsids";
	public static final String TITLE="title";
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.datail_activity);
		viewPager=(ViewPager) findViewById(R.id.viewpager);
		currentIndex=getIntent().getIntExtra(CURRENTINDEX, 0);
		tangshiIds=getIntent().getLongArrayExtra(TANGSHIIDS);
		db=MyDB.getInstance(this);
		viewPager.setAdapter(new ViewPagerAdapter(this));
		viewPager.setCurrentItem(currentIndex);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		String title=getIntent().getStringExtra(TITLE);
		if(title!=null){
			getActionBar().setTitle(title);
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
			long id=tangshiIds[index];
			db.open();
			TangShi ts=db.findTangshiById(id);
			db.close();

			return DetailFragment.newInstance(ts, db);
		}

		@Override
		public int getCount() {
			return tangshiIds.length;
		}
		
	}
}
