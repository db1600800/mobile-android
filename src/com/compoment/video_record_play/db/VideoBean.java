package com.compoment.video_record_play.db;

import java.util.Calendar;

import android.graphics.Bitmap;

public class VideoBean {
	private String id;
	private String path; // ����ý���ļ�·��
	private String previewImagePath;
	private String type; // �������audio �� video
	private Calendar recordTime;
	private int duration; // ����ý��ʱ�䳤�ȣ���λ�����룬��ȷ����
	private long fileLength;
	private Bitmap photo; // ��ϵ��ͷ�񣬴��ֻ��ȡ

	public VideoBean() {
		super();
	}

	public VideoBean(String id, String path, String previewImagePath,
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
	
	/** ��ʽ�����ڣ���ʽ��2011-11-02 09:10:00 */
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
