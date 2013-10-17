package com.zzy.xiaoyacz;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

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
        mTabHost.addTab(mTabHost.newTabSpec("test").setIndicator("练习"),
                TestFragment.class, null);
    }
}
