package com.zzy.xiaoyacz;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.SherlockListFragment;
import com.zzy.xiaoyacz.db.MyDB;

public class ByAuthorFragment extends SherlockListFragment {
	MyDB db;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		db=new MyDB(getActivity());
		db.open();
		final List<String> authors=db.authorList();
		db.close();
		this.setListAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,authors));
		getListView().setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				String author=authors.get(position);
				Intent i=new Intent(getActivity(),ListByCategory.class);
				i.putExtra("type", "author");
				i.putExtra("param", author);
				startActivity(i);
			}
			
		});
	}

	
}
