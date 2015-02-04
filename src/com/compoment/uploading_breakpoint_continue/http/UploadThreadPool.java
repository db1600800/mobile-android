package com.compoment.uploading_breakpoint_continue.http;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

public class UploadThreadPool {
	private static Map<String, UploadThread> pool = new HashMap<String, UploadThread>();

	public static void put(String msg_id, UploadThread thread) {
		synchronized (pool) {
			pool.put(msg_id, thread);
		}
	}

	public static UploadThread get(String msg_id) {
		UploadThread thread = null;
		synchronized (pool) {
			thread = pool.get(msg_id); // �����ڣ�����null
		}
		return thread;
	}

	public static void remove(String msg_id) {
		synchronized (pool) {
			pool.remove(msg_id);
		}
	}

	public static boolean hasUploadingThread() {
		boolean result = false;
		synchronized (pool) {
			result = pool.size() > 0;
		}
		return result;
	}

	public static void clear() {
		synchronized (pool) {
			pool.clear();
		}
	}
}
