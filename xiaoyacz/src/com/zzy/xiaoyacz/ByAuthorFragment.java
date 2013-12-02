package com.zzy.xiaoyacz;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.zzy.xiaoyacz.data.Author;
import com.zzy.xiaoyacz.db.MyDB;

public class ByAuthorFragment extends SherlockListFragment {
	MyDB db;
	List<Author> authors;
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
		initAuthors();
		this.setListAdapter(new AuthorAdapter(getActivity(),authors));
		getListView().setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Author author=authors.get(position);
				Intent i=new Intent(getActivity(),ListByCategory.class);
				i.putExtra("type", ListByCategory.TYPE_AUTHOR);
				i.putExtra("param", author.getName());
				startActivity(i);
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
	}
	private class AuthorAdapter extends ArrayAdapter<Author>{
		public AuthorAdapter(Context context,List<Author> list){
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
