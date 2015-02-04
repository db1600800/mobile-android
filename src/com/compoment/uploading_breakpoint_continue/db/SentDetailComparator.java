package com.compoment.uploading_breakpoint_continue.db;

import java.util.Comparator;

import com.compoment.uploading_breakpoint_continue.util.CalendarDealWith;


public class SentDetailComparator implements Comparator<SentDetailBean> {
	public int compare(SentDetailBean messageSent0, SentDetailBean messageSent1) {
		// 首先比较发送时间，如果发送时间相同，则比较id
		// 首先比较发送时间，如果发送时间相同，则比较id
		String time0 = CalendarDealWith.formatCalendar(messageSent0.getSendTime());
		String time1 = CalendarDealWith.formatCalendar(messageSent1.getSendTime());

		int flag = time0.compareTo(time1);

		if (flag == 0) {
			return messageSent0.getId().compareTo(messageSent1.getId());
		} else {
			return (-1) * flag; // 倒序，时间越迟，排序越前
		}
	}
}