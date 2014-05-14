package com.zzy.xiaoyacz.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.zzy.xiaoyacz.data.Author;
import com.zzy.xiaoyacz.data.TangShi;

public class MyDB {
	private SQLiteDatabase db;
	private final Context context;
	private final MyDBhelper dbhelper;
	private static MyDB mydb;

	public static MyDB getInstance(Context c){
		if(mydb==null){
			mydb=new MyDB(c);
		}
		return mydb;
	}
	private MyDB(Context c) {
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
	
	public void collect(long id){
		String sql="update "+Constants.TABLE_NAME+" set "+Constants.COLLECT+"= 1 where "+Constants.KEY_ID+" =?";
		db.execSQL(sql, new Object[]{id});
	}
	
	public void cancelCollect(long id){
		String sql="update "+Constants.TABLE_NAME+" set "+Constants.COLLECT+"= 0 where "+Constants.KEY_ID+" =?";
		db.execSQL(sql, new Object[]{id});
	}
	
	public List<TangShi> findTangshiCollected(){
		Cursor c = db.query(Constants.TABLE_NAME, null,Constants.COLLECT+ "=?", new String[]{"1"}, null, null,null);
		return cursorToTangshi(c);
	}
	
	public List<TangShi> findTangshiByAuthor(String author){
		Cursor c = db.query(Constants.TABLE_NAME, null,Constants.AUTHOR+ "=?", new String[]{author}, null, null,null);
		return cursorToTangshi(c);
	}
	
	public List<TangShi> findTangshiByType(String type){
		Cursor c = db.query(Constants.TABLE_NAME, null,Constants.TYPE+ "=?", new String[]{type}, null, null,null);
		return cursorToTangshi(c);
	}

	public List<TangShi> tangShiList() {
		Cursor c = db.query(Constants.TABLE_NAME, null, null, null, null, null,null);
		return cursorToTangshi(c);
	}
	
	public TangShi findTangshiById(long id){
		Cursor c = db.query(Constants.TABLE_NAME, null,Constants.KEY_ID+ "=?", new String[]{String.valueOf(id)}, null, null,null);
		List<TangShi> list=cursorToTangshi(c);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public List<TangShi> findTangshi(String condition){
		String sql="select * from "+Constants.TABLE_NAME+" where "+Constants.AUTHOR+" like '%"+condition+"%' ";
		sql=sql+"or "+Constants.TITLE+" like '%"+condition+"%' ";
		sql=sql+"or "+Constants.CONTENT+" like '%"+condition+"%' ";
		Cursor cur=db.rawQuery(sql, null);
		return cursorToTangshi(cur);
	}
	private List<TangShi> cursorToTangshi(Cursor c){
		List<TangShi> list=new ArrayList<TangShi>();
		if(c.moveToFirst()){
			do{
				TangShi ts=new TangShi();
				ts.setAudio(c.getString(c.getColumnIndex(Constants.AUDIO)));
				ts.setAuthor(c.getString(c.getColumnIndex(Constants.AUTHOR)));
				ts.setContent(c.getString(c.getColumnIndex(Constants.CONTENT)));
				ts.setDegree(c.getInt(c.getColumnIndex(Constants.DEGREE)));
				ts.setExplain(c.getString(c.getColumnIndex(Constants.EXPLAIN)));
				ts.setComments(c.getString(c.getColumnIndex(Constants.COMMENTS)));
				ts.setTranslate(c.getString(c.getColumnIndex(Constants.TRANSLATE)));
				ts.setId(c.getLong(c.getColumnIndex(Constants.KEY_ID)));
//				ts.setImg(img);
				ts.setTitle(c.getString(c.getColumnIndex(Constants.TITLE)));
				ts.setType(c.getString(c.getColumnIndex(Constants.TYPE)));
				ts.setCollectStatus(c.getInt(c.getColumnIndex(Constants.COLLECT)));
				list.add(ts);
			}while(c.moveToNext());
		}
		return list;
	}
	public List<Author> authorList() {
		Cursor c = db.query(Author.TABLE, null, null, null, null, null,Author.NAME);
		List<Author> list=new ArrayList<Author>();
		if(c.moveToFirst()){
			do{
				Author ts=new Author();
				ts.setName(c.getString(c.getColumnIndex(Author.NAME)));
				ts.setInitial(c.getString(c.getColumnIndex(Author.INITIAL)));
				ts.setIntro(c.getString(c.getColumnIndex(Author.INTRO)));
				list.add(ts);
			}while(c.moveToNext());
		}
		return list;
	}
	public Author findAuthorByName(String name){
		Cursor c = db.query(Author.TABLE, null, Author.NAME+"=?", new String[]{name}, null, null,null);
		if(c.moveToFirst()){
			Author ts=new Author();
			ts.setName(c.getString(c.getColumnIndex(Author.NAME)));
			ts.setInitial(c.getString(c.getColumnIndex(Author.INITIAL)));
			ts.setIntro(c.getString(c.getColumnIndex(Author.INTRO)));
			return ts;
		}
		return null;
	}
	
//	public List<String> authorList(){
//		Cursor cur=db.rawQuery("select distinct "+Constants.AUTHOR+" from "+Constants.TABLE_NAME, null);
//		List<String> result=new ArrayList<String>();
//		if(cur.moveToFirst()){
//			do{
//				result.add(cur.getString(0));
//			}while(cur.moveToNext());
//		}
//		return result;
//	}

	public Cursor getdiaries() {
		Cursor c = db.query(Constants.TABLE_NAME, null, null, null, null, null,
				null);
		return c;
	}

}
