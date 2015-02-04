package com.compoment.uploading_breakpoint_continue.db;

public class UploadPartBean {
	private String file_id;
	private int block; // 已上传块数 已发送块数。块数 * buffer大小 = 断点
	private String snderNumber;
	private String rcverNumber;
	private int duration;
	private String type;
	private String path;
	private String snt_id;

	public UploadPartBean() {
		super();
	}

	public UploadPartBean(String file_id, int block, String snderNumber,
			String rcverNumber, int duration, String type, String path, String snt_id) {
		super();
		this.file_id = file_id;
		this.block = block;
		this.snderNumber = snderNumber;
		this.rcverNumber = rcverNumber;
		this.duration = duration;
		this.type = type;
		this.path = path;
		this.snt_id = snt_id;
	}

	public String getFile_Id() {
		return file_id;
	}

	public void setRcd_id(String rcd_id) {
		this.file_id = rcd_id;
	}

	public int getBlock() {
		return block;
	}

	public void setBlock(int block) {
		this.block = block;
	}

	public String getSnderNumber() {
		return snderNumber;
	}

	public void setSnderNumber(String snderNumber) {
		this.snderNumber = snderNumber;
	}

	public String getRcverNumber() {
		return rcverNumber;
	}

	public void setRcverNumber(String rcverNumber) {
		this.rcverNumber = rcverNumber;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSnt_id() {
		return snt_id;
	}

	public void setSnt_id(String snt_id) {
		this.snt_id = snt_id;
	}

	@Override
	public String toString() {
		return "MessageUploading [rcd_id=" + file_id + ", block=" + block
				+ ", snderNumber=" + snderNumber + ", rcverNumber=" + rcverNumber
				+ ", duration=" + duration + ", type=" + type + ", path=" + path
				+ ", snt_id=" + snt_id + "]";
	}

}
