package com.zzy.xiaoyacz.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zzy.xiaoyacz.R;

public class MyDBhelper extends SQLiteOpenHelper {
	Context context;
	private static final String CREATE_TABLE = "create table "
			+ Constants.TABLE_NAME + " (" 
			+ Constants.KEY_ID + " integer primary key autoincrement, " 
			+ Constants.TITLE + " text not null, " 
			+ Constants.CONTENT + " text not null, "
			+ Constants.AUDIO + " text, "
			+ Constants.AUTHOR + " text not null, "
			+ Constants.DEGREE + " integer, "
			+ Constants.EXPLAIN + " text, "
			+ Constants.IMG + " text);";

	public MyDBhelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.context=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("MyDBhelper onCreate", "Creating all the tables");
		try {
			db.execSQL(CREATE_TABLE);
			insertTangShis(db);
		} catch (SQLiteException ex) {
			Log.v("Create table exception", ex.getMessage());
		}
	}
	private void insertTangShis(SQLiteDatabase db){
		StringBuilder sb=new StringBuilder();
		BufferedReader br=new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.tangshis)));
		try {
			String str=br.readLine();
			while(str!=null){
				sb.append(str);
				str=br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("JSON file", sb.toString());
		System.out.println(sb.toString());
		try {
			JSONArray arr=new JSONArray(sb.toString());
			for(int i=0;i<arr.length();i++){
				JSONObject obj=arr.getJSONObject(i);
				ContentValues cv = new ContentValues();
				cv.put(Constants.AUDIO, obj.getString("audio"));
				cv.put(Constants.AUTHOR, obj.getString("author"));
				cv.put(Constants.CONTENT, obj.getString("content"));
				cv.put(Constants.EXPLAIN, obj.getString("explain"));
				cv.put(Constants.IMG, obj.getString("img"));
				cv.put(Constants.DEGREE, obj.getInt("degree"));
				cv.put(Constants.TITLE, obj.getString("title"));
				db.insert(Constants.TABLE_NAME, null, cv);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("TaskDBAdapter", "Upgrading from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("drop table if exists " + Constants.TABLE_NAME);
		onCreate(db);
	}

}
