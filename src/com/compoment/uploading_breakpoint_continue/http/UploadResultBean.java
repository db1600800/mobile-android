package com.compoment.uploading_breakpoint_continue.http;

import java.util.Calendar;

import com.compoment.uploading_breakpoint_continue.util.CalendarDealWith;
import com.compoment.uploading_breakpoint_continue.util.Util;



public class UploadResultBean {
	private String id;
	private long fileLength; // 留言媒体文件大小，单位：byte
	private long receivedLength; // 实际上传文件大小，单位：byte
	private Calendar sendDate; // 留言日期
	private String code; // 发送状态：0发送成功，1保存在草稿，2发送失败
	private String error; // 错误信息

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getFileLength() {
		return fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public long getReceivedLength() {
		return receivedLength;
	}

	public void setReceivedLength(long receivedLength) {
		this.receivedLength = receivedLength;
	}

	public Calendar getSendDate() {
		return sendDate;
	}

	public void setSendDate(Calendar sendDate) {
		this.sendDate = sendDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String toString() {
		return "MessageUploadResult [id=" + id + ", fileLength=" + fileLength
				+ ", receivedLength=" + receivedLength + ", sendDate="
				+ CalendarDealWith.formatCalendar(sendDate) + ", code=" + code + ", error="
				+ error + "]";
	}
}
