package com.compoment.uploading_breakpoint_continue.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import com.compoment.uploading_breakpoint_continue.UploadActivity;
import com.compoment.uploading_breakpoint_continue.db.SentDetailDBService;
import com.compoment.uploading_breakpoint_continue.db.UploadCompleteBean;
import com.compoment.uploading_breakpoint_continue.db.UploadCompleteDBService;
import com.compoment.uploading_breakpoint_continue.db.UploadPartBean;
import com.compoment.uploading_breakpoint_continue.db.UploadPartDBService;
import com.compoment.uploading_breakpoint_continue.util.Util;
import com.compoment.uploading_breakpoint_continue.util.Urls;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;



public class UploadThread extends Thread {
	private static final String TAG = "MessageUploadThread";

	private static final int BLOCK_SIZE = 128 * 1024; // 上传块大小，128KB。以块为单位，支持断点续传
	private static final String END = "\r\n";
	private static final String TOW_HYPHENS = "--";
	private static final String BOUNDARY = "******";

	private boolean isFinished = false;
	private boolean isStoped = false;
	private boolean stopByUser = false; // 用户点击取消按钮
	private boolean stopForTimeout = false; // 上传超时
	private boolean shouldStopProgress = false;

	private Handler handler = null;
	private int total;
	private int remain;
	private String snderNumber;
	private String[] rcverNumber;
	private int duration;
	private String type;
	private String path;
	private String[] snd_id;
	/**
	 * 为了查是否存在此文件上传记录
	 */
	private String file_id;
	private Context context;

	// UI线程，检查网络线程
	private Thread progressThread;
	private Thread checkNetwordThread;

	// 网络连接
	private HttpURLConnection httpURLConnection;
	private OutputStream outStream;

	public UploadThread(Context context, String snd_id, String file_id,
			String snderNumber, String rcverNumber, int duration, String type,
			String path, Handler handler) {
		super();
		this.context = context;
		this.handler = handler;
		this.snderNumber = snderNumber;
		this.rcverNumber = rcverNumber.split(";");
		this.duration = duration;
		this.type = type;
		this.path = path;
		this.snd_id = snd_id.split(";");
		this.file_id = file_id;
	}

	public UploadThread(Context context,
			UploadPartBean messageUploading, Handler handler) {
		super();
		this.context = context;
		this.handler = handler;
		this.snderNumber = messageUploading.getSnderNumber();
		this.rcverNumber = messageUploading.getRcverNumber().split(";");
		this.duration = messageUploading.getDuration();
		this.type = messageUploading.getType();
		this.path = messageUploading.getPath();
		this.snd_id = messageUploading.getSnt_id().split(";");
		this.file_id = messageUploading.getFile_Id();
	}

	public void run() {
		UploadThreadPool.put(file_id, this);

		// 上传文件，支持断点续传
		File file = new File(path);

		// 检查文件合法性
		if (!Util.isFileAvailable(file)) {
			return;
		}

		String fileName = file.getName();
		long fileLength = file.length();

		String receiverNumbers = "";
		for (int i = 0; i < rcverNumber.length; i++) {
			receiverNumbers += rcverNumber[i] + ";";
		}
		if (!"".equals(receiverNumbers)) {
			receiverNumbers = receiverNumbers.substring(0,
					(receiverNumbers.length() - 1));
		}

		String uploadURL = Urls.getUploadURL2(snderNumber, receiverNumbers,
				duration, fileLength, type, fileName);
		Log.e(TAG, uploadURL);
		uploadFile(file_id, path, uploadURL);
	}

	/** 上传文件的方法（支持断点续传） */
	private void uploadFile(String file_id, String filePath,
			String uploadUrl) {
		Log.e(TAG, "upload begin");

		// 开始检查网络状态，直到上传线程结束
		startCheckNetworkThread();

		// 获取文件信息，用于更新进度条等
		File file = new File(filePath);
		final long fileLength = file.length();
		String fileName = file.getName();
		total = (int) fileLength;

		String tempStr1 = "Content-Disposition: form-data; name=\"file\"; filename=\""
				+ fileName + "\"" + END;

		long position = 0;
		long remainBlockLength = 0;

		// 读取上传进度
		UploadPartDBService uploadPartDBService = new UploadPartDBService(
				context);
		UploadPartBean uploadPartBean = uploadPartDBService
				.queryUploadingById(file_id);
		if (uploadPartBean != null) { // 存在上传进度
			position = uploadPartBean.getBlock() * BLOCK_SIZE; // 断点
			remainBlockLength = fileLength - position;
		} else { // 不存在上传进度
			remainBlockLength = fileLength;
			String snd_id1 = "";
			for (int i = 0; i < snd_id.length; i++) {
				snd_id1 += snd_id[i] + ";";
			}
			if (!"".equals(snd_id1)) {
				snd_id1 = snd_id1.substring(0, snd_id1.length() - 1);
			}
			String receiverNumbers = "";
			for (int i = 0; i < rcverNumber.length; i++) {
				receiverNumbers += rcverNumber[i] + ";";
			}
			if (!"".equals(receiverNumbers)) {
				receiverNumbers = receiverNumbers
						.substring(0, receiverNumbers.length());
			}
			uploadPartBean = new UploadPartBean(file_id, 0, snderNumber,
					receiverNumbers, duration, type, path, snd_id1);
			uploadPartDBService.add(uploadPartBean); // 新增上传进度
		}

		isFinished = false;

		try {
			InputStream is = null;
			String isEnd = "false";
			notifyUploadBegin(handler, total);

			byte[] buffer = new byte[BLOCK_SIZE];
			int block = (int) (position / BLOCK_SIZE); // 正在上传的块号

			// 每sleepTime进度条增加progressUnit
			final int progressUnit = 4096;
			long sleepTime = 500;
			while (true) {
				if (stopByUser || stopForTimeout) {
					break;
				}

				// 判断是否已到文件末端
				if (remainBlockLength == 0) {
					break;
				} else if (remainBlockLength < BLOCK_SIZE) {
					isEnd = "true";
				}

				URL url = new URL(uploadUrl + "&POS=" + position + "&isEnd=" + isEnd);

				if (remainBlockLength >= BLOCK_SIZE) { // 剩余一块或以上
					Log.e(TAG, position + " - " + (position + BLOCK_SIZE));

					remainBlockLength -= BLOCK_SIZE;

					// 建立HTTP连接
					Log.e(TAG, "build HTTP connection start");
					long t3 = System.currentTimeMillis();
          					httpURLConnection = getManagedConnection(url);
					outStream = httpURLConnection.getOutputStream();
					long t4 = System.currentTimeMillis();
					Log.e(TAG, "build HTTP connection finish, cost = " + (t4 - t3)
							+ " ms");
					// 建立连接可能耗时很长，所以再检查一下是否被用户取消下载
					if (stopByUser || stopForTimeout) {
						break;
					}

					long t1 = System.currentTimeMillis();

					shouldStopProgress = false;
					final int finalProgress = (int) position;
					final long finalSleepTime = sleepTime;
					progressThread = new Thread(new Runnable() {
						public void run() {
							int progress = finalProgress;
							handler.sendEmptyMessageDelayed(
									UploadActivity.MESSAGE_UPLOAD_STOP,
									Util.UPLOAD_OR_DOWNLOAD_TIME_OUT);
							while (!shouldStopProgress) {
								if (progressThread.isInterrupted()) {
									break;
								}
								if ((progress + progressUnit) < (finalProgress + BLOCK_SIZE)) {
									progress += progressUnit;
									remain = total - progress;
									updateProgress(handler, progress);
								} else {
									break;
								}

								try {
									Thread.sleep(finalSleepTime);
								} catch (InterruptedException e) {
									break;
								}
							}

							// 一块实际上传完了
							progress = (int) (finalProgress + BLOCK_SIZE);
							remain = total - progress;
							updateProgress(handler, progress);
							shouldStopProgress = false;
						}
					});
					progressThread.start();
					// 写HTTP头
					outStream.write((TOW_HYPHENS + BOUNDARY + END).getBytes());
					outStream.write(tempStr1.getBytes());
					outStream.write(END.getBytes());

					// 写文件
					RandomAccessFile raf = new RandomAccessFile(file, "rw");
					raf.seek(position);
					raf.read(buffer);

					Log.e(TAG, "write File begin");
					outStream.write(buffer);
					Log.e(TAG, "write File finish");

					// 写HTTP尾
					outStream.write(END.getBytes());
					outStream.write((TOW_HYPHENS + BOUNDARY + TOW_HYPHENS + END)
							.getBytes());
					outStream.flush();

					Log.e(TAG, "get InputStream begin");
					is = httpURLConnection.getInputStream();
					Log.e(TAG, "get InputStream finish");

					Log.e(TAG, position + " - " + (position + BLOCK_SIZE) + " finished");
					handler.removeMessages(UploadActivity.MESSAGE_UPLOAD_STOP);
					shouldStopProgress = true;
					progressThread.interrupt();

					long t2 = System.currentTimeMillis();
					sleepTime = ((t2 - t1) * progressUnit) / BLOCK_SIZE; // 更新平均间隔时间
					Log.e(TAG, "sleepTime = " + sleepTime);

					// 修改正在上传的块号
					position += BLOCK_SIZE;
					block++;
					uploadPartBean.setBlock(block);
				} else { // 最后一块
					Log.e(TAG, position + " - end(" + fileLength + ")");

					handler.sendEmptyMessageDelayed(
							UploadActivity.MESSAGE_UPLOAD_STOP,
							Util.UPLOAD_OR_DOWNLOAD_TIME_OUT);

					// 建立HTTP连接
					Log.e(TAG, "build HTTP connection start");
					httpURLConnection = getManagedConnection(url);
					outStream = httpURLConnection.getOutputStream();
					Log.e(TAG, "build HTTP connection finish");
					// 建立连接可能耗时很长，所以再检查一下是否被用户取消下载
					if (stopByUser || stopForTimeout) {
						break;
					}

					final long size = (fileLength - position);
					shouldStopProgress = false;
					final int finalProgress = (int) position;
					final long finalSleepTime = sleepTime;
					progressThread = new Thread(new Runnable() {
						public void run() {
							int progress = finalProgress;

							while (!shouldStopProgress) {
								if (progressThread.isInterrupted()) {
									break;
								}
								if ((progress + progressUnit) < fileLength) {
									progress += progressUnit;
									remain = total - progress;
									updateProgress(handler, progress);
								} else {
									break;
								}

								try {
									Thread.sleep(finalSleepTime);
								} catch (InterruptedException e) {
									break;
								}
							}
							// 一块实际上传完了
							progress = (int) (finalProgress + size);
							remain = total - progress;
							updateProgress(handler, progress);
							shouldStopProgress = false;
						}
					});
					progressThread.start();

					// 写HTTP头
					outStream.write((TOW_HYPHENS + BOUNDARY + END).getBytes());
					outStream.write(tempStr1.getBytes());
					outStream.write(END.getBytes());

					// 写文件
					RandomAccessFile raf = new RandomAccessFile(new File(filePath), "rw");
					raf.seek(position);
					int len = raf.read(buffer);
					outStream.write(buffer, 0, len);

					// 写HTTP尾
					outStream.write(END.getBytes());
					outStream.write((TOW_HYPHENS + BOUNDARY + TOW_HYPHENS + END)
							.getBytes());
					outStream.flush();

					is = httpURLConnection.getInputStream();
					Log.e(TAG, position + " - end(" + fileLength + ") finished");
					handler.removeMessages(UploadActivity.MESSAGE_UPLOAD_STOP);
					shouldStopProgress = true;
					progressThread.interrupt();
					isFinished = true;

					break;
				}
			}

			if (!stopByUser && !stopForTimeout) {
				// 接收、解析上传结果xml
				is = httpURLConnection.getInputStream();
				UploadResultParser parser = new UploadResultParser(is);
				UploadResultBean result = parser.getMessageUploadResult();

				Log.e(TAG, result.toString());

				// 已上传好了，保存到MessageUploaded数据库，以后不再上传
				try {
					// 获取服务器生成的留言记录id
					String msgSntId = result.getId();

					// 【服务器留言记录id】和【本地录制记录id】组成【已上传记录】，以后无需再次上传
					if (msgSntId != null && !"null".equals(msgSntId)) {
						UploadCompleteBean messageUploaded = new UploadCompleteBean(file_id,
								msgSntId);
						UploadCompleteDBService manager = new UploadCompleteDBService(context);
						Log.e(TAG,
								"MessageUploadedManager.add(" + messageUploaded.toString()
										+ ")");

						manager.add(messageUploaded);
					}
				} catch (Exception e) {
					Log.e(TAG, "saving MessageUploaded has exception " + e.toString());
					e.printStackTrace();
				}

				// 更新上传时间，与服务器同步
				SentDetailDBService sentManager = new SentDetailDBService(context);
				for (int i = 0; i < snd_id.length; i++) {
					// sentManager.updateMessageSendTime(snd_id[i], result.getSendDate());
					sentManager.updateMessageSentCode(snd_id[i],
							Util.CODE_SENT_SUCCEED);
				}

				this.isFinished = true;
				handler
						.sendEmptyMessage(UploadActivity.MESSAGE_UPLOAD_SUCCEED);

				// 检查用户是否已登录，如果未登录，则发送短信提醒
//				for (int i = 0; i < rcverNumber.length; i++) {
//					StatusUtil.sendMissMessageSMSIfNotLoggin(this.snderNumber,
//							this.rcverNumber[i]);
//				}
			}
			is.close();
		} catch (Exception e) {
			Log.e(TAG, "upload Exception");
			Log.e(TAG, e.toString());
			e.printStackTrace();

			if (stopByUser) { // 用户点击取消
				Log.e(TAG, "upload cancel by user");
			} else if (stopForTimeout) { // 连接超时
				Log.e(TAG, "upload stop for timeout");
			} else {
				handler.sendEmptyMessage(UploadActivity.MESSAGE_UPLOAD_FAILD);
			}
		} finally {
			Log.e(TAG, "finally isFinished = " + isFinished + ", stopByUser = "
					+ stopByUser);
			UploadThreadPool.remove(file_id);
			this.isStoped = true;

			stopCheckNetworkThread();

			// 记录已上传块号。如果上传结束或者点击取消，则删除数据库记录，不再断点续传
			if (isFinished || stopByUser) {
				uploadPartDBService.delete(file_id);
			} else {
				uploadPartDBService.update(uploadPartBean);
			}
		}
	}

	/** 更新进度条UI */
	private static void updateProgress(Handler handler, int progress) {
		Message msg3 = Message.obtain();
		msg3.what = UploadActivity.MESSAGE_UPDATE_PROGRESS;
		msg3.arg1 = (int) progress;
		handler.sendMessage(msg3);
	}

	/** 每3秒检查一次网络状态 */
	private void startCheckNetworkThread() {
		checkNetwordThread = new Thread(new Runnable() {
			public void run() {
				Log.e(TAG, " ");
				Log.e(TAG, "startCheckNetworkThread");
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
				Log.e(TAG, "checkNetwordThread finished");
				Log.e(TAG, " ");
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
		Log.e(TAG, "gprsState = " + getNetworkStateString(gprsState)
				+ ", wifiState = " + getNetworkStateString(wifiState));
		if (android.net.NetworkInfo.State.CONNECTED != gprsState
				&& android.net.NetworkInfo.State.CONNECTED != wifiState) {

			// 断开连接
			closeConnection();

			isStoped = true;
			handler.removeMessages(UploadActivity.MESSAGE_UPLOAD_STOP);
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
		Log.e(TAG, "closeConnection(), httpURLConnection != null "
				+ (httpURLConnection != null) + ", outStream != null) "
				+ (outStream != null));

		// 断开链接
		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
			httpURLConnection = null;
		}
		if (outStream != null) {
			try {
				outStream.close();
				outStream = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void notifyUploadBegin(Handler handler, int total) {
		Message msg = Message.obtain();
		msg.what = UploadActivity.MESSAGE_UPLOAD_BEGIN;
		msg.arg1 = total;
		handler.sendMessage(msg);
	}

	private HttpURLConnection getManagedConnection(URL url) throws IOException {



		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		httpURLConnection.setDoInput(true);
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setUseCaches(false);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
		httpURLConnection.setRequestProperty("Charset", "UTF-8");
		httpURLConnection.setRequestProperty("Content-Type",
				"multipart/form-data;BOUNDARY=" + BOUNDARY);
		httpURLConnection.setConnectTimeout(10 * 1000); // 连接超时
		httpURLConnection.setReadTimeout(Util.UPLOAD_OR_DOWNLOAD_TIME_OUT); // 读取内容超时

		return httpURLConnection;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public boolean isStoped() {
		return isStoped;
	}

	public void stopIt() {
		handler.sendEmptyMessage(UploadActivity.MESSAGE_UPLOAD_CANCEL);
		stopByUser = true;
		stopForTimeout = false;
		isStoped = true;

		// 断开链接
		closeConnection();
	}

	public void stopForTimeout() {
		new Thread(new Runnable() {
			public void run() {
				stopByUser = false;
				stopForTimeout = true;
				isStoped = true;

				// 断开链接
				closeConnection();
			}
		}).start();
	}

	public int getDone() {
		return (total - remain);
	}

	public int getTotal() {
		return total;
	}
}
