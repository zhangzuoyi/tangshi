package com.zzy.xiaoyacz;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzy.xiaoyacz.data.TangShi;
import com.zzy.xiaoyacz.db.MyDB;

public class ListAllFragment extends ListFragment {
	List<TangShi> tangShiList;
	MyDB db;
	private EditText conditionEt;
	private Button searchButton;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		db=new MyDB(getActivity());
//		listView=(ListView) findViewById(R.id.listView1);
//		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"JAVA","PYTHON","C"});
//		int[] imgs=new int[]{R.drawable.car_cadillac,R.drawable.car_chery,R.drawable.car_chevrolet};
		db.open();
		tangShiList=db.tangShiList();
		db.close();
//		View view=((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.search_footer, null);
//		getListView().addFooterView(view, null, true);
		setListAdapter(new MyCustomAdapter(tangShiList));
		getListView().setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				TangShi ts=tangShiList.get(position);
				//Toast.makeText(MainActivity.this, ts.getContent(), Toast.LENGTH_SHORT).show();
				Intent i=new Intent(getActivity(),DetailActivity.class);
				i.putExtra("ts", ts);
				startActivity(i);
			}
			
		});
		conditionEt=(EditText) getView().findViewById(R.id.searchCondition);
		searchButton=(Button) getView().findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String condition=conditionEt.getText().toString().trim();
				db.open();
				List<TangShi> list=db.findTangshi(condition);
				db.close();
				tangShiList.clear();
				tangShiList.addAll(list);
				( (BaseAdapter) getListAdapter() ).notifyDataSetChanged();
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_all, container, false);
	}


	class MyCustomAdapter extends ArrayAdapter<TangShi>{
		List<TangShi> data;
//		int[] imgs;

		public MyCustomAdapter(List<TangShi> data) {
			super(getActivity(), R.layout.list_item, data);
			this.data=data;
//			this.imgs=imgs;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if(convertView==null){
				LayoutInflater inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			vh.imageView.setImageResource(R.drawable.align_left_icon);
			return convertView;
		}
	}
	private class ViewHolder{
		public ImageView imageView;
		public TextView textView1;
		public TextView textView2;
	}
	
}
