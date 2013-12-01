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
import com.zzy.xiaoyacz.data.Author;

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
			+ Constants.COLLECT + " integer, "
			+ Constants.EXPLAIN + " text, "
			+ Constants.COMMENTS + " text, "
			+ Constants.TRANSLATE + " text, "
			+ Constants.TYPE + " text, "
			+ Constants.IMG + " text);";
	private static final String CREATE_TABLE_QUESTION = "create table "
			+ Constants.TABLE_QUESTIONS + " (" 
			+ Constants.KEY_ID + " integer primary key autoincrement, " 
			+ Constants.QUESTION + " text not null, " 
			+ Constants.TYPE + " text not null, "
			+ Constants.OPTIONS + " text, "
			+ Constants.ANSWER + " text not null);";
	private static final String CREATE_TABLE_AUTHOR = "create table "
			+ Author.TABLE + " (" 
			+ Author.ID + " integer primary key autoincrement, " 
			+ Author.INITIAL + " text not null, " 
			+ Author.INTRO + " text, "
			+ Author.NAME + " text not null, "
			+ Author.STAGE + " text );";

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
			createAuthorTableAndInsertData(db);
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
				if(obj.has("audio"))
					cv.put(Constants.AUDIO, obj.getString("audio"));
				if(obj.has("author"))
					cv.put(Constants.AUTHOR, obj.getString("author"));
				if(obj.has("content"))
					cv.put(Constants.CONTENT, obj.getString("content"));
				if(obj.has("explain"))
					cv.put(Constants.EXPLAIN, obj.getString("explain"));
				if(obj.has("img"))
					cv.put(Constants.IMG, obj.getString("img"));
				if(obj.has("degree"))
					cv.put(Constants.DEGREE, obj.getInt("degree"));
				if(obj.has("title"))
					cv.put(Constants.TITLE, obj.getString("title"));
				if(obj.has("type"))
					cv.put(Constants.TYPE, obj.getString("type"));
				if(obj.has(Constants.COMMENTS))
					cv.put(Constants.COMMENTS, obj.getString(Constants.COMMENTS));
				if(obj.has(Constants.TRANSLATE))
					cv.put(Constants.TRANSLATE, obj.getString(Constants.TRANSLATE));
				cv.put(Constants.COLLECT, 0);
				db.insert(Constants.TABLE_NAME, null, cv);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
//	private void createQuestionTableAndInsertData(SQLiteDatabase db){
//		db.execSQL(CREATE_TABLE_QUESTION);
//	}
	private void createAuthorTableAndInsertData(SQLiteDatabase db){
		db.execSQL(CREATE_TABLE_AUTHOR);
		insertAuthors(db);
	}
	private void insertAuthors(SQLiteDatabase db){
		StringBuilder sb=new StringBuilder();
		BufferedReader br=new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.authors)));
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
		try {
			JSONArray arr=new JSONArray(sb.toString());
			for(int i=0;i<arr.length();i++){
				JSONObject obj=arr.getJSONObject(i);
				ContentValues cv = new ContentValues();
				if(obj.has("name"))
					cv.put(Author.NAME, obj.getString("name"));
				if(obj.has("initial"))
					cv.put(Author.INITIAL, obj.getString("initial"));
				db.insert(Author.TABLE, null, cv);
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
		db.execSQL("drop table if exists " + Author.TABLE);
		onCreate(db);
	}

}
