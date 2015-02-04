package com.compoment.downloading_breakpoint_continue;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;

import com.android_demonstrate_abstractcode.R;
import com.compoment.downloading_breakpoint_continue.db.MessageReceivedManager;
import com.compoment.downloading_breakpoint_continue.http.DownloadThread;
import com.compoment.downloading_breakpoint_continue.http.DownloadThreadPool;
import com.compoment.downloading_breakpoint_continue.util.Util;
import com.compoment.downloading_breakpoint_continue.util.Urls;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//http://www.cnblogs.com/zxl-jay/archive/2011/10/09/2204195.html
//http://llb988.blog.51cto.com/1940549/510035
//http://gundumw100.iteye.com/blog/906072


public class DownloadActivity extends Activity {
	public static final int MESSAGE_DOWNLOAD_BEGIN = 0;
	public static final int MESSAGE_UPDATE_PROGRESS = 1;
	public static final int MESSAGE_DOWNLOAD_SUCCEED = 2;
	public static final int MESSAGE_DOWNLOAD_FAILD = 3;
	public static final int MESSAGE_STOP_DOWNLOAD = 4;
	public static final int MESSAGE_NOT_ENOUGH_MEMORY = 5;
	public static final int MESSAGE_STOP_IT = 6;
	// UI组件
	private TextView text_Title;
	private TextView text_State;
	private TextView text_Done;
	private TextView text_Total;
	private Button btn_HideDialog;
	private Button btn_Cancel;
	private ProgressBar progressBar1;

	private Thread downloadThread;

	private int done = 0; // 已下载长度
	private long contentLength = 0; // 总下载长度

	private String id;
	private String url;
	private String path;
	private String type;

	private AlertDialog dialog = null;
	// 资源对象
	private Resources resources;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 保持屏幕常亮
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.downloading_breakpoint_continue_type_dialog);

		// 判断SD Card是否插入
		boolean isSdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (!isSdCardExist) {
			Toast.makeText(DownloadActivity.this, "请先插入SD卡",
					Toast.LENGTH_SHORT).show();
			DownloadThreadPool.readyToPut = false;
			finish();
			return;
		}

		if (!Urls.isNetworkAvailable(this)) {
			Toast.makeText(DownloadActivity.this, "留言下载失败：无法连接到网络",
					Toast.LENGTH_SHORT).show();
			DownloadThreadPool.readyToPut = false;
			finish();
			return;
		}

		Builder builder = new Builder(DownloadActivity.this);
		// 创建一个列表对话框
		dialog = builder.create();
		dialog.show();

		// 修改对话框的样式
		LayoutInflater inflater = LayoutInflater
				.from(DownloadActivity.this);
		View dialogView = inflater.inflate(
				R.layout.downloading_or_uploading_dialog, null);

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

		dialog.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				if (arg1 == KeyEvent.KEYCODE_BACK) {
					dialog.cancel();
				}
				return false;
			}
		});

		resources = getResources();
		dialog.setContentView(dialogView);
		dialog.setCanceledOnTouchOutside(true);

		text_Title.setText("下载");
		text_State.setText("正在连接服务器");

		btn_HideDialog.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		btn_Cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (messageDownloadThread != null) {
					messageDownloadThread.stopIt();
				}
				dialog.cancel();
			}
		});

		// 从Intent取得数据
		Intent intent = getIntent();
		id = null;//intent.getStringExtra("id");
		url = "http://download.cksource.com/CKEditor/CKEditor/CKEditor%204.0/ckeditor_4.0_standard.zip";//intent.getStringExtra("url");
		type = "audio";//intent.getStringExtra("type");
		path = null;//intent.getStringExtra("path");

		File file = null;
		if (path != null && !"null".equals(path.trim()) && !"".equals(path.trim())) {
			file = new File(path);
		}

		if (path == null || "null".equals(path.trim()) || "".equals(path.trim())
				|| (file != null && !file.exists())) {
			// 生成文件路径
			if (Util.MESSAGE_TYPE_VIDEO.equals(type)) {
				path = Util.getDownloadVideoRecordDirPath()
						+ Util.createDownloadVideoFileName();
			} else {
				path = Util.getDownloadAudioRecordDirPath()
						+ Util.createDownloadAudioFileName();
			}

			// 更新msg_rcv.xml的path字段
			MessageReceivedManager manager = new MessageReceivedManager(
					DownloadActivity.this);
			manager.updateMessageReceivedPath(id, path);
		}

		downloadThread = new Thread(new Runnable() {
			public void run() {
				download(url, path);
			}
		});
		downloadThread.start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MESSAGE_UPDATE_PROGRESS:
				done = msg.arg1;
				text_Done.setText("" + (done / 1024));
				progressBar1.setProgress(done);
				break;
			case MESSAGE_DOWNLOAD_BEGIN:
				text_State.setText("正在下载");
				progressBar1.setProgress(0);
				progressBar1.setMax((int) contentLength);
				text_Total.setText("" + (contentLength / 1024));
				break;
			case MESSAGE_DOWNLOAD_SUCCEED:
			
				DownloadThreadPool.clear();
				dialog.cancel();
				break;
			case MESSAGE_DOWNLOAD_FAILD:
				// 终止下载进程
				if (downloadThread != null && !downloadThread.isInterrupted()) {
					downloadThread.interrupt();
				}
				if (messageDownloadThread != null
						&& messageDownloadThread.isInterrupted()) {
					messageDownloadThread.interrupt();
				}
				Toast.makeText(DownloadActivity.this, "留言下载失败：无法连接服务器",
						Toast.LENGTH_LONG).show();
				DownloadThreadPool.clear();
				dialog.cancel();
				break;
			case MESSAGE_STOP_DOWNLOAD:
				// 终止下载进程
				if (downloadThread != null && downloadThread.isInterrupted()) {
					downloadThread.interrupt();
				}
				if (messageDownloadThread != null
						&& messageDownloadThread.isInterrupted()) {
					messageDownloadThread.interrupt();
				}
				Toast.makeText(DownloadActivity.this, "留言下载失败：网络超时，请检查网络",
						Toast.LENGTH_SHORT).show();
				DownloadThreadPool.clear();
				dialog.cancel();
				break;
			case MESSAGE_NOT_ENOUGH_MEMORY:
				// 终止下载进程
				if (downloadThread != null && downloadThread.isInterrupted()) {
					downloadThread.interrupt();
				}
				if (messageDownloadThread != null
						&& messageDownloadThread.isInterrupted()) {
					messageDownloadThread.interrupt();
				}
				Toast.makeText(DownloadActivity.this, "留言下载失败：存储空间不足",
						Toast.LENGTH_SHORT).show();
				DownloadThreadPool.clear();
				dialog.cancel();
				break;
			case MESSAGE_STOP_IT:
				Toast.makeText(DownloadActivity.this, "留言下载已取消",
						Toast.LENGTH_SHORT).show();
				// 终止下载进程
				if (downloadThread != null && !downloadThread.isInterrupted()) {
					downloadThread.interrupt();
				}
				if (messageDownloadThread != null
						&& !messageDownloadThread.isInterrupted()) {
					messageDownloadThread.interrupt();
				}
				DownloadThreadPool.clear();
				if (dialog != null && dialog.isShowing()) {
					dialog.cancel();
				}
				break;
			default:
				break;
			}
		}
	};

	private DownloadThread messageDownloadThread;

	/** 下载文件，播放文件 */
	private void download(final String urlStr, final String path) {
		try {
			handler.sendEmptyMessageDelayed(MESSAGE_DOWNLOAD_FAILD,
					Util.UPLOAD_OR_DOWNLOAD_TIME_OUT);
			// 取得连接
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(10 * 1000);
			conn.setReadTimeout(10 * 1000);
			conn.connect();
			contentLength = conn.getContentLength();
			handler.removeMessages(MESSAGE_DOWNLOAD_FAILD);

			long sizeInByte = Util.getAvailableSizeOfSDCard();
			if (sizeInByte < contentLength) {
				Toast.makeText(DownloadActivity.this, "无法下载留言：SD卡空间不足",
						Toast.LENGTH_LONG).show();
				finish();
				return;
			}

			// 检查SD卡剩余空间
			long availableSize = Util.getAvailableSizeOfSDCard();
			if ((availableSize - 1 * (1024 * 1024)) < contentLength) { // 预留1MB，应变情况
				handler.sendEmptyMessage(MESSAGE_NOT_ENOUGH_MEMORY);
				return;
			}

			// 开始下载，根据contentLength，初始化进度条
			handler.sendEmptyMessage(MESSAGE_DOWNLOAD_BEGIN);
			File file = new File(path);

			// 等待其他下载线程被回收
			int count = 0;
			while (DownloadThreadPool.hasDownloadingThread() && count++ < 30) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// 强制清除下载池
			if (count == 30) {
				DownloadThreadPool.clear();
			}

			messageDownloadThread = new DownloadThread(
					DownloadActivity.this, id, url, file, handler);
			messageDownloadThread.start();

			// 等待下载线程结束
			while (true) {
				if (messageDownloadThread.isStoped()) {
					if (messageDownloadThread.isFinished()) {
						// 更新下载已完成，不再下载
//						MessageReceivedManager manager = new MessageReceivedManager(
//								MessageDownloadingActivity.this);
//						manager.updateMessageReceivedDownloadFinished(id, true);
//
//						startPlayActivity(type, path);
					}
					break;
				} else {
					try {
						Thread.sleep(250);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			handler.post(new Runnable() {
				public void run() {
					Toast.makeText(DownloadActivity.this, "留言下载失败：无法连接服务器",
							Toast.LENGTH_SHORT).show();
					DownloadThreadPool.readyToPut = false;
					dialog.cancel();
				}
			});
			e.printStackTrace();
		}
	}

	/** 启动Activity，播放文件 */
	private void startPlayActivity(String type, String path) {
//		Intent intent = null;
//		if (MessageUtil.MESSAGE_TYPE_VIDEO.equals(type)) {
//			intent = new Intent(MessageDownloadingActivity.this,
//					MessageVideoPlayActivity.class);
//		} else {
//			intent = new Intent(MessageDownloadingActivity.this,
//					MessageAudioPlayActivity.class);
//		}
//		intent.putExtra("path", path);
//		MessageDownloadingActivity.this.startActivity(intent);
	}

	protected void onDestroy() {
		if (messageDownloadThread != null && !messageDownloadThread.isInterrupted()
				&& !messageDownloadThread.isStoped()) {
			Toast.makeText(this,
					"留言在后台继续下载",
					Toast.LENGTH_SHORT).show();
		} else {
			handler.removeMessages(MESSAGE_DOWNLOAD_BEGIN);
			handler.removeMessages(MESSAGE_DOWNLOAD_SUCCEED);
			handler.removeMessages(MESSAGE_DOWNLOAD_FAILD);
			handler.removeMessages(MESSAGE_STOP_DOWNLOAD);
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