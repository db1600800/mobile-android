package com.compoment.downloading_breakpoint_continue.db;

import java.io.File;
import java.util.Collections;
import java.util.List;

import android.content.Context;



public class DownloadDetailProvider {
	private Context context;

	public DownloadDetailProvider(Context context) {
		this.context = context;
	}

	public synchronized DownloadDetailBean getMessageRecordById(String url) {
		DownloadDetailDao dao = new DownloadDetailDao(context);

		DownloadDetailBean messageRecord = dao.queryDownloadPartByUrl(url);

		return messageRecord;
	}

	/**  */
	public synchronized void addMessageRecord(DownloadDetailBean messageRecord) {
		DownloadDetailDao dao = new DownloadDetailDao(context);

		dao.insert(messageRecord);
	}

	
	
}