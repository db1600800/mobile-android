package com.compoment.uploading_breakpoint_continue.db;

import java.util.Calendar;

public class SentDetailBean {
	private String id="";
	private String name=""; // 收件人姓名，从手机读取
	private String receiverPhoneNumber="";
	private String senderPhoneNumber="";
	private String code=""; // 发送状态：0发送成功，1保存在草稿，2发送失败
	private Calendar sendTime;// 留言日期
	private FileDetailBean messageRecord; // 留言文件

	public SentDetailBean() {
		super();
	}

	public SentDetailBean(String id, String name, String receiverPhoneNumber,
			String senderPhoneNumber, String code, Calendar sendTime,
			FileDetailBean messageRecord) {
		super();
		this.id = id;
		this.name = name;
		this.receiverPhoneNumber = receiverPhoneNumber;
		this.senderPhoneNumber = senderPhoneNumber;
		this.code = code;
		this.sendTime = sendTime;
		this.messageRecord = messageRecord;
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

	public String getReceiverPhoneNumber() {
		return receiverPhoneNumber;
	}

	public void setReceiverPhoneNumber(String receiverPhoneNumber) {
		this.receiverPhoneNumber = receiverPhoneNumber;
	}

	public String getSenderPhoneNumber() {
		return senderPhoneNumber;
	}

	public void setSenderPhoneNumber(String senderPhoneNumber) {
		this.senderPhoneNumber = senderPhoneNumber;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Calendar getSendTime() {
		return sendTime;
	}

	public void setSendTime(Calendar sendTime) {
		this.sendTime = sendTime;
	}

	public FileDetailBean getFileDetailBean() {
		return messageRecord;

	}

	public void setFileDetailBean(FileDetailBean messageRecord) {
		this.messageRecord = messageRecord;
	}

	public String toString() {
		return "MessageSent [id=" + id + ", name=" + name
				+ ", receiverPhoneNumber=" + receiverPhoneNumber
				+ ", senderPhoneNumber=" + senderPhoneNumber + ", code=" + code
				+ ", sendTime=" + formatCalendar(sendTime) + ", messageRecord="
				+ messageRecord + "]";
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