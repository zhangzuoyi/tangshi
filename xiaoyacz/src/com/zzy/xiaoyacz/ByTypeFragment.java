package com.zzy.xiaoyacz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;

public class ByTypeFragment extends SherlockFragment {
	private Button[] buttons;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.type_list, container, false);
		buttons=new Button[7];
		buttons[0]=(Button) view.findViewById(R.id.type1);
		buttons[1]=(Button) view.findViewById(R.id.type2);
		buttons[2]=(Button) view.findViewById(R.id.type3);
		buttons[3]=(Button) view.findViewById(R.id.type4);
		buttons[4]=(Button) view.findViewById(R.id.type5);
		buttons[5]=(Button) view.findViewById(R.id.type6);
		buttons[6]=(Button) view.findViewById(R.id.type7);
		for(Button button:buttons){
			button.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Button b=(Button)v;
					String type=b.getText().toString();
					Intent i=new Intent(getActivity(),ListByCategory.class);
					i.putExtra("type", "type");
					i.putExtra("param", type);
					startActivity(i);
				}
				
			});
		}
		return view;
	}
	
}
