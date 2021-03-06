package com.zzy.xiaoyacz;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zzy.xiaoyacz.data.TangShi;
import com.zzy.xiaoyacz.db.MyDB;
import com.zzy.xiaoyacz.util.ConfigUtil;
import com.zzy.xiaoyacz.util.FeiwoUtil;
import com.zzy.xiaoyacz.util.NetConnectionUtil;
import com.zzy.xiaoyacz.util.StringUtil;
import com.zzy.xiaoyacz.widget.MyTextView;
import com.zzy.xiaoyacz.widget.MyTextView2;

public class DetailFragment extends Fragment{
	private TangShi ts;
//	private ImageButton playPauseButton;
//	private SeekBar seekbar;
	private MenuItem playItem;
	private MediaPlayer mediaPlayer;
	private TextView title;
	private MyTextView titlePinyin;
	private TextView author;
	private MyTextView authorPinyin;
	private TextView content;
	private MyTextView2 contentPinyin;
	private MyDB db;
	boolean onlyWifi;
	boolean needToResume = false;//音频是否需要继续播放
	private Handler handler = new Handler();//用于控制音频进度条
	private Runnable updateThread = new Runnable(){  
        public void run() {  
            //获得歌曲现在播放位置并设置成播放进度条的值  
//        	seekbar.setProgress(m_mediaPlayer.getCurrentPosition());  
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
	    title=(TextView) view.findViewById(R.id.title);
	    titlePinyin=(MyTextView) view.findViewById(R.id.titlePinyin);
		author=(TextView)view. findViewById(R.id.author);
		authorPinyin=(MyTextView)view. findViewById(R.id.authorPinyin);
		content=(TextView)view. findViewById(R.id.content);
		contentPinyin=(MyTextView2)view. findViewById(R.id.contentPinyin);
		TextView commentsLabel=(TextView) view.findViewById(R.id.comments_label);
		TextView comments=(TextView) view.findViewById(R.id.comments);
		TextView translateLabel=(TextView) view.findViewById(R.id.translate_label);
		TextView translate=(TextView) view.findViewById(R.id.translate);
		TextView explainLabel=(TextView) view.findViewById(R.id.explain_label);
		TextView explain=(TextView) view.findViewById(R.id.explain);
		title.setText(ts.getTitle());
		titlePinyin.setFullText(ts.getPinyinTitle(), ts.getTitle());
		author.setText(ts.getAuthor());
		authorPinyin.setFullText(ts.getPinyinAuthor(), ts.getAuthor());
		content.setText(Html.fromHtml(ts.getContent()));
		contentPinyin.setFullText(ts.getPinyinContent(), ts.getContent());
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
		
		setHasOptionsMenu(true);
		
        //Feiwo ad
        LinearLayout adlayout =(LinearLayout)view.findViewById(R.id.AdLinearLayout);
        FeiwoUtil.addFeiwoAd(adlayout,getActivity());
        
	    return view;
	  }
	
	private void setTangshiShow(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		boolean showPinyin=prefs.getBoolean("showPinyin", true);
		if(showPinyin){
			title.setVisibility(View.GONE);
			titlePinyin.setVisibility(View.VISIBLE);
			author.setVisibility(View.GONE);
			authorPinyin.setVisibility(View.VISIBLE);
			content.setVisibility(View.GONE);
			contentPinyin.setVisibility(View.VISIBLE);
		}else{
			title.setVisibility(View.VISIBLE);
			titlePinyin.setVisibility(View.GONE);
			author.setVisibility(View.VISIBLE);
			authorPinyin.setVisibility(View.GONE);
			content.setVisibility(View.VISIBLE);
			contentPinyin.setVisibility(View.GONE);
		}
	}
	
	private void fillContent(LinearLayout contentContainer, TangShi ts){
		String[] ps=ts.getPinyinContent().split("<br />");
		String[] cs=ts.getContent().split("<br />");
		for(int i=0;i<ps.length;i++){
//			MyTextView tv=(MyTextView) getActivity().getLayoutInflater().inflate(R.layout.content_textview, null);
//			android:gravity="center"
//			        android:textSize="22sp"
//			        android:lineSpacingMultiplier="1.2"
			MyTextView tv=new MyTextView(getActivity());
			tv.setGravity(Gravity.CENTER);
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22);
			tv.setFullText(ps[i].trim(), cs[i].trim());
			contentContainer.addView(tv);
		}
	}
	
	@Override
	public void onPause() {
		if(mediaPlayer != null && mediaPlayer.isPlaying()) {
			needToResume = true;
			pauseMP();
		}
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if(needToResume && mediaPlayer != null) {
			startMP();
		}
	}
	private void pauseMP() {
		playItem.setIcon(android.R.drawable.ic_media_play);
		mediaPlayer.pause();
	}
	private void startMP() {
		mediaPlayer.start();
		playItem.setIcon(android.R.drawable.ic_media_pause);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.activity_detail, menu);
		setCollectMenuItemIcon(menu.findItem(R.id.collect));
		playItem=menu.findItem(R.id.playItem);
		initPlayItem();
		setPinyinItem(menu.findItem(R.id.pinyin));
	}
	private void initPlayItem(){
		playItem.setIcon(android.R.drawable.ic_media_play);
		if(StringUtil.isBlank(ts.getAudio())){
			playItem.setVisible(false);
		}
	}
	private void setPinyinItem(MenuItem item){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		boolean showPinyin=prefs.getBoolean("showPinyin", true);
		item.setChecked(showPinyin);
		if(item.isChecked()){
			item.setIcon(R.drawable.pinyin_on);
		}else{
			item.setIcon(R.drawable.pinyin_off);
		}
		setTangshiShow();
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
			setCollectMenuItemIcon(item);
		}else if(item.getItemId()==R.id.playItem){
			playAudio();
		}else if(item.getItemId()==R.id.pinyin){
			boolean showPinyin=false;
			if(item.isChecked()){
				showPinyin=false;
			}else{
				showPinyin=true;
			}
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
			prefs.edit().putBoolean("showPinyin", showPinyin).commit();
			setPinyinItem(item);
		}
		return true;
	}
	private void playAudio(){
		//网络是否可用
		if(NetConnectionUtil.isNetworkAvailable(getActivity())){
			//WIFI是否可用
			if(NetConnectionUtil.isWifiAvailable(getActivity())){
				playAudioWithMediaplayer();
			}else{
				//是否设置了只在WIFI下播放音频
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
				boolean onlyUseWifi=prefs.getBoolean("onlyuse_wifi", true);
				if(onlyUseWifi){
					Toast.makeText(getActivity(), R.string.only_play_wifi_on, Toast.LENGTH_SHORT).show();
				}else{
					// TODO 在播放前提示
					playAudioWithMediaplayer();
				}
			}
		}else{
			Toast.makeText(getActivity(), R.string.netconection_not_available, Toast.LENGTH_SHORT).show();
		}
	}
	private void playAudioWithMediaplayer(){
		if(mediaPlayer==null){
			//启动TASK来初始化mediaPlayer
//			playItem.setIcon(R.drawable.progress_medium);
			playItem.setActionView(R.layout.actionbar_indeterminate_progress);
			playItem.setEnabled(false);
			new MediaPlayerTask().execute();
		}else{
			if(mediaPlayer.isPlaying()) {
				pauseMP();
			}else{
				startMP();
			}
		}
	}
	private void setCollectMenuItemIcon(MenuItem item){
		if(ts.getCollectStatus()==1){
			item.setIcon(android.R.drawable.btn_star_big_on);
		}else{
			item.setIcon(android.R.drawable.btn_star_big_off);
		}
	}

	private class MediaPlayerTask extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... params) {
			mediaPlayer = MediaPlayer.create(getActivity(),Uri.parse(ConfigUtil.ALIYUN_URL+ts.getAudio()));
			mediaPlayer.setOnCompletionListener(new OnCompletionListener(){

				@Override
				public void onCompletion(MediaPlayer arg0) {
					playItem.setIcon(android.R.drawable.ic_media_play);
				}
				
			});
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
//			playItem.setIcon(android.R.drawable.ic_media_play);
			playItem.setActionView(null);
			playItem.setEnabled(true);
			playAudioWithMediaplayer();
		}
	}
}
