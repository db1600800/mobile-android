package com.compoment.uploading_breakpoint_continue.db;

import java.util.Comparator;

public class FileDetailComparator implements Comparator<FileDetailBean> {
	public int compare(FileDetailBean messageRecord0, FileDetailBean messageRecord1) {
		// 首先比较发送时间，如果发送时间相同，则比较id
		int flag = messageRecord0.getRecordTime().compareTo(
				messageRecord1.getRecordTime());

		if (flag == 0) {
			return messageRecord0.getId().compareTo(messageRecord1.getId());
		} else {
			return (-1) * flag; // 倒序，时间越迟，排序越前
		}
	}
}
