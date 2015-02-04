package com.compoment.storage;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

public class FileStorage {
	
	public static String getLocalVideoRecordDirPath() {
		String voipDir = getStorageDirPath();
		String dir = null;
		//default/VideoRecord/"
		dir = voipDir + File.separator + "default" + File.separator + "VideoRecord"
				+ File.separator;

		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}

		return dir;
	}
	
	public static String getStorageDirPath() {
		// 
		File defaultDir = Environment.getExternalStorageDirectory();
		String dir = null;

		// "/mnt/sdcard/yueshi/"
		dir = defaultDir.getAbsolutePath() + File.separator + "yueshi"
				+ File.separator;

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
	
	
	// 判断SD Card是否插入
		private boolean isSDCardExist() {
			return Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED);
		}

		private boolean isNotEnoughMemory() {
			long sizeInByte = getAvailableSizeOfSDCard();
			long sizeInKB = sizeInByte / 1024;
			long sizeInMB = sizeInKB / 1024;
			if (sizeInMB < 50) {
				return true;
			} else {
				return false;
			}
		}
		
		
		
		
		
	
}
