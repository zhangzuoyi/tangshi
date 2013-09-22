package com.zzy.xiaoyacz;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

public class ByAuthorFragment extends ListFragment {

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.setListAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,
				new String[]{"李白","杜甫","白居易"}));
	}

	
}
