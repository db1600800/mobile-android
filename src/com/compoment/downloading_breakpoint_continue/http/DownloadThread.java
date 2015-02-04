package com.compoment.downloading_breakpoint_continue.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

import com.compoment.downloading_breakpoint_continue.DownloadActivity;
import com.compoment.downloading_breakpoint_continue.util.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class DownloadThread extends Thread {
	private static final String TAG = "MessageDownloadThread";
	private static final int BUFFER_SIZE = 1024;

	private Context context;
	private String id;
	private URL url;
	private BufferedInputStream bis;
	private File file;
	private Handler handler;
	private boolean isFinished = false;
	private boolean isStoped = false;
	// private boolean shouldStopIt = false;
	private boolean stopByUser; // 用户点击取消按钮
	private int done; // 已下载长度
	private int total; // 总长度

	private Thread checkNetwordThread;

	// 分块构造函数
	public DownloadThread(Context context, String id, URL url, File file,
			Handler handler) {
		this.context = context;
		this.id = id;
		this.url = url;
		this.file = file;
		this.handler = handler;
	}

	public void run() {
		DownloadThreadPool.put(id, this);
		DownloadThreadPool.readyToPut = false;

		// 开始检查网络状态，直到下载线程结束
		startCheckNetworkThread();

		RandomAccessFile randomAccessFile = null;
		byte[] buf = new byte[BUFFER_SIZE];
		URLConnection con = null;
		try {
			// 打开URL连接
			con = url.openConnection();
			con.setConnectTimeout(10 * 1000);
			con.setReadTimeout(Util.UPLOAD_OR_DOWNLOAD_TIME_OUT); // 连接超时
			con.setAllowUserInteraction(true);
			con.setRequestProperty("RANGE", "bytes=" + file.length() + "-"); // 设置下载范围
			total = con.getContentLength();

			// 如果该文件下载完成，直接返回。
			if (file.length() == total) {
				this.isFinished = true;
				this.isStoped = true;
			} else {// 文件未下载完成，获取到当前指针位置，继续下载。
				randomAccessFile = new RandomAccessFile(file, "rw");
				randomAccessFile.seek(file.length());

				bis = new BufferedInputStream(con.getInputStream());

				while (file.length() < total) {
					if (stopByUser) {
						break;
					}

					// UPLOAD_OR_DOWNLOAD_TIME_OUT内没有执行完一次，停止下载
					handler.sendEmptyMessageDelayed(
							DownloadActivity.MESSAGE_STOP_DOWNLOAD,
							Util.UPLOAD_OR_DOWNLOAD_TIME_OUT);

					int len = bis.read(buf, 0, BUFFER_SIZE);
					if (len != -1) {
						randomAccessFile.write(buf, 0, len);
						done = (int) file.length();

						// 更新进度条
						updateProgress(handler, done);
					} else {
						handler
								.removeMessages(DownloadActivity.MESSAGE_STOP_DOWNLOAD);
						break;
					}
					handler
							.removeMessages(DownloadActivity.MESSAGE_STOP_DOWNLOAD);
				}

				if (!stopByUser) {
					this.isFinished = true;
					handler
							.sendEmptyMessage(DownloadActivity.MESSAGE_DOWNLOAD_SUCCEED);
				}

				if (bis != null) {
					try {
						bis.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (randomAccessFile != null) {
					try {
						randomAccessFile.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			handler.removeMessages(DownloadActivity.MESSAGE_STOP_DOWNLOAD);
			Log.e(TAG, e.toString());
			e.printStackTrace();
			if (!stopByUser) {
				handler
						.sendEmptyMessage(DownloadActivity.MESSAGE_DOWNLOAD_FAILD);
			} else {
				Log.e(Util.TAG, "download cancel by user");
			}
		} finally {
			stopCheckNetworkThread();
			DownloadThreadPool.remove(id);
			this.isStoped = true;
		}
	}

	/** 更新进度条UI */
	private static void updateProgress(Handler handler, int done) {
		Message msg = Message.obtain();
		msg.what = DownloadActivity.MESSAGE_UPDATE_PROGRESS;
		msg.arg1 = done;
		handler.sendMessage(msg);
	}

	/** 每3秒检查一次网络状态 */
	private void startCheckNetworkThread() {
		checkNetwordThread = new Thread(new Runnable() {
			public void run() {
				Log.e("Main", " ");
				Log.e("Main", "startCheckNetworkThread");
				ConnectivityManager connManager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);// 获得网络连接服务
				while (!shouldCloseCheckNetwordThread) {
					closeConnectionIfNoNetwork(connManager);

					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						break;
					}
				}
				Log.e("Main", "checkNetwordThread finished");
				Log.e("Main", " ");
			}
		});
		checkNetwordThread.start();
	}

	// 停止检查网络线程
	private boolean shouldCloseCheckNetwordThread = false;

	private void stopCheckNetworkThread() {
		if (checkNetwordThread != null && !checkNetwordThread.isInterrupted()) {
			checkNetwordThread.interrupt();
		}
		shouldCloseCheckNetwordThread = true;
	}

	private void closeConnectionIfNoNetwork(ConnectivityManager connManager) {
		android.net.NetworkInfo.State gprsState = connManager.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).getState();
		android.net.NetworkInfo.State wifiState = connManager.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).getState();
		Log.e("MessageDownloadThread", "gprsState = "
				+ getNetworkStateString(gprsState) + ", wifiState = "
				+ getNetworkStateString(wifiState));
		if (android.net.NetworkInfo.State.CONNECTED != gprsState
				&& android.net.NetworkInfo.State.CONNECTED != wifiState) {

			// 断开连接
			closeConnection();

			isStoped = true;
			//handler.removeMessages(MessageUploadingActivity.MESSAGE_UPLOAD_STOP);
		}
	}

	private android.net.NetworkInfo.State getNetworkStateString(
			android.net.NetworkInfo.State state) {
		if (android.net.NetworkInfo.State.CONNECTED == state) {
			return android.net.NetworkInfo.State.CONNECTED;
		} else if (android.net.NetworkInfo.State.CONNECTING == state) {
			return android.net.NetworkInfo.State.CONNECTING;
		} else if (android.net.NetworkInfo.State.DISCONNECTED == state) {
			return android.net.NetworkInfo.State.DISCONNECTED;
		} else if (android.net.NetworkInfo.State.DISCONNECTING == state) {
			return android.net.NetworkInfo.State.DISCONNECTING;
		} else if (android.net.NetworkInfo.State.SUSPENDED == state) {
			return android.net.NetworkInfo.State.SUSPENDED;
		} else {
			return android.net.NetworkInfo.State.UNKNOWN;
		}
	}

	/** 关闭http连接，关闭输出流 */
	private void closeConnection() {
		Log.e("Main", "closeConnection(), bis == null) " + (bis == null));

		// 断开链接
		if (bis != null) {
			try {
				bis.close();
				bis = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isFinished() {
		return isFinished;
	}

	public boolean isStoped() {
		return isStoped;
	}

	public void stopIt() {
		handler.sendEmptyMessage(DownloadActivity.MESSAGE_STOP_IT);
		stopByUser = true;
		isStoped = true;
		closeConnection();
	}

	public int getDone() {
		return done;
	}

	public int getTotal() {
		return total;
	}
}