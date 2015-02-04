package com.compoment.uploading_breakpoint_continue.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.RandomAccessFile;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore.Images;


public class PictureDealWith {
	
	/** 生成视频截图，保存在本地。返回截图文件名：video00001.png */
	public static String saveToPng(final String vedioPath) {
		// 与视频文件同名，不同后缀名
		final String imageName = vedioPath.substring(
				vedioPath.lastIndexOf('/') + 1, vedioPath.lastIndexOf('.')) + ".png";

		new Thread(new Runnable() {
			public void run() {
				try {
					// 获取截图
					Bitmap bm = android.media.ThumbnailUtils.createVideoThumbnail(
							vedioPath, Images.Thumbnails.MICRO_KIND);
					bm = ThumbnailUtils.extractThumbnail(bm, 151, 146);

					// 保存截图
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
					byte[] buffer = stream.toByteArray();
					File file = new File(FilePathDealWith.getPreviewImageRecordDirPath());
					if (!file.exists()) {
						file.mkdirs();
					}
					File imageFile = new File(FilePathDealWith.getPreviewImageRecordDirPath()
							+ imageName);
					RandomAccessFile accessFile = null;

					accessFile = new RandomAccessFile(imageFile, "rw");
					accessFile.write(buffer);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

		return imageName;
	}
}
