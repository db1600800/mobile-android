package com.compoment.uploading_breakpoint_continue.db;


import java.util.ArrayList;
import java.util.List;

import com.compoment.uploading_breakpoint_continue.util.CalendarDealWith;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

//http://www.2cto.com/kf/201207/144022.html

public class UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver {
	private ContentResolver resolver;

	public static final String FALSE = "0";
	public static final String TRUE = "1";

	public UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(Context context) {
		this.resolver = context.getContentResolver();
	}

	public void insert(FileDetailBean messageRecord) {
		// 插入数据
		ContentValues values = new ContentValues();
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.ID, messageRecord.getId());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.DURATION, messageRecord.getDuration());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.FILE_LENGTH,
				messageRecord.getFileLength());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.PATH, messageRecord.getPath());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.PREVIEW_IMAGE_PATH,
				messageRecord.getPreviewImagePath());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.RECORDER_TIME,
				CalendarDealWith.formatCalendar(messageRecord.getRecordTime()));
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.TYPE, messageRecord.getType());

		Log.e("MessageDAO_New",
				"insert MessageRecord  values = " + values.toString());

		resolver.insert(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.CONTENT_URI, values);
	}

	public void insert(SentDetailBean messageSent) {
		ContentValues values = new ContentValues();

		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.MESSAGE_SENT_ID, messageSent.getId());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.CODE, messageSent.getCode());
		if(messageSent.getFileDetailBean()!=null)
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.MESSAGE_RECORD_ID, messageSent.getFileDetailBean().getId());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.RECEIVER_NUMBER,
				messageSent.getReceiverPhoneNumber());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.SENDER_NUMBER,
				messageSent.getSenderPhoneNumber());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.TIME,
				CalendarDealWith.formatCalendar(messageSent.getSendTime()));

		resolver.insert(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.CONTENT_URI, values);
	}

//	public void insert(MessageReceived messageReceived) {
//		ContentValues values = new ContentValues();
//
//		values.put(MessageData.MessageReceived.ID, messageReceived.getId());
//		values.put(MessageData.MessageReceived.DURATION,
//				messageReceived.getDuration());
//		values.put(MessageData.MessageReceived.FILE_LENGTH,
//				messageReceived.getFileLength());
//		values.put(MessageData.MessageReceived.IS_DOWNLOAD_FINISHED,
//				messageReceived.isDownloadFinished() ? TRUE : FALSE);
//		values.put(MessageData.MessageReceived.IS_READED,
//				messageReceived.isReaded() ? TRUE : FALSE);
//		values.put(MessageData.MessageReceived.PATH, messageReceived.getPath());
//		values.put(MessageData.MessageReceived.PREVIEW_IMAGE_PATH,
//				messageReceived.getPreviewImagePath());
//		values.put(MessageData.MessageReceived.PREVIEW_IMAGE_URL,
//				messageReceived.getPreviewImageUrl());
//		values.put(MessageData.MessageReceived.RECEIVER_NUMBER,
//				messageReceived.getReceiverPhoneNumber());
//		values.put(MessageData.MessageReceived.SENDER_NUMBER,
//				messageReceived.getSenderPhoneNumber());
//		values.put(MessageData.MessageReceived.SENT_TIME,
//				CalendarDealWith.formatCalendar(messageReceived.getSendTime()));
//		values.put(MessageData.MessageReceived.TYPE, messageReceived.getType());
//		values.put(MessageData.MessageReceived.URL, messageReceived.getUrl());
//
//		resolver.insert(MessageData.MessageReceived.CONTENT_URI, values);
//	}

//	public void insert(MessageDownloading messageDownloading) {
//		ContentValues values = new ContentValues();
//		values.put(MessageData.MessageDownloading.ID,
//				messageDownloading.getMsgRcvId());
//		values.put(MessageData.MessageDownloading.PATH,
//				messageDownloading.getPath());
//		values.put(MessageData.MessageDownloading.URL, messageDownloading.getUrl());
//		values.put(MessageData.MessageDownloading.RECEIVER_NUMBER,
//				messageDownloading.getReceiverNumber());
//
//		resolver.insert(MessageData.MessageDownloading.CONTENT_URI, values);
//	}

	public void insert(UploadPartBean messageUploading) {
		ContentValues values = new ContentValues();
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.FILE_ID,
				messageUploading.getFile_Id());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.BLOCK, messageUploading.getBlock());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.SNDER_NUMBER,
				messageUploading.getSnderNumber());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.RCVER_NUMBER,
				messageUploading.getRcverNumber());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.DURATION,
				messageUploading.getDuration());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.TYPE, messageUploading.getType());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.PATH, messageUploading.getPath());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.SENT_ID,
				messageUploading.getSnt_id());

		resolver.insert(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.CONTENT_URI, values);
	}

	/** 插入一条数据 */
	public void insert(UploadCompleteBean messageUploaded) {
		String recordId = messageUploaded.getMsgRcdId();
		String sentId = messageUploaded.getMsgSntId();

		ContentValues values = new ContentValues();
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable.MESSAGE_RECORD_ID, recordId);
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable.MESSAGE_SENT_ID, sentId);

		resolver.insert(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable.CONTENT_URI, values);
	}

	public int update(FileDetailBean messageRecord) {
		ContentValues values = new ContentValues();
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.ID, messageRecord.getId());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.DURATION, messageRecord.getDuration());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.FILE_LENGTH,
				messageRecord.getFileLength());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.PATH, messageRecord.getPath());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.PREVIEW_IMAGE_PATH,
				messageRecord.getPreviewImagePath());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.RECORDER_TIME,
				CalendarDealWith.formatCalendar(messageRecord.getRecordTime()));
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.TYPE, messageRecord.getType());

		int count = resolver.update(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.CONTENT_URI, values,
				UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.ID + "=?",
				new String[] { messageRecord.getId() });

		return count;
	}

	public int update(SentDetailBean messageSent) {
		ContentValues values = new ContentValues();

		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.MESSAGE_SENT_ID, messageSent.getId());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.CODE, messageSent.getCode());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.MESSAGE_RECORD_ID, messageSent
				.getFileDetailBean().getId());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.RECEIVER_NUMBER,
				messageSent.getReceiverPhoneNumber());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.SENDER_NUMBER,
				messageSent.getSenderPhoneNumber());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.TIME,
				CalendarDealWith.formatCalendar(messageSent.getSendTime()));

		int count = resolver.update(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.CONTENT_URI, values,
				UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.MESSAGE_SENT_ID + "=?",
				new String[] { messageSent.getId() });

		return count;
	}

	public int update(UploadPartBean messageUploading) {
		ContentValues values = new ContentValues();
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.FILE_ID,
				messageUploading.getFile_Id());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.BLOCK, messageUploading.getBlock());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.SNDER_NUMBER,
				messageUploading.getSnderNumber());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.RCVER_NUMBER,
				messageUploading.getRcverNumber());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.DURATION,
				messageUploading.getDuration());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.TYPE, messageUploading.getType());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.PATH, messageUploading.getPath());
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.SENT_ID,
				messageUploading.getSnt_id());

		int count = resolver.update(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.CONTENT_URI,
				values, UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.FILE_ID + "=?",
				new String[] { messageUploading.getFile_Id() });

		return count;
	}

//	public int update(MessageReceived messageReceived) {
//		ContentValues values = new ContentValues();
//
//		values.put(MessageData.MessageReceived.ID, messageReceived.getId());
//		values.put(MessageData.MessageReceived.DURATION,
//				messageReceived.getDuration());
//		values.put(MessageData.MessageReceived.FILE_LENGTH,
//				messageReceived.getFileLength());
//		values.put(MessageData.MessageReceived.IS_DOWNLOAD_FINISHED,
//				messageReceived.isDownloadFinished() ? TRUE : FALSE);
//		values.put(MessageData.MessageReceived.IS_READED,
//				messageReceived.isReaded() ? TRUE : FALSE);
//		values.put(MessageData.MessageReceived.PATH, messageReceived.getPath());
//		values.put(MessageData.MessageReceived.PREVIEW_IMAGE_PATH,
//				messageReceived.getPreviewImagePath());
//		values.put(MessageData.MessageReceived.PREVIEW_IMAGE_URL,
//				messageReceived.getPreviewImageUrl());
//		values.put(MessageData.MessageReceived.RECEIVER_NUMBER,
//				messageReceived.getReceiverPhoneNumber());
//		values.put(MessageData.MessageReceived.SENDER_NUMBER,
//				messageReceived.getSenderPhoneNumber());
//		values.put(MessageData.MessageReceived.SENT_TIME,
//				CalendarDealWith.formatCalendar(messageReceived.getSendTime()));
//		values.put(MessageData.MessageReceived.TYPE, messageReceived.getType());
//		values.put(MessageData.MessageReceived.URL, messageReceived.getUrl());
//
//		int count = resolver.update(MessageData.MessageReceived.CONTENT_URI,
//				values, MessageData.MessageReceived.ID + "=?",
//				new String[] { messageReceived.getId() });
//
//		return count;
//	}



	/** 更新数据 */
	public int update(UploadCompleteBean messageUploaded) {
		String recordId = messageUploaded.getMsgRcdId();
		String sentId = messageUploaded.getMsgSntId();

		ContentValues values = new ContentValues();
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable.MESSAGE_RECORD_ID, recordId);
		values.put(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable.MESSAGE_SENT_ID, sentId);

		int count = resolver.update(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageDownloadingTable.CONTENT_URI,
				values, UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable.MESSAGE_RECORD_ID + "=?",
				new String[] { recordId });

		return count;
	}

	public int deleteMessageRecord(String rcd_id) {
		int count = resolver.delete(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.CONTENT_URI,
				UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.ID + "=?", new String[] { rcd_id });

		return count;
	}

	public int deleteMessageSent(String snt_id) {
		int count = resolver
				.delete(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.CONTENT_URI,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.MESSAGE_SENT_ID + "=?",
						new String[] { snt_id });

		return count;
	}

	public int deleteMessageSentByRecordId(String msgRcdId) {
		int count = resolver.delete(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.CONTENT_URI,
				UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.MESSAGE_RECORD_ID + "=?",
				new String[] { msgRcdId });

		return count;
	}

	public int deleteMessageReceived(String rcv_id) {
		int count = resolver.delete(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.CONTENT_URI,
				UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.ID + "=?", new String[] { rcv_id });

		return count;
	}

	public int deleteMessageDownloading(String rcv_id) {
		int count = resolver.delete(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageDownloadingTable.CONTENT_URI,
				UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageDownloadingTable.ID + "=?", new String[] { rcv_id });

		return count;
	}

	public int deleteMessageUploading(String rcd_id) {
		int count = resolver.delete(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.CONTENT_URI,
				UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.FILE_ID + "=?", new String[] { rcd_id });

		return count;
	}

	/** 删除数据 */
	public int deleteMessageUploaded(String msgRcdId) {
		int count = resolver.delete(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable.CONTENT_URI,
				UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable.MESSAGE_RECORD_ID + "=?",
				new String[] { msgRcdId });

		return count;
	}

	public int clearMessageRecord() {
		int count = resolver.delete(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.CONTENT_URI, null,
				null);

		return count;
	}

	public int clearMessageSent() {
		int count = resolver
				.delete(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.CONTENT_URI, null, null);

		return count;
	}

	public int clearMessageReceived() {
		int count = resolver.delete(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.CONTENT_URI, null,
				null);

		return count;
	}

	public int clearMessageDownloading() {
		int count = resolver.delete(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageDownloadingTable.CONTENT_URI,
				null, null);

		return count;
	}

	public int clearMessageUploading() {
		int count = resolver.delete(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.CONTENT_URI, null,
				null);

		return count;
	}

	/** 删除数据 */
	public int clearMessageUploaded() {
		int count = resolver.delete(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable.CONTENT_URI, null,
				null);

		return count;
	}

	public int getCountOfMessageNotReaded(String receiverNumber) {
		Cursor cursor = resolver.query(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.CONTENT_URI,
				new String[] { UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.IS_READED },
				UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.RECEIVER_NUMBER + "=?",
				new String[] { receiverNumber }, null);
		// TODO
		int count = 0;
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int indexOfIsReaded = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.IS_READED);
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

	public List<SentDetailBean> querySentsBySenderNumber(String senderNumber) {
		List<SentDetailBean> list = new ArrayList<SentDetailBean>();
		Cursor cursor = resolver.query(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.CONTENT_URI,
				new String[] { UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.MESSAGE_SENT_ID,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.MESSAGE_RECORD_ID,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.TIME,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.RECEIVER_NUMBER,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.CODE },
				UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.SENDER_NUMBER + "=?",
				new String[] { senderNumber }, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int indexOfSentId = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.MESSAGE_SENT_ID);
				int indexOfRecordId = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.MESSAGE_RECORD_ID);
				int indexOfSentTime = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.TIME);
				int indexOfReceiverNumber = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.RECEIVER_NUMBER);
				int indexOfCode = cursor.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.CODE);

				do {
					String sentId = cursor.getString(indexOfSentId);
					String recordId = cursor.getString(indexOfRecordId);
					FileDetailBean messageRecord = queryRecordById(recordId);
					String sentTime = cursor.getString(indexOfSentTime);
					String receiverNumber = cursor.getString(indexOfReceiverNumber);
					String code = cursor.getString(indexOfCode);

					SentDetailBean messageSent = new SentDetailBean(sentId, null,
							receiverNumber, senderNumber, code,
							CalendarDealWith.parseStringToCalendar(sentTime), messageRecord);
					list.add(messageSent);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return list;
	}

	public SentDetailBean querySentById(String snt_id) {
		SentDetailBean messageSent = null;

		Cursor cursor = resolver
				.query(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.CONTENT_URI,
						new String[] { UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.MESSAGE_RECORD_ID,
								UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.TIME,
								UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.RECEIVER_NUMBER,
								UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.SENDER_NUMBER,
								UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.CODE },
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.MESSAGE_SENT_ID + "=?",
						new String[] { snt_id }, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int indexOfRecordId = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.MESSAGE_RECORD_ID);
				int indexOfTime = cursor.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.TIME);
				int indexOfReceiverNumber = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.RECEIVER_NUMBER);
				int indexOfSenderNumber = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.SENDER_NUMBER);
				int indexOfCode = cursor.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.CODE);

				String recordId = cursor.getString(indexOfRecordId);
				FileDetailBean messageRecord = queryRecordById(recordId);
				String sentTime = cursor.getString(indexOfTime);
				String receiverNumber = cursor.getString(indexOfReceiverNumber);
				String senderNumber = cursor.getString(indexOfSenderNumber);
				String code = cursor.getString(indexOfCode);

				messageSent = new SentDetailBean(snt_id, null, receiverNumber,
						senderNumber, code, CalendarDealWith.parseStringToCalendar(sentTime),
						messageRecord);
			}

			cursor.close();
		}

		return messageSent;
	}

	public List<SentDetailBean> querySentByRecordId(String rcd_id) {
		List<SentDetailBean> list = new ArrayList<SentDetailBean>();

		Cursor cursor = resolver
				.query(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.CONTENT_URI,
						new String[] { UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.MESSAGE_SENT_ID,
								UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.TIME,
								UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.RECEIVER_NUMBER,
								UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.SENDER_NUMBER,
								UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.CODE },
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.MESSAGE_RECORD_ID + "=?",
						new String[] { rcd_id }, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int indexOfSentId = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.MESSAGE_SENT_ID);
				int indexOfSentTime = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.TIME);
				int indexOfReceiverNumber = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.RECEIVER_NUMBER);
				int indexOfSenderNumber = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.SENDER_NUMBER);
				int indexOfCode = cursor.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageSentTable.CODE);

				do {
					String sentId = cursor.getString(indexOfSentId);
					FileDetailBean messageRecord = queryRecordById(rcd_id);
					String sentTime = cursor.getString(indexOfSentTime);
					String receiverNumber = cursor.getString(indexOfReceiverNumber);
					String senderNumber = cursor.getString(indexOfSenderNumber);
					String code = cursor.getString(indexOfCode);

					SentDetailBean messageSent = new SentDetailBean(sentId, null,
							receiverNumber, senderNumber, code,
							CalendarDealWith.parseStringToCalendar(sentTime), messageRecord);
					list.add(messageSent);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return list;
	}

	public FileDetailBean queryRecordById(String recordId) {
		FileDetailBean messageRecord = null;

		Cursor cursor = resolver.query(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.CONTENT_URI,
				new String[] { UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.PATH,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.RECORDER_TIME,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.TYPE,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.FILE_LENGTH,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.DURATION,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.PREVIEW_IMAGE_PATH },
				UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.ID + "=?", new String[] { recordId }, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int indexOfPath = cursor.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.PATH);
				int indexOfPreviewImagePath = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.PREVIEW_IMAGE_PATH);
				int indexOfType = cursor.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.TYPE);
				int indexOfRecordTime = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.RECORDER_TIME);
				int indexOfDuration = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.DURATION);
				int indexOfFileLength = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.FILE_LENGTH);

				String path = cursor.getString(indexOfPath);
				String previewImagePath = cursor.getString(indexOfPreviewImagePath);
				String type = cursor.getString(indexOfType);
				String recordTime = cursor.getString(indexOfRecordTime);
				int duration = cursor.getInt(indexOfDuration);
				long fileLength = cursor.getLong(indexOfFileLength);

				messageRecord = new FileDetailBean(recordId, path, previewImagePath,
						type, CalendarDealWith.parseStringToCalendar(recordTime), duration,
						fileLength, null);
			}

			cursor.close();
		}

		return messageRecord;
	}

//	public List<MessageReceived> queryReceivedsByReceiverNumber(
//			String receiverNumber) {
//		List<MessageReceived> list = new ArrayList<MessageReceived>();
//
//		Cursor cursor = resolver.query(MessageData.MessageReceived.CONTENT_URI,
//				new String[] { MessageData.MessageReceived.ID,
//						MessageData.MessageReceived.TYPE, MessageData.MessageReceived.PATH,
//						MessageData.MessageReceived.URL,
//						MessageData.MessageReceived.PREVIEW_IMAGE_PATH,
//						MessageData.MessageReceived.PREVIEW_IMAGE_URL,
//						MessageData.MessageReceived.SENDER_NUMBER,
//						MessageData.MessageReceived.SENT_TIME,
//						MessageData.MessageReceived.IS_READED,
//						MessageData.MessageReceived.IS_DOWNLOAD_FINISHED,
//						MessageData.MessageReceived.FILE_LENGTH,
//						MessageData.MessageReceived.DURATION },
//				MessageData.MessageReceived.RECEIVER_NUMBER + "=?",
//				new String[] { receiverNumber }, null);
//
//		if (cursor != null) {
//			if (cursor.moveToFirst()) {
//
//				int indexOfReceivedId = cursor
//						.getColumnIndex(MessageData.MessageReceived.ID);
//				int indexOfType = cursor
//						.getColumnIndex(MessageData.MessageReceived.TYPE);
//				int indexOfPath = cursor
//						.getColumnIndex(MessageData.MessageReceived.PATH);
//				int indexOfUrl = cursor.getColumnIndex(MessageData.MessageReceived.URL);
//				int indexOfPreviewImagePath = cursor
//						.getColumnIndex(MessageData.MessageReceived.PREVIEW_IMAGE_PATH);
//				int indexOfPreviewImageUrl = cursor
//						.getColumnIndex(MessageData.MessageReceived.PREVIEW_IMAGE_URL);
//				int indexOfSenderNumber = cursor
//						.getColumnIndex(MessageData.MessageReceived.SENDER_NUMBER);
//				int indexOfSentTime = cursor
//						.getColumnIndex(MessageData.MessageReceived.SENT_TIME);
//				int indexOfIsReaded = cursor
//						.getColumnIndex(MessageData.MessageReceived.IS_READED);
//				int indexOfIsDownloadFinished = cursor
//						.getColumnIndex(MessageData.MessageReceived.IS_DOWNLOAD_FINISHED);
//				int indexOfFileLength = cursor
//						.getColumnIndex(MessageData.MessageReceived.FILE_LENGTH);
//				int indexOfDuration = cursor
//						.getColumnIndex(MessageData.MessageReceived.DURATION);
//
//				do {
//					String receiverId = cursor.getString(indexOfReceivedId);
//					String type = cursor.getString(indexOfType);
//					String path = cursor.getString(indexOfPath);
//					String url = cursor.getString(indexOfUrl);
//					String previewImagePath = cursor.getString(indexOfPreviewImagePath);
//					String previewImageUrl = cursor.getString(indexOfPreviewImageUrl);
//					String senderNumber = cursor.getString(indexOfSenderNumber);
//					String sentTime = cursor.getString(indexOfSentTime);
//					String isReadedStr = cursor.getString(indexOfIsReaded);
//					String isDownloadFinishedStr = cursor
//							.getString(indexOfIsDownloadFinished);
//					long fileLength = cursor.getLong(indexOfFileLength);
//					int duration = cursor.getInt(indexOfDuration);
//
//					boolean isReaded = (TRUE.equals(isReadedStr)) ? (true) : (false);
//					boolean isDownloadFinished = (TRUE.equals(isDownloadFinishedStr)) ? (true)
//							: (false);
//
//					MessageReceived messageReceived = new MessageReceived(receiverId,
//							null, null, type, url, path, previewImageUrl, previewImagePath,
//							senderNumber, receiverNumber, duration, fileLength, isReaded,
//							MessageUtil.parseStringToCalendar(sentTime), isDownloadFinished);
//					list.add(messageReceived);
//				} while (cursor.moveToNext());
//			}
//
//			cursor.close();
//		}
//		return list;
//	}

//	public MessageReceived queryReceivedById(String rcv_id) {
//		MessageReceived messageReceived = null;
//
//		Cursor cursor = resolver.query(MessageData.MessageReceived.CONTENT_URI,
//				new String[] { MessageData.MessageReceived.TYPE,
//						MessageData.MessageReceived.PATH, MessageData.MessageReceived.URL,
//						MessageData.MessageReceived.PREVIEW_IMAGE_PATH,
//						MessageData.MessageReceived.PREVIEW_IMAGE_URL,
//						MessageData.MessageReceived.SENDER_NUMBER,
//						MessageData.MessageReceived.RECEIVER_NUMBER,
//						MessageData.MessageReceived.SENT_TIME,
//						MessageData.MessageReceived.IS_READED,
//						MessageData.MessageReceived.IS_DOWNLOAD_FINISHED,
//						MessageData.MessageReceived.FILE_LENGTH,
//						MessageData.MessageReceived.DURATION },
//				MessageData.MessageReceived.ID + "=?", new String[] { rcv_id }, null);
//
//		if (cursor != null) {
//			if (cursor.moveToFirst()) {
//				int indexOfType = cursor
//						.getColumnIndex(MessageData.MessageReceived.TYPE);
//				int indexOfPath = cursor
//						.getColumnIndex(MessageData.MessageReceived.PATH);
//				int indexOfUrl = cursor.getColumnIndex(MessageData.MessageReceived.URL);
//				int indexOfPreviewImagePath = cursor
//						.getColumnIndex(MessageData.MessageReceived.PREVIEW_IMAGE_PATH);
//				int indexOfPreviewImageUrl = cursor
//						.getColumnIndex(MessageData.MessageReceived.PREVIEW_IMAGE_URL);
//				int indexOfSenderNumber = cursor
//						.getColumnIndex(MessageData.MessageReceived.SENDER_NUMBER);
//				int indexOfReceiverNumber = cursor
//						.getColumnIndex(MessageData.MessageReceived.RECEIVER_NUMBER);
//				int indexOfSentTime = cursor
//						.getColumnIndex(MessageData.MessageReceived.SENT_TIME);
//				int indexOfIsReaded = cursor
//						.getColumnIndex(MessageData.MessageReceived.IS_READED);
//				int indexOfIsDownloadFinished = cursor
//						.getColumnIndex(MessageData.MessageReceived.IS_DOWNLOAD_FINISHED);
//				int indexOfFileLength = cursor
//						.getColumnIndex(MessageData.MessageReceived.FILE_LENGTH);
//				int indexOfDuration = cursor
//						.getColumnIndex(MessageData.MessageReceived.DURATION);
//
//				String type = cursor.getString(indexOfType);
//				String path = cursor.getString(indexOfPath);
//				String url = cursor.getString(indexOfUrl);
//				String previewImagePath = cursor.getString(indexOfPreviewImagePath);
//				String previewImageUrl = cursor.getString(indexOfPreviewImageUrl);
//				String receiverNumber = cursor.getString(indexOfReceiverNumber);
//				String senderNumber = cursor.getString(indexOfSenderNumber);
//				String sentTime = cursor.getString(indexOfSentTime);
//				String isReadedStr = cursor.getString(indexOfIsReaded);
//				String isDownloadFinishedStr = cursor
//						.getString(indexOfIsDownloadFinished);
//				long fileLength = cursor.getLong(indexOfFileLength);
//				int duration = cursor.getInt(indexOfDuration);
//
//				boolean isReaded = (TRUE.equals(isReadedStr)) ? (true) : (false);
//				boolean isDownloadFinished = (TRUE.equals(isDownloadFinishedStr)) ? (true)
//						: (false);
//
//				messageReceived = new MessageReceived(rcv_id, null, null, type, url,
//						path, previewImageUrl, previewImagePath, senderNumber,
//						receiverNumber, duration, fileLength, isReaded,
//						MessageUtil.parseStringToCalendar(sentTime), isDownloadFinished);
//			}
//
//			cursor.close();
//		}
//		return messageReceived;
//	}

	public ArrayList<String> queryMessageReceivedNotReadedIds(
			String receiverNumber) {
		ArrayList<String> ids = new ArrayList<String>();

		Cursor cursor = resolver.query(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.CONTENT_URI,
				new String[] { UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.ID,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.IS_READED },
				UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.RECEIVER_NUMBER + "=?",
				new String[] { receiverNumber }, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int indexOfId = cursor.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.ID);
				int indexOfReaded = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.IS_READED);

				do {
					String id = cursor.getString(indexOfId);
					String isReadedStr = cursor.getString(indexOfReaded);

					boolean isReaded = (TRUE.equals(isReadedStr)) ? (true) : (false);

					if (!isReaded) {
						ids.add(id);
					}
				} while (cursor.moveToNext());
			}

			cursor.close();
		}
		return ids;
	}

	public String queryReceivedPreviewImagePathById(String rcv_id) {
		String previewImagePath = null;

		Cursor cursor = resolver.query(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.CONTENT_URI,
				new String[] { UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.PREVIEW_IMAGE_PATH },
				UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.ID + "=?", new String[] { rcv_id }, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int indexOfPreviewImagePath = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageReceivedTable.PREVIEW_IMAGE_PATH);

				previewImagePath = cursor.getString(indexOfPreviewImagePath);
			}

			cursor.close();
		}

		return previewImagePath;
	}

	public List<FileDetailBean> queryAllRecord() {
		List<FileDetailBean> messageRecords = new ArrayList<FileDetailBean>();

		Cursor cursor = resolver.query(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.CONTENT_URI,
				new String[] { UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.ID,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.PATH,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.RECORDER_TIME,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.TYPE,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.FILE_LENGTH,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.DURATION,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.PREVIEW_IMAGE_PATH }, null, null, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {

				int indexOfRecordId = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.ID);
				int indexOfPath = cursor.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.PATH);
				int indexOfPreviewImagePath = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.PREVIEW_IMAGE_PATH);
				int indexOfType = cursor.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.TYPE);
				int indexOfRecordTime = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.RECORDER_TIME);
				int indexOfDuration = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.DURATION);
				int indexOfFileLength = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageRecordTable.FILE_LENGTH);

				do {
					String recordId = cursor.getString(indexOfRecordId);
					String path = cursor.getString(indexOfPath);
					String previewImagePath = cursor.getString(indexOfPreviewImagePath);
					String type = cursor.getString(indexOfType);
					String recordTime = cursor.getString(indexOfRecordTime);
					int duration = cursor.getInt(indexOfDuration);
					long fileLength = cursor.getLong(indexOfFileLength);

					FileDetailBean messageRecord = new FileDetailBean(recordId, path,
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

	public List<UploadCompleteBean> queryAllMessageUploaded() {
		List<UploadCompleteBean> list = new ArrayList<UploadCompleteBean>();

		Cursor cursor = resolver.query(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable.CONTENT_URI,
				new String[] { UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable.MESSAGE_RECORD_ID,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable.MESSAGE_SENT_ID }, null, null, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {

				int indexOfMsgRcdId = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable.MESSAGE_RECORD_ID);
				int indexOfMsgSntId = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable.MESSAGE_SENT_ID);

				do {
					String msgRcdId = cursor.getString(indexOfMsgRcdId);
					String msgSntId = cursor.getString(indexOfMsgSntId);

					UploadCompleteBean messageUploaded = new UploadCompleteBean(msgRcdId,
							msgSntId);
					list.add(messageUploaded);
				} while (cursor.moveToNext());
			}

			cursor.close();
		}

		return list;
	}

//	public List<MessageDownloading> queryDownloadingListByNumber(
//			String receiverNumber) {
//		List<MessageDownloading> list = new ArrayList<MessageDownloading>();
//
//		Cursor cursor = resolver.query(MessageData.MessageDownloading.CONTENT_URI,
//				new String[] { MessageData.MessageDownloading.ID,
//						MessageData.MessageDownloading.PATH,
//						MessageData.MessageDownloading.URL },
//				MessageData.MessageDownloading.RECEIVER_NUMBER + "=?",
//				new String[] { receiverNumber }, null);
//
//		if (cursor != null) {
//			if (cursor.moveToFirst()) {
//				int indexOfId = cursor
//						.getColumnIndex(MessageData.MessageDownloading.ID);
//				int indexOfPath = cursor
//						.getColumnIndex(MessageData.MessageDownloading.PATH);
//				int indexOfUrl = cursor
//						.getColumnIndex(MessageData.MessageDownloading.URL);
//
//				do {
//					String msgRcvId = cursor.getString(indexOfId);
//					String path = cursor.getString(indexOfPath);
//					String url = cursor.getString(indexOfUrl);
//
//					MessageDownloading messageDownloading = new MessageDownloading(
//							msgRcvId, path, url, receiverNumber);
//
//					list.add(messageDownloading);
//				} while (cursor.moveToNext());
//			}
//
//			cursor.close();
//		}
//
//		return list;
//	}

	public UploadPartBean queryUploading(String file_id) {
		UploadPartBean messageUploading = null;

		Cursor cursor = resolver.query(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.CONTENT_URI,
				new String[] { UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.BLOCK,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.SNDER_NUMBER,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.RCVER_NUMBER,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.DURATION,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.TYPE,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.PATH,
						UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.SENT_ID },
				UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.FILE_ID + "=?", new String[] { file_id },
				null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int indexOfBlock = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.BLOCK);
				int indexOfSenderNumber = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.SNDER_NUMBER);
				int indexOfReceiverNumber = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.RCVER_NUMBER);
				int indexOfPath = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.PATH);
				int indexOfType = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.TYPE);
				int indexOfDuration = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.DURATION);
				int indexOfSentId = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.FileUploadPartTable.SENT_ID);

				int block = cursor.getInt(indexOfBlock);
				String snderNumber = cursor.getString(indexOfSenderNumber);
				String rcverNumber = cursor.getString(indexOfReceiverNumber);
				String path = cursor.getString(indexOfPath);
				String type = cursor.getString(indexOfType);
				int duration = cursor.getInt(indexOfDuration);
				String sentId = cursor.getString(indexOfSentId);

				messageUploading = new UploadPartBean(file_id, block, snderNumber,
						rcverNumber, duration, type, path, sentId);
			}

			cursor.close();
		}
		return messageUploading;
	}

	public UploadCompleteBean queryMessageUploadedByMsgRcdId(String msgRcdId) {
		UploadCompleteBean messageUploaded = null;
		Cursor cursor = resolver.query(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable.CONTENT_URI,
				new String[] { UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable.MESSAGE_SENT_ID },
				UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable.MESSAGE_RECORD_ID + "=?",
				new String[] { msgRcdId }, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int indexOfMsgSntId = cursor
						.getColumnIndex(UploadComplete_UploadPart_FileDetail_SendDetail_DBTableDescribe.MessageUploadedTable.MESSAGE_SENT_ID);

				String msgSntId = cursor.getString(indexOfMsgSntId);

				messageUploaded = new UploadCompleteBean(msgRcdId, msgSntId);
			}

			cursor.close();
		}

		return messageUploaded;
	}
}
