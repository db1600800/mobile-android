package com.compoment.imageCache;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.util.Log;


//首先限制内存图片缓冲的堆内存大小，每次有图片往缓存里加时判断是否超过限制大小，超过的话就从中取出最少使用的图片并将其移除，
//当然这里如果不采用这种方式，换做软引用也是可行的，二者目的皆是最大程度的利用已存在于内存中的图片缓存，避免重复制造垃圾增加GC负担，
//OOM溢出往往皆因内存瞬时大量增加而垃圾回收不及时造成的。只不过二者区别在于LinkedHashMap里的图片缓存在没有移除出去之前是不会被GC回收的，
//而SoftReference里的图片缓存在没有其他引用保存时随时都会被GC回收。所以在使用LinkedHashMap这种LRU算法缓存更有利于图片的有效命中，
//当然二者配合使用的话效果更佳，即从LinkedHashMap里移除出的缓存放到SoftReference里，这就是内存的二级缓存，有兴趣的童鞋不凡一试。

public class ImgSizeLimitMemoryCache {

	private static final String TAG = "SizeLimitMemoryCache";
	// 放入缓存时是个同步操作
	// LinkedHashMap构造方法的最后一个参数true代表这个map里的元素将按照最近使用次数由少到多排列，即LRU
	// 这样的好处是如果要将缓存中的元素替换，则先遍历出最近最少使用的元素来替换以提高效率
	private Map<String, Bitmap> cache = Collections
			.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));
	// 缓存中图片所占用的字节，初始0，将通过此变量严格控制缓存所占用的堆内存
	private long size = 0;// current allocated size
	// 缓存只能占用的最大堆内存
	private long limit = 1000000;// max memory in bytes

	public ImgSizeLimitMemoryCache() {
		// use 25% of available heap size
		setLimit(Runtime.getRuntime().maxMemory() / 10);
	}

	public void setLimit(long new_limit) {
		limit = new_limit;
		Log.i(TAG, "MemoryCache will use up to " + limit / 1024. / 1024. + "MB");
	}

	public Bitmap get(String id) {
		try {
			if (!cache.containsKey(id))
				return null;
			return cache.get(id);
		} catch (NullPointerException ex) {
			return null;
		}
	}

	public void put(String id, Bitmap bitmap) {
		try {
			if (cache.containsKey(id))
				size -= getSizeInBytes(cache.get(id));
			cache.put(id, bitmap);
			size += getSizeInBytes(bitmap);
			checkSize();
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}

	/**
	 * 严格控制堆内存，如果超过将首先替换最近最少使用的那个图片缓存
	 *
	 */
	private void checkSize() {
		Log.i(TAG, "cache size=" + size + " length=" + cache.size());
		if (size > limit) {
			// 先遍历最近最少使用的元素
			Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Bitmap> entry = iter.next();
				size -= getSizeInBytes(entry.getValue());
				iter.remove();
				if (size <= limit)
					break;
			}
			Log.i(TAG, "Clean cache. New size " + cache.size());
		}
	}

	public void clear() {
		cache.clear();
	}

	/**
	 * 图片占用的内存
	 *
	 * [url=home.php?mod=space&uid=2768922]@Param[/url] bitmap
	 *
	 * @return
	 */
	long getSizeInBytes(Bitmap bitmap) {
		if (bitmap == null)
			return 0;
		return bitmap.getRowBytes() * bitmap.getHeight();
	}
}