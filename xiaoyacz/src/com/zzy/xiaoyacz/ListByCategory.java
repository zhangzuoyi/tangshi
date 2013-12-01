package com.zzy.xiaoyacz;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.zzy.xiaoyacz.data.TangShi;
import com.zzy.xiaoyacz.data.Type;
import com.zzy.xiaoyacz.db.MyDB;

public class ListByCategory extends SherlockListActivity {
	private MyDB db;
	private List<TangShi> tangShiList;
	private String explain;//分类或者诗人的介绍
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_by_category);
		String type=getIntent().getStringExtra("type");
		String param=getIntent().getStringExtra("param");
		db=new MyDB(this);
		db.open();
		if(type.equals("author")){
			tangShiList=db.findTangshiByAuthor(param);
		}else if(type.equals("type")){
			tangShiList=db.findTangshiByType(param);
			explain=Type.getTypeExplain(param);
		}
		db.close();
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
				TangShi ts=tangShiList.get(position);
				Intent i=new Intent(ListByCategory.this,DetailActivity.class);
				i.putExtra("ts", ts);
				startActivity(i);
			}
			
		});
		TextView title=(TextView) findViewById(R.id.title);
		title.setText(param);//设置分类标题
	}

}
