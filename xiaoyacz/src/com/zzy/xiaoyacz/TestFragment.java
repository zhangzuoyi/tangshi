package com.zzy.xiaoyacz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TestFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.test, container, false);
		TextView titleView=(TextView) view.findViewById(R.id.title);
		titleView.setText("title");
		LinearLayout contentLayout=(LinearLayout) view.findViewById(R.id.content_layout);
		Button opeButton=(Button) view.findViewById(R.id.operation_button);
		opeButton.setText("Next");
		return view;
	}
	
}
