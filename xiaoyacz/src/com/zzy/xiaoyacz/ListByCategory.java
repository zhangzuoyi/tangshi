package com.zzy.xiaoyacz;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.zzy.xiaoyacz.data.Author;
import com.zzy.xiaoyacz.data.TangShi;
import com.zzy.xiaoyacz.data.Type;
import com.zzy.xiaoyacz.db.MyDB;

public class ListByCategory extends SherlockListActivity {
	private MyDB db;
	private List<TangShi> tangShiList;
	private long[] tangshiIds;
	private String explain;//分类或者诗人的介绍
	private String type;
	private String param;
	public static final String TYPE_AUTHOR="author";
	public static final String TYPE_TYPE="type";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_by_category);
		type=getIntent().getStringExtra("type");
		param=getIntent().getStringExtra("param");
		db=new MyDB(this);
		db.open();
		if(type.equals(TYPE_AUTHOR)){
			tangShiList=db.findTangshiByAuthor(param);
			Author author=db.findAuthorByName(param);
			if(author!=null){
				explain=author.getIntro();
			}
		}else if(type.equals(TYPE_TYPE)){
			tangShiList=db.findTangshiByType(param);
			explain=Type.getTypeExplain(param);
		}
		db.close();
		tangshiIds=new long[tangShiList.size()];
		int i=0;
		for(TangShi ts:tangShiList){
			tangshiIds[i]=ts.getId();
			i++;
		}
		if(explain!=null){//set header要放在set adapter前面
			LayoutInflater inflator = LayoutInflater.from(this);
		    View header = inflator.inflate(R.layout.by_catetory_header, getListView(), false);
			TextView explainTv=(TextView) header.findViewById(R.id.explain);
			explainTv.setText("\u3000\u3000"+explain);//缩进
			getListView().addHeaderView(header, null, false);
		}
		setListAdapter(new TangshiListAdapter(this,tangShiList));
		getListView().setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				/*TangShi ts=tangShiList.get(position);
				Intent i=new Intent(ListByCategory.this,DetailActivity.class);
				i.putExtra("ts", ts);*/
				String title=null;
				Intent i=new Intent(ListByCategory.this,DetailFragmentActivity.class);
				i.putExtra(DetailFragmentActivity.CURRENTINDEX, position);
//				i.putParcelableArrayListExtra(DetailFragmentActivity.TANGSHIS, (ArrayList<TangShi>)tangShiList);
				i.putExtra(DetailFragmentActivity.TANGSHIIDS, tangshiIds);
				if(type.equals(TYPE_AUTHOR)){
					title=param+"诗集";
				}else{
					title=param;
				}
				i.putExtra(DetailFragmentActivity.TITLE,title);
				startActivity(i);
			}
			
		});
		TextView title=(TextView) findViewById(R.id.title);
		title.setText(param);//设置分类标题
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

}
