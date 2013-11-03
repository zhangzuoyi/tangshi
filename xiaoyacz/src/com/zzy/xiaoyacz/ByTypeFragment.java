package com.zzy.xiaoyacz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

public class ByTypeFragment extends SherlockFragment {
	private ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.type_list, container, false);
		listView=(ListView) view.findViewById(R.id.typeList);
		final String[] types=new String[]{"五言绝句","七言绝句","五言律诗","七言律诗","五言古诗","七言古诗","乐府"};
		listView.setAdapter(new ArrayAdapter<String>(this.getActivity(), 
				android.R.layout.simple_list_item_1, 
				types));
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				String type=types[position];
				Intent i=new Intent(getActivity(),ListByCategory.class);
				i.putExtra("type", "type");
				i.putExtra("param", type);
				startActivity(i);
			}
			
		});
		return view;
	}
	
}
