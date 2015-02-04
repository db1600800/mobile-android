package com.compoment.downloading_breakpoint_continue.db;

import java.util.Calendar;

import android.graphics.Bitmap;

public class DownloadDetailBean {
	private String id;
	private String path;  
	private String start_pos;
	private String end_pos;	
	
	private String url;
	private Calendar time;
	private Bitmap photo;
	private int duration;
	private String compelete_size;

	
	public String getCompelete_size() {
		return compelete_size;
	}

	public void setCompelete_size(String compelete_size) {
		this.compelete_size = compelete_size;
	}


	public Calendar getTime() {
		return time;
	}

	public void setTime(Calendar time) {
		this.time = time;
	}

	public Bitmap getPhoto() {
		return photo;
	}

	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public DownloadDetailBean() {
		super();
	}

	public DownloadDetailBean(String id, String path, String url,String start_pos,String end_pos
			,Calendar time) {
		super();
		this.id = id;
		this.path = path;
		this.url=url;
		this.start_pos=start_pos;
		this.end_pos=end_pos;
		this.time=time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


	
	public String getStart_pos() {
		return start_pos;
	}

	public void setStart_pos(String start_pos) {
		this.start_pos = start_pos;
	}

	public String getEnd_pos() {
		return end_pos;
	}

	public void setEnd_pos(String end_pos) {
		this.end_pos = end_pos;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/** 2011-11-02 09:10:00 */
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
}
