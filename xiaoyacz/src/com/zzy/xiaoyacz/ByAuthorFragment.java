package com.zzy.xiaoyacz;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

import com.zzy.xiaoyacz.db.MyDB;

public class ByAuthorFragment extends ListFragment {
	MyDB db;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		db=new MyDB(getActivity());
		db.open();
		List<String> authors=db.authorList();
		db.close();
		this.setListAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,authors));
	}

	
}
