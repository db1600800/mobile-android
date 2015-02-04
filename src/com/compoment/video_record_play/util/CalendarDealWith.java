package com.compoment.video_record_play.util;

import java.util.Calendar;

public class CalendarDealWith {
	/** 解析字符串日期，格式：2011-11-02 09:10:00 -> Calendar */
	public static Calendar parseStringToCalendar(String str_Calendar) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, Integer.valueOf(str_Calendar.substring(0, 4)));
		calendar.set(Calendar.MONTH,
				(Integer.valueOf(str_Calendar.substring(5, 7)) - 1)); // Calendar的month比实际month少1
		calendar.set(Calendar.DAY_OF_MONTH,
				Integer.valueOf(str_Calendar.substring(8, 10)));
		calendar.set(Calendar.HOUR_OF_DAY,
				Integer.valueOf(str_Calendar.substring(11, 13)));
		calendar.set(Calendar.MINUTE,
				Integer.valueOf(str_Calendar.substring(14, 16)));
		if (str_Calendar.length() > 16) {
			calendar.set(Calendar.SECOND,
					Integer.valueOf(str_Calendar.substring(17, 19)));
		} else {
			calendar.set(Calendar.SECOND, 0);
		}

		return calendar;
	}
	
	
	/** 格式化日期，格式：2011-11-02 09:10:00 */
	public static String formatCalendar(Calendar calendar) {
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		String str = year + "-" + ((month < 10) ? ("0" + month) : (month)) + "-"
				+ ((day < 10) ? ("0" + day) : (day)) + " "
				+ ((hour < 10) ? ("0" + hour) : (hour)) + ":"
				+ ((minute < 10) ? ("0" + minute) : (minute)) + ":"
				+ ((second < 10) ? ("0" + second) : (second));
		return str;
	}
	
	/** 格式化时间：1000L -> 00:01 */
	public static String formatTime1(long msec) {
		long minute = (msec / 1000) / 60;
		long second = (msec / 1000) % 60;

		String minStr = (minute < 10) ? ("0" + minute) : ("" + minute);
		String secStr = (second < 10) ? ("0" + second) : ("" + second);

		return (minStr + ":" + secStr);
	}

}
