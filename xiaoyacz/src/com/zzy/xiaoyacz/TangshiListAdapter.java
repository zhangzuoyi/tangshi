package com.zzy.xiaoyacz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzy.xiaoyacz.data.TangShi;
import com.zzy.xiaoyacz.data.Type;

public class TangshiListAdapter  extends ArrayAdapter<TangShi>{
	private List<TangShi> data;
	private Context context;
	private static Map<String,String> icons;
	public static final String ICON_FORMAT="<font color=\"#1e90ff\"><b>%s</b></font><br /><font color=\"#228b22\"><b>%s</b></font>";
	static{
		icons=new HashMap<String,String>();
		icons.put(Type.WJ, String.format(ICON_FORMAT, "五","绝"));
		icons.put(Type.QJ, String.format(ICON_FORMAT, "七","绝"));
		icons.put(Type.WL, String.format(ICON_FORMAT, "五","律"));
		icons.put(Type.QL, String.format(ICON_FORMAT, "七","律"));
		icons.put(Type.WG, String.format(ICON_FORMAT, "五","古"));
		icons.put(Type.QG, String.format(ICON_FORMAT, "七","古"));
		icons.put(Type.YF, String.format(ICON_FORMAT, "乐","府"));
	}

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
//			vh.imageView=(ImageView) convertView.findViewById(R.id.imageView1);
			vh.textView1=(TextView) convertView.findViewById(R.id.textView1);
			vh.textView2=(TextView) convertView.findViewById(R.id.textView2);
			vh.iconView=(TextView) convertView.findViewById(R.id.iconTv);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		TangShi ts=data.get(position);
		vh.textView1.setText(ts.getTitle());
		vh.textView2.setText(ts.getAuthor());
//		vh.imageView.setImageResource(R.drawable.align_left_icon);
		vh.iconView.setText(Html.fromHtml(getIconText(ts.getType())));
		return convertView;
	}
	private String getIconText(String type){
		return icons.get(type);
	}
	static class ViewHolder{
//		public ImageView imageView;
		public TextView iconView;
		public TextView textView1;
		public TextView textView2;
	}
}

