package com.compoment.uploading_breakpoint_continue.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;


/**<provider
android:name="com.compoment.uploading_breakpoint_continue.db.UploadComplete_UploadPart_FileDetail_SendDetail_DBContentProvider"
android:authorities="com.compoment.uploading_breakpoint_continue.db.UploadComplete_UploadPart_FileDetail_SendDetail_DBContentProvider" />
*/


public class UploadComplete_UploadPart_FileDetail_SendDetail_DBContentProvider extends ContentProvider {

	private DatabaseHelper openHelper;

	// UriMatcher用的匹配
	private static final int MESSAGE_RECORD = 0;
	private static final int MESSAGE_RECORD_ID = 1;
	private static final int MESSAGE_RECEIVED = 2;
	private static final int MESSAGE_RECEIVED_ID = 3;
	private static final int MESSAGE_SENT = 4;
	private static final int MESSAGE_SENT_ID = 5;
	private static final int MESSAGE_UPLOADING = 6;
	private static final int MESSAGE_UPLOADING_ID = 7;
	private static final int MESSAGE_DOWNLOADING = 8;
	private static final int MESSAGE_DOWNLOADING_ID = 9;
	private static final int MESSAGE_UPLOADED = 10;
	private static final int MESSAGE_UPLOADED_ID = 11;

	public static final String FALSE = "0";
	public static final String TRUE = "1";

	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		// 留言录制
		uriMatcher.addURI(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY, UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECORD,
				MESSAGE_RECORD);
		uriMatcher.addURI(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY, UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECORD
				+ "/#", MESSAGE_RECORD_ID);
		// 留言接收
		uriMatcher.addURI(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY,
				UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECEIVED, MESSAGE_RECEIVED);
		uriMatcher.addURI(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY, UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECEIVED
				+ "/#", MESSAGE_RECEIVED_ID);

		// 留言发送
		uriMatcher.addURI(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY, UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_SENT,
				MESSAGE_SENT);
		uriMatcher.addURI(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY, UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_SENT
				+ "/#", MESSAGE_SENT_ID);

		// 留言上传
		uriMatcher.addURI(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY,
				UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_UPLOAD_PART, MESSAGE_UPLOADING);
		uriMatcher.addURI(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY,
				UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_UPLOAD_PART + "/#", MESSAGE_UPLOADING_ID);

		// 留言下载
		uriMatcher.addURI(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY,
				UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_DOWNLOADING, MESSAGE_DOWNLOADING);
		uriMatcher.addURI(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY,
				UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_DOWNLOADING + "/#", MESSAGE_DOWNLOADING_ID);

		// 留言已上传
		uriMatcher.addURI(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY,
				UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_UPLOADED, MESSAGE_UPLOADED);
		uriMatcher.addURI(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY, UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_UPLOADED
				+ "/#", MESSAGE_UPLOADED_ID);
	}

	public boolean onCreate() {
		openHelper = new DatabaseHelper(getContext());
		return false;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		SQLiteDatabase db = openHelper.getWritableDatabase();

		switch (uriMatcher.match(uri)) {
		case MESSAGE_RECORD:
			count = db.delete(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECORD, selection,
					selectionArgs);
			break;
		case MESSAGE_RECORD_ID:
			String segment1 = uri.getPathSegments().get(1);
			Log.e("", "segment1 = " + segment1);
			count = db.delete(
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECORD,
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable._ID
							+ "="
							+ segment1
							+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
									: ""), selectionArgs);
			break;

		case MESSAGE_RECEIVED:
			count = db.delete(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECEIVED, selection,
					selectionArgs);
			break;
		case MESSAGE_RECEIVED_ID:
			String segment2 = uri.getPathSegments().get(1);
			Log.e("", "segment2 = " + segment2);
			count = db.delete(
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECEIVED,
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable._ID
							+ "="
							+ segment2
							+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
									: ""), selectionArgs);
			break;

		case MESSAGE_SENT:
			count = db.delete(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_SENT, selection,
					selectionArgs);
			break;
		case MESSAGE_SENT_ID:
			String segment3 = uri.getPathSegments().get(1);
			Log.e("", "segment3 = " + segment3);
			count = db.delete(
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_SENT,
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable._ID
							+ "="
							+ segment3
							+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
									: ""), selectionArgs);
			break;

		case MESSAGE_DOWNLOADING:
			count = db.delete(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_DOWNLOADING, selection,
					selectionArgs);
			break;
		case MESSAGE_DOWNLOADING_ID:
			String segment4 = uri.getPathSegments().get(1);
			Log.e("", "segment4 = " + segment4);
			count = db.delete(
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_DOWNLOADING,
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageDownloadingTable._ID
							+ "="
							+ segment4
							+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
									: ""), selectionArgs);
			break;

		case MESSAGE_UPLOADING:
			count = db.delete(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_UPLOAD_PART, selection,
					selectionArgs);
			break;
		case MESSAGE_UPLOADING_ID:
			String segment5 = uri.getPathSegments().get(1);
			Log.e("", "segment5 = " + segment5);
			count = db.delete(
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_UPLOAD_PART,
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable._ID
							+ "="
							+ segment5
							+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
									: ""), selectionArgs);
			break;

		case MESSAGE_UPLOADED:
			count = db.delete(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_UPLOADED, selection,
					selectionArgs);
			break;
		case MESSAGE_UPLOADED_ID:
			String segment6 = uri.getPathSegments().get(1);
			Log.e("", "segment6 = " + segment6);
			count = db.delete(
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_UPLOADED,
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable._ID
							+ "="
							+ segment6
							+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
									: ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case MESSAGE_RECORD:
			return "vnd.android.cursor.dir/" + UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY + "."
					+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECORD;
		case MESSAGE_RECORD_ID:
			return "vnd.android.cursor.item/" + UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY + "."
					+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECORD;

		case MESSAGE_RECEIVED:
			return "vnd.android.cursor.dir/" + UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY + "."
					+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECEIVED;
		case MESSAGE_RECEIVED_ID:
			return "vnd.android.cursor.item/" + UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY + "."
					+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECEIVED;

		case MESSAGE_SENT:
			return "vnd.android.cursor.dir/" + UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY + "."
					+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_SENT;
		case MESSAGE_SENT_ID:
			return "vnd.android.cursor.item/" + UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY + "."
					+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_SENT;

		case MESSAGE_DOWNLOADING:
			return "vnd.android.cursor.dir/" + UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY + "."
					+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_DOWNLOADING;
		case MESSAGE_DOWNLOADING_ID:
			return "vnd.android.cursor.item/" + UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY + "."
					+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_DOWNLOADING;

		case MESSAGE_UPLOADING:
			return "vnd.android.cursor.dir/" + UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY + "."
					+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_UPLOAD_PART;
		case MESSAGE_UPLOADING_ID:
			return "vnd.android.cursor.item/" + UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY + "."
					+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_UPLOAD_PART;

		case MESSAGE_UPLOADED:
			return "vnd.android.cursor.dir/" + UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY + "."
					+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_UPLOADED;
		case MESSAGE_UPLOADED_ID:
			return "vnd.android.cursor.item/" + UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.AUTHORITY + "."
					+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_UPLOADED;
		default:
			throw new IllegalArgumentException("Unsupported URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		switch (uriMatcher.match(uri)) {
		case MESSAGE_DOWNLOADING:
		case MESSAGE_DOWNLOADING_ID:
			// Insert the new row, will return the row number if successful
			long _id1 = db
					.insert(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_DOWNLOADING, null, values);
			if (_id1 > 0) {
				Uri newUri = ContentUris.withAppendedId(
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageDownloadingTable.CONTENT_URI, _id1);
				getContext().getContentResolver().notifyChange(newUri, null);
				return newUri;
			}
			break;

		case MESSAGE_RECEIVED:
		case MESSAGE_RECEIVED_ID:
			// Insert the new row, will return the row number if successful
			long _id2 = db.insert(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECEIVED, null, values);
			if (_id2 > 0) {
				Uri newUri = ContentUris.withAppendedId(
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.CONTENT_URI, _id2);
				getContext().getContentResolver().notifyChange(newUri, null);
				return newUri;
			}
			break;

		case MESSAGE_RECORD:
		case MESSAGE_RECORD_ID:
			// Insert the new row, will return the row number if successful
			long _id3 = db.insert(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECORD, null, values);
			if (_id3 > 0) {
				Uri newUri = ContentUris.withAppendedId(
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.CONTENT_URI, _id3);
				getContext().getContentResolver().notifyChange(newUri, null);
				return newUri;
			}
			break;

		case MESSAGE_SENT:
		case MESSAGE_SENT_ID:
			// Insert the new row, will return the row number if successful
			long _id4 = db.insert(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_SENT, null, values);
			if (_id4 > 0) {
				Uri newUri = ContentUris.withAppendedId(
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.CONTENT_URI, _id4);
				getContext().getContentResolver().notifyChange(newUri, null);
				return newUri;
			}
			break;

		case MESSAGE_UPLOADING:
		case MESSAGE_UPLOADING_ID:
			// Insert the new row, will return the row number if successful
			long _id5 = db.insert(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_UPLOAD_PART, null, values);
			if (_id5 > 0) {
				Uri newUri = ContentUris.withAppendedId(
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.CONTENT_URI, _id5);
				getContext().getContentResolver().notifyChange(newUri, null);
				return newUri;
			}
			break;

		case MESSAGE_UPLOADED:
		case MESSAGE_UPLOADED_ID:
			// Insert the new row, will return the row number if successful
			long _id6 = db.insert(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_UPLOADED, null, values);
			if (_id6 > 0) {
				Uri newUri = ContentUris.withAppendedId(
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.CONTENT_URI, _id6);
				getContext().getContentResolver().notifyChange(newUri, null);
				return newUri;
			}
			break;

		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		// If no sort order is specified sort by contact id
		String orderBy;

		// Generate the body of the query
		int match = uriMatcher.match(uri);
		switch (match) {
		case MESSAGE_DOWNLOADING:
			qb.setTables(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_DOWNLOADING);

			orderBy = UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageDownloadingTable._ID;
			break;
		case MESSAGE_DOWNLOADING_ID:
			qb.setTables(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_DOWNLOADING);
			qb.appendWhere(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageDownloadingTable._ID + "="
					+ uri.getPathSegments().get(1));

			orderBy = UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageDownloadingTable._ID;
			break;

		case MESSAGE_RECEIVED:
			qb.setTables(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECEIVED);

			orderBy = UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable._ID;
			break;
		case MESSAGE_RECEIVED_ID:
			qb.setTables(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECEIVED);
			qb.appendWhere(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable._ID + "="
					+ uri.getPathSegments().get(1));

			orderBy = UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable._ID;
			break;

		case MESSAGE_RECORD:
			qb.setTables(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECORD);

			orderBy = UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable._ID;
			break;
		case MESSAGE_RECORD_ID:
			qb.setTables(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECORD);
			qb.appendWhere(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable._ID + "="
					+ uri.getPathSegments().get(1));

			orderBy = UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable._ID;
			break;

		case MESSAGE_SENT:
			qb.setTables(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_SENT);

			orderBy = UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable._ID;
			break;
		case MESSAGE_SENT_ID:
			qb.setTables(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_SENT);
			qb.appendWhere(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable._ID + "="
					+ uri.getPathSegments().get(1));

			orderBy = UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable._ID;
			break;

		case MESSAGE_UPLOADING:
			qb.setTables(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_UPLOAD_PART);

			orderBy = UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable._ID;
			break;
		case MESSAGE_UPLOADING_ID:
			qb.setTables(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_UPLOAD_PART);
			qb.appendWhere(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable._ID + "="
					+ uri.getPathSegments().get(1));

			orderBy = UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable._ID;
			break;

		case MESSAGE_UPLOADED:
			qb.setTables(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_UPLOADED);

			orderBy = UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable._ID;
			break;
		case MESSAGE_UPLOADED_ID:
			qb.setTables(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_UPLOADED);
			qb.appendWhere(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable._ID + "="
					+ uri.getPathSegments().get(1));

			orderBy = UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable._ID;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		if (!TextUtils.isEmpty(sortOrder)) {
			orderBy = sortOrder;
		}

		// Apply the query to the underlying database.
		SQLiteDatabase db = openHelper.getWritableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null,
				orderBy);
		// Register the contexts ContentResolver to be notified if
		// the cursor result set changes.
		if (c != null) {
			c.setNotificationUri(getContext().getContentResolver(), uri);
		}
		// Return a cursor to the query result
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;
		SQLiteDatabase db = openHelper.getWritableDatabase();

		int match = uriMatcher.match(uri);
		switch (match) {
		case MESSAGE_DOWNLOADING:
			count = db.update(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_DOWNLOADING, values,
					selection, selectionArgs);
			break;
		case MESSAGE_DOWNLOADING_ID:
			String segment1 = uri.getPathSegments().get(1);
			count = db.update(
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_DOWNLOADING,
					values,
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageDownloadingTable._ID
							+ "="
							+ segment1
							+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
									: ""), selectionArgs);
			break;

		case MESSAGE_RECEIVED:
			count = db.update(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECEIVED, values, selection,
					selectionArgs);
			break;
		case MESSAGE_RECEIVED_ID:
			String segment2 = uri.getPathSegments().get(1);
			count = db.update(
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECEIVED,
					values,
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable._ID
							+ "="
							+ segment2
							+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
									: ""), selectionArgs);
			break;

		case MESSAGE_RECORD:
			count = db.update(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECORD, values, selection,
					selectionArgs);
			break;
		case MESSAGE_RECORD_ID:
			String segment3 = uri.getPathSegments().get(1);
			count = db.update(
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECORD,
					values,
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.ID
							+ "="
							+ segment3
							+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
									: ""), selectionArgs);
			break;

		case MESSAGE_SENT:
			count = db.update(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_SENT, values, selection,
					selectionArgs);
			break;
		case MESSAGE_SENT_ID:
			String segment4 = uri.getPathSegments().get(1);
			count = db.update(
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_SENT,
					values,
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable._ID
							+ "="
							+ segment4
							+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
									: ""), selectionArgs);
			break;

		case MESSAGE_UPLOADING:
			count = db.update(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_UPLOAD_PART, values, selection,
					selectionArgs);
			break;
		case MESSAGE_UPLOADING_ID:
			String segment5 = uri.getPathSegments().get(1);
			count = db.update(
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_UPLOAD_PART,
					values,
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable._ID
							+ "="
							+ segment5
							+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
									: ""), selectionArgs);
			break;

		case MESSAGE_UPLOADED:
			count = db.update(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_UPLOADED, values, selection,
					selectionArgs);
			break;
		case MESSAGE_UPLOADED_ID:
			String segment6 = uri.getPathSegments().get(1);
			count = db.update(
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_UPLOADED,
					values,
					UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable._ID
							+ "="
							+ segment6
							+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
									: ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		private static final String TAG = DatabaseHelper.class.getCanonicalName();
		private static final String DATABASE_NAME = "Message";
		private static final int DATABASE_VERSION = 1;

		// 创建表的sql语句
		private static final String SQL_CREATE_TABLE_MESSAGE_DOWNLOADING = "CREATE TABLE "
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_DOWNLOADING
				+ " ("
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageDownloadingTable._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageDownloadingTable.ID
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageDownloadingTable.PATH
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageDownloadingTable.RECEIVER_NUMBER
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageDownloadingTable.URL + " TEXT)";

		private static final String SQL_CREATE_TABLE_MESSAGE_UPLOADING = "CREATE TABLE "
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_UPLOAD_PART
				+ " ("
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.FILE_ID
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.BLOCK
				+ " INTERGER,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.SNDER_NUMBER
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.RCVER_NUMBER
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.DURATION
				+ " INTEGER,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.TYPE
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.PATH
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.SENT_ID + " TEXT)";

		private static final String SQL_CREATE_TABLE_MESSAGE_RECORD = "CREATE TABLE "
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECORD
				+ " ("
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.ID
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.PATH
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.RECORDER_TIME
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.TYPE
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.FILE_LENGTH
				+ " INTEGER,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.DURATION
				+ " INTEGER,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.PREVIEW_IMAGE_PATH + " TEXT)";

		private static final String SQL_CREATE_TABLE_MESSAGE_SENT = "CREATE TABLE "
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_SENT + " (" + UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.MESSAGE_SENT_ID + " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.MESSAGE_RECORD_ID + " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.TIME + " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.RECEIVER_NUMBER + " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.SENDER_NUMBER + " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.CODE + " TEXT)";

		private static final String SQL_CREATE_TABLE_MESSAGE_RECEIVED = "CREATE TABLE "
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_RECEIVED
				+ " ("
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.ID
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.TYPE
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.PATH
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.URL
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.PREVIEW_IMAGE_PATH
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.PREVIEW_IMAGE_URL
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.SENDER_NUMBER
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.RECEIVER_NUMBER
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.SENT_TIME
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.IS_READED
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.IS_DOWNLOAD_FINISHED
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.FILE_LENGTH
				+ " INTEGER,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.DURATION + " INTERGER)";

		// 创建表的sql语句
		private static final String SQL_CREATE_TABLE_MESSAGE_UPLOADED = "CREATE TABLE "
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.TABLE_MESSAGE_UPLOADED
				+ " ("
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable.MESSAGE_RECORD_ID
				+ " TEXT,"
				+ UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable.MESSAGE_SENT_ID + " TEXT)";

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.e(TAG, SQL_CREATE_TABLE_MESSAGE_DOWNLOADING);
			Log.e(TAG, SQL_CREATE_TABLE_MESSAGE_UPLOADING);
			Log.e(TAG, SQL_CREATE_TABLE_MESSAGE_RECORD);
			Log.e(TAG, SQL_CREATE_TABLE_MESSAGE_SENT);
			Log.e(TAG, SQL_CREATE_TABLE_MESSAGE_RECEIVED);
			Log.e(TAG, SQL_CREATE_TABLE_MESSAGE_UPLOADED);

			db.execSQL(SQL_CREATE_TABLE_MESSAGE_DOWNLOADING);
			db.execSQL(SQL_CREATE_TABLE_MESSAGE_UPLOADING);
			db.execSQL(SQL_CREATE_TABLE_MESSAGE_RECORD);
			db.execSQL(SQL_CREATE_TABLE_MESSAGE_SENT);
			db.execSQL(SQL_CREATE_TABLE_MESSAGE_RECEIVED);
			db.execSQL(SQL_CREATE_TABLE_MESSAGE_UPLOADED);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

}
