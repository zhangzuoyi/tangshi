package com.zzy.xiaoyacz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zzy.xiaoyacz.data.DataUtil;
import com.zzy.xiaoyacz.data.TangShi;
import com.zzy.xiaoyacz.db.MyDB;

public class MainActivity extends Activity {
	private ListView listView;
	List<TangShi> tangShiList;
	MyDB db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		db=new MyDB(this);
		listView=(ListView) findViewById(R.id.listView1);
//		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"JAVA","PYTHON","C"});
		int[] imgs=new int[]{R.drawable.car_cadillac,R.drawable.car_chery,R.drawable.car_chevrolet};
		db.open();
		tangShiList=db.tangShiList();
		db.close();
		listView.setAdapter(new MyCustomAdapter(tangShiList,imgs));
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				TangShi ts=tangShiList.get(position);
				//Toast.makeText(MainActivity.this, ts.getContent(), Toast.LENGTH_SHORT).show();
				Intent i=new Intent(MainActivity.this,DetailActivity.class);
				i.putExtra("ts", ts);
				startActivity(i);
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.menu_settings:
			Intent i=new Intent(this,SettingsActivity.class);
			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}

	private List<TangShi> getTangShiFromJson(){
		List<TangShi> list=new ArrayList<TangShi>();
		StringBuilder sb=new StringBuilder();
		BufferedReader br=new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.tangshis)));
		try {
			String str=br.readLine();
			while(str!=null){
				sb.append(str);
				str=br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("JSON file", sb.toString());
		System.out.println(sb.toString());
		try {
			//JSONObject obj=new JSONObject(sb.toString());
			JSONArray arr=new JSONArray(sb.toString());
			for(int i=0;i<arr.length();i++){
				JSONObject obj=arr.getJSONObject(i);
				TangShi ts=new TangShi();
//				ts.setAudio(R.raw.cx);
				ts.setAuthor(obj.getString("author"));
				ts.setContent(obj.getString("content"));
				ts.setDegree(obj.getInt("degree"));
				ts.setExplain(obj.getString("explain"));
				ts.setTitle(obj.getString("title"));
				list.add(ts);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return list;
	}

	class MyCustomAdapter extends ArrayAdapter<TangShi>{
		List<TangShi> data;
		int[] imgs;

		public MyCustomAdapter(List<TangShi> data,int[] imgs) {
			super(MainActivity.this, R.layout.list_item, data);
			this.data=data;
			this.imgs=imgs;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if(convertView==null){
				LayoutInflater inflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView=inflater.inflate(R.layout.list_item, parent,false);
				vh=new ViewHolder();
				vh.imageView=(ImageView) convertView.findViewById(R.id.imageView1);
				vh.textView1=(TextView) convertView.findViewById(R.id.textView1);
				vh.textView2=(TextView) convertView.findViewById(R.id.textView2);
				convertView.setTag(vh);
			}else{
				vh=(ViewHolder) convertView.getTag();
			}
			TangShi ts=data.get(position);
			vh.textView1.setText(ts.getTitle());
			vh.textView2.setText(ts.getAuthor());
			vh.imageView.setImageResource(imgs[0]);
			return convertView;
		}
	}
	private class ViewHolder{
		public ImageView imageView;
		public TextView textView1;
		public TextView textView2;
	}
}
