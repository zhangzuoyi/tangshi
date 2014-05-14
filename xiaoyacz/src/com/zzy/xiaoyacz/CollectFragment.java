package com.zzy.xiaoyacz;

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
	private long[] tangshiIds;
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
	}
	private void init(){
		MyDB db=MyDB.getInstance(getActivity());
		db.open();
		tangShiList=db.findTangshiCollected();
		db.close();
		tangshiIds=new long[tangShiList.size()];
		int i=0;
		for(TangShi ts:tangShiList){
			tangshiIds[i]=ts.getId();
			i++;
		}
		setListAdapter(new TangshiListAdapter(getActivity(),tangShiList));
		getListView().setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent i=new Intent(getActivity(),DetailFragmentActivity.class);
				i.putExtra(DetailFragmentActivity.CURRENTINDEX, position);
				i.putExtra(DetailFragmentActivity.TANGSHIIDS, tangshiIds);
				i.putExtra(DetailFragmentActivity.TITLE, "收藏诗集");
				startActivity(i);
			}
			
		});
	}
	@Override
	public void onResume() {
		super.onResume();
		init();
	}
	
}
