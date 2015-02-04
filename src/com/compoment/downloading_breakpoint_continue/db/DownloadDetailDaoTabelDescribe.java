package com.compoment.downloading_breakpoint_continue.db;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public final class DownloadDetailDaoTabelDescribe {
	public static final String AUTHORITY = "com.compoment.download.download_detail_provider";
	public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

	public static final String TABLE_DOWNLOADING = "download_detail";

	private SQLiteDatabase mSQLiteDatabase = null;
	private DatabaseHelper mDatabaseHelper = null;
	Context context;


	public static final class DownloadDetail implements BaseColumns,
	          DownloadDetailColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI,
				DownloadDetailDaoTabelDescribe.TABLE_DOWNLOADING);
	}



	protected interface DownloadDetailColumns {
		public static final String ID = "download_detail_id"; 
		public static final String PATH = "path";
		public static final String URL = "url";
		public static final String START_POS="start_pos";
		public static final String END_POS="end_pos";
		public static final String COMPLETE_SIZE="compelete_size";
		public static final String TIME="time";
	}

	

	private static class DatabaseHelper extends SQLiteOpenHelper {
		// 创建表的sql语句
		private static final String SQL_CREATE_TABLE_MESSAGE_DOWNLOADING_LIST = "CREATE TABLE "
				+ DownloadDetailDaoTabelDescribe.TABLE_DOWNLOADING
				+ " ("
				+DownloadDetail.ID+ " TEXT PRIMARY KEY," 
				+DownloadDetail.START_POS+" TEXT,"
				+DownloadDetail.END_POS + " TEXT,"  
				+DownloadDetail.COMPLETE_SIZE + " TEXT," 
				+DownloadDetail.TIME + " TEXT," 
				+DownloadDetail.PATH + " TEXT," 
				+DownloadDetail.URL + " TEXT" 
				+")";

		// 构造函数-创建一个数据库
		DatabaseHelper(Context context) {
			super(context, TABLE_DOWNLOADING, null, 1);
		}

		public void onCreate(SQLiteDatabase db) {
		
			db.execSQL(SQL_CREATE_TABLE_MESSAGE_DOWNLOADING_LIST);
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	public DownloadDetailDaoTabelDescribe(Context context) {
		this.context = context;
	}

	/** 打开数据库 */
	public void open() throws SQLException {
		mDatabaseHelper = new DatabaseHelper(context);
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
	}

	/** 关闭数据库 */
	public void close() {
		mSQLiteDatabase.close();
		mDatabaseHelper.close();
	}

	
	

}