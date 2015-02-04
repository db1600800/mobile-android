package com.compoment.video_record_play.util;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtils {
	/**
	 * 根据显示组件的尺寸，加载合适大小的图片，避免浪费内存。
	 * 
	 * @return 资源图片对象（可能略大于(reqWidth, reqHeight)，但一定不大于资源图片的原尺寸）。
	 * 
	 * @param res
	 *          资源对象
	 * @param resId
	 *          资源id
	 * @param reqWidth
	 *          显示组件的宽度
	 * @param reqHeight
	 *          显示组件的高度
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		
		// 不将图片读入内存的前提下，获得图片的尺寸、类型信息，保存在BitmapFactory.Options中
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}
}
