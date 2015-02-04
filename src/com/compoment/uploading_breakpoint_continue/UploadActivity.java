package com.compoment.uploading_breakpoint_continue;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import com.android_demonstrate_abstractcode.R;
import com.compoment.uploading_breakpoint_continue.db.FileDetailBean;
import com.compoment.uploading_breakpoint_continue.db.FileDetailDBService;
import com.compoment.uploading_breakpoint_continue.db.SentDetailBean;
import com.compoment.uploading_breakpoint_continue.db.SentDetailDBService;
import com.compoment.uploading_breakpoint_continue.db.UploadCompleteBean;
import com.compoment.uploading_breakpoint_continue.db.UploadCompleteDBService;
import com.compoment.uploading_breakpoint_continue.db.UploadPartBean;
import com.compoment.uploading_breakpoint_continue.db.UploadPartDBService;
import com.compoment.uploading_breakpoint_continue.http.UploadThread;
import com.compoment.uploading_breakpoint_continue.http.UploadThreadPool;
import com.compoment.uploading_breakpoint_continue.util.Util;
import com.compoment.uploading_breakpoint_continue.util.Urls;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * . 上传
 * */
public class UploadActivity extends Activity {
	private static final String TAG = "MessageUploadingActivity";

	public static final int MESSAGE_UPLOAD_SUCCEED = 0;
	public static final int MESSAGE_UPLOAD_FAILD = 1;
	public static final int MESSAGE_UPLOAD_BEGIN = 2;
	public static final int MESSAGE_UPDATE_PROGRESS = 3;
	public static final int MESSAGE_UPLOAD_STOP = 4;
	public static final int MESSAGE_UPLOAD_CANCEL = 5;
	public static final int REQUEST_CODE_SELECT_CONTACT = 0;



	private SentDetailBean sentDetailBean = null;
	private String fileDetailBeanId = null; // 文件id
	private UploadPartBean messageUploading = null; // 上传进度数据库对象

	// 上传进度
	private int total = 0;
	private int done = 0;
	// UI组件
	private AlertDialog dialog = null;
	private TextView text_Title;
	private TextView text_State;
	private TextView text_Done;
	private TextView text_Total;
	private Button btn_HideDialog;
	private Button btn_Cancel;

	private ProgressBar progressBar1;
	// 资源对象
	private Resources resources;

	//
	private int duration;
	private String type;
	private String path;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.uploading_breakpoint_continue);

		// 判断SD Card是否插入
		boolean isSdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (!isSdCardExist) {
			toastShort("留言发送失败：请先插入SD卡");
			finish();
			return;
		}

		// 保持屏幕常亮
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		resources = getResources();



		//传进来的参数（测试数据）
		fileDetailBeanId = Util.createId();//如果是新文件就生成个
		final String snderNumber = "18820070027";
		final String receiverPhoneNumber = "18820070027";// null
		duration = 60000;
		type = "video";
		path = "/sdcard/1.mp3";



		// 查看是否断点上传
		UploadPartDBService manager = new UploadPartDBService(this);
		messageUploading = manager.queryUploadingById(fileDetailBeanId);

		if (messageUploading == null) { // 无断点，无文件记录，新建一个发送记录，开始上传
			FileDetailDBService fileDetailProvider = new FileDetailDBService(this);
			FileDetailBean fileDetailBean = fileDetailProvider
					.getFileDetailBeanRecordById(fileDetailBeanId);

			if (fileDetailBean == null)//无文件记录,新建一个文件bean
			{
				fileDetailBean = new FileDetailBean();
				fileDetailBean.setDuration(60000);
				fileDetailBean.setId(fileDetailBeanId);
				fileDetailBean.setPath(path);
				fileDetailBean.setType("audio");

			}

			sentDetailBean = new SentDetailBean();
			sentDetailBean.setId(Util.createId()); // 随机生成id
			sentDetailBean.setCode(Util.CODE_SENT_SENDING);
			sentDetailBean.setFileDetailBean(fileDetailBean);
			sentDetailBean.setSenderPhoneNumber(Util.getMyPhoneNumber());
			sentDetailBean.setSendTime(Calendar.getInstance());
			sentDetailBean.setReceiverPhoneNumber(receiverPhoneNumber); // 设置接收人号码

			//Sent行为  保存本地db
			SentDetailDBService sentManager = new SentDetailDBService(this);
			sentManager.addMessageSent(sentDetailBean);


			// 查看文件是否已经上传过(查  已完成上传  表)
			UploadCompleteDBService uploadAllProvider = new UploadCompleteDBService(
					this);
			final UploadCompleteBean messageUploaded = uploadAllProvider.queryMessageUploadedByMsgRcdId(fileDetailBeanId);

			if (messageUploaded != null) {
				// 调用转发接口
				Log.e(TAG, "forwardMessage " + messageUploaded.toString());

				toastShort("正在发送留言");
				Thread thread = new Thread(new Runnable() {
					public void run() {
						boolean isForwardSucceed = forwardMessage(snderNumber,
								receiverPhoneNumber,
								messageUploaded.getMsgSntId());
						if (isForwardSucceed) {
							toastLong(0);

							// 更新留言上传结果
							SentDetailDBService sentManager = new SentDetailDBService(
									UploadActivity.this);
							sentManager.updateMessageSentCode(
									sentDetailBean.getId(),
									Util.CODE_SENT_SUCCEED);

							// 检查用户是否已登录，如果未登录，则发送短信提醒
							// StatusUtil.sendMissMessageSMSIfNotLoggin(
							// messageSent.getSenderPhoneNumber(), rcverNumber);
						} else {
							toastLong(1);
						}
					}
				});
				thread.start();

				finish();
			} else {
			//	Log.e(TAG, "sendMessage " + sentDetailBean.toString());
				// 发送消息上传到服务器
				sendMessage(sentDetailBean.getId(), sentDetailBean
						.getFileDetailBean().getId(), snderNumber,
						receiverPhoneNumber, duration, type, path);
			}

		} else { // 从断点开始传
			sendMessage(messageUploading.getSnt_id(),
					messageUploading.getFile_Id(),
					messageUploading.getSnderNumber(),
					messageUploading.getRcverNumber(),
					messageUploading.getDuration(), messageUploading.getType(),
					messageUploading.getPath());
		}
	}

	private void showUploadDialog() {
		// 显示上传对话框
		Builder builder = new Builder(UploadActivity.this);
		// 创建一个列表对话框
		dialog = builder.create();
		dialog.show();

		// 修改对话框的样式
		LayoutInflater inflater = LayoutInflater.from(UploadActivity.this);
		View dialogView = inflater.inflate(
				R.layout.uploading_or_downloading_dialog, null);

		text_Title = (TextView) dialogView.findViewById(R.id.textView1);
		text_State = (TextView) dialogView.findViewById(R.id.textView2);
		text_Done = (TextView) dialogView.findViewById(R.id.textView3);
		text_Total = (TextView) dialogView.findViewById(R.id.textView5);
		btn_HideDialog = (Button) dialogView.findViewById(R.id.button1);
		btn_Cancel = (Button) dialogView.findViewById(R.id.button2);
		progressBar1 = (ProgressBar) dialogView.findViewById(R.id.progressBar1);

		dialog.setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface arg0) {
				finish();
			}
		});

		text_Title.setText("发送");
		text_State.setText("正在连接服务器");

		dialog.setContentView(dialogView);
		dialog.setCanceledOnTouchOutside(true);

		btn_HideDialog.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		btn_Cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (messageUploadThread != null) {
					messageUploadThread.stopIt();
				}
				// dialog.cancel();
			}
		});
	}

	boolean shouldShowUploadingInBackgroundToast = true;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			// MessageUploadingNotificationManager manager = null;
			// MessageUploadingNotificationManager.getInstance(MessageUploadingActivity.this);

			switch (msg.what) {
			case MESSAGE_UPLOAD_SUCCEED:
				UploadThreadPool.clear(); // 清空上传池
				// MessageBoxScreen.updateRecordViews();
				toastLong(0);
				shouldShowUploadingInBackgroundToast = false;

				// 取消Notification
				// if (manager != null) {
				// manager.cancel();
				// }

				dialog.cancel();

				break;
			case MESSAGE_UPLOAD_FAILD:
				UploadThreadPool.clear(); // 清空上传池
				// MessageBoxScreen.updateRecordViews();
				// 终止上传进程
				if (messageUploadThread != null
						&& !messageUploadThread.isInterrupted()) {
					messageUploadThread.interrupt();
				}

				// 取消Notification
				// if (manager != null) {
				// manager.cancel();
				// }

				toastLong(1);
				dialog.cancel();
				break;
			case MESSAGE_UPLOAD_BEGIN:

				// MessageBoxScreen.updateRecordViews();

				total = msg.arg1;
				text_State.setText("正在发送");
				progressBar1.setProgress(0);
				progressBar1.setMax(total);
				text_Total.setText("" + (total / 1024));
				break;
			case MESSAGE_UPDATE_PROGRESS:
				done = msg.arg1;
				text_Done.setText("" + (done / 1024));
				progressBar1.setProgress(done);
				break;
			case MESSAGE_UPLOAD_STOP:
				UploadThreadPool.clear(); // 清空上传池

				// MessageBoxScreen.updateRecordViews();

				toastShort("留言发送失败：网络超时，请检查网络");

				// 取消Notification
				// if (manager != null) {
				// manager.cancel();
				// }

				dialog.cancel();
				// 终止上传进程
				messageUploadThread.stopForTimeout();
				handler.removeMessages(MESSAGE_UPLOAD_BEGIN);
				handler.removeMessages(MESSAGE_UPLOAD_SUCCEED);
				handler.removeMessages(MESSAGE_UPLOAD_FAILD);
				handler.removeMessages(MESSAGE_UPDATE_PROGRESS);
				break;
			case MESSAGE_UPLOAD_CANCEL:
				toastShort("留言发送已取消");

				// MessageBoxScreen.updateRecordViews();

				// 取消Notification
				// if (manager != null) {
				// manager.cancel();
				// }

				dialog.cancel();
				break;
			default:
				break;
			}
		}
	};

	/** 发送消息，上传媒体文件到服务器 */
	private void sendMessage(final String snd_id, final String rcd_id,
			final String snderNumber, final String rcverNumber,
			final int duration, final String type, final String path) {

		showUploadDialog();
		text_State.setText("正在连接服务器");

		uploadThread = new Thread(new Runnable() {
			public void run() {
				if (messageUploading != null
						&& messageUploading.getRcverNumber()
								.equals(rcverNumber)
						&& messageUploading.getSnderNumber().equals(
								Util.getMyPhoneNumber())) { // 从数据库读取上传记录
					messageUploadThread = new UploadThread(UploadActivity.this,
							messageUploading, handler);
				} else { // 新建上传记录
					messageUploadThread = new UploadThread(UploadActivity.this,
							snd_id, rcd_id, snderNumber, rcverNumber, duration,
							type, path, handler);
				}
				messageUploadThread.start();

				// 等待上传线程结束
				while (true) {
					if (messageUploadThread.isStoped()) {
						break;
					} else {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		uploadThread.start();
	}

	private Thread uploadThread;
	private UploadThread messageUploadThread;

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_CANCELED:
			finish();
			break;
		case RESULT_OK:
			FileDetailDBService messageRecordManager = new FileDetailDBService(
					this);
			final FileDetailBean messageRecord = messageRecordManager
					.getFileDetailBeanRecordById(fileDetailBeanId);
			final String snderNumber = Util.getMyPhoneNumber();
			final String receiverNumbers = data.getStringExtra("numbers");
			String[] receiverNumber = receiverNumbers.split(";");

			String snd_id = ""; // 保存多个sendId，用";"分隔
			for (int i = 0; i < receiverNumber.length; i++) {
				sentDetailBean = new SentDetailBean();
				sentDetailBean.setId(Util.createId()); // 随机生成id
				snd_id += sentDetailBean.getId() + ";";
				sentDetailBean.setCode(Util.CODE_SENT_SENDING);
				sentDetailBean.setFileDetailBean(messageRecord);
				sentDetailBean.setSenderPhoneNumber(Util.getMyPhoneNumber());
				sentDetailBean.setSendTime(Calendar.getInstance());
				sentDetailBean.setReceiverPhoneNumber(receiverNumber[i]); // 设置接收人号码
				// 发送消息保存在本地
				SentDetailDBService manager = new SentDetailDBService(
						UploadActivity.this);
				manager.addMessageSent(sentDetailBean);
			}
			// 去掉末尾的";"号
			if (!"".equals(snd_id)) {
				snd_id = snd_id.substring(0, snd_id.length() - 1);
			}

			// 查看留言是否已经上传过
			UploadCompleteDBService messageUploadedManager = new UploadCompleteDBService(
					this);
			final UploadCompleteBean messageUploaded = messageUploadedManager
					.queryMessageUploadedByMsgRcdId(fileDetailBeanId);

			if (messageUploaded != null) {
				// 调用转发接口
				Log.e(TAG, "forwardMessage " + messageUploaded.toString());

				final String snd_id_final = new String(snd_id);
				toastShort("开始发送留言");
				Thread thread = new Thread(new Runnable() {
					public void run() {
						boolean isForwardSucceed = forwardMessage(snderNumber,
								receiverNumbers, messageUploaded.getMsgSntId());
						if (isForwardSucceed) {
							toastLong(0);

							String[] snd_ids = snd_id_final.split(";");
							// 更新留言上传结果
							SentDetailDBService sentManager = new SentDetailDBService(
									UploadActivity.this);

							for (int i = 0; i < snd_ids.length; i++) {
								sentManager.updateMessageSentCode(snd_ids[i],
										Util.CODE_SENT_SUCCEED);
							}

							// 检查用户是否已登录，如果未登录，则发送短信提醒
							// StatusUtil.sendMissMessageSMSIfNotLoggin(
							// messageSent.getSenderPhoneNumber(),
							// messageSent.getReceiverPhoneNumber());
						} else {
							// 刷新界面中的

							// MessageBoxScreen.updateRecordViews();
							toastLong(1);
						}
					}
				});
				thread.start();

				finish();
			} else {
				Log.e(TAG, "sendMessage " + sentDetailBean.toString());
				// 发送消息上传到服务器
				sendMessage(snd_id, sentDetailBean.getFileDetailBean().getId(),
						snderNumber, receiverNumbers, duration, type, path);
			}

			break;
		default:
			break;
		}
	}

	/** 转发留言 */
	private boolean forwardMessage(String snderNumber, String rcverNumber,
			String msgRcvId) {
		boolean result = true;
		String forwardURL = Urls.getForwardURL(snderNumber, rcverNumber,
				msgRcvId);

		try {
			URL myurl = new URL(forwardURL);
			HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
			conn.setDoInput(true);
			conn.setReadTimeout(1000 * 10);
			conn.setConnectTimeout(1000 * 10);
			conn.connect();
			InputStream is = conn.getInputStream(); // 如果成功，返回<result>true</result>

			StringBuffer out = new StringBuffer();
			byte[] b = new byte[4096];
			for (int n; (n = is.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}

			String response = out.toString();
			Log.e("MessageUploadingActivity", "forward msgRcvId=" + msgRcvId
					+ ": [" + response + "]");

			// 如果为空，则为转发失败。
			if (response == null || "".equals(response)
					|| "null".equals(response)) {
				result = false;
			}

			is.close();
		} catch (Exception e) {
			result = false;
			Log.e(Util.TAG, "forwardMessage() has exception");
			Log.e(Util.TAG, e.toString());
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * @param code
	 *            0: "留言发送成功"; 1: "留言发送失败：无法连接服务器";
	 * */
	private void toastLong(int code) {
		switch (code) {
		case 0:
			toastLong("留言发送成功");
			break;
		case 1:
			toastLong("留言发送失败：无法连接服务器");
			break;
		default:
			break;
		}
	}

	private void toastShort(final String text) {
		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(UploadActivity.this, text, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	private void toastLong(final String text) {
		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(UploadActivity.this, text, Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	protected void onDestroy() {
		if (messageUploadThread != null && !messageUploadThread.isInterrupted()
				&& !messageUploadThread.isStoped()
				&& shouldShowUploadingInBackgroundToast) {
			toastShort("留言在后台继续发送");

			// MessageUploadingNotificationManager manager =
			// MessageUploadingNotificationManager
			// .getInstance(MessageUploadingActivity.this);
			// manager.show(recordId);
		} else {
			handler.removeMessages(MESSAGE_UPLOAD_STOP);
			handler.removeMessages(MESSAGE_UPDATE_PROGRESS);
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}
}