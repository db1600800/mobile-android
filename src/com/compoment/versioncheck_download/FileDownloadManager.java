package com.compoment.versioncheck_download;

import java.io.File;

import com.android_demonstrate_abstractcode.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;



public class FileDownloadManager {

	private final static int FILE_DOWNLOAD_NOTIFICATION = 10088;

	public static final String FILEDOWNLOAD_SHARED_PREFERENCES_NAME = "file_download";
	public static final String KEY_DOWNLOAD_STATUS = "download_status";
	public static final String KEY_DOWNLOAD_CURRENT_SIZE = "download_current_size";
	public static final String KEY_DOWNLOAD_TOTAL_SIZE = "download_total_size";
	public static final String KEY_DOWNLOAD_FILE_PATH = "download_file_path";
	public static final String KEY_DOWNLOAD_FILE_URL = "download_file_url";

	public static final int STATUS_NOREADY = -1;
	public static final int STATUS_ASK = 0;
	public static final int STATUS_PROGRESS = 2;
	public static final int STATUS_FINISH = 3;
	public static final int STATUS_ERROR = 4;

	private static FileDownloadManager instance;

	private Context context;

	private static FileDownloadListener fileDownloadListener;

	private boolean running = false;

	private FileDownloadManager(Context context) {
		this.context = context;
	}

	public static void createInstance(Context context) {
		instance = new FileDownloadManager(context);
	}

	public static synchronized FileDownloadManager getInstance() {
		return instance;
	}

	public void reset() {
		SharedPreferences user = context.getSharedPreferences(
				FILEDOWNLOAD_SHARED_PREFERENCES_NAME, 0);
		SharedPreferences.Editor edit = user.edit();
		edit.putInt(KEY_DOWNLOAD_STATUS, STATUS_NOREADY);
		edit.putLong(KEY_DOWNLOAD_CURRENT_SIZE, 0L);
		edit.putLong(KEY_DOWNLOAD_TOTAL_SIZE, 0L);
		edit.putString(KEY_DOWNLOAD_FILE_PATH, "");
		edit.commit();
	}

	public void setDownloadStatus(int status) {
		SharedPreferences user = context.getSharedPreferences(
				FILEDOWNLOAD_SHARED_PREFERENCES_NAME, 0);
		SharedPreferences.Editor edit = user.edit();
		edit.putInt(KEY_DOWNLOAD_STATUS, status);
		edit.commit();
	}

	public int getDownloadStatus() {
		SharedPreferences user = context.getSharedPreferences(
				FILEDOWNLOAD_SHARED_PREFERENCES_NAME, 0);
		int status = user.getInt(KEY_DOWNLOAD_STATUS, STATUS_NOREADY);
		return status;
	}

	public void setDownloadCurrentSize(long currentSize) {
		SharedPreferences user = context.getSharedPreferences(
				FILEDOWNLOAD_SHARED_PREFERENCES_NAME, 0);
		SharedPreferences.Editor edit = user.edit();
		edit.putLong(KEY_DOWNLOAD_CURRENT_SIZE, currentSize);
		edit.commit();
	}

	public long getDownloadCurrentSize() {
		SharedPreferences user = context.getSharedPreferences(
				FILEDOWNLOAD_SHARED_PREFERENCES_NAME, 0);
		long currentSize = user.getLong(KEY_DOWNLOAD_CURRENT_SIZE, 0L);
		return currentSize;
	}

	public void setDownloadTotalSize(long currentSize) {
		SharedPreferences user = context.getSharedPreferences(
				FILEDOWNLOAD_SHARED_PREFERENCES_NAME, 0);
		SharedPreferences.Editor edit = user.edit();
		edit.putLong(KEY_DOWNLOAD_TOTAL_SIZE, currentSize);
		edit.commit();
	}

	public long getDownloadTotalSize() {
		SharedPreferences user = context.getSharedPreferences(
				FILEDOWNLOAD_SHARED_PREFERENCES_NAME, 0);
		long totalSize = user.getLong(KEY_DOWNLOAD_TOTAL_SIZE, 0L);
		return totalSize;
	}

	public void setDownloadFilePath(String filePath) {
		SharedPreferences user = context.getSharedPreferences(
				FILEDOWNLOAD_SHARED_PREFERENCES_NAME, 0);
		SharedPreferences.Editor edit = user.edit();
		edit.putString(KEY_DOWNLOAD_FILE_PATH, filePath);
		edit.commit();
	}

	public String getDownloadFilePath() {
		SharedPreferences user = context.getSharedPreferences(
				FILEDOWNLOAD_SHARED_PREFERENCES_NAME, 0);
		String filePath = user.getString(KEY_DOWNLOAD_FILE_PATH, null);
		return filePath;
	}

	public void setDownloadFileUrl(String fileUrl) {
		SharedPreferences user = context.getSharedPreferences(
				FILEDOWNLOAD_SHARED_PREFERENCES_NAME, 0);
		SharedPreferences.Editor edit = user.edit();
		edit.putString(KEY_DOWNLOAD_FILE_URL, fileUrl);
		edit.commit();
	}

	public String getDownloadFileUrl() {
		SharedPreferences user = context.getSharedPreferences(
				FILEDOWNLOAD_SHARED_PREFERENCES_NAME, 0);
		String fileUrl = user.getString(KEY_DOWNLOAD_FILE_URL, null);
		return fileUrl;
	}

	public void addFileDownloadNotification(String downloadUrl, long fileSize,
			String fileVersion, String updateInfo) {
		// Create notification
		
	     /* <activity
          android:name="com.gmcc.yueshi.screen.AppUpdateActivity"
          android:launchMode="singleTask"
          android:process=":voip_update"
          android:screenOrientation="portrait"
          android:theme="@android:style/Theme.Translucent" >
          <intent-filter>
              <action android:name="com.suntek.mway.cmcc.voip.APP_UPDATE_ACTIVITY" />

              <category android:name="android.intent.category.LAUNCHER" />
              <category android:name="android.intent.category.DEFAULT" />
          </intent-filter>
      </activity>*/
		
	   String APP_UPDATE_ACTIVITY = "com.suntek.mway.cmcc.voip.APP_UPDATE_ACTIVITY";
		
		Intent intent = new Intent(APP_UPDATE_ACTIVITY);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		intent.putExtra("downloadUrl", downloadUrl);
		intent.putExtra("fileSize", fileSize);
		intent.putExtra("fileVersion", fileVersion);
		intent.putExtra("updateInfo", updateInfo);

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent,
				0);
		int iconId = R.drawable.versioncheck_download;
		Notification notif = new Notification(iconId, "",
				System.currentTimeMillis());
		notif.flags = Notification.FLAG_NO_CLEAR;
		notif.setLatestEventInfo(context, "����", "���µ�����汾", contentIntent);

		// Send notification
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(FILE_DOWNLOAD_NOTIFICATION, notif);
	}

	public void updateFileDownloadNotification() {
		// Create notification
		 String APP_UPDATE_ACTIVITY = "com.suntek.mway.cmcc.voip.APP_UPDATE_ACTIVITY";

		Intent intent = new Intent(APP_UPDATE_ACTIVITY);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent,
				0);
		int iconId = R.drawable.versioncheck_download;
		Notification notif = new Notification(iconId, "",
				System.currentTimeMillis());
		notif.flags = Notification.FLAG_NO_CLEAR;
		notif.setLatestEventInfo(context, "����", "��������汾��...", contentIntent);

		// Send notification
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(FILE_DOWNLOAD_NOTIFICATION, notif);
	}

	public void removeFileDownloadNotification() {
		if (context != null) {
			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(FILE_DOWNLOAD_NOTIFICATION);
		}
	}

	

	public static void setFileDownloadListener(FileDownloadListener listener) {
		fileDownloadListener = listener;
		System.out.println("#### fileDownloadListener=" + fileDownloadListener);
	}

	public static FileDownloadListener getFileDownloadListener() {
		return fileDownloadListener;
	}



	public interface FileDownloadListener {
		
		void downloadStart();
		void downloadProgress(long currentSize, long totalSize);
		void downloadFinish(File downloadFile);
		void downloadError(int code, String msg);
		void downloadCancel();
	}
	
	
}
