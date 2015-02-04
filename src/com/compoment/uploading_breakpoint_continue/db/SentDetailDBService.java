package com.compoment.uploading_breakpoint_continue.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.compoment.uploading_breakpoint_continue.util.Util;

import android.content.Context;
import android.util.Log;


public class SentDetailDBService {
	private Context context;

	public SentDetailDBService(Context context) {
		this.context = context;
	}

	// public synchronized List<MessageSent> getMessageSentList() {
	// MessageDAO dao = new MessageDAO(context);
	// dao.open();
	// List<MessageSent> msgSntList = dao.querySentsBySenderNumber(MessageUtil
	// .getMyPhoneNumber());
	// dao.close();
	//
	// // 根据发送时间排序
	// MessageBoxSentItemComparator comparator = new
	// MessageBoxSentItemComparator();
	// Collections.sort(msgSntList, comparator);
	// return msgSntList;
	// }
	//
	// /** 更新留言发送日期 */
	// public synchronized void updateMessageSendTime(String id, Calendar
	// calendar) {
	// Log.e("MessageSentManager", "updateMessageSendTime(" + id + ", "
	// + MessageUtil.formatCalendar(calendar) + ")");
	// MessageDAO dao = new MessageDAO(context);
	// dao.open();
	// MessageSent messageSent = dao.querySentById(id);
	// messageSent.setSendTime(calendar);
	// dao.update(messageSent);
	// dao.close();
	// }
	//
	// /** 更新留言发送是否成功 */
	// public synchronized void updateMessageSentCode(String id, String code) {
	// Log.e("MessageSentManager", "updateMessageSentCode(" + id + ", " + code
	// + ")");
	// MessageDAO dao = new MessageDAO(context);
	// dao.open();
	// MessageSent messageSent = dao.querySentById(id);
	// messageSent.setCode(code);
	// dao.update(messageSent);
	// dao.close();
	// }
	//
	// public synchronized MessageSent getMessageSentById(String id) {
	// MessageDAO dao = new MessageDAO(context);
	// dao.open();
	// MessageSent messageSent = dao.querySentById(id);
	// dao.close();
	//
	// return messageSent;
	// }
	//
	// public synchronized List<MessageSent> getMessageSentByRecordId(String
	// rcd_id) {
	// List<MessageSent> messageSents = new ArrayList<MessageSent>();
	// try {
	// MessageDAO dao = new MessageDAO(context);
	// dao.open();
	// messageSents = dao.querySentByRecordId(rcd_id);
	// dao.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return messageSents;
	// }
	//
	// /** 保存发送信息到数据库 */
	// public synchronized void addMessageSent(MessageSent msg) {
	// Log.e("MessageSentManager", "addMessageSent(" + msg.toString() + ")");
	// MessageDAO dao = new MessageDAO(context);
	// dao.open();
	// dao.insert(msg);
	// dao.close();
	// }
	//
	// /** 删除数据库的发送信息 */
	// public synchronized void deleteMessageSent(String id) {
	// Log.e("MessageSentManager", "deleteMessageSent(" + id + ")");
	// MessageDAO dao = new MessageDAO(context);
	// dao.open();
	// dao.deleteMessageSent(id);
	// dao.close();
	// }

	public synchronized List<SentDetailBean> getMessageSentList() {
		UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver dao = new UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(context);

		List<SentDetailBean> msgSntList = dao.querySentsBySenderNumber(Util
				.getMyPhoneNumber());

		// 根据发送时间排序
		SentDetailComparator comparator = new SentDetailComparator();
		Collections.sort(msgSntList, comparator);
		return msgSntList;
	}

	/** 更新留言发送日期 */
	public synchronized void updateMessageSendTime(String id, Calendar calendar) {
		UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver dao = new UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(context);

		SentDetailBean messageSent = dao.querySentById(id);
		messageSent.setSendTime(calendar);
		dao.update(messageSent);
	}

	/** 更新留言发送是否成功 */
	public synchronized void updateMessageSentCode(String id, String code) {
		UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver dao = new UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(context);

		SentDetailBean messageSent = dao.querySentById(id);
		messageSent.setCode(code);
		dao.update(messageSent);
	}

	public synchronized SentDetailBean getMessageSentById(String id) {
		UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver dao = new UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(context);

		SentDetailBean messageSent = dao.querySentById(id);

		return messageSent;
	}

	public synchronized List<SentDetailBean> getMessageSentByRecordId(String rcd_id) {
		List<SentDetailBean> messageSents = new ArrayList<SentDetailBean>();
		try {
			UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver dao = new UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(context);

			messageSents = dao.querySentByRecordId(rcd_id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return messageSents;
	}

	/** 保存发送信息到数据库 */
	public synchronized void addMessageSent(SentDetailBean msg) {
		//Log.e("MessageSentManager", "addMessageSent(" + msg.toString() + ")");
		UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver dao = new UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(context);

		dao.insert(msg);
	}

	/** 删除数据库的发送信息 */
	public synchronized void deleteMessageSent(String id) {
		Log.e("MessageSentManager", "deleteMessageSent(" + id + ")");
		UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver dao = new UploadComplete_UploadPart_FileDetail_SendDetail_DBContentResolver(context);

		dao.deleteMessageSent(id);
	}
}