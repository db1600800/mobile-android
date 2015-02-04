package com.compoment.uploading_breakpoint_continue.db;

import java.util.Calendar;

import android.graphics.Bitmap;

/**
 * 
 * 文件详情 
 * 
 * 文件是什么类型的   文件有多大  文件创建时间   文件图标   如是媒体文件还有录音或视频时长   
 *
 */

public class FileDetailBean {
	private String id;
	private String path; // 留言媒体文件路径
	private String previewImagePath;
	private String type; // 留言类别：audio 或 video
	private Calendar recordTime;
	private int duration; // 留言媒体时间长度，单位：毫秒，精确到秒
	private long fileLength;
	private Bitmap photo; // 联系人头像，从手机读取

	public FileDetailBean() {
		super();
	}

	public FileDetailBean(String id, String path, String previewImagePath,
			String type, Calendar recordTime, int duration, long fileLength,
			Bitmap photo) {
		super();
		this.id = id;
		this.path = path;
		this.previewImagePath = previewImagePath;
		this.type = type;
		this.recordTime = recordTime;
		this.duration = duration;
		this.fileLength = fileLength;
		this.photo = photo;
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

	public String getPreviewImagePath() {
		return previewImagePath;
	}

	public void setPreviewImagePath(String previewImagePath) {
		this.previewImagePath = previewImagePath;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Calendar getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Calendar recordTime) {
		this.recordTime = recordTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public long getFileLength() {
		return fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public Bitmap getPhoto() {
		return photo;
	}

	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

	public String toString() {
		return "MessageRecord [id=" + id + ", path=" + path + ", previewImagePath="
				+ previewImagePath + ", type=" + type + ", recordTime="
				+ formatCalendar(recordTime) + ", duration=" + duration
				+ ", fileLength=" + fileLength + ", photo=" + photo + "]";
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
}
