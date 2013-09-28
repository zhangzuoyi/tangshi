package com.zzy.xiaoyacz.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.zzy.xiaoyacz.R;
import com.zzy.xiaoyacz.data.TangShi;

public class MyDB {
	private SQLiteDatabase db;
	private final Context context;
	private final MyDBhelper dbhelper;

	public MyDB(Context c) {
		context = c;
		dbhelper = new MyDBhelper(context, Constants.DATABASE_NAME, null,
				Constants.DATABASE_VERSION);
	}

	public void close() {
		db.close();
	}

	public void open() throws SQLiteException {
		try {
			db = dbhelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			Log.v("Open database exception caught", ex.getMessage());
			db = dbhelper.getReadableDatabase();
		}
	}

	public List<TangShi> tangShiList() {
		List<TangShi> list=new ArrayList<TangShi>();
		Cursor c = db.query(Constants.TABLE_NAME, null, null, null, null, null,null);
		if(c.moveToFirst()){
			do{
				TangShi ts=new TangShi();
				ts.setAudio(c.getString(c.getColumnIndex(Constants.AUDIO)));
				ts.setAuthor(c.getString(c.getColumnIndex(Constants.AUTHOR)));
				ts.setContent(c.getString(c.getColumnIndex(Constants.CONTENT)));
				ts.setDegree(c.getInt(c.getColumnIndex(Constants.DEGREE)));
				ts.setExplain(c.getString(c.getColumnIndex(Constants.EXPLAIN)));
				ts.setId(c.getLong(c.getColumnIndex(Constants.KEY_ID)));
//				ts.setImg(img);
				ts.setTitle(c.getString(c.getColumnIndex(Constants.TITLE)));
				list.add(ts);
			}while(c.moveToNext());
		}
		return list;
	}
	
	public List<String> authorList(){
		Cursor cur=db.rawQuery("select distinct "+Constants.AUTHOR+" from "+Constants.TABLE_NAME, null);
		List<String> result=new ArrayList<String>();
		if(cur.moveToFirst()){
			do{
				result.add(cur.getString(0));
			}while(cur.moveToNext());
		}
		return result;
	}

	public Cursor getdiaries() {
		Cursor c = db.query(Constants.TABLE_NAME, null, null, null, null, null,
				null);
		return c;
	}

}
