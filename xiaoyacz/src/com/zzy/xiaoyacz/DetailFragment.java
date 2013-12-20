package com.zzy.xiaoyacz;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.zzy.xiaoyacz.data.TangShi;
import com.zzy.xiaoyacz.db.MyDB;
import com.zzy.xiaoyacz.util.StringUtil;

public class DetailFragment extends SherlockFragment{
	private TangShi ts;
	private ImageButton playPauseButton;
	private SeekBar seekbar;
	private MediaPlayer m_mediaPlayer;
	public static final String aliyunUrl="http://oss.aliyuncs.com/object_test/tangshi/";
	private MyDB db;
	boolean onlyWifi;
	boolean needToResume = false;//音频是否需要继续播放
	private Handler handler = new Handler();//用于控制音频进度条
	private Runnable updateThread = new Runnable(){  
        public void run() {  
            //获得歌曲现在播放位置并设置成播放进度条的值  
        	seekbar.setProgress(m_mediaPlayer.getCurrentPosition());  
            //每次延迟100毫秒再启动线程  
            handler.postDelayed(updateThread, 100);  
        }  
    };
	public static DetailFragment newInstance(TangShi ts,MyDB db){
		DetailFragment frag=new DetailFragment();
		frag.ts=ts;
		frag.db=db;
		return frag;
	}

	@Override
	  public View onCreateView(LayoutInflater inflater,
	      ViewGroup container, Bundle savedInstanceState) {

	    if (container == null) {
	      return null;
	    }

	    final View view = (View) inflater.inflate(
	        R.layout.activity_detail, container, false);
	    TextView title=(TextView) view.findViewById(R.id.title);
		TextView author=(TextView)view. findViewById(R.id.author);
		TextView content=(TextView)view. findViewById(R.id.content);
		TextView commentsLabel=(TextView) view.findViewById(R.id.comments_label);
		TextView comments=(TextView) view.findViewById(R.id.comments);
		TextView translateLabel=(TextView) view.findViewById(R.id.translate_label);
		TextView translate=(TextView) view.findViewById(R.id.translate);
		TextView explainLabel=(TextView) view.findViewById(R.id.explain_label);
		TextView explain=(TextView) view.findViewById(R.id.explain);
		title.setText(ts.getTitle());
		author.setText(ts.getAuthor());
		content.setText(Html.fromHtml(ts.getContent()));
		if(StringUtil.isBlank(ts.getComments())){
			commentsLabel.setVisibility(View.GONE);
			comments.setVisibility(View.GONE);
		}else{
			comments.setText(Html.fromHtml(ts.getComments()));
		}
		if(StringUtil.isBlank(ts.getTranslate())){
			translateLabel.setVisibility(View.GONE);
			translate.setVisibility(View.GONE);
		}else{
			translate.setText(Html.fromHtml(ts.getTranslate()));
		}
		if(StringUtil.isBlank(ts.getExplain())){
			explainLabel.setVisibility(View.GONE);
			explain.setVisibility(View.GONE);
		}else{
			explain.setText(Html.fromHtml(ts.getExplain()));
		}
		
		playPauseButton=(ImageButton) view.findViewById(R.id.button1);
		seekbar=(SeekBar) view.findViewById(R.id.seek_bar);
		if(ts.getAudio()!=null&&!ts.getAudio().equals("")){//有音频文件
//			new MediaPlayerTask().execute();
			setPlayPauseButtonAction2();
		}else{//没有音频文件
			seekbar.setVisibility(View.GONE);
			playPauseButton.setVisibility(View.GONE);
		}
//		getActionBar().setDisplayHomeAsUpEnabled(true);
		setHasOptionsMenu(true);
	    return view;
	  }
	void pauseMP() {
		playPauseButton.setImageResource(android.R.drawable.ic_media_play);
		m_mediaPlayer.pause();
		handler.removeCallbacks(updateThread);
	}
	void startMP() {
		m_mediaPlayer.start();
		playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
		handler.post(updateThread);
	}
	
	@Override
	public void onPause() {
		if(m_mediaPlayer != null && m_mediaPlayer.isPlaying()) {
			needToResume = true;
			pauseMP();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if(needToResume && m_mediaPlayer != null) {
			startMP();
		}
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getSupportMenuInflater().inflate(R.menu.activity_detail, menu);
//		setMenuItemIcon(menu.findItem(R.id.collect));
//		return true;
//	}

	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.activity_detail, menu);
		setMenuItemIcon(menu.findItem(R.id.collect));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.collect){
			item.setIcon(android.R.drawable.btn_star_big_off);
			db.open();
			if(ts.getCollectStatus()==1){
				db.cancelCollect(ts.getId());
				ts.setCollectStatus(0);
				Toast.makeText(getActivity(), getResources().getString(R.string.cancel_collect_success), Toast.LENGTH_SHORT).show();
			}else{
				db.collect(ts.getId());
				ts.setCollectStatus(1);
				Toast.makeText(getActivity(), getResources().getString(R.string.collect_success), Toast.LENGTH_SHORT).show();
			}
			db.close();
			setMenuItemIcon(item);
		}
		return true;
	}
	private void setMenuItemIcon(MenuItem item){
		if(ts.getCollectStatus()==1){
			item.setIcon(android.R.drawable.btn_star_big_on);
		}else{
			item.setIcon(android.R.drawable.btn_star_big_off);
		}
	}
	/**
	 * 设置播放按钮的动作
	 */
	private void setPlayPauseButtonAction(){
		initMediaplayer();
		//m_mediaPlayer不为空，设置播放动作
		if(m_mediaPlayer!=null){
			playPauseButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					//播放音频
					if(m_mediaPlayer.isPlaying()) {
						pauseMP();
					}else{
						startMP();
					}
				}
			});
		}else{//m_mediaPlayer为空，设置播放不可用
			playPauseButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
				}
			});
		}
		
	}
	private void initMediaplayer(){
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
		boolean onlyUseWifi=settings.getBoolean("onlyuse_wifi", true);//是否只使用wifi播放音频
		NetworkInfo wifiNetworkInfo, mobileNetworkInfo;
		ConnectivityManager connectivity = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		wifiNetworkInfo =connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		mobileNetworkInfo =connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		//wifi可用，初始化m_mediaPlayer
		if(wifiNetworkInfo.isConnected()){
			m_mediaPlayer = MediaPlayer.create(getActivity(),Uri.parse(aliyunUrl+ts.getAudio()));
		}else if(mobileNetworkInfo.isConnected()&&!onlyUseWifi){//移动网络可用，且设置了"只使用WIFI"为false，初始化m_mediaPlayer
			m_mediaPlayer = MediaPlayer.create(getActivity(),Uri.parse(aliyunUrl+ts.getAudio()));
		}
		
		if(m_mediaPlayer!=null){
			m_mediaPlayer.setOnCompletionListener(new OnCompletionListener(){

				@Override
				public void onCompletion(MediaPlayer arg0) {
					playPauseButton.setImageResource(android.R.drawable.ic_media_play);
					handler.removeCallbacks(updateThread);
					seekbar.setProgress(0);
				}
				
			});
			seekbar.setMax(m_mediaPlayer.getDuration());
			seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {  
	            @Override  
	            public void onProgressChanged(SeekBar seekBar, int progress,  
	                    boolean fromUser) {  
	                // fromUser判断是用户改变的滑块的值  
	                if(fromUser==true){  
	                	m_mediaPlayer.seekTo(progress);  
	                }  
	            }  
	            @Override  
	            public void onStartTrackingTouch(SeekBar seekBar) {  
	            }  
	            @Override  
	            public void onStopTrackingTouch(SeekBar seekBar) {  
	            }  
	        });
		}
	}
	private void setPlayPauseButtonAction2(){
		playPauseButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(m_mediaPlayer==null){
					initMediaplayer();
				}
				
				if(m_mediaPlayer!=null){
					//播放音频
					if(m_mediaPlayer.isPlaying()) {
						pauseMP();
					}else{
						startMP();
					}
				}else{
					Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private class MediaPlayerTask extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... params) {
//			NetworkInfo wifiNetworkInfo, mobileNetworkInfo;

			m_mediaPlayer = MediaPlayer.create(getActivity(),Uri.parse(aliyunUrl+ts.getAudio()));
//			ConnectivityManager connectivity = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//			wifiNetworkInfo =connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//			mobileNetworkInfo =connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//			//||(!onlyWifi&&mobileNetworkInfo.isConnected())
//			if(wifiNetworkInfo.isConnected()){
//				m_mediaPlayer = MediaPlayer.create(getActivity(),Uri.parse(aliyunUrl+ts.getAudio()));
//				Log.i("network", "connected");
//			}else{
//				m_mediaPlayer=null;
//				String msg=getResources().getString(R.string.no_network_no_recite);
//				Looper.prepare();
//				Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
//				Looper.loop();
//			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(m_mediaPlayer==null)
				return;
			
			m_mediaPlayer.setOnCompletionListener(new OnCompletionListener(){

				@Override
				public void onCompletion(MediaPlayer arg0) {
					playPauseButton.setImageResource(android.R.drawable.ic_media_play);
					handler.removeCallbacks(updateThread);
					seekbar.setProgress(0);
				}
				
			});
			playPauseButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					//播放音频
					if(m_mediaPlayer.isPlaying()) {
						pauseMP();
					}else{
						startMP();
					}
				}
			});
			seekbar.setMax(m_mediaPlayer.getDuration());
			seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {  
	            @Override  
	            public void onProgressChanged(SeekBar seekBar, int progress,  
	                    boolean fromUser) {  
	                // fromUser判断是用户改变的滑块的值  
	                if(fromUser==true){  
	                	m_mediaPlayer.seekTo(progress);  
	                }  
	            }  
	            @Override  
	            public void onStartTrackingTouch(SeekBar seekBar) {  
	            }  
	            @Override  
	            public void onStopTrackingTouch(SeekBar seekBar) {  
	            }  
	        });
		}
	}
}
