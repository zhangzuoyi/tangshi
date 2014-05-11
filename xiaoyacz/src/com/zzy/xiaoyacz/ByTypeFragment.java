package com.zzy.xiaoyacz;

import com.zzy.xiaoyacz.data.Type;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ByTypeFragment extends Fragment {
	private Button[] buttons;
	private String[] types={Type.WJ,Type.QJ, Type.WL, Type.QL, Type.WG, Type.QG, Type.YF};

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
		for(int i=0;i<buttons.length;i++){
			Button button=buttons[i];
			final String type=types[i];
			button.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent i=new Intent(getActivity(),ListByCategory.class);
					i.putExtra("type", ListByCategory.TYPE_TYPE);
					i.putExtra("param", type);
					startActivity(i);
				}
				
			});
		}
		return view;
	}
	
}
