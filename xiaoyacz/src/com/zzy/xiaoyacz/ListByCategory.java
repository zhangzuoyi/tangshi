package com.zzy.xiaoyacz;

import java.util.List;

import com.zzy.xiaoyacz.data.TangShi;
import com.zzy.xiaoyacz.db.MyDB;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class ListByCategory extends ListActivity {
	MyDB db;
	List<TangShi> tangShiList;
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
		}
		db.close();
		setListAdapter(new TangshiListAdapter(this,tangShiList));
		getListView().setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				TangShi ts=tangShiList.get(position);
				//Toast.makeText(MainActivity.this, ts.getContent(), Toast.LENGTH_SHORT).show();
				Intent i=new Intent(ListByCategory.this,DetailActivity.class);
				i.putExtra("ts", ts);
				startActivity(i);
			}
			
		});
		TextView title=(TextView) findViewById(R.id.title);
		title.setText(param);
	}

}
