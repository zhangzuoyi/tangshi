package com.zzy.xiaoyacz;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.zzy.xiaoyacz.data.TangShi;
import com.zzy.xiaoyacz.db.MyDB;

public class CollectFragment extends ListFragment {
	private List<TangShi> tangShiList;
	private MyDB db;
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		db=new MyDB(getActivity());
		db.open();
		tangShiList=db.findTangshiCollected();
		db.close();
		setListAdapter(new TangshiListAdapter(getActivity(),tangShiList));
		getListView().setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
//				TangShi ts=tangShiList.get(position);
//				Intent i=new Intent(getActivity(),DetailActivity.class);
//				i.putExtra("ts", ts);
				Intent i=new Intent(getActivity(),DetailFragmentActivity.class);
				i.putExtra(DetailFragmentActivity.CURRENTINDEX, position);
				i.putParcelableArrayListExtra(DetailFragmentActivity.TANGSHIS, (ArrayList<TangShi>)tangShiList);
				i.putExtra(DetailFragmentActivity.TITLE, "收藏诗集");
				startActivity(i);
			}
			
		});
	}
}
