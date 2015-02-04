package com.compoment.uploading_breakpoint_continue.db;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;



public class FileDetailDBService {
	private Context context;

	public FileDetailDBService(Context context) {
		this.context = context;
	}

	// public synchronized List<MessageRecord> getMessageRecordList() {
	// MessageDAO dao = new MessageDAO(context);
	// dao.open();
	// List<MessageRecord> msgRcdList = dao.queryAllRecord();
	// dao.close();
	// // 根据录制时间排序
	// MessageRecordComparator comparator = new MessageRecordComparator();
	// Collections.sort(msgRcdList, comparator);
	// return msgRcdList;
	// }
	//
	// public synchronized MessageRecord getMessageRecordById(String id) {
	// MessageDAO dao = new MessageDAO(context);
	// dao.open();
	// MessageRecord messageRecord = dao.queryRecordById(id);
	// dao.close();
	// return messageRecord;
	// }
	//
	// /** 保存录制信息 */
	// public synchronized void addMessageRecord(MessageRecord messageRecord) {
	// MessageDAO dao = new MessageDAO(context);
	// dao.open();
	// dao.insert(messageRecord);
	// dao.close();
	// }
	//
	// /** 删除录制信息，并删除媒体文件 */
	// public synchronized void deleteMessageRecord(String id) {
	// MessageDAO dao = new MessageDAO(context);
	// dao.open();
	// MessageRecord messageRecord = dao.queryRecordById(id);
	// dao.deleteMessageRecord(id);
	// dao.close();
	// String path = messageRecord.getPath();
	// if (path != null && !"null".equals(path.trim()) && !"".equals(path.trim()))
	// {
	// // 删除媒体文件
	// File file = new File(path);
	// if (file.exists()) {
	// file.delete();
	// }
	// }
	// }

	public synchronized List<FileDetailBean> getMessageRecordList() {
		UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver dao = new UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(context);

		List<FileDetailBean> msgRcdList = dao.queryAllRecord();

		// 根据录制时间排序
		FileDetailComparator comparator = new FileDetailComparator();
		Collections.sort(msgRcdList, comparator);
		return msgRcdList;
	}

	public synchronized FileDetailBean getFileDetailBeanRecordById(String id) {
		UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver dao = new UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(context);

		FileDetailBean messageRecord = dao.queryRecordById(id);

		return messageRecord;
	}

	/** 保存录制信息 */
	public synchronized void addMessageRecord(FileDetailBean messageRecord) {
		UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver dao = new UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(context);

		dao.insert(messageRecord);
	}

	/** 删除录制信息，并删除媒体文件 */
	public synchronized void deleteMessageRecord(String id) {
		UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver dao = new UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(context);

		FileDetailBean messageRecord = dao.queryRecordById(id);
		dao.deleteMessageRecord(id);

		// 删除所有与该记录关联的数据库记录
		dao.deleteMessageSentByRecordId(id);
		dao.deleteMessageUploading(id);
		dao.deleteMessageUploaded(id);

		String path = messageRecord.getPath();
		String previewImagePath = messageRecord.getPreviewImagePath();
		if (path != null && !"null".equals(path.trim()) && !"".equals(path.trim())) {
			// 删除媒体文件
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
		}
		if (previewImagePath != null && !"null".equals(previewImagePath.trim())
				&& !"".equals(previewImagePath.trim())) {
			// 删除预览图
			File file = new File(previewImagePath);
			if (file.exists()) {
				file.delete();
			}
		}
	}

	/** 清空留言收件箱，同时清空所有断点上传、已上传等相关记录 */
	public void clearMessageRecord() {
		UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver dao = new UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(context);

		List<FileDetailBean> records = dao.queryAllRecord();
		dao.clearMessageRecord();
		dao.clearMessageSent();
		dao.clearMessageUploading();
		dao.clearMessageUploaded();

		for (FileDetailBean record : records) {
			String path = record.getPath();
			if (path != null && !"null".equals(path.trim())
					&& !"".equals(path.trim())) {
				// 删除媒体文件
				File file = new File(path);
				if (file.exists()) {
					file.delete();
				}
			}
		}
		
	
}}