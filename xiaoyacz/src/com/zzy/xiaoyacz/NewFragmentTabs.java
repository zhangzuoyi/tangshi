package com.zzy.xiaoyacz;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TabHost;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class NewFragmentTabs extends SherlockFragmentActivity {
	private TabHost mTabHost;
	TabManager mTabManager;
	ViewPager viewPager;
	private List<FragmentInfo> infos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_tabs);
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        viewPager=(ViewPager) findViewById(R.id.viewpager);
//        mTabManager = new TabManager(this, mTabHost, R.id.realtabcontent);
//
//        mTabManager.addTab(mTabHost.newTabSpec("list").setIndicator("列表"),
//                ListAllFragment.class, null);
//        mTabManager.addTab(mTabHost.newTabSpec("type").setIndicator("分类"),
//                ByTypeFragment.class, null);
//        mTabManager.addTab(mTabHost.newTabSpec("author").setIndicator("诗人"),
//                ByAuthorFragment.class, null);
//        mTabManager.addTab(mTabHost.newTabSpec("collect").setIndicator("收藏"),
//                CollectFragment.class, null);
//        mTabManager.addTab(mTabHost.newTabSpec("test").setIndicator("练习"),
//                TestFragment.class, null);
        
        infos=new ArrayList<FragmentInfo>();
        infos.add(new FragmentInfo("list","列表",ListAllFragment.class));
        infos.add(new FragmentInfo("type","分类",ByTypeFragment.class));
        infos.add(new FragmentInfo("author","诗人",ByAuthorFragment.class));
        infos.add(new FragmentInfo("collect","收藏",CollectFragment.class));
        infos.add(new FragmentInfo("test","练习",TestFragment.class));
        for(FragmentInfo info:infos){
        	mTabHost.addTab(mTabHost.newTabSpec(info.tag).setIndicator(info.title).setContent(new EmptyTabFactory(this)));
        }
        viewPager.setAdapter(new ViewPagerAdapter(this));
        mTabHost.setOnTabChangedListener(new TabListener());
        viewPager.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}

			@Override
			public void onPageSelected(int arg0) {
				mTabHost.setCurrentTab(arg0);
			}
        	
        });
        getOverflowMenu();
    }
    private void getOverflowMenu() {

        try {
           ViewConfiguration config = ViewConfiguration.get(this);
           Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
           if(menuKeyField != null) {
               menuKeyField.setAccessible(true);
               menuKeyField.setBoolean(config, false);
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
   }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.menu_settings){
			Intent i=new Intent(this,SettingsActivity.class);
			startActivity(i);
		}
		return true;
	}
	private class ViewPagerAdapter extends FragmentPagerAdapter{
		FragmentActivity mActivity;
		
		public ViewPagerAdapter(FragmentActivity mActivity) {
			super(mActivity.getSupportFragmentManager());
			this.mActivity=mActivity;
		}

		@Override
		public Fragment getItem(int index) {
			FragmentInfo info=infos.get(index);
			if(info.frag==null){
				info.frag=Fragment.instantiate(mActivity, info.cla.getName());
			}
			return info.frag;
		}

		@Override
		public int getCount() {
			return infos.size();
		}
		
	}
	private class FragmentInfo{
		public FragmentInfo(String tag,String title,Class cla){
			this.tag=tag;
			this.title=title;
			this.cla=cla;
		}
		String tag;
		Class cla;
		String title;
		Fragment frag;
	}
	private class TabListener implements TabHost.OnTabChangeListener{

		@Override
		public void onTabChanged(String tabId) {
			int ind=0;
			int size=infos.size();
			for(int i=0;i<size;i++){
				FragmentInfo info=infos.get(i);
				if(tabId.equals(info.tag)){
					ind=i;
					break;
				}
			}
			viewPager.setCurrentItem(ind);
			
		}
		
	}
	private class EmptyTabFactory implements TabHost.TabContentFactory {
        private final Context mContext;

        public EmptyTabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }
	/**
     * This is a helper class that implements a generic mechanism for
     * associating fragments with the tabs in a tab host.  It relies on a
     * trick.  Normally a tab host has a simple API for supplying a View or
     * Intent that each tab will show.  This is not sufficient for switching
     * between fragments.  So instead we make the content part of the tab host
     * 0dp high (it is not shown) and the TabManager supplies its own dummy
     * view to show as the tab content.  It listens to changes in tabs, and takes
     * care of switch to the correct fragment shown in a separate content area
     * whenever the selected tab changes.
     */
    public static class TabManager implements TabHost.OnTabChangeListener {
        private final FragmentActivity mActivity;
        private final TabHost mTabHost;
        private final int mContainerId;
        private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
        TabInfo mLastTab;

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;
            private Fragment fragment;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabManager(FragmentActivity activity, TabHost tabHost, int containerId) {
            mActivity = activity;
            mTabHost = tabHost;
            mContainerId = containerId;
            mTabHost.setOnTabChangedListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
            tabSpec.setContent(new DummyTabFactory(mActivity));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state.  If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            info.fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
            if (info.fragment != null && !info.fragment.isDetached()) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                ft.detach(info.fragment);
                ft.commit();
            }

            mTabs.put(tag, info);
            mTabHost.addTab(tabSpec);
        }

        @Override
        public void onTabChanged(String tabId) {
            TabInfo newTab = mTabs.get(tabId);
            if (mLastTab != newTab) {
                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                if (mLastTab != null) {
                    if (mLastTab.fragment != null) {
                        ft.detach(mLastTab.fragment);
                    }
                }
                if (newTab != null) {
                    if (newTab.fragment == null) {
                        newTab.fragment = Fragment.instantiate(mActivity,
                                newTab.clss.getName(), newTab.args);
                        ft.add(mContainerId, newTab.fragment, newTab.tag);
                    } else {
                        ft.attach(newTab.fragment);
                    }
                }

                mLastTab = newTab;
                ft.commit();
                mActivity.getSupportFragmentManager().executePendingTransactions();
            }
        }
    }
}
