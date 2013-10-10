package com.zzy.xiaoyacz;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TabFragment extends Fragment {
	private static final int LIST_STATE = 0x1;
    private static final int TYPE_STATE = 0x2;
    private static final int AUTHOR_STATE = 0x3;
    
    private int mTabState;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);
        
        // Grab the tab buttons from the layout and attach event handlers. The code just uses standard
        // buttons for the tab widgets. These are bad tab widgets, design something better, this is just
        // to keep the code simple.
        final TextView listButton = (TextView) view.findViewById(R.id.listButton);
        final TextView typeButton = (TextView) view.findViewById(R.id.byTypeButton);
        final TextView authorButton = (TextView) view.findViewById(R.id.byAuthorButton);
//        Button listButton = (Button) view.findViewById(R.id.listButton);
//        Button typeButton = (Button) view.findViewById(R.id.byTypeButton);
//        Button authorButton = (Button) view.findViewById(R.id.byAuthorButton);

        listButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoListView();
            }
        });
//        listButton.setOnTouchListener(new OnTouchListener(){
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				gotoListView();
//				return false;
//			}
//        	
//        });
        
        typeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoTypeView();
            }
        });
        
        authorButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAuthorView();
            }
        });
        
        return view;
    }
    
    public void gotoListView() {
        // mTabState keeps track of which tab is currently displaying its contents.
        // Perform a check to make sure the list tab content isn't already displaying.
        
        if (mTabState != LIST_STATE) {
            // Update the mTabState 
            mTabState = LIST_STATE;
            
            // Fragments have access to their parent Activity's FragmentManager. You can
            // obtain the FragmentManager like this.
            FragmentManager fm = getFragmentManager();
            
            if (fm != null) {
                // Perform the FragmentTransaction to load in the list tab content.
                // Using FragmentTransaction#replace will destroy any Fragments
                // currently inside R.id.fragment_content and add the new Fragment
                // in its place.
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_content, new ListAllFragment());
                ft.commit();
            }
        }
    }
    
    public void gotoTypeView() {
        // See gotoListView(). This method does the same thing except it loads
        // the grid tab.
        
        if (mTabState !=TYPE_STATE) {
            mTabState = TYPE_STATE;
            
            FragmentManager fm = getFragmentManager();
            
            if (fm != null) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_content, new ByTypeFragment());
                ft.commit();
            }
        }
    }
    public void gotoAuthorView() {
        // See gotoListView(). This method does the same thing except it loads
        // the grid tab.
        
        if (mTabState !=AUTHOR_STATE) {
            mTabState = AUTHOR_STATE;
            
            FragmentManager fm = getFragmentManager();
            
            if (fm != null) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragment_content, new ByAuthorFragment());
                ft.commit();
            }
        }
    }
}
