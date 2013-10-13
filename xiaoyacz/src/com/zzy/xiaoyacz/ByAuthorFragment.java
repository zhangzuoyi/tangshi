package com.zzy.xiaoyacz;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.zzy.xiaoyacz.data.TangShi;
import com.zzy.xiaoyacz.db.MyDB;

public class ByAuthorFragment extends ListFragment {
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
				Toast.makeText(getActivity(), author, Toast.LENGTH_SHORT).show();
				Intent i=new Intent(getActivity(),ListByCategory.class);
				i.putExtra("type", "author");
				i.putExtra("param", author);
				startActivity(i);
			}
			
		});
	}

	
}
