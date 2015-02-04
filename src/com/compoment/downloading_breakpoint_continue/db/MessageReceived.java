package com.compoment.downloading_breakpoint_continue.db;

import java.util.Calendar;

import android.graphics.Bitmap;
import android.util.Log;

public class MessageReceived {
	private String id;
	private String name; // 联系人姓名，从手机读取
	private Bitmap photo; // 联系人头像，从手机读取
	private String type; // 留言类别：audio 或 video
	private String url; // 留言媒体链接
	private String path; // 留言媒体文件路径（本地）
	private String previewImageUrl; // 视频预览图链接
	private String previewImagePath; // 视频预览图路径
	private String senderPhoneNumber;
	private String receiverPhoneNumber;
	private int duration; // 留言媒体时间长度，单位：毫秒
	private long fileLength; // 留言媒体文件大小，单位：byte
	private boolean isReaded;
	private Calendar sendTime; // 留言日期
	private boolean isDownloadFinished; // 已完整下载
	 private int complete;//完成度

	public MessageReceived() {
		super();
	}

	public MessageReceived(String id, String name, Bitmap photo, String type,
			String url, String path, String previewImageUrl, String previewImagePath,
			String senderPhoneNumber, String receiverPhoneNumber, int duration,
			long fileLength, boolean isReaded, Calendar sendTime,
			boolean isDownloadFinished) {
		super();
		this.id = id;
		this.name = name;
		this.photo = photo;
		this.type = type;
		this.url = url;
		this.path = path;
		this.previewImageUrl = previewImageUrl;
		this.previewImagePath = previewImagePath;
		this.senderPhoneNumber = senderPhoneNumber;
		this.receiverPhoneNumber = receiverPhoneNumber;
		this.duration = duration;
		this.fileLength = fileLength;
		this.isReaded = isReaded;
		this.sendTime = sendTime;
		this.isDownloadFinished = isDownloadFinished;
	}

	public String getReceiverPhoneNumber() {
		return receiverPhoneNumber;
	}

	public void setReceiverPhoneNumber(String receiverPhoneNumber) {
		this.receiverPhoneNumber = receiverPhoneNumber;
	}

	public boolean isDownloadFinished() {
		return isDownloadFinished;
	}

	public void setDownloadFinished(boolean isDownloadFinished) {
		this.isDownloadFinished = isDownloadFinished;
	}

	public String getPreviewImagePath() {
		return previewImagePath;
	}

	public void setPreviewImagePath(String previewImagePath) {
		this.previewImagePath = previewImagePath;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Bitmap getPhoto() {
		return photo;
	}

	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPreviewImageUrl() {
		return previewImageUrl;
	}

	public void setPreviewImageUrl(String previewImageUrl) {
		this.previewImageUrl = previewImageUrl;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public boolean isReaded() {
		return isReaded;
	}

	public void setReaded(boolean isReaded) {
		this.isReaded = isReaded;
	}

	public Calendar getSendTime() {
		return sendTime;
	}

	public void setSendTime(Calendar sendTime) {
		this.sendTime = sendTime;
	}

	public String getSenderPhoneNumber() {
		return senderPhoneNumber;
	}

	public void setSenderPhoneNumber(String senderPhoneNumber) {
		this.senderPhoneNumber = senderPhoneNumber;
	}

	public long getFileLength() {
		return fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String toString() {
		return "MessageReceived [id=" + id + ", name=" + name + ", photo=" + photo
				+ ", type=" + type + ", url=" + url + ", path=" + path
				+ ", previewImageUrl=" + previewImageUrl + ", previewImagePath="
				+ previewImagePath + ", senderPhoneNumber=" + senderPhoneNumber
				+ ", duration=" + duration + ", fileLength=" + fileLength
				+ ", isReaded=" + isReaded + ", sendTime=" + formatCalendar(sendTime)
				+ ", isDownloadFinished=" + isDownloadFinished + "]";
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