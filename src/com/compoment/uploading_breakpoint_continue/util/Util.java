package com.compoment.uploading_breakpoint_continue.util;

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
		return "18820070027";
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

	

	public static boolean isFileAvailable(File file) {
		if (!file.exists() || file.length() == 0) {
			return false;
		} else {
			return true;
		}
	}
}
