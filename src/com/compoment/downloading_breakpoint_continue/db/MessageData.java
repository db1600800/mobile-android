package com.compoment.downloading_breakpoint_continue.db;

import android.net.Uri;
import android.provider.BaseColumns;

public final class MessageData {
	public static final String AUTHORITY = "com.gmcc.yueshi.provider.message_provider";
	public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

	public static final String TABLE_MESSAGE_RECORD = "message_record";
	public static final String TABLE_MESSAGE_RECEIVED = "message_received";
	public static final String TABLE_MESSAGE_SENT = "message_sent";
	public static final String TABLE_MESSAGE_UPLOADING = "message_uploading";
	public static final String TABLE_MESSAGE_DOWNLOADING = "message_downloading";
	public static final String TABLE_MESSAGE_UPLOADED = "message_uploaded";

	public static final class MessageUploaded implements BaseColumns,
			MessageUploadedColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI,
				MessageData.TABLE_MESSAGE_UPLOADED);
	}

	public static final class MessageRecord implements BaseColumns,
			MessageRecordColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI,
				MessageData.TABLE_MESSAGE_RECORD);
	}

	public static final class MessageReceived implements BaseColumns,
			MessageReceivedColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI,
				MessageData.TABLE_MESSAGE_RECEIVED);
	}

	public static final class MessageSent implements BaseColumns,
			MessageSentColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI,
				MessageData.TABLE_MESSAGE_SENT);
	}

	public static final class MessageDownloading implements BaseColumns,
			MessageDownloadingColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI,
				MessageData.TABLE_MESSAGE_DOWNLOADING);
	}

	public static final class MessageUploading implements BaseColumns,
			MessageUploadingColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI,
				MessageData.TABLE_MESSAGE_UPLOADING);
	}

	protected interface MessageRecordColumns {
		public static final String ID = "rcd_id"; // 录制id
		public static final String PATH = "path"; // 留言媒体文件
		public static final String RECORDER_TIME = "rcd_time"; // 录制时间
		public static final String TYPE = "type"; // 留言媒体类型：audio或video
		public static final String FILE_LENGTH = "file_len"; // 留言媒体文件长度
		public static final String DURATION = "duration"; // 留言媒体播放长度，单位：毫秒，精确到秒
		public static final String PREVIEW_IMAGE_PATH = "prev_img_path"; // 视频预览图路径
	}

	protected interface MessageDownloadingColumns {
		public static final String ID = "msg_rcv_id"; // 留言ID
		public static final String PATH = "path";
		public static final String URL = "url";
		public static final String RECEIVER_NUMBER = "rcver_num";
	}

	protected interface MessageUploadingColumns {
		public static final String RECORD_ID = "rcd_id";
		public static final String BLOCK = "block";// 已发送块数。块数 * buffer大小 = 断点
		public static final String SNDER_NUMBER = "snder_num"; // 发送者号码
		public static final String RCVER_NUMBER = "rcver_num"; // 收件人号码
		public static final String DURATION = "duration";
		public static final String TYPE = "type";
		public static final String PATH = "path";
		public static final String SENT_ID = "msg_snt_id"; // 发送ID
	}

	protected interface MessageReceivedColumns {
		public static final String ID = "rcv_id"; // 留言接收记录id
		public static final String TYPE = "type"; // 留言媒体类型：audio或video
		public static final String PATH = "path"; // 留言媒体文件
		public static final String URL = "url"; // 留言媒体文件链接
		public static final String PREVIEW_IMAGE_PATH = "prev_img_path"; // 留言视频预览图
		public static final String PREVIEW_IMAGE_URL = "prev_img_url"; // 留言视频预览图链接
		public static final String SENDER_NUMBER = "snder_num"; // 发送者号码
		public static final String RECEIVER_NUMBER = "rcver_num"; // 接收者号码
		public static final String SENT_TIME = "snd_time"; // 发送时间字符串
		public static final String IS_READED = "is_readed"; // 是否已读，未读：0，已读：1
		public static final String IS_DOWNLOAD_FINISHED = "is_download_finished"; // 是否已下载完成，未完成：0，已完成：1
		public static final String FILE_LENGTH = "file_len"; // 留言媒体文件长度
		public static final String DURATION = "duration"; // 留言媒体播放长度，单位：毫秒，精确到秒
	}

	protected interface MessageSentColumns {
		public static final String MESSAGE_SENT_ID = "snt_id"; // 留言发送记录id
		public static final String MESSAGE_RECORD_ID = "rcd_id"; // 留言发送记录id
		public static final String TIME = "snt_time"; // 发送时间字符串
		public static final String RECEIVER_NUMBER = "rcver_num"; // 接收者号码
		public static final String SENDER_NUMBER = "snder_num"; // 发送者号码
		public static final String CODE = "code"; // 发送状态：0发送成功，1发送失败
	}

	protected interface MessageUploadedColumns {
		public static final String MESSAGE_RECORD_ID = "msg_rcd_id"; // 留言录制ID，由手机生成
		public static final String MESSAGE_SENT_ID = "msg_snt_id"; // 留言发送ID，由服务器生成
	}
}