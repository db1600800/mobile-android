package com.compoment.uploading_breakpoint_continue.db;

public class UploadCompleteBean {
	private String msgRcdId;//FileDetail  id
	private String msgSntId;//SendDetailBean Id  

	public UploadCompleteBean() {
		super();
	}

	public UploadCompleteBean(String msgRcdId, String msgSntId) {
		super();
		this.msgRcdId = msgRcdId;
		this.msgSntId = msgSntId;
	}

	public String getMsgRcdId() {
		return msgRcdId;
	}

	public void setMsgRcdId(String msgRcdId) {
		this.msgRcdId = msgRcdId;
	}

	public String getMsgSntId() {
		return msgSntId;
	}

	public void setMsgSntId(String msgSntId) {
		this.msgSntId = msgSntId;
	}

	public String toString() {
		return "MessageUploaded [msgRcdId=" + msgRcdId + ", msgSntId=" + msgSntId
				+ "]";
	}
}
