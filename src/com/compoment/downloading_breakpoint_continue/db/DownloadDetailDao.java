package com.compoment.downloading_breakpoint_continue.db;

import java.util.ArrayList;
import java.util.List;

import com.compoment.video_record_play.util.CalendarDealWith;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;



public class DownloadDetailDao {
	private ContentResolver resolver;

	public static final String FALSE = "0";
	public static final String TRUE = "1";

	public DownloadDetailDao(Context context) {
		this.resolver = context.getContentResolver();
	}

	public void insert(DownloadDetailBean downloadDetailBean) {
		//	
		ContentValues values = new ContentValues();
		values.put(DownloadDetailDaoTabelDescribe.DownloadDetail.ID, downloadDetailBean.getId());
		values.put(DownloadDetailDaoTabelDescribe.DownloadDetail.URL, downloadDetailBean.getUrl());
		values.put(DownloadDetailDaoTabelDescribe.DownloadDetail.PATH,
				downloadDetailBean.getPath());
		values.put(DownloadDetailDaoTabelDescribe.DownloadDetail.START_POS,
				downloadDetailBean.getStart_pos());
		values.put(DownloadDetailDaoTabelDescribe.DownloadDetail.END_POS, downloadDetailBean.getEnd_pos());
		values.put(DownloadDetailDaoTabelDescribe.DownloadDetail.COMPLETE_SIZE, downloadDetailBean.getCompelete_size());
		
		values.put(DownloadDetailDaoTabelDescribe.DownloadDetail.TIME,
				CalendarDealWith.formatCalendar(downloadDetailBean.getTime()));

		resolver.insert(DownloadDetailDaoTabelDescribe.DownloadDetail.CONTENT_URI, values);
	}

	public int update(DownloadDetailBean downloadDetailBean) {
		ContentValues values = new ContentValues();
		values.put(DownloadDetailDaoTabelDescribe.DownloadDetail.ID, downloadDetailBean.getId());
		values.put(DownloadDetailDaoTabelDescribe.DownloadDetail.URL, downloadDetailBean.getUrl());
		values.put(DownloadDetailDaoTabelDescribe.DownloadDetail.PATH,
				downloadDetailBean.getPath());
		values.put(DownloadDetailDaoTabelDescribe.DownloadDetail.START_POS,
				downloadDetailBean.getStart_pos());
		values.put(DownloadDetailDaoTabelDescribe.DownloadDetail.END_POS, downloadDetailBean.getEnd_pos());
		values.put(DownloadDetailDaoTabelDescribe.DownloadDetail.COMPLETE_SIZE, downloadDetailBean.getCompelete_size());
		
		values.put(DownloadDetailDaoTabelDescribe.DownloadDetail.TIME,
				CalendarDealWith.formatCalendar(downloadDetailBean.getTime()));


		int count = resolver.update(DownloadDetailDaoTabelDescribe.DownloadDetail.CONTENT_URI, values,
				DownloadDetailDaoTabelDescribe.DownloadDetail.ID + "=?",
				new String[] { downloadDetailBean.getId() });

		return count;
	}

	

	public int deleteMessageRecord(String download_detail_id) {
		int count = resolver.delete(DownloadDetailDaoTabelDescribe.DownloadDetail.CONTENT_URI,
				DownloadDetailDaoTabelDescribe.DownloadDetail.ID + "=?", new String[] { download_detail_id });

		return count;
	}
	

	public int clearMessageRecord() {
		int count = resolver.delete(DownloadDetailDaoTabelDescribe.DownloadDetail.CONTENT_URI, null,
				null);

		return count;
	}

	
//	public int getCountOfMessageNotReaded(String receiverNumber) {
//		Cursor cursor = resolver.query(DownloadDetailDaoTabelDescribe.DownloadDetail.CONTENT_URI,
//				new String[] { DownloadDetailDaoTabelDescribe.MessageReceived.IS_READED },
//				DownloadDetailDaoTabelDescribe.MessageReceived.RECEIVER_NUMBER + "=?",
//				new String[] { receiverNumber }, null);
//		// TODO
//		int count = 0;
//		if (cursor != null) {
//			if (cursor.moveToFirst()) {
//				int indexOfIsReaded = cursor
//						.getColumnIndex(DownloadDetailDaoTabelDescribe.MessageReceived.IS_READED);
//				do {
//					boolean isReaded = TRUE.equals(cursor.getString(indexOfIsReaded)) ? true
//							: false;
//					if (!isReaded) {
//						count++;
//					}
//				} while (cursor.moveToNext());
//			}
//			cursor.close();
//		}
//
//		return count;
//	}

	

	public DownloadDetailBean queryDownloadPartByUrl(String url) {
		DownloadDetailBean messageRecord = null;

		Cursor cursor = resolver.query(DownloadDetailDaoTabelDescribe.DownloadDetail.CONTENT_URI,
				new String[] { DownloadDetailDaoTabelDescribe.DownloadDetail.PATH,
						DownloadDetailDaoTabelDescribe.DownloadDetail.START_POS,
						DownloadDetailDaoTabelDescribe.DownloadDetail.END_POS,
						DownloadDetailDaoTabelDescribe.DownloadDetail.URL,
						DownloadDetailDaoTabelDescribe.DownloadDetail.TIME,
						DownloadDetailDaoTabelDescribe.DownloadDetail.ID
					 },
				DownloadDetailDaoTabelDescribe.DownloadDetail.URL + "=?", new String[] { url }, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				
				String path = cursor.getString(cursor.getColumnIndex(DownloadDetailDaoTabelDescribe.DownloadDetail.PATH));
				String start_pos = cursor.getString(cursor.getColumnIndex(DownloadDetailDaoTabelDescribe.DownloadDetail.START_POS));
				String end_pos = cursor.getString(cursor.getColumnIndex(DownloadDetailDaoTabelDescribe.DownloadDetail.END_POS));
				String time  = cursor.getString(cursor.getColumnIndex(DownloadDetailDaoTabelDescribe.DownloadDetail.TIME));
				String id  = cursor.getString(cursor.getColumnIndex(DownloadDetailDaoTabelDescribe.DownloadDetail.ID));
		
				messageRecord = new DownloadDetailBean( id,  path,  url,  start_pos,  end_pos, CalendarDealWith.parseStringToCalendar(time));
			}

			cursor.close();
		}

		return messageRecord;
	}


	

	

}
