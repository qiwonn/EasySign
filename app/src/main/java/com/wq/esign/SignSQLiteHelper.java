package com.wq.esign;
import android.content.Context; 
import android.database.sqlite.SQLiteDatabase; 
import android.database.sqlite.SQLiteDatabase.CursorFactory; 
import android.database.sqlite.SQLiteOpenHelper; 
import android.util.Log;
import android.content.ContentValues;
import android.database.Cursor;
import java.util.List;
import java.util.ArrayList;
//SQLite数据库
public class SignSQLiteHelper extends SQLiteOpenHelper { 

	private static final String TAG = "SignSQLite"; 
	public static final int VERSION = 1;
	//库名
	public static final String DATABASE = "sign_db";
	//表
	public static final String TABLE = "sign_table";
	public static final String TABLE_TOP = "topsign_table";
	public static final String TABLE_FILE = "file_table";
	public static final String TABLE_INSERT = "insertimg_table";
	//键
	public static final String[] KEY_SIGN = {"lid","sh1","sh2","sh3","simg","skey"};
	public static final String[] KEY_INSERT = {"lid","idx","sdir"};
	public static final String[] KEY_FILE = {"lid","stext"};
	//必须要有构造函数 
	public SignSQLiteHelper(Context context, String name, CursorFactory factory, 
					   int version) { 
		super(context, name, factory, version); 
	} 

	//当第一次创建数据库的时候，调用该方法  
	@Override
	public void onCreate(SQLiteDatabase db) { 
		String sql = "create table "+TABLE+"(lid long,sh1 varchar(40),sh2 varchar(40),sh3 varchar(40),simg varchar(33),skey varchar(33))";
		//输出创建数据库的日志信息 
		Log.i(TAG, "create Database------------->"); 
		//execSQL函数用于执行SQL语句 
		db.execSQL(sql);
		sql = "create table "+TABLE_TOP+"(lid long,sh1 varchar(40),sh2 varchar(40),sh3 varchar(40),simg varchar(33),skey varchar(33))";
		db.execSQL(sql);
		sql = "create table "+TABLE_FILE+"(lid long,stext text)";
		db.execSQL(sql);
		sql = "create table "+TABLE_INSERT+"(lid long,idx int,sdir varchar(128))";
		db.execSQL(sql);
	} 

	//当更新数据库的时候执行该方法 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { 
		//输出更新数据库的日志信息 
		Log.i(TAG, "update Database------------->"); 
	} 
	
	//插入item数据
	public void insertItem(String[] strs){
		//得到一个可写的数据库
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = putCVData(strs);
		//调用insert方法，将数据插入数据库
		db.insert(TABLE, null, cv);
		//关闭数据库
		db.close();
	}
	//填充item数据
	private ContentValues putCVData(String[] strs){
		//生成ContentValues对象 //key:列名，value:想插入的值
		ContentValues cv = new ContentValues();
		//往ContentValues对象存放数据，键-值对模式
		cv.put("lid", strs[0]);
		cv.put("sh1", strs[1]);
		cv.put("sh2", strs[2]);
		cv.put("sh3", strs[3]);
		if(strs.length>4)cv.put("simg", strs[4]);
		if(strs.length>5)cv.put("skey", strs[5]);
		return cv;
	}
	
	public List<String[]> queryItem(){
		return query(TABLE);
	}
	
	public List<String[]> queryItemTop(){
		return query(TABLE_TOP);
	} 
	
	//查询item数据
	private List<String[]> query(String table){
		List<String[]> sarlist = new ArrayList<String[]>();
		//得到一个可读的数据库
		SQLiteDatabase db = getReadableDatabase();
//参数1：表名
//参数2：要想显示的列
//参数3：where子句
//参数4：where子句对应的条件值
//参数5：分组方式
//参数6：having条件
//参数7：排序方式
		Cursor cursor = db.query(table, new String[]{"lid","sh1","sh2","sh3","simg","skey"}, null, null, null, null, null);
		if(null==cursor){db.close();return sarlist;}
		if(cursor.moveToLast())
			sarlist.add(getItemData(cursor));
		
		while(cursor.moveToPrevious())
			sarlist.add(getItemData(cursor));
		
		//关闭数据库
		db.close();
		return sarlist;
	}
	
	//获取item数据从Cursor
	private String[] getItemData(Cursor cursor){
		String[] strs = new String[6];
		/*strs[0] = String.valueOf(cursor.getLong(cursor.getColumnIndex("lid")));
		strs[1] = cursor.getString(cursor.getColumnIndex("sh1"));
		strs[2] = cursor.getString(cursor.getColumnIndex("sh2"));
		strs[3] = cursor.getString(cursor.getColumnIndex("sh3"));
		strs[4] = cursor.getString(cursor.getColumnIndex("simg"));
		strs[5] = cursor.getString(cursor.getColumnIndex("skey"));*/
		strs[0] = String.valueOf(cursor.getLong(0));
		strs[1] = cursor.getString(1);
		strs[2] = cursor.getString(2);
		strs[3] = cursor.getString(3);
		strs[4] = cursor.getString(4);
		strs[5] = cursor.getString(5);
		return strs;
	}
	
	//插入内容
	public void insertFileContent(long id,String s){
		//得到一个可写的数据库
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("lid",id);
		cv.put("stext",s);
		//调用insert方法，将数据插入数据库
		db.insert(TABLE_FILE, null, cv);
		//关闭数据库
		db.close();
	}
	
	//请求内容
	public String queryFileContent(long lid){
		String s = "";
		//得到一个可读的数据库
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_FILE, new String[]{"lid","stext"}, "lid=?", new String[]{String.valueOf(lid)}, null, null, null);
		if(cursor.moveToLast())
			s = cursor.getString(1);
		db.close();
		return s;
	}
	
	//插入插图数据
	public void insertInsertImg(List<String[]> strs){
		if(null==strs || strs.size()<=0)return;
		//得到一个可写的数据库
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		//往ContentValues对象存放数据，键-值对模式
		for(String[] s : strs){
			cv.put("lid",s[0]);
			cv.put("idx",s[1]);
			cv.put("sdir",s[2]);
			//调用insert方法，将数据插入数据库
			db.insert(TABLE_INSERT, null, cv);
		}
		//关闭数据库
		db.close();
	}
	
	//请求插图数据 参数：时间戳
	public List<String[]> queryInsertImg(long lid){
		List<String[]> sarlist = new ArrayList<String[]>();
		String[] strs;
		//得到一个可读的数据库
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_INSERT, new String[]{"lid","idx","sdir"}, "lid=?", new String[]{String.valueOf(lid)}, null, null, null);
		if(cursor.moveToLast()){
			strs = new String[3];
			strs[0] = String.valueOf(cursor.getLong(0));
			strs[1] = String.valueOf(cursor.getInt(1));
			strs[2] = cursor.getString(2);
			sarlist.add(strs);
		}
		while(cursor.moveToPrevious()){
			strs = new String[3];
			strs[0] = String.valueOf(cursor.getLong(0));
			strs[1] = String.valueOf(cursor.getInt(1));
			strs[2] = cursor.getString(2);
			sarlist.add(strs);
		}
		//关闭数据库
		db.close();
		return sarlist;
	}
	
	//更新插图
	public void updateInsertImg(long lid,List<String[]> putvalue){
		//得到一个可写的数据库
		SQLiteDatabase db = getWritableDatabase();
		//where 子句 "?"是占位符号，对应后面的"1",
		String whereClause="lid=?";
		String[] whereArgs = {String.valueOf(lid)};
		ContentValues cv;
		for(String[] v : putvalue){
			cv = new ContentValues();
			cv.put("lid",v[0]);
			cv.put("idx",v[1]);
			cv.put("sdir",v[2]);
			db.update(TABLE_INSERT, cv, whereClause, whereArgs);
		}
		//关闭数据库
		db.close();
	}
	
	public void updateItem(long lid,String[] putValue){
		//得到一个可写的数据库
		SQLiteDatabase db = getWritableDatabase();
		//where 子句 "?"是占位符号，对应后面的"1",
		String whereClause="lid=?";
		String[] whereArgs = {String.valueOf(lid)};
		ContentValues cv = new ContentValues();
		cv.put("lid", putValue[0]);
		cv.put("sh1", putValue[1]);
		cv.put("sh2", putValue[2]);
		cv.put("sh3", putValue[3]);
		cv.put("simg", putValue[4]);
		cv.put("skey", putValue[5]);
		db.update(TABLE, cv, whereClause, whereArgs);
	}
	
	//更新文件内容
	public void updateFileContent(long lid,String putValue){
		//得到一个可写的数据库
		SQLiteDatabase db = getWritableDatabase();
		//where 子句 "?"是占位符号，对应后面的"1",
		String whereClause="lid=?";
		String[] whereArgs = {String.valueOf(lid)};
		ContentValues cv = new ContentValues();
		cv.put("lid",lid);
		cv.put("stext",putValue);
		db.update(TABLE_FILE, cv, whereClause, whereArgs);
	}
	
	//删除多个
	public void deleteMany(String whereClauses,String[] whereArgs){
		//得到一个可写的数据库
		SQLiteDatabase db = getWritableDatabase();
		//String whereClauses = "lid=?";
		//调用delete方法，删除数据
		db.delete(TABLE, whereClauses, whereArgs);
		db.delete(TABLE_FILE,whereClauses, whereArgs);
		db.delete(TABLE_INSERT,whereClauses, whereArgs);
		//关闭数据库
		db.close();
	}
	
	//item数据从一个表移到另一个表
	public void translate(String deTable,String inTable,String[] strs){
		//得到一个可写的数据库
		SQLiteDatabase db = getWritableDatabase();
		String whereClauses = "lid=?";
		String[] whereArgs = {strs[0]};
		//调用delete方法，删除数据
		db.delete(deTable, whereClauses, whereArgs);
		//插入数据
		db.insert(inTable, null, putCVData(strs));
		//关闭数据库
		db.close();
	}
	
	public void clearTable(String table){
		//得到一个可写的数据库
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from "+table);
	}
	
	public void deleteTable(String table){
		//得到一个可写的数据库
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("drop table "+table);
	}
}
