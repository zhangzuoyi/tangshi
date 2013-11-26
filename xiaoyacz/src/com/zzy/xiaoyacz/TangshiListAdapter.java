package com.zzy.xiaoyacz;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzy.xiaoyacz.data.TangShi;

public class TangshiListAdapter  extends ArrayAdapter<TangShi>{
	private List<TangShi> data;
	private Context context;

	public TangshiListAdapter(Context context,List<TangShi> data) {
		super(context, R.layout.list_item, data);
		this.context=context;
		this.data=data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if(convertView==null){
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView=inflater.inflate(R.layout.list_item, parent,false);
			vh=new ViewHolder();
			vh.imageView=(ImageView) convertView.findViewById(R.id.imageView1);
			vh.textView1=(TextView) convertView.findViewById(R.id.textView1);
			vh.textView2=(TextView) convertView.findViewById(R.id.textView2);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		TangShi ts=data.get(position);
		vh.textView1.setText(ts.getTitle());
		vh.textView2.setText(ts.getAuthor());
		vh.imageView.setImageResource(R.drawable.align_left_icon);
		return convertView;
	}
	static class ViewHolder{
		public ImageView imageView;
		public TextView textView1;
		public TextView textView2;
	}
}

