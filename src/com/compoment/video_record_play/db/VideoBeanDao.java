package com.compoment.video_record_play.db;

import java.util.ArrayList;
import java.util.List;

import com.compoment.video_record_play.util.CalendarDealWith;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;



public class VideoBeanDao {
	private ContentResolver resolver;

	public static final String FALSE = "0";
	public static final String TRUE = "1";

	public VideoBeanDao(Context context) {
		this.resolver = context.getContentResolver();
	}

	public void insert(VideoBean messageRecord) {
		// �������
		ContentValues values = new ContentValues();
		values.put(VideoBeanDaoTabelDescribe.MessageRecord.ID, messageRecord.getId());
		values.put(VideoBeanDaoTabelDescribe.MessageRecord.DURATION, messageRecord.getDuration());
		values.put(VideoBeanDaoTabelDescribe.MessageRecord.FILE_LENGTH,
				messageRecord.getFileLength());
		values.put(VideoBeanDaoTabelDescribe.MessageRecord.PATH, messageRecord.getPath());
		values.put(VideoBeanDaoTabelDescribe.MessageRecord.PREVIEW_IMAGE_PATH,
				messageRecord.getPreviewImagePath());
		values.put(VideoBeanDaoTabelDescribe.MessageRecord.RECORDER_TIME,
				CalendarDealWith.formatCalendar(messageRecord.getRecordTime()));
		values.put(VideoBeanDaoTabelDescribe.MessageRecord.TYPE, messageRecord.getType());

		Log.e("MessageDAO_New",
				"insert MessageRecord  values = " + values.toString());

		resolver.insert(VideoBeanDaoTabelDescribe.MessageRecord.CONTENT_URI, values);
	}

	public int update(VideoBean messageRecord) {
		ContentValues values = new ContentValues();
		values.put(VideoBeanDaoTabelDescribe.MessageRecord.ID, messageRecord.getId());
		values.put(VideoBeanDaoTabelDescribe.MessageRecord.DURATION, messageRecord.getDuration());
		values.put(VideoBeanDaoTabelDescribe.MessageRecord.FILE_LENGTH,
				messageRecord.getFileLength());
		values.put(VideoBeanDaoTabelDescribe.MessageRecord.PATH, messageRecord.getPath());
		values.put(VideoBeanDaoTabelDescribe.MessageRecord.PREVIEW_IMAGE_PATH,
				messageRecord.getPreviewImagePath());
		values.put(VideoBeanDaoTabelDescribe.MessageRecord.RECORDER_TIME,
				CalendarDealWith.formatCalendar(messageRecord.getRecordTime()));
		values.put(VideoBeanDaoTabelDescribe.MessageRecord.TYPE, messageRecord.getType());

		int count = resolver.update(VideoBeanDaoTabelDescribe.MessageRecord.CONTENT_URI, values,
				VideoBeanDaoTabelDescribe.MessageRecord.ID + "=?",
				new String[] { messageRecord.getId() });

		return count;
	}

	

	public int deleteMessageRecord(String rcd_id) {
		int count = resolver.delete(VideoBeanDaoTabelDescribe.MessageRecord.CONTENT_URI,
				VideoBeanDaoTabelDescribe.MessageRecord.ID + "=?", new String[] { rcd_id });

		return count;
	}
	

	public int clearMessageRecord() {
		int count = resolver.delete(VideoBeanDaoTabelDescribe.MessageRecord.CONTENT_URI, null,
				null);

		return count;
	}

	
	public int getCountOfMessageNotReaded(String receiverNumber) {
		Cursor cursor = resolver.query(VideoBeanDaoTabelDescribe.MessageReceived.CONTENT_URI,
				new String[] { VideoBeanDaoTabelDescribe.MessageReceived.IS_READED },
				VideoBeanDaoTabelDescribe.MessageReceived.RECEIVER_NUMBER + "=?",
				new String[] { receiverNumber }, null);
		// TODO
		int count = 0;
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int indexOfIsReaded = cursor
						.getColumnIndex(VideoBeanDaoTabelDescribe.MessageReceived.IS_READED);
				do {
					boolean isReaded = TRUE.equals(cursor.getString(indexOfIsReaded)) ? true
							: false;
					if (!isReaded) {
						count++;
					}
				} while (cursor.moveToNext());
			}
			cursor.close();
		}

		return count;
	}

	

	public VideoBean queryRecordById(String recordId) {
		VideoBean messageRecord = null;

		Cursor cursor = resolver.query(VideoBeanDaoTabelDescribe.MessageRecord.CONTENT_URI,
				new String[] { VideoBeanDaoTabelDescribe.MessageRecord.PATH,
						VideoBeanDaoTabelDescribe.MessageRecord.RECORDER_TIME,
						VideoBeanDaoTabelDescribe.MessageRecord.TYPE,
						VideoBeanDaoTabelDescribe.MessageRecord.FILE_LENGTH,
						VideoBeanDaoTabelDescribe.MessageRecord.DURATION,
						VideoBeanDaoTabelDescribe.MessageRecord.PREVIEW_IMAGE_PATH },
				VideoBeanDaoTabelDescribe.MessageRecord.ID + "=?", new String[] { recordId }, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int indexOfPath = cursor.getColumnIndex(VideoBeanDaoTabelDescribe.MessageRecord.PATH);
				int indexOfPreviewImagePath = cursor
						.getColumnIndex(VideoBeanDaoTabelDescribe.MessageRecord.PREVIEW_IMAGE_PATH);
				int indexOfType = cursor.getColumnIndex(VideoBeanDaoTabelDescribe.MessageRecord.TYPE);
				int indexOfRecordTime = cursor
						.getColumnIndex(VideoBeanDaoTabelDescribe.MessageRecord.RECORDER_TIME);
				int indexOfDuration = cursor
						.getColumnIndex(VideoBeanDaoTabelDescribe.MessageRecord.DURATION);
				int indexOfFileLength = cursor
						.getColumnIndex(VideoBeanDaoTabelDescribe.MessageRecord.FILE_LENGTH);

				String path = cursor.getString(indexOfPath);
				String previewImagePath = cursor.getString(indexOfPreviewImagePath);
				String type = cursor.getString(indexOfType);
				String recordTime = cursor.getString(indexOfRecordTime);
				int duration = cursor.getInt(indexOfDuration);
				long fileLength = cursor.getLong(indexOfFileLength);

				messageRecord = new VideoBean(recordId, path, previewImagePath,
						type, CalendarDealWith.parseStringToCalendar(recordTime), duration,
						fileLength, null);
			}

			cursor.close();
		}

		return messageRecord;
	}


	public List<VideoBean> queryAllRecord() {
		List<VideoBean> messageRecords = new ArrayList<VideoBean>();

		Cursor cursor = resolver.query(VideoBeanDaoTabelDescribe.MessageRecord.CONTENT_URI,
				new String[] { VideoBeanDaoTabelDescribe.MessageRecord.ID,
						VideoBeanDaoTabelDescribe.MessageRecord.PATH,
						VideoBeanDaoTabelDescribe.MessageRecord.RECORDER_TIME,
						VideoBeanDaoTabelDescribe.MessageRecord.TYPE,
						VideoBeanDaoTabelDescribe.MessageRecord.FILE_LENGTH,
						VideoBeanDaoTabelDescribe.MessageRecord.DURATION,
						VideoBeanDaoTabelDescribe.MessageRecord.PREVIEW_IMAGE_PATH }, null, null, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {

				int indexOfRecordId = cursor
						.getColumnIndex(VideoBeanDaoTabelDescribe.MessageRecord.ID);
				int indexOfPath = cursor.getColumnIndex(VideoBeanDaoTabelDescribe.MessageRecord.PATH);
				int indexOfPreviewImagePath = cursor
						.getColumnIndex(VideoBeanDaoTabelDescribe.MessageRecord.PREVIEW_IMAGE_PATH);
				int indexOfType = cursor.getColumnIndex(VideoBeanDaoTabelDescribe.MessageRecord.TYPE);
				int indexOfRecordTime = cursor
						.getColumnIndex(VideoBeanDaoTabelDescribe.MessageRecord.RECORDER_TIME);
				int indexOfDuration = cursor
						.getColumnIndex(VideoBeanDaoTabelDescribe.MessageRecord.DURATION);
				int indexOfFileLength = cursor
						.getColumnIndex(VideoBeanDaoTabelDescribe.MessageRecord.FILE_LENGTH);

				do {
					String recordId = cursor.getString(indexOfRecordId);
					String path = cursor.getString(indexOfPath);
					String previewImagePath = cursor.getString(indexOfPreviewImagePath);
					String type = cursor.getString(indexOfType);
					String recordTime = cursor.getString(indexOfRecordTime);
					int duration = cursor.getInt(indexOfDuration);
					long fileLength = cursor.getLong(indexOfFileLength);

					VideoBean messageRecord = new VideoBean(recordId, path,
							previewImagePath, type,
							CalendarDealWith.parseStringToCalendar(recordTime), duration,
							fileLength, null);
					messageRecords.add(messageRecord);
				} while (cursor.moveToNext());
			}

			cursor.close();
		}

		return messageRecords;
	}

	

}
