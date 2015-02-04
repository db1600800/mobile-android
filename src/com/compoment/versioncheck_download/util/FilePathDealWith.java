package com.compoment.versioncheck_download.util;

import java.io.File;
import java.util.Random;


import android.os.Environment;
import android.os.StatFs;


public class FilePathDealWith {

	/** 获取项目的目录 */
	public static String getStorageDirPath() {
		// 获取SD卡目录
		File defaultDir = Environment.getExternalStorageDirectory();
		String dir = null;

		// 存放留言记录的文件夹路径："/mnt/sdcard/yueshi/"
		dir = defaultDir.getAbsolutePath() + File.separator + "yueshi"
				+ File.separator;

		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}

		return dir;
	}
	
	/** 获取录制视频预览图的目录 */
	public static String getPreviewImageRecordDirPath() {
		// 获取SD卡目录
		String voipDir = getStorageDirPath();
		String dir = null;

		// 存放留言记录的文件夹路径："/sdcard/void/号码/AudioRecord/"
		dir = voipDir + "default" + File.separator + "PreImageSent"
				+ File.separator;

		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}

		return dir;
	}
	
	
	/** 获取录像文件的目录 */
	public static String getLocalVideoRecordDirPath() {
		String voipDir = getStorageDirPath();
		String dir = null;
		// 存放留言记录的文件夹路径："/sdcard/void/default/VideoRecord/"
		dir = voipDir + File.separator + "default" + File.separator + "VideoRecord"
				+ File.separator;

		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}

		return dir;
	}
	
	/** 生成视频文件名 */
	public static String createLocalVideoFileName() {
		return "video" + generateRandomNumericString(15) + ".mp4";
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
	
	public static boolean isSDCardExist() {
		return Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}
	
	public static boolean isNotEnoughMemory() {
		long sizeInByte = getAvailableSizeOfSDCard();
		long sizeInKB = sizeInByte / 1024;
		long sizeInMB = sizeInKB / 1024;
		if (sizeInMB < 50) {
			return true;
		} else {
			return false;
		}
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
}
