package com.zzy.xiaoyacz;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.SearchView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.zzy.xiaoyacz.data.TangShi;
import com.zzy.xiaoyacz.db.MyDB;

public class ListAllFragment extends SherlockListFragment {
	private List<TangShi> tangShiList;
	private MyDB db;
	private SearchView mSearchView;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		db=new MyDB(getActivity());
		db.open();
		tangShiList=db.tangShiList();
		db.close();
		setListAdapter(new TangshiListAdapter(getActivity(),tangShiList));
		getListView().setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
//				TangShi ts=tangShiList.get(position);
//				Intent i=new Intent(getActivity(),DetailActivity.class);
//				i.putExtra("ts", ts);
				Intent i=new Intent(getActivity(),DetailFragmentActivity.class);
				i.putExtra(DetailFragmentActivity.CURRENTINDEX, position);
				i.putParcelableArrayListExtra(DetailFragmentActivity.TANGSHIS, (ArrayList<TangShi>)tangShiList);
				startActivity(i);
			}
			
		});
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
}
