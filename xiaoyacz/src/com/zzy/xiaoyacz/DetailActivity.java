package com.zzy.xiaoyacz;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.zzy.xiaoyacz.data.TangShi;
import com.zzy.xiaoyacz.db.MyDB;
//TODO 调整字体大小
//TODO 有几种字体大小可选
public class DetailActivity extends SherlockActivity {
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		db=new MyDB(this);
		Bundle bundle=getIntent().getExtras();
		ts=(TangShi) bundle.getSerializable("ts");
		TextView title=(TextView) findViewById(R.id.title);
		TextView author=(TextView) findViewById(R.id.author);
		TextView content=(TextView) findViewById(R.id.content);
		TextView explain=(TextView) findViewById(R.id.explain);
		title.setText(ts.getTitle());
		author.setText(ts.getAuthor());
		content.setText(Html.fromHtml(ts.getContent()));
		explain.setText(Html.fromHtml(ts.getExplain()));
		
		playPauseButton=(ImageButton) findViewById(R.id.button1);
		seekbar=(SeekBar) findViewById(R.id.seek_bar);
		if(ts.getAudio()!=null&&!ts.getAudio().equals("")){//有音频文件
			new MediaPlayerTask().execute();
			
		}else{//没有音频文件
			seekbar.setVisibility(View.GONE);
			playPauseButton.setVisibility(View.GONE);
		}
		getActionBar().setDisplayHomeAsUpEnabled(true);
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
	protected void onPause() {
		if(m_mediaPlayer != null && m_mediaPlayer.isPlaying()) {
			needToResume = true;
			pauseMP();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(needToResume && m_mediaPlayer != null) {
			startMP();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_detail, menu);
		setMenuItemIcon(menu.findItem(R.id.collect));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.collect){
			item.setIcon(android.R.drawable.btn_star_big_off);
			db.open();
			if(ts.getCollectStatus()==1){
				db.cancelCollect(ts.getId());
				ts.setCollectStatus(0);
				Toast.makeText(this, getResources().getString(R.string.cancel_collect_success), Toast.LENGTH_SHORT).show();
			}else{
				db.collect(ts.getId());
				ts.setCollectStatus(1);
				Toast.makeText(this, getResources().getString(R.string.collect_success), Toast.LENGTH_SHORT).show();
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


	private class MediaPlayerTask extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... params) {
//			NetworkInfo wifiNetworkInfo, mobileNetworkInfo;

			m_mediaPlayer = MediaPlayer.create(DetailActivity.this,Uri.parse(aliyunUrl+ts.getAudio()));
			/*ConnectivityManager connectivity = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			wifiNetworkInfo =connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			mobileNetworkInfo =connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			//||(!onlyWifi&&mobileNetworkInfo.isConnected())
			if(wifiNetworkInfo.isConnected()){
				m_mediaPlayer = MediaPlayer.create(DetailActivity.this,Uri.parse(aliyunUrl+ts.getAudio()));
				Log.i("network", "connected");
			}else{
				m_mediaPlayer=null;
				String msg=getResources().getString(R.string.no_network_no_recite);
				Looper.prepare();
				Toast.makeText(DetailActivity.this, msg, Toast.LENGTH_SHORT).show();
				Looper.loop();
			}*/
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
