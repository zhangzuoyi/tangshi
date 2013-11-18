package com.zzy.xiaoyacz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.zzy.xiaoyacz.data.Author;
import com.zzy.xiaoyacz.db.MyDB;

public class ByAuthorFragment extends SherlockListFragment {
	MyDB db;
	List<Author> authors;
	List<Object> authorsAndSections;//按字母分组的列表，包含了分组的字母
	TextView sectionView;
	private int topVisiblePosition = -1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.author_list, container,false);
		sectionView=(TextView) view.findViewById(R.id.initial);
		return view;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		/*this.setListAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,authors));
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
			
		});*/
		initAuthors();
		this.setListAdapter(new AuthorAdapter2(getActivity(),authors));
		getListView().setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Object obj=authors.get(position);
				if(obj instanceof Author){
					Author author=(Author)obj;
					Intent i=new Intent(getActivity(),ListByCategory.class);
					i.putExtra("type", "author");
					i.putExtra("param", author.getName());
					startActivity(i);
				}
			}
			
		});
		getListView().setOnScrollListener(
		        new AbsListView.OnScrollListener() {
		          @Override
		          public void onScrollStateChanged(AbsListView view,
		              int scrollState) {
		            // Empty.
		          }

		          @Override
		          public void onScroll(AbsListView view, int firstVisibleItem,
		              int visibleItemCount, int totalItemCount) {
		            if (firstVisibleItem != topVisiblePosition) {
		              topVisiblePosition = firstVisibleItem;
		              setTopHeader(firstVisibleItem);
		            }
		          }
		    });
		    setTopHeader(0);
	}
	private void setTopHeader(int position){
		Author author=authors.get(position);
		sectionView.setText(author.getInitial());
	}
	private void initAuthors(){
		db=new MyDB(getActivity());
		db.open();
		authors=db.authorList();
		db.close();
		Collections.sort(authors, new Comparator<Author>(){

			@Override
			public int compare(Author lhs, Author rhs) {
				return lhs.getInitial().compareTo(rhs.getInitial());
			}
			
		});
		authorsAndSections=new ArrayList<Object>();
		String initial="";
		for(Author author:authors){
			if(!initial.equals(author.getInitial())){
				initial=author.getInitial();
				authorsAndSections.add(initial);
			}
			authorsAndSections.add(author);
		}
	}

	private class AuthorAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return authorsAndSections.size();
		}

		@Override
		public Object getItem(int position) {
			return authorsAndSections.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Object obj=authorsAndSections.get(position);
//			if(obj instanceof String){//分组信息
//				ViewHolder vh;
//				if(convertView==null){
//					LayoutInflater inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//					convertView=inflater.inflate(R.layout.author_list_section, parent,false);
//					vh=new ViewHolder();
//					vh.textView=(TextView) convertView.findViewById(R.id.name);
//					convertView.setTag(R.layout.author_list_section,vh);
//				}else{
//					vh=(ViewHolder) convertView.getTag(R.layout.author_list_section);
//				}
//				vh.textView.setText(obj.toString());
//			}else{//作者信息
//				ViewHolder vh;
//				Author author=(Author)obj;
//				if(convertView==null){
//					LayoutInflater inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//					convertView=inflater.inflate(R.layout.author_list_item, parent,false);
//					vh=new ViewHolder();
//					vh.textView=(TextView) convertView.findViewById(R.id.name);
//					convertView.setTag(R.layout.author_list_item,vh);
//				}else{
//					vh=(ViewHolder) convertView.getTag(R.layout.author_list_item);
//				}
//				vh.textView.setText(author.getName());
//			}
			View view;
			if(obj instanceof String){//分组信息
				LayoutInflater inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view=inflater.inflate(R.layout.author_list_section, parent,false);
				TextView textView=(TextView) view.findViewById(R.id.name);
				textView.setText(obj.toString());
			}else{//作者信息
				Author author=(Author)obj;
				LayoutInflater inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view=inflater.inflate(R.layout.author_list_item, parent,false);
				TextView textView=(TextView) view.findViewById(R.id.name);
				textView.setText(author.getName());
			}
			return view;
		}
		
	}
	static class ViewHolder{
		public TextView textView;
		public TextView sectionView;
	}
	private class AuthorAdapter2 extends ArrayAdapter<Author>{
		public AuthorAdapter2(Context context,List<Author> list){
			super(context,R.layout.author_list_item,R.id.name,list);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				LayoutInflater inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView=inflater.inflate(R.layout.author_list_item, parent,false);
			}
			TextView sectionView=(TextView) convertView.findViewById(R.id.initial);
			Author author=getItem(position);
			if(position==0||!getItem(position-1).getInitial().equals(author.getInitial())){//第一个列表，或者首字母发生变化
				sectionView.setVisibility(View.VISIBLE);
				sectionView.setText(author.getInitial());
			}else{
				sectionView.setVisibility(View.GONE);
			}
			return super.getView(position, convertView, parent);
		}
		
	}
}
