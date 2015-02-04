package com.compoment.video_record_play.db;

import android.net.Uri;
import android.provider.BaseColumns;

public final class VideoBeanDaoTabelDescribe {
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
				VideoBeanDaoTabelDescribe.TABLE_MESSAGE_UPLOADED);
	}

	public static final class MessageRecord implements BaseColumns,
			MessageRecordColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI,
				VideoBeanDaoTabelDescribe.TABLE_MESSAGE_RECORD);
	}

	public static final class MessageReceived implements BaseColumns,
			MessageReceivedColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI,
				VideoBeanDaoTabelDescribe.TABLE_MESSAGE_RECEIVED);
	}

	public static final class MessageSent implements BaseColumns,
			MessageSentColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI,
				VideoBeanDaoTabelDescribe.TABLE_MESSAGE_SENT);
	}

	public static final class MessageDownloading implements BaseColumns,
			MessageDownloadingColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI,
				VideoBeanDaoTabelDescribe.TABLE_MESSAGE_DOWNLOADING);
	}

	public static final class MessageUploading implements BaseColumns,
			MessageUploadingColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI,
				VideoBeanDaoTabelDescribe.TABLE_MESSAGE_UPLOADING);
	}

	protected interface MessageRecordColumns {
		public static final String ID = "rcd_id"; // ¼��id
		public static final String PATH = "path"; // ����ý���ļ�
		public static final String RECORDER_TIME = "rcd_time"; // ¼��ʱ��
		public static final String TYPE = "type"; // ����ý�����ͣ�audio��video
		public static final String FILE_LENGTH = "file_len"; // ����ý���ļ�����
		public static final String DURATION = "duration"; // ����ý�岥�ų��ȣ���λ�����룬��ȷ����
		public static final String PREVIEW_IMAGE_PATH = "prev_img_path"; // ��ƵԤ��ͼ·��
	}

	protected interface MessageDownloadingColumns {
		public static final String ID = "msg_rcv_id"; // ����ID
		public static final String PATH = "path";
		public static final String URL = "url";
		public static final String RECEIVER_NUMBER = "rcver_num";
	}

	protected interface MessageUploadingColumns {
		public static final String RECORD_ID = "rcd_id";
		public static final String BLOCK = "block";// �ѷ��Ϳ������ * buffer��С = �ϵ�
		public static final String SNDER_NUMBER = "snder_num"; // �����ߺ���
		public static final String RCVER_NUMBER = "rcver_num"; // �ռ��˺���
		public static final String DURATION = "duration";
		public static final String TYPE = "type";
		public static final String PATH = "path";
		public static final String SENT_ID = "msg_snt_id"; // ����ID
	}

	protected interface MessageReceivedColumns {
		public static final String ID = "rcv_id"; // ���Խ��ռ�¼id
		public static final String TYPE = "type"; // ����ý�����ͣ�audio��video
		public static final String PATH = "path"; // ����ý���ļ�
		public static final String URL = "url"; // ����ý���ļ�����
		public static final String PREVIEW_IMAGE_PATH = "prev_img_path"; // ������ƵԤ��ͼ
		public static final String PREVIEW_IMAGE_URL = "prev_img_url"; // ������ƵԤ��ͼ����
		public static final String SENDER_NUMBER = "snder_num"; // �����ߺ���
		public static final String RECEIVER_NUMBER = "rcver_num"; // �����ߺ���
		public static final String SENT_TIME = "snd_time"; // ����ʱ���ַ�
		public static final String IS_READED = "is_readed"; // �Ƿ��Ѷ���δ����0���Ѷ���1
		public static final String IS_DOWNLOAD_FINISHED = "is_download_finished"; // �Ƿ���������ɣ�δ��ɣ�0������ɣ�1
		public static final String FILE_LENGTH = "file_len"; // ����ý���ļ�����
		public static final String DURATION = "duration"; // ����ý�岥�ų��ȣ���λ�����룬��ȷ����
	}

	protected interface MessageSentColumns {
		public static final String MESSAGE_SENT_ID = "snt_id"; // ���Է��ͼ�¼id
		public static final String MESSAGE_RECORD_ID = "rcd_id"; // ���Է��ͼ�¼id
		public static final String TIME = "snt_time"; // ����ʱ���ַ�
		public static final String RECEIVER_NUMBER = "rcver_num"; // �����ߺ���
		public static final String SENDER_NUMBER = "snder_num"; // �����ߺ���
		public static final String CODE = "code"; // ����״̬��0���ͳɹ���1����ʧ��
	}

	protected interface MessageUploadedColumns {
		public static final String MESSAGE_RECORD_ID = "msg_rcd_id"; // ����¼��ID�����ֻ����
		public static final String MESSAGE_SENT_ID = "msg_snt_id"; // ���Է���ID���ɷ��������
	}
}