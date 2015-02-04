package com.compoment.uploading_breakpoint_continue.db;

import java.util.List;


import android.content.Context;


/**
 *
 * （用数据库记录信息）
 *
 * 已经完全上传到服务器的（非部分上传，不需断点续传）
 *
 */
public class UploadCompleteDBService {
	private static final String TAG = "UploadedProvider";

	private Context context;

	public UploadCompleteDBService(Context context) {
		this.context = context;
	}

	public synchronized void add(UploadCompleteBean messageUploaded) {
		UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver dao = new UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(context);
		dao.insert(messageUploaded);
	}

	public synchronized void delete(String msgRcdId) {
		UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver dao = new UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(context);

		dao.deleteMessageUploaded(msgRcdId);
	}

	public synchronized void update(UploadCompleteBean messageUploaded) {
		UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver dao = new UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(context);
		dao.update(messageUploaded);
	}

	public synchronized UploadCompleteBean queryMessageUploadedByMsgRcdId(
			String msgRcdId) {
		UploadCompleteBean messageUploaded = null;

		UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver dao = new UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(context);
		messageUploaded = dao.queryMessageUploadedByMsgRcdId(msgRcdId); //
		// �����ڣ�����null

		return messageUploaded;
	}

	public synchronized List<UploadCompleteBean> queryAllMessageUploaded() {

		UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver dao = new UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(context);
		List<UploadCompleteBean> list = dao.queryAllMessageUploaded();

		return list;
	}
}
