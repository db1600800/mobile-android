package com.compoment.uploading_breakpoint_continue.db;

import android.content.Context;

/**
 *
 * （用数据库记录信息）
 *
 * 部分上传到服务器的（需断点续传的）
 *
 */

public class UploadPartDBService {
	public static final String TAG = "MessageUploadingManager";

	private Context context;

	public UploadPartDBService(Context context) {
		this.context = context;
	}



	public synchronized void add(UploadPartBean messageUploading) {
		UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver dao = new UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(context);
		dao.insert(messageUploading);
	}

	public synchronized void delete(String rcd_id) {
		UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver dao = new UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(context);
		dao.deleteMessageUploading(rcd_id);
	}

	public synchronized void update(UploadPartBean messageUploading) {
		UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver dao = new UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(context);
		dao.update(messageUploading);
	}

	public synchronized UploadPartBean queryUploadingById(String file_id) {
		UploadPartBean messageUploading = null;

		UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver dao = new UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(context);
		messageUploading = dao.queryUploading(file_id); // 如果不存在，返回null

		return messageUploading;
	}
}