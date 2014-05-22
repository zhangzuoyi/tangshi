package com.zzy.xiaoyacz.util;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.feiwoone.banner.AdBanner;
import com.feiwoone.banner.RecevieAdListener;
import com.zzy.xiaoyacz.R;

public class FeiwoUtil {
	public static final String APP_KEY="iGjvOrIpiu2sHL202Dh6zlv6";

	public static void addFeiwoAd(final LinearLayout adlayout,Activity activity){
    	
    	AdBanner myAdView = new AdBanner(activity);
    	adlayout.addView(myAdView);
		myAdView.setAppKey(APP_KEY);
		RecevieAdListener adListener = new RecevieAdListener() {
			@Override
			public void onSucessedRecevieAd(AdBanner adView) {
				adlayout.setVisibility(View.VISIBLE);
			}
			@Override
			public void onFailedToRecevieAd(AdBanner adView) {
				adlayout.setVisibility(View.GONE);
			}
		};
		myAdView.setRecevieAdListener(adListener);
    }
}
