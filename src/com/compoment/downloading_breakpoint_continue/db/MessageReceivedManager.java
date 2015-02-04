package com.compoment.downloading_breakpoint_continue.db;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.compoment.downloading_breakpoint_continue.util.Util;

public class MessageReceivedManager {
	private Context context;

	public MessageReceivedManager(Context context) {
		this.context = context;
	}

	public synchronized List<MessageReceived> getReceivedsByReceiverNumber(
			String receiverNumber) {
		MessageDAO_New dao = new MessageDAO_New(context);

		List<MessageReceived> msgRcvList = dao
				.queryReceivedsByReceiverNumber(receiverNumber);

		// 删除由于并发引起的重复保存的留言记录（msg_rcv_id相同的多条记录）
		HashMap<String, MessageReceived> redundantMessages = getRedundantMessageReceived(msgRcvList);

		if (redundantMessages.size() != 0) {
			for (String id : redundantMessages.keySet()) {
				dao.deleteMessageReceived(id); // 删除多条
				dao.insert(redundantMessages.get(id)); // 插入一条
			}

			msgRcvList = dao.queryReceivedsByReceiverNumber(receiverNumber);
		}

		// 根据发送时间排序
		MessageBoxReceivedItemComparator comparator = new MessageBoxReceivedItemComparator();
		Collections.sort(msgRcvList, comparator);
		return msgRcvList;
	}

	// 检查冗余的留言记录
	private HashMap<String, MessageReceived> getRedundantMessageReceived(
			List<MessageReceived> msgRcvList) {
		HashMap<String, MessageReceived> redundantMessages = new HashMap<String, MessageReceived>(); // <msg_rcg_id,MessageReceived>

		for (MessageReceived messageReceved : msgRcvList) {
			String id = messageReceved.getId();

			if (id != null) {
				int count = 0;
				for (MessageReceived item : msgRcvList) {
					if (id.equals(item.getId())) {
						count++;

						if (count > 1) { // 留言冗余了
							redundantMessages.put(id, messageReceved);
							break;
						}
					}
				}
			}
		}

		return redundantMessages;
	}

	public synchronized MessageReceived getMessageReceivedById(String id) {
		MessageDAO_New dao = new MessageDAO_New(context);

		MessageReceived messageReceived = dao.queryReceivedById(id);

		return messageReceived;
	}

	public synchronized String getMessageReceivedPreviewImagePathById(String id) {
		MessageDAO_New dao = new MessageDAO_New(context);

		String previewImagePath = dao.queryReceivedPreviewImagePathById(id);

		return previewImagePath;
	}

	/** 获取未读留言数量 */
	public int getMessageIsNotReadedCount(String receiverNumber) {
		MessageDAO_New dao = new MessageDAO_New(context);

		int count = dao.getCountOfMessageNotReaded(receiverNumber);

		return count;
	}

	/** 保存下载留言信息到msg_rcv.xml */
	public synchronized void addMessageReceived(MessageReceived msg) {
		// 保存到本地
		MessageDAO_New dao = new MessageDAO_New(context);

		dao.insert(msg);

		// 通知服务器，已读，下次不再下载
		MessageReceivedUpdate update = new MessageReceivedUpdate();
		update.updateMessageIsReaded(msg.getId());
	}

	/** 删除msg_rcv.xml的留言信息，并删除媒体文件 */
	public synchronized void deleteMessageReceived(String rcv_id) {
		MessageDAO_New dao = new MessageDAO_New(context);

		MessageReceived msg = dao.queryReceivedById(rcv_id);
		dao.deleteMessageReceived(rcv_id);

		// 删除所有与该记录关联的数据库记录
		dao.deleteMessageDownloading(rcv_id);

		String path = msg.getPath();
		String previewImagePath = msg.getPreviewImagePath();

		if (path != null && !"null".equals(path.trim()) && !"".equals(path.trim())) {
			// 删除媒体文件
			File file = new File(msg.getPath());
			if (file.exists()) {
				file.delete();
			}
		}

		if (previewImagePath != null && !"null".equals(previewImagePath.trim())
				&& !"".equals(previewImagePath.trim())) {
			// 删除预览图
			File file = new File(msg.getPreviewImagePath());
			if (file.exists()) {
				file.delete();
			}
		}
		//
		// if (!msg.isReaded()) {
		// // 更新网络
		// MessageReceivedUpdate update = new MessageReceivedUpdate();
		// update.updateMessageIsReaded(msg.getId());
		// }
	}

	/** 将留言设置为已读 */
	public synchronized void updateMessageReceivedIsReaded(String id,
			boolean isReaded) {
		// 更新本地
		MessageDAO_New dao = new MessageDAO_New(context);

		MessageReceived msg = dao.queryReceivedById(id);
		if (msg != null) {
			msg.setReaded(isReaded);
			dao.update(msg);
		}

		// 通知服务器，已读，下次不再下载
		MessageReceivedUpdate update = new MessageReceivedUpdate();
		update.updateMessageIsReaded(id);
	}

	/** 设置留言媒体本地路径 */
	public synchronized void updateMessageReceivedPath(String id, String path) {
		MessageDAO_New dao = new MessageDAO_New(context);

		MessageReceived msg = dao.queryReceivedById(id);
		msg.setPath(path);
		dao.update(msg);
	}

	/** 设置视频预览图本地路径 */
	public synchronized void updateMessageReceivedPreviewImagePath(String id,
			String previewImagePath) {
		MessageDAO_New dao = new MessageDAO_New(context);

		MessageReceived msg = dao.queryReceivedById(id);
		msg.setPreviewImagePath(previewImagePath);
		dao.update(msg);
	}

	/** 设置媒体文件是否完成下载 */
	public synchronized void updateMessageReceivedDownloadFinished(String id,
			boolean isDownloadFinished) {
		MessageDAO_New dao = new MessageDAO_New(context);

		MessageReceived msg = dao.queryReceivedById(id);
		msg.setDownloadFinished(isDownloadFinished);
		dao.update(msg);
	}

	/** 清空留言收件箱，同时清空所有下载记录 */
	public void clearMessageReceived() {
		MessageDAO_New dao = new MessageDAO_New(context);

		List<MessageReceived> receiveds = dao
				.queryReceivedsByReceiverNumber(Util.getMyPhoneNumber());
		dao.clearMessageDownloading();
		dao.clearMessageReceived();

		// 删除媒体文件
		for (MessageReceived received : receiveds) {
			String path = received.getPath();

			if (path != null && !"null".equals(path.trim())
					&& !"".equals(path.trim())) {
				File file = new File(path);
				if (file.exists()) {
					file.delete();
				}
			}
		}
	}

	public ArrayList<String> getMessageReceivedNotReadedIds() {
		MessageDAO_New dao = new MessageDAO_New(context);

		ArrayList<String> ids = dao.queryMessageReceivedNotReadedIds(Util
				.getMyPhoneNumber());

		return ids;
	}
}
