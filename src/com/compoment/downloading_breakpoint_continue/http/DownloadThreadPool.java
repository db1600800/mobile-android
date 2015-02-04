package com.compoment.downloading_breakpoint_continue.http;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public class DownloadThreadPool {
	private static Map<String, DownloadThread> pool = new HashMap<String, DownloadThread>();
	public static boolean readyToPut = false; // 保证同一时间只有一个下载对话框在运行。

	public static void put(String msg_id, DownloadThread thread) {
		synchronized (pool) {
			Log.e("MessageDownloadThreadPool", "put");
			pool.put(msg_id, thread);
			Log.e("MessageDownloadThreadPool", "put finished");
		}
	}

	public static DownloadThread get(String msg_id) {
		DownloadThread thread = null;
		synchronized (pool) {
			Log.e("MessageDownloadThreadPool", "get");
			thread = pool.get(msg_id); // 如果不存在，返回null
			Log.e("MessageDownloadThreadPool", "get finished");
		}
		return thread;
	}

	public static void remove(String msg_id) {
		synchronized (pool) {
			Log.e("MessageDownloadThreadPool", "remove");
			pool.remove(msg_id);
			Log.e("MessageDownloadThreadPool", "remove finished");
		}
	}

	public static boolean hasDownloadingThread() {
		boolean result = false;
		synchronized (pool) {
			Log.e("MessageDownloadThreadPool", "hasDownloadingThread");
			result = pool.size() > 0;
			Log.e("MessageDownloadThreadPool", "hasDownloadingThread finished");
		}
		return result;
	}

	public static void clear() {
		synchronized (pool) {
			Log.e("MessageDownloadThreadPool", "clear");
			pool.clear();
			Log.e("MessageDownloadThreadPool", "clear finished");
		}
	}
}
