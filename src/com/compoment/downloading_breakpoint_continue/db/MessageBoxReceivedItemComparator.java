package com.compoment.downloading_breakpoint_continue.db;

import java.util.Comparator;

import com.compoment.downloading_breakpoint_continue.util.CalendarDealWith;


public class MessageBoxReceivedItemComparator implements
		Comparator<MessageReceived> {
	public int compare(MessageReceived messageReceived0,
			MessageReceived messageReceived1) {
		// 首先比较发送时间，如果发送时间相同，则比较id
		String time0 = CalendarDealWith.formatCalendar(messageReceived0.getSendTime());
		String time1 = CalendarDealWith.formatCalendar(messageReceived1.getSendTime());

		int flag = time0.compareTo(time1);
		if (flag == 0) {
			return messageReceived0.getId().compareTo(messageReceived1.getId());
		} else {
			return (-1) * flag; // 倒序，时间越迟，排序越前
		}
	}
}
