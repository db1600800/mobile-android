package com.compoment.downloading_breakpoint_continue.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.util.Log;

import com.compoment.downloading_breakpoint_continue.util.Urls;
import com.compoment.downloading_breakpoint_continue.util.Util;


public class MessageReceivedUpdate {
	private Map<String, String> heads = new HashMap<String, String>();
	private Map<String, String> cookies = new HashMap<String, String>();

	/** 向服务器请求未读留言 */
	public synchronized static void updateMessageBoxReceived(Context context,
			String number) throws Exception {
		MessageReceivedUpdate update = new MessageReceivedUpdate();
		String url = Urls.getCheckNewMsgURL(number);
		byte[] buffer = update.getNewMessagesXML(url);

		if (buffer != null) {
			InputStream is = new ByteArrayInputStream(buffer);

			try {
				MessageReceivedParser parser = new MessageReceivedParser(is);

				List<MessageReceived> messageReceiveds = parser
						.getMessageReceivedList();

				int size = messageReceiveds.size();

				if (size > 0) {

					MessageReceivedManager manager = new MessageReceivedManager(context);

					for (int i = 0; i < size; i++) {
						MessageReceived messageReceived = messageReceiveds.get(i);

						messageReceived.setReceiverPhoneNumber(Util
								.getMyPhoneNumber()); // 设置接受人号码。为了在数据库中可以查出
						manager.addMessageReceived(messageReceived);

						// 下载视频预览图
						if (Util.MESSAGE_TYPE_VIDEO
								.equals(messageReceived.getType())) {
							String imageUrl = messageReceived.getPreviewImageUrl();

							// String imageName = imageUrl
							// .substring(imageUrl.lastIndexOf("/") + 1);

							String imageName = Util.generateRandomNumericString(15);

							dowloadPreImage(context, messageReceived.getId(),
									Urls.getPreVideoImageUrl(imageUrl), imageName,
									Util.getPreviewImageDownloadDirPath());
						}
					}
				}
			} catch (Exception e) {
				Log.e(Util.TAG, "error updateMessageBoxReceived");
			}
		}

		downloadPreviewImageIfFileLost(context);
	}

	private static boolean isCheckPreviewImageThreadRunning = false;

	/** 遍历留言收件箱，检查视频预览图，如果无预览图，则重新下载。如果正在执行检查中，则忽略此次调用 */
	public synchronized static void downloadPreviewImageIfFileLost(
			final Context context) {
		if (!isCheckPreviewImageThreadRunning) {
			isCheckPreviewImageThreadRunning = true;
			new Thread(new Runnable() {
				public void run() {

					List<MessageReceived> list_MessageReceiveds = new MessageReceivedManager(
							context).getReceivedsByReceiverNumber(Util
							.getMyPhoneNumber());

					for (int i = 0, size = list_MessageReceiveds.size(); i < size; i++) {
						MessageReceived messageReceived = list_MessageReceiveds.get(i);
						String previewImagePath = messageReceived.getPreviewImagePath(); // 预览图本地文件路径

						if (Util.MESSAGE_TYPE_VIDEO.equals(messageReceived.getType())
								&& previewImagePath != null) { // 如果是新留言，则previewImagePath为null，直到首次下载完成
							File file = new File(previewImagePath);

							if (!file.exists() || file.length() == 0) {
								String imageName = Util.generateRandomNumericString(15);

								Log.e("MessageReceivedUpdate",
										"preview image lost. start to re-download");

								dowloadPreImage(context, messageReceived.getId(), Urls
										.getPreVideoImageUrl(messageReceived.getPreviewImageUrl()),
										imageName, Util.getPreviewImageDownloadDirPath());
							}
						}
					}

					isCheckPreviewImageThreadRunning = false;
				}
			}).start();
		}
	}

	/** 下载收到留言的预览图 */
	public synchronized static void dowloadPreImage(final Context context,
			final String messageReceivedId, final String url, String imageName,
			String filePath) {
		final String path = filePath + imageName;

		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				Log.e(Util.TAG, e.toString());
				e.printStackTrace();
			}
		}

		new Thread(new Runnable() {
			public void run() {

				try {
					URL myURL = new URL(url);
					URLConnection conn = myURL.openConnection();
					conn.connect();

					InputStream is = conn.getInputStream();
					if (is == null) {
						throw new RuntimeException("stream is null");
					}
					File file = new File(path);
					OutputStream os = new FileOutputStream(file);
					byte buf[] = new byte[1024];
					do {
						int length = is.read(buf);
						if (length == -1) {
							break;
						}
						os.write(buf, 0, length);
						os.flush();
					} while (true);
					is.close();
					os.close();

					Thread.sleep(500); // TODO 避免其他线程在写msg_rcv.xml，有没有更安全的方法？
					// 如果完整下载好，则更新数据库，以后不再下载
					MessageReceivedManager manager = new MessageReceivedManager(context);
					manager
							.updateMessageReceivedPreviewImagePath(messageReceivedId, path);
				} catch (Exception e) {
					Log.e(Util.TAG, e.toString());
					Log.e(Util.TAG, "image download exception");
					e.printStackTrace();
				}
			}
		}).start();
	}

	public byte[] getNewMessagesXML(String url) throws Exception {
		HttpURLConnection urlCon = null;
		try {
			urlCon = (HttpURLConnection) (new URL(url)).openConnection();
			urlCon.setDoOutput(true);
			urlCon.setRequestMethod("GET");
			urlCon.setUseCaches(false);
			urlCon.setDoInput(true);
			// 设置连接、访问超时
			urlCon.setConnectTimeout(10 * 1000);
			urlCon.setReadTimeout(10 * 1000);
			if (cookies != null) {
				String cookieStr = "";
				Set<String> keys = cookies.keySet();
				for (Iterator<String> it = keys.iterator(); it.hasNext();) {
					String key = it.next();
					cookieStr += " " + key + "=" + cookies.get(key) + ";";
				}
				if (cookieStr.length() > 0) {
					urlCon.addRequestProperty("Cookie", cookieStr);
				}
			}
			urlCon
					.setRequestProperty(
							"User-Agent",
							"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			if (heads != null) {
				Set<String> keys = heads.keySet();
				for (Iterator<String> it = keys.iterator(); it.hasNext();) {
					String key = it.next();
					urlCon.setRequestProperty(key, heads.get(key));
				}
			}
			InputStream input = urlCon.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int count = -1;
			while ((count = input.read(buffer, 0, 1024)) != -1) {
				baos.write(buffer, 0, count);
			}
			Map<String, List<String>> fileds = urlCon.getHeaderFields();
			Set<String> keys = fileds.keySet();
			for (Iterator<String> it = keys.iterator(); it.hasNext();) {
				String key = it.next();
				List<String> values = fileds.get(key);
				for (int i = 0; i < values.size(); i++) {
					String value = values.get(i);
					if ("Set-Cookie".equals(key)) {
						String nameValueStr = value.split(";")[0];
						String[] nameValue = nameValueStr.split("=");
						cookies.put(nameValue[0], nameValue[1]);
					}
				}
			}
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 连接服务器，更新服务器is_readed字段
	public byte[] updateMessageIsReaded(final String msg_id) {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				String url = Urls.getMarkAsReadedURL(msg_id);
				HttpURLConnection urlCon = null;
				try {
					urlCon = (HttpURLConnection) (new URL(url)).openConnection();
					urlCon.setDoOutput(true);
					urlCon.setRequestMethod("GET");
					urlCon.setUseCaches(false);
					urlCon.setDoInput(true);
					if (cookies != null) {
						String cookieStr = "";
						Set<String> keys = cookies.keySet();
						for (Iterator<String> it = keys.iterator(); it.hasNext();) {
							String key = it.next();
							cookieStr += " " + key + "=" + cookies.get(key) + ";";
						}
						if (cookieStr.length() > 0) {
							urlCon.addRequestProperty("Cookie", cookieStr);
						}
					}
					urlCon
							.setRequestProperty(
									"User-Agent",
									"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
					if (heads != null) {
						Set<String> keys = heads.keySet();
						for (Iterator<String> it = keys.iterator(); it.hasNext();) {
							String key = it.next();
							urlCon.setRequestProperty(key, heads.get(key));
						}
					}
					InputStream input = urlCon.getInputStream();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int count = -1;
					while ((count = input.read(buffer, 0, 1024)) != -1) {
						baos.write(buffer, 0, count);
					}
					Map<String, List<String>> fileds = urlCon.getHeaderFields();
					Set<String> keys = fileds.keySet();
					for (Iterator<String> it = keys.iterator(); it.hasNext();) {
						String key = it.next();
						List<String> values = fileds.get(key);
						for (int i = 0; i < values.size(); i++) {
							String value = values.get(i);
							if ("Set-Cookie".equals(key)) {
								String nameValueStr = value.split(";")[0];
								String[] nameValue = nameValueStr.split("=");
								cookies.put(nameValue[0], nameValue[1]);
							}
						}
					}
					// return baos.toByteArray();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		return null;
	}
}
