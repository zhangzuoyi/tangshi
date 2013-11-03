package com.zzy.xiaoyacz;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.zzy.xiaoyacz.data.TangShi;
import com.zzy.xiaoyacz.db.MyDB;

public class ListAllFragment extends SherlockListFragment {
	List<TangShi> tangShiList;
	MyDB db;
	/*private EditText conditionEt;
	private Button searchButton;*/
	private SearchView mSearchView;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		db=new MyDB(getActivity());
//		listView=(ListView) findViewById(R.id.listView1);
//		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"JAVA","PYTHON","C"});
//		int[] imgs=new int[]{R.drawable.car_cadillac,R.drawable.car_chery,R.drawable.car_chevrolet};
		db.open();
		tangShiList=db.tangShiList();
		db.close();
//		View view=((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.search_footer, null);
//		getListView().addFooterView(view, null, true);
		setListAdapter(new MyCustomAdapter(tangShiList));
		getListView().setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				TangShi ts=tangShiList.get(position);
				//Toast.makeText(MainActivity.this, ts.getContent(), Toast.LENGTH_SHORT).show();
				Intent i=new Intent(getActivity(),DetailActivity.class);
				i.putExtra("ts", ts);
				startActivity(i);
			}
			
		});
		/*conditionEt=(EditText) getView().findViewById(R.id.searchCondition);
		searchButton=(Button) getView().findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String condition=conditionEt.getText().toString().trim();
				db.open();
				List<TangShi> list=db.findTangshi(condition);
				db.close();
				tangShiList.clear();
				tangShiList.addAll(list);
				( (BaseAdapter) getListAdapter() ).notifyDataSetChanged();
			}
		});*/
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.list_all, container, false);
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.searchview_in_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        setupSearchView(searchItem);
	}

	private void setupSearchView(MenuItem searchItem) {
		 
        if (isAlwaysExpanded()) {
            mSearchView.setIconifiedByDefault(false);
        } else {
        	//API 14
            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        }
 
        /*SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();
 
            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
            for (SearchableInfo inf : searchables) {
                if (inf.getSuggestAuthority() != null
                        && inf.getSuggestAuthority().startsWith("applications")) {
                    info = inf;
                }
            }
            mSearchView.setSearchableInfo(info);
        }*/
        mSearchView.setQueryHint(getActivity().getResources().getText(R.string.search_hint));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				db.open();
				List<TangShi> list=db.findTangshi(query);
				db.close();
				tangShiList.clear();
				tangShiList.addAll(list);
				( (BaseAdapter) getListAdapter() ).notifyDataSetChanged();
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				if("".equals(newText)){
					db.open();
					List<TangShi> list=db.findTangshi(newText);
					db.close();
					tangShiList.clear();
					tangShiList.addAll(list);
					( (BaseAdapter) getListAdapter() ).notifyDataSetChanged();
				}
				return false;
			}
		});
    }
	protected boolean isAlwaysExpanded() {
        return false;
    }

	class MyCustomAdapter extends ArrayAdapter<TangShi>{
		List<TangShi> data;
//		int[] imgs;

		public MyCustomAdapter(List<TangShi> data) {
			super(getActivity(), R.layout.list_item, data);
			this.data=data;
//			this.imgs=imgs;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if(convertView==null){
				LayoutInflater inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
	}
	private class ViewHolder{
		public ImageView imageView;
		public TextView textView1;
		public TextView textView2;
	}
	
}
