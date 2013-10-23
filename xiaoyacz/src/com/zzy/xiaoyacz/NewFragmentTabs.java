package com.zzy.xiaoyacz;

import java.lang.reflect.Field;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;

public class NewFragmentTabs extends FragmentActivity {
	private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_tabs);
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("list").setIndicator("列表"),
                ListAllFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("type").setIndicator("分类"),
                ByTypeFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("author").setIndicator("诗人"),
                ByAuthorFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("collect").setIndicator("收藏"),
                CollectFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("test").setIndicator("练习"),
                TestFragment.class, null);
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
		getMenuInflater().inflate(R.menu.activity_main, menu);
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
    
}
