package com.compoment.downloading_breakpoint_continue.db;

import java.util.ArrayList;
import java.util.List;

import com.compoment.downloading_breakpoint_continue.util.CalendarDealWith;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;



public class MessageDAO_New {
	private ContentResolver resolver;

	public static final String FALSE = "0";
	public static final String TRUE = "1";

	public MessageDAO_New(Context context) {
		this.resolver = context.getContentResolver();
	}

	

	public void insert(MessageReceived messageReceived) {
		ContentValues values = new ContentValues();

		values.put(MessageData.MessageReceived.ID, messageReceived.getId());
		values.put(MessageData.MessageReceived.DURATION,
				messageReceived.getDuration());
		values.put(MessageData.MessageReceived.FILE_LENGTH,
				messageReceived.getFileLength());
		values.put(MessageData.MessageReceived.IS_DOWNLOAD_FINISHED,
				messageReceived.isDownloadFinished() ? TRUE : FALSE);
		values.put(MessageData.MessageReceived.IS_READED,
				messageReceived.isReaded() ? TRUE : FALSE);
		values.put(MessageData.MessageReceived.PATH, messageReceived.getPath());
		values.put(MessageData.MessageReceived.PREVIEW_IMAGE_PATH,
				messageReceived.getPreviewImagePath());
		values.put(MessageData.MessageReceived.PREVIEW_IMAGE_URL,
				messageReceived.getPreviewImageUrl());
		values.put(MessageData.MessageReceived.RECEIVER_NUMBER,
				messageReceived.getReceiverPhoneNumber());
		values.put(MessageData.MessageReceived.SENDER_NUMBER,
				messageReceived.getSenderPhoneNumber());
		values.put(MessageData.MessageReceived.SENT_TIME,
				CalendarDealWith.formatCalendar(messageReceived.getSendTime()));
		values.put(MessageData.MessageReceived.TYPE, messageReceived.getType());
		values.put(MessageData.MessageReceived.URL, messageReceived.getUrl());

		resolver.insert(MessageData.MessageReceived.CONTENT_URI, values);
	}

	






	public int update(MessageReceived messageReceived) {
		ContentValues values = new ContentValues();

		values.put(MessageData.MessageReceived.ID, messageReceived.getId());
		values.put(MessageData.MessageReceived.DURATION,
				messageReceived.getDuration());
		values.put(MessageData.MessageReceived.FILE_LENGTH,
				messageReceived.getFileLength());
		values.put(MessageData.MessageReceived.IS_DOWNLOAD_FINISHED,
				messageReceived.isDownloadFinished() ? TRUE : FALSE);
		values.put(MessageData.MessageReceived.IS_READED,
				messageReceived.isReaded() ? TRUE : FALSE);
		values.put(MessageData.MessageReceived.PATH, messageReceived.getPath());
		values.put(MessageData.MessageReceived.PREVIEW_IMAGE_PATH,
				messageReceived.getPreviewImagePath());
		values.put(MessageData.MessageReceived.PREVIEW_IMAGE_URL,
				messageReceived.getPreviewImageUrl());
		values.put(MessageData.MessageReceived.RECEIVER_NUMBER,
				messageReceived.getReceiverPhoneNumber());
		values.put(MessageData.MessageReceived.SENDER_NUMBER,
				messageReceived.getSenderPhoneNumber());
		values.put(MessageData.MessageReceived.SENT_TIME,
				CalendarDealWith.formatCalendar(messageReceived.getSendTime()));
		values.put(MessageData.MessageReceived.TYPE, messageReceived.getType());
		values.put(MessageData.MessageReceived.URL, messageReceived.getUrl());

		int count = resolver.update(MessageData.MessageReceived.CONTENT_URI,
				values, MessageData.MessageReceived.ID + "=?",
				new String[] { messageReceived.getId() });

		return count;
	}

	
	

	




	public int deleteMessageReceived(String rcv_id) {
		int count = resolver.delete(MessageData.MessageReceived.CONTENT_URI,
				MessageData.MessageReceived.ID + "=?", new String[] { rcv_id });

		return count;
	}

	public int deleteMessageDownloading(String rcv_id) {
		int count = resolver.delete(MessageData.MessageDownloading.CONTENT_URI,
				MessageData.MessageDownloading.ID + "=?", new String[] { rcv_id });

		return count;
	}






	public int clearMessageReceived() {
		int count = resolver.delete(MessageData.MessageReceived.CONTENT_URI, null,
				null);

		return count;
	}

	public int clearMessageDownloading() {
		int count = resolver.delete(MessageData.MessageDownloading.CONTENT_URI,
				null, null);

		return count;
	}

	



	public int getCountOfMessageNotReaded(String receiverNumber) {
		Cursor cursor = resolver.query(MessageData.MessageReceived.CONTENT_URI,
				new String[] { MessageData.MessageReceived.IS_READED },
				MessageData.MessageReceived.RECEIVER_NUMBER + "=?",
				new String[] { receiverNumber }, null);
		// TODO
		int count = 0;
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int indexOfIsReaded = cursor
						.getColumnIndex(MessageData.MessageReceived.IS_READED);
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



	



	public List<MessageReceived> queryReceivedsByReceiverNumber(
			String receiverNumber) {
		List<MessageReceived> list = new ArrayList<MessageReceived>();

		Cursor cursor = resolver.query(MessageData.MessageReceived.CONTENT_URI,
				new String[] { MessageData.MessageReceived.ID,
						MessageData.MessageReceived.TYPE, MessageData.MessageReceived.PATH,
						MessageData.MessageReceived.URL,
						MessageData.MessageReceived.PREVIEW_IMAGE_PATH,
						MessageData.MessageReceived.PREVIEW_IMAGE_URL,
						MessageData.MessageReceived.SENDER_NUMBER,
						MessageData.MessageReceived.SENT_TIME,
						MessageData.MessageReceived.IS_READED,
						MessageData.MessageReceived.IS_DOWNLOAD_FINISHED,
						MessageData.MessageReceived.FILE_LENGTH,
						MessageData.MessageReceived.DURATION },
				MessageData.MessageReceived.RECEIVER_NUMBER + "=?",
				new String[] { receiverNumber }, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {

				int indexOfReceivedId = cursor
						.getColumnIndex(MessageData.MessageReceived.ID);
				int indexOfType = cursor
						.getColumnIndex(MessageData.MessageReceived.TYPE);
				int indexOfPath = cursor
						.getColumnIndex(MessageData.MessageReceived.PATH);
				int indexOfUrl = cursor.getColumnIndex(MessageData.MessageReceived.URL);
				int indexOfPreviewImagePath = cursor
						.getColumnIndex(MessageData.MessageReceived.PREVIEW_IMAGE_PATH);
				int indexOfPreviewImageUrl = cursor
						.getColumnIndex(MessageData.MessageReceived.PREVIEW_IMAGE_URL);
				int indexOfSenderNumber = cursor
						.getColumnIndex(MessageData.MessageReceived.SENDER_NUMBER);
				int indexOfSentTime = cursor
						.getColumnIndex(MessageData.MessageReceived.SENT_TIME);
				int indexOfIsReaded = cursor
						.getColumnIndex(MessageData.MessageReceived.IS_READED);
				int indexOfIsDownloadFinished = cursor
						.getColumnIndex(MessageData.MessageReceived.IS_DOWNLOAD_FINISHED);
				int indexOfFileLength = cursor
						.getColumnIndex(MessageData.MessageReceived.FILE_LENGTH);
				int indexOfDuration = cursor
						.getColumnIndex(MessageData.MessageReceived.DURATION);

				do {
					String receiverId = cursor.getString(indexOfReceivedId);
					String type = cursor.getString(indexOfType);
					String path = cursor.getString(indexOfPath);
					String url = cursor.getString(indexOfUrl);
					String previewImagePath = cursor.getString(indexOfPreviewImagePath);
					String previewImageUrl = cursor.getString(indexOfPreviewImageUrl);
					String senderNumber = cursor.getString(indexOfSenderNumber);
					String sentTime = cursor.getString(indexOfSentTime);
					String isReadedStr = cursor.getString(indexOfIsReaded);
					String isDownloadFinishedStr = cursor
							.getString(indexOfIsDownloadFinished);
					long fileLength = cursor.getLong(indexOfFileLength);
					int duration = cursor.getInt(indexOfDuration);

					boolean isReaded = (TRUE.equals(isReadedStr)) ? (true) : (false);
					boolean isDownloadFinished = (TRUE.equals(isDownloadFinishedStr)) ? (true)
							: (false);

					MessageReceived messageReceived = new MessageReceived(receiverId,
							null, null, type, url, path, previewImageUrl, previewImagePath,
							senderNumber, receiverNumber, duration, fileLength, isReaded,
							CalendarDealWith.parseStringToCalendar(sentTime), isDownloadFinished);
					list.add(messageReceived);
				} while (cursor.moveToNext());
			}

			cursor.close();
		}
		return list;
	}

	public MessageReceived queryReceivedById(String rcv_id) {
		MessageReceived messageReceived = null;

		Cursor cursor = resolver.query(MessageData.MessageReceived.CONTENT_URI,
				new String[] { MessageData.MessageReceived.TYPE,
						MessageData.MessageReceived.PATH, MessageData.MessageReceived.URL,
						MessageData.MessageReceived.PREVIEW_IMAGE_PATH,
						MessageData.MessageReceived.PREVIEW_IMAGE_URL,
						MessageData.MessageReceived.SENDER_NUMBER,
						MessageData.MessageReceived.RECEIVER_NUMBER,
						MessageData.MessageReceived.SENT_TIME,
						MessageData.MessageReceived.IS_READED,
						MessageData.MessageReceived.IS_DOWNLOAD_FINISHED,
						MessageData.MessageReceived.FILE_LENGTH,
						MessageData.MessageReceived.DURATION },
				MessageData.MessageReceived.ID + "=?", new String[] { rcv_id }, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int indexOfType = cursor
						.getColumnIndex(MessageData.MessageReceived.TYPE);
				int indexOfPath = cursor
						.getColumnIndex(MessageData.MessageReceived.PATH);
				int indexOfUrl = cursor.getColumnIndex(MessageData.MessageReceived.URL);
				int indexOfPreviewImagePath = cursor
						.getColumnIndex(MessageData.MessageReceived.PREVIEW_IMAGE_PATH);
				int indexOfPreviewImageUrl = cursor
						.getColumnIndex(MessageData.MessageReceived.PREVIEW_IMAGE_URL);
				int indexOfSenderNumber = cursor
						.getColumnIndex(MessageData.MessageReceived.SENDER_NUMBER);
				int indexOfReceiverNumber = cursor
						.getColumnIndex(MessageData.MessageReceived.RECEIVER_NUMBER);
				int indexOfSentTime = cursor
						.getColumnIndex(MessageData.MessageReceived.SENT_TIME);
				int indexOfIsReaded = cursor
						.getColumnIndex(MessageData.MessageReceived.IS_READED);
				int indexOfIsDownloadFinished = cursor
						.getColumnIndex(MessageData.MessageReceived.IS_DOWNLOAD_FINISHED);
				int indexOfFileLength = cursor
						.getColumnIndex(MessageData.MessageReceived.FILE_LENGTH);
				int indexOfDuration = cursor
						.getColumnIndex(MessageData.MessageReceived.DURATION);

				String type = cursor.getString(indexOfType);
				String path = cursor.getString(indexOfPath);
				String url = cursor.getString(indexOfUrl);
				String previewImagePath = cursor.getString(indexOfPreviewImagePath);
				String previewImageUrl = cursor.getString(indexOfPreviewImageUrl);
				String receiverNumber = cursor.getString(indexOfReceiverNumber);
				String senderNumber = cursor.getString(indexOfSenderNumber);
				String sentTime = cursor.getString(indexOfSentTime);
				String isReadedStr = cursor.getString(indexOfIsReaded);
				String isDownloadFinishedStr = cursor
						.getString(indexOfIsDownloadFinished);
				long fileLength = cursor.getLong(indexOfFileLength);
				int duration = cursor.getInt(indexOfDuration);

				boolean isReaded = (TRUE.equals(isReadedStr)) ? (true) : (false);
				boolean isDownloadFinished = (TRUE.equals(isDownloadFinishedStr)) ? (true)
						: (false);

				messageReceived = new MessageReceived(rcv_id, null, null, type, url,
						path, previewImageUrl, previewImagePath, senderNumber,
						receiverNumber, duration, fileLength, isReaded,
						CalendarDealWith.parseStringToCalendar(sentTime), isDownloadFinished);
			}

			cursor.close();
		}
		return messageReceived;
	}

	public ArrayList<String> queryMessageReceivedNotReadedIds(
			String receiverNumber) {
		ArrayList<String> ids = new ArrayList<String>();

		Cursor cursor = resolver.query(MessageData.MessageReceived.CONTENT_URI,
				new String[] { MessageData.MessageReceived.ID,
						MessageData.MessageReceived.IS_READED },
				MessageData.MessageReceived.RECEIVER_NUMBER + "=?",
				new String[] { receiverNumber }, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int indexOfId = cursor.getColumnIndex(MessageData.MessageReceived.ID);
				int indexOfReaded = cursor
						.getColumnIndex(MessageData.MessageReceived.IS_READED);

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

		Cursor cursor = resolver.query(MessageData.MessageReceived.CONTENT_URI,
				new String[] { MessageData.MessageReceived.PREVIEW_IMAGE_PATH },
				MessageData.MessageReceived.ID + "=?", new String[] { rcv_id }, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				int indexOfPreviewImagePath = cursor
						.getColumnIndex(MessageData.MessageReceived.PREVIEW_IMAGE_PATH);

				previewImagePath = cursor.getString(indexOfPreviewImagePath);
			}

			cursor.close();
		}

		return previewImagePath;
	}

	

	

	

}
