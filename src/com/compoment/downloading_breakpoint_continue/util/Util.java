package com.compoment.downloading_breakpoint_continue.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.Random;


import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore.Images;




public class Util {
	public static final String TAG = "voipClient_cmcc";
	public static final String MESSAGE_TYPE_AUDIO = "audio";
	public static final String MESSAGE_TYPE_VIDEO = "video";
	public static final String CODE_SENT_SUCCEED = "0";
	public static final String CODE_SENT_SENDING = "1";
	public static final String CODE_SENT_FAILD = "2";
	public static final String BROADCAST_MESSAGE_UPLOAD = "MessageUploadService.ACTION_CONTROL";
	public static final int UPLOAD_OR_DOWNLOAD_TIME_OUT = 60 * 1000; // 上传、下载超时
	public static final int CHECK_NEW_MESSAGE_PERIOD = 2 * 60 * 1000; // 2分钟向服务器更新一次留言


	/** 获取下载视频预览图的目录 */
	public static String getPreviewImageDownloadDirPath() {
		// 获取SD卡目录
		String voipDir = FilePathDealWith.getStorageDirPath();
		String number = Util.getMyPhoneNumber();
		String dir = null;

		if (number == null || "".equals(number)) {
			// 存放留言记录的文件夹路径："/sdcard/void/号码/AudioRecord/"
			dir = voipDir + "default" + File.separator + "PreImageRecord"
					+ File.separator;
		} else {
			// 存放留言记录的文件夹路径："/mnt/sdcard/void/号码/PreImageRecord/"
			dir = voipDir + number + File.separator + "PreImageRecord"
					+ File.separator;
		}

		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}

		return dir;
	}


	/** 获取存储卡的剩余容量，单位为byte */
	public static long getAvailableSizeOfSDCard() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long blockSize = sf.getBlockSize(); // 块大小
			long availCount = sf.getAvailableBlocks(); // 扇区数
			// 可用尺寸
			return availCount * blockSize;
		}
		return -1;
	}

	/** 读取设置中的用户名，作为本机号码 */
	public static String getMyPhoneNumber() {
//		RcsSettings rcsSettings = RcsSettings.getInstance();
//		if (rcsSettings != null) {
//			String number = rcsSettings.getUserProfileImsUserName();
//			if (number == null) {
//				return "";
//			} else {
//				return number;
//			}
//		}
		return "";
	}

	/** 获取录像文件的目录 */
	public static String getDownloadVideoRecordDirPath() {
		String number = Util.getMyPhoneNumber();
		String voipDir = FilePathDealWith.getStorageDirPath();
		String dir = null;

		if (number == null || "".equals(number)) {
			// 存放留言记录的文件夹路径："/sdcard/void/号码/Message/"
			dir = voipDir + "default" + File.separator + "VideoRecord"
					+ File.separator;
		} else {
			// 存放留言记录的文件夹路径："/sdcard/void/号码/Message/"
			dir = voipDir + number + File.separator + "VideoRecord" + File.separator;
		}

		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}

		return dir;
	}



	/** 生成录像文件名 */
	public static String createDownloadVideoFileName() {
	

		return "video" + generateRandomNumericString(15) + ".mp4";
	}

	/** 获取录像文件的目录 */
	public static String getDownloadAudioRecordDirPath() {
		// 获取SD卡目录
		String voipDir = FilePathDealWith.getStorageDirPath();
		String number = Util.getMyPhoneNumber();
		String dir = null;

		if (number == null || "".equals(number)) {
			// 存放留言记录的文件夹路径："/sdcard/void/号码/AudioRecord/"
			dir = voipDir + "default" + File.separator + "AudioRecord"
					+ File.separator;
		} else {
			// 存放留言记录的文件夹路径："/sdcard/void/号码/AudioRecord/"
			dir = voipDir + number + File.separator + "AudioRecord" + File.separator;
		}

		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}

		return dir;
	}

	

	/** 获取新的录音文件名 */
	public static String createDownloadAudioFileName() {
		

		return "audio" + generateRandomNumericString(15) + ".amr";
	}




	/** 生成id */
	public static String createId() {
		return generateRandomNumericString(15);
	}

	/** 生成随机数 */
	public static String generateRandomNumericString(int length) {
		length = Math.abs(length);
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int oneRandomNumericCharacter = random.nextInt(9);
			sb.append(oneRandomNumericCharacter);
		}
		return sb.toString();
	}

	


}
