package com.compoment.video_record_play.db;

import java.io.File;
import java.util.Collections;
import java.util.List;

import android.content.Context;



public class VideoBeanProvider {
	private Context context;

	public VideoBeanProvider(Context context) {
		this.context = context;
	}

	public synchronized VideoBean getMessageRecordById(String id) {
		VideoBeanDao dao = new VideoBeanDao(context);

		VideoBean messageRecord = dao.queryRecordById(id);

		return messageRecord;
	}

	/** ����¼����Ϣ */
	public synchronized void addMessageRecord(VideoBean messageRecord) {
		VideoBeanDao dao = new VideoBeanDao(context);

		dao.insert(messageRecord);
	}

	
	
}