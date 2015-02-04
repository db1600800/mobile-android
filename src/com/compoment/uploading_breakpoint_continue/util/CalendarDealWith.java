package com.compoment.uploading_breakpoint_continue.util;

import java.util.Calendar;

public class CalendarDealWith {
	
	private static final String STRING_YESTERDAY = "昨天";
	private static final String STRING_DAY_BEFORE_YESTERDAY = "前天";
	private static final String STRING_LESS_THAN_A_MINUTE = "1分钟内";
	private static final String STRING_MINUTES_AGO = "分钟前";
	private static final String STRING_HOURS_AGO = "小时前";
	private static final String STRING_YEAR = "年";
	private static final String STRING_MONTH = "月";
	private static final String STRING_DAY_OF_MONTH = "日";
	
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
	
	

	/** 格式化时间：1000L -> 00:00:01 */
	public static String formatTime2(long msec) {
		long hour = (msec / 1000) / (60 * 60);
		long minute = (msec / 1000) / 60;
		long second = (msec / 1000) % 60;

		String hourStr = (minute < 10) ? ("0" + hour) : ("" + hour);
		String minStr = (minute < 10) ? ("0" + minute) : ("" + minute);
		String secStr = (second < 10) ? ("0" + second) : ("" + second);

		return (minStr + ":" + secStr);
//		return (hourStr + ":" + minStr + ":" + secStr);
	}
	
	
	/** 格式化时间：n分钟前、n小时前、昨天、前天、具体日期... */
	public static String formatDate(Calendar calendar) {
		Calendar current = Calendar.getInstance();
		long distant = current.getTimeInMillis() - calendar.getTimeInMillis();

		int msecOfMinute = 60 * 1000;
		int msecOfHour = 60 * msecOfMinute;
		int msecOfDay = 24 * msecOfHour;

		if (distant < msecOfMinute) { // 1分钟以内
			return STRING_LESS_THAN_A_MINUTE;
		} else if (distant < msecOfHour) { // 60分钟内
			return ((distant % msecOfHour) / msecOfMinute) + STRING_MINUTES_AGO;
		} else if (distant < msecOfDay) { // 24小时内
			return (distant / msecOfHour) + STRING_HOURS_AGO;
		} else {
			int msecOf2Days = msecOfDay * 2;
			int msecOf3Days = msecOfDay * 3;

			current.set(current.get(Calendar.YEAR), current.get(Calendar.MONTH),
					current.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
			calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
			distant = current.getTimeInMillis() - calendar.getTimeInMillis();

			if (distant < msecOf2Days) { // 昨天
				return STRING_YESTERDAY;
			} else if (distant < msecOf3Days) { // 前天
				return STRING_DAY_BEFORE_YESTERDAY;
			}
		}

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1; // Calendar的month下标从0开始
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

		if (year != current.get(Calendar.YEAR)) { // yyyy年MM月dd日
			return year + STRING_YEAR + month + STRING_MONTH + dayOfMonth
					+ STRING_DAY_OF_MONTH;
		} else { // MM月dd日
			return month + STRING_MONTH + dayOfMonth + STRING_DAY_OF_MONTH;
		}
	}


}
