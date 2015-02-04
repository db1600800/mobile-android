package com.compoment.imageCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
//优化一:先从内存中加载，没有则开启线程从SD卡或网络中获取，这里注意从SD卡获取图片是放在子线程里执行的，否则快速滑屏的话会不够流畅。
//优化二:于此同时，在adapter里有个busy变量，表示listview是否处于滑动状态，如果是滑动状态则仅从内存中获取图片，没有的话无需再开启线程去外存或网络获取图片。
//优化三：ImageLoader里的线程使用了线程池，从而避免了过多线程频繁创建和销毁，有的童鞋每次总是new一个线程去执行这是非常不可取的，好一点的用的AsyncTask类，其实内部也是用到了线程池。
//优化四：在从网络获取图片时，先是将其保存到sd卡，然后再加载到内存，这么做的好处是在加载到内存时可以做个压缩处理，以减少图片所占内存。

//优化五：而图片错位问题的本质源于我们的listview使用了缓存convertView，假设一种场景，一个listview一屏显示九个item，那么在拉出第十个item的时候，
//事实上该item是重复使用了第一个item，也就是说在第一个item从网络中下载图片并最终要显示的时候其实该item已经不在当前显示区域内了，
//此时显示的后果将是在可能在第十个item上输出图像，这就导致了图片错位的问题。所以解决之道在于可见则显示，不可见则不显示。在ImageLoader里有个imageViews的map对象，就是用于保存当前显示区域图像对应的url集，在显示前判断处理一下即可。

//http://my.eoe.cn/817027/archive/2474.html
//http://blog.csdn.net/zircon_1973/article/details/7693839

//放在多个view一个activity  imageloader新建在activity 全局使用

public class ImageLoader {

	ImgSizeLimitMemoryCache memoryCache = new ImgSizeLimitMemoryCache();
	ImgFileCache fileCache;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	// 线程池
	ExecutorService executorService;

	public ImageLoader(Context context) {
		fileCache = new ImgFileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}

	// 当进入listview时默认的图片，可换成你自己的默认图片
	 int defaultImg ;

	// 最主要的方法
	public void setImgToImageView(String url, ImageView imageView,int defaultImg,boolean isScrolling) {
		this.defaultImg=defaultImg;
		imageViews.put(imageView, url);
		// 先从内存缓存中查找
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else {
			// 若没有则开启新线程从文件缓存或网络加载图片
			if(!isScrolling)
			{
			PhotoToLoadBean bean = new PhotoToLoadBean(url, imageView);
			executorService.submit(new LoadImgFromFileCacheOrHttpRunnable(bean));
			}
			imageView.setImageResource(defaultImg);
		}
	}



	private Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		// 先从文件缓存中查找是否有
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		// 最后从指定的url中下载图片
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoadBean {
		public String url;
		public ImageView imageView;

		public PhotoToLoadBean(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class LoadImgFromFileCacheOrHttpRunnable implements Runnable {
		PhotoToLoadBean photoToLoadBean;

		LoadImgFromFileCacheOrHttpRunnable(PhotoToLoadBean photoToLoadBean) {
			this.photoToLoadBean = photoToLoadBean;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoadBean))
				return;
			Bitmap bmp = getBitmap(photoToLoadBean.url);
			memoryCache.put(photoToLoadBean.url, bmp);
			if (imageViewReused(photoToLoadBean))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoadBean);
			// 更新的操作放在UI线程中
			Activity a = (Activity) photoToLoadBean.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	/**
	 * 防止图片错位
	 *
	 * @param photoToLoad
	 * @return
	 */
	boolean imageViewReused(PhotoToLoadBean photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// 用于在UI线程中更新界面
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoadBean photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoadBean p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
			else
				photoToLoad.imageView.setImageResource(defaultImg);
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	/**网络取得的图片存一份到文件*/
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
}