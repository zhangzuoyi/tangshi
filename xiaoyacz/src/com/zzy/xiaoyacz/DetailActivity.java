package com.zzy.xiaoyacz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
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
import com.zzy.xiaoyacz.util.FileUtil;

public class DetailActivity extends SherlockActivity {
	TangShi ts;
	ImageButton playPauseButton;
	SeekBar seekbar;
//	ImageButton recordButton;
	MediaPlayer m_mediaPlayer;
	String aliyunUrl="http://oss.aliyuncs.com/object_test/tangshi/";
	boolean isPrepare=false;//音频文件是否已经设置好
	Handler handler;
	MyDB db;
	boolean onlyWifi;

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
//		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(DetailActivity.this);
//		boolean download = settings.getBoolean("audio_download", false);
//		onlyWifi = settings.getBoolean("onlyuse_wifi", false);
		if(ts.getAudio()!=null&&!ts.getAudio().equals("")){
			new MediaPlayerTask().execute();
			
		}else{
			seekbar.setVisibility(View.GONE);
			playPauseButton.setVisibility(View.GONE);
		}
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	private void setRecordButton(){
		/*recordButton=(ImageButton) findViewById(R.id.button2);
		recordButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Toast.makeText(DetailActivity.this, "start recording", Toast.LENGTH_SHORT).show();
				//recording
				short[] myRecordedAudio = new short[10000];
				AudioRecord audioRecord = new AudioRecord(
					MediaRecorder.AudioSource.MIC, 11025,
					AudioFormat.CHANNEL_IN_MONO,
					AudioFormat.ENCODING_PCM_16BIT, 10000);
				audioRecord.startRecording();
				audioRecord.read(myRecordedAudio, 0, 10000);
				audioRecord.stop();

				//playing
				AudioTrack audioTrack = new AudioTrack(
						AudioManager.STREAM_MUSIC, 11025,
						AudioFormat.CHANNEL_OUT_MONO,
						AudioFormat.ENCODING_PCM_16BIT, 4096,
						AudioTrack.MODE_STREAM);
				audioTrack.play();
				audioTrack.write(myRecordedAudio, 0, 10000);
				audioTrack.stop();

			}
			
		});*/
	}
	private void prepareAudio(){
		if(FileUtil.IsExternalStorageAvailableAndWriteable()){
			Toast.makeText(DetailActivity.this,"storage writeable:true", Toast.LENGTH_SHORT).show();
			//先去本地找音频文件
			File dir=DetailActivity.this.getExternalFilesDir(null);
			File file=new File(dir,ts.getAudio());
			if(file.exists()){
				try {
					m_mediaPlayer.setDataSource(file.getPath());
					m_mediaPlayer.prepare();
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}else{
				//再看是否需要缓存
				SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(DetailActivity.this);
				 boolean download = settings.getBoolean("audio_download", false);
				 Toast.makeText(DetailActivity.this,"need cache:"+String.valueOf( download), Toast.LENGTH_SHORT).show();
				 if(download){
					 InputStream in=openHttpGETConnection(aliyunUrl+ts.getAudio());
					 byte[] bs=new byte[1024];
					 try {
						OutputStream out=new FileOutputStream(file);
						int length=in.read(bs);
						while(length>0){
							out.write(bs, 0, length);
							 length=in.read(bs);
						}
						out.close();
						in.close();
						m_mediaPlayer.setDataSource(file.getPath());
						m_mediaPlayer.prepare();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				 }else{
					 try {
						m_mediaPlayer.setDataSource(DetailActivity.this,Uri.parse(aliyunUrl+ts.getAudio()));
						m_mediaPlayer.prepare();
					} catch (IOException e) {
						e.printStackTrace();
					}
				 }
			}
		}else{//外部存储不可用，直接从网络链接播放
			try {
				m_mediaPlayer.setDataSource(DetailActivity.this,Uri.parse(aliyunUrl+ts.getAudio()));
				m_mediaPlayer.prepare();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public InputStream openHttpGETConnection(String url) {
		InputStream inputStream = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
			inputStream = httpResponse.getEntity().getContent();
		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}
		return inputStream;
	}

	void pauseMP() {
		playPauseButton.setImageResource(android.R.drawable.ic_media_play);
		m_mediaPlayer.pause();
		handler2.removeCallbacks(updateThread);
	}
	void startMP() {
		m_mediaPlayer.start();
		playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
		handler2.post(updateThread);
	}
	Handler handler2 = new Handler();  
    Runnable updateThread = new Runnable(){  
        public void run() {  
            //获得歌曲现在播放位置并设置成播放进度条的值  
        	seekbar.setProgress(m_mediaPlayer.getCurrentPosition());  
            //每次延迟100毫秒再启动线程  
            handler2.postDelayed(updateThread, 100);  
        }  
    };  

	boolean needToResume = false;
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
			NetworkInfo wifiNetworkInfo, mobileNetworkInfo;

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
					handler2.removeCallbacks(updateThread);
					seekbar.setProgress(0);
				}
				
			});
			playPauseButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					if(!isPrepare){
//						prepareAudio();//暂先不做缓存
						isPrepare=true;
					}
					
					//最后播放音频
					
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
	                // TODO Auto-generated method stub  
	            }  
	            @Override  
	            public void onStopTrackingTouch(SeekBar seekBar) {  
	                // TODO Auto-generated method stub        
	            }  
	        });
		}
		
		
	}
}
