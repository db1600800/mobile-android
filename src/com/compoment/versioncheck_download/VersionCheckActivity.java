package com.compoment.versioncheck_download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.android_demonstrate_abstractcode.R;
import com.compoment.versioncheck_download.FileDownloadManager.FileDownloadListener;
import com.compoment.versioncheck_download.util.FilePathDealWith;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



public class VersionCheckActivity extends Activity implements FileDownloadListener {


	private Handler handler = new Handler();

	// UI
	private AlertDialog dialog_Downloading;
	private AlertDialog dialog_DownloadFinished;
	private AlertDialog dialog_Asking;
	private AlertDialog dialog_Error;

	private ProgressBar progressBar1;
	private TextView text_Current;
	private TextView text_Total;

	private File downloadFile;

	// 
	private static FileDownloadThread downloadThread = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.versioncheck);

		FileDownloadManager.createInstance(getApplicationContext());

		// findViews();

		FileDownloadManager manager = FileDownloadManager.getInstance();
		manager.removeFileDownloadNotification();
		int status = manager.getDownloadStatus();
		// System.out.println("============== status=" + status);
		if (status == FileDownloadManager.STATUS_NOREADY) {
			manager.setDownloadStatus(FileDownloadManager.STATUS_ASK);
			status = FileDownloadManager.STATUS_ASK;
		}

		Intent intent = getIntent();
		String downloadUrl = intent.getStringExtra("downloadUrl");
		long fileSize = intent.getLongExtra("fileSize", 0L);
		String fileVersion = intent.getStringExtra("fileVersion");
		String updateInfo = intent.getStringExtra("updateInfo");

		Log.e("AppUpdateActivity", "status = " + status);

		switch (status) {
		case FileDownloadManager.STATUS_ASK:
			// showAskingDialog();

			// �µĶԻ��򣬴��������
			showAskingDialog2(fileVersion, fileSize, updateInfo);
			break;
		case FileDownloadManager.STATUS_PROGRESS:
			showDownloadingDialog();
			break;
		case FileDownloadManager.STATUS_FINISH:
			showDownloadFinishedDialog();
			break;
		case FileDownloadManager.STATUS_ERROR:
			showErrorDialog();
			break;
		default:
			break;
		}


	}



	/** ���������̣߳���ʾ���ضԻ��� */
	private void startDownload() {
		showDownloadingDialog();

		FileDownloadManager manager = FileDownloadManager.getInstance();
		String fileUrl = manager.getDownloadFileUrl();
		long fileSize = manager.getDownloadTotalSize();

		downloadThread = new FileDownloadThread(fileUrl, fileSize);
		downloadThread.start();
	}

	/***/
	private void showAskingDialog() {
		Resources resources = getResources();
		String str_Title = "软件更新";
		String str_Message = "有新的版本，是否现在升级？";
		String str_Yes = "是";
		String str_No = "否";

		// ���ȷ�Ͽ�
		AlertDialog.Builder builder = new Builder(VersionCheckActivity.this);
		builder.setTitle(str_Title);
		builder.setMessage(str_Message);
		builder.setPositiveButton(str_Yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// ��ʼ����
				startDownload();
			}
		});
		builder.setNegativeButton(str_No, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// �ر�Activity
				VersionCheckActivity.this.finish();
				FileDownloadManager.getInstance().setDownloadStatus(
						FileDownloadManager.STATUS_NOREADY);
				FileDownloadManager.getInstance().reset();
			}
		});
		dialog_Asking = builder.create();
		dialog_Asking.show();
		dialog_Asking.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					// �ر�Activity
					VersionCheckActivity.this.finish();
					FileDownloadManager.getInstance().setDownloadStatus(
							FileDownloadManager.STATUS_NOREADY);
					FileDownloadManager.getInstance().reset();
				}
				return false;
			}
		});
	}

	/** ѯ���Ƿ�ʼ���¡���������� */
	private void showAskingDialog2(String appVersion, long fileSize,
			String updateInfo) {
		Resources resources = getResources();
		String str_Title = "软件更新";
		// String str_Message =
		// resources.getString(R.string.app_update_ask_message);
		String str_Yes = "是";
		String str_No = "否";

		// ���ȷ�Ͽ�
		AlertDialog.Builder builder = new Builder(VersionCheckActivity.this);
		builder.setTitle("�����������");
		builder.setPositiveButton(str_Yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// ��ʼ����
				startDownload();
			}
		});

		View view = LayoutInflater.from(this).inflate(
				R.layout.versioncheck_description, null);

		TextView text_Version = (TextView) view.findViewById(R.id.textView2);
		TextView text_FileSize = (TextView) view.findViewById(R.id.textView4);
		TextView text_Description = (TextView) view.findViewById(R.id.textView6);

		text_Version.setText(appVersion);
		text_FileSize.setText((fileSize / 1024) + " KB");
		text_Description.setText(Html.fromHtml(updateInfo));

		builder.setView(view);

		builder.setNegativeButton(str_No, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// �ر�Activity
				VersionCheckActivity.this.finish();
				FileDownloadManager.getInstance().setDownloadStatus(
						FileDownloadManager.STATUS_NOREADY);
				FileDownloadManager.getInstance().reset();
			}
		});
		dialog_Asking = builder.create();
		dialog_Asking.show();
		dialog_Asking.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					// �ر�Activity
					VersionCheckActivity.this.finish();
					FileDownloadManager.getInstance().setDownloadStatus(
							FileDownloadManager.STATUS_NOREADY);
					FileDownloadManager.getInstance().reset();
				}
				return false;
			}
		});
	}

	/** ��ʾ���ؽ�ȶԻ��� */
	private void showDownloadingDialog() {
		View view = LayoutInflater.from(this).inflate(
				R.layout.versioncheck_downloading_dialog, null);
		progressBar1 = (ProgressBar) view.findViewById(R.id.progressBar1);
		text_Current = (TextView) view.findViewById(R.id.textView3);
		text_Total = (TextView) view.findViewById(R.id.textView5);

		Resources resources = getResources();
		String str_Title = "软件更新";
		String str_Hide = "隐藏";
		String str_Cancel = "取消";

		//
		AlertDialog.Builder builder = new Builder(VersionCheckActivity.this);
		builder.setTitle(str_Title);
		builder.setPositiveButton(str_Hide, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// �������ضԻ���
				dialog.cancel();
				VersionCheckActivity.this.finish();
				FileDownloadManager manager = FileDownloadManager.getInstance();
				manager.updateFileDownloadNotification();
				manager.setDownloadStatus(FileDownloadManager.STATUS_PROGRESS);
			}
		});
		builder.setNegativeButton(str_Cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// ȡ������
						if (downloadThread != null) {
							try {
								downloadThread.setRunning(false);
								downloadThread.interrupt();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						FileDownloadManager manager = FileDownloadManager.getInstance();
						manager.setDownloadCurrentSize(0);
						manager.setDownloadStatus(FileDownloadManager.STATUS_NOREADY);

						dialog.cancel();
						VersionCheckActivity.this.finish();
						Toast.makeText(VersionCheckActivity.this, "��ȡ�����", Toast.LENGTH_SHORT)
								.show();
					}
				});
		builder.setView(view);

		dialog_Downloading = builder.create();
		dialog_Downloading.show();
		dialog_Downloading.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					// �������ضԻ���
					dialog.cancel();
					VersionCheckActivity.this.finish();
					FileDownloadManager manager = FileDownloadManager.getInstance();
					manager.updateFileDownloadNotification();
					;
					manager.setDownloadStatus(FileDownloadManager.STATUS_PROGRESS);
				}
				return false;
			}
		});

		FileDownloadManager manager = FileDownloadManager.getInstance();
		manager.setDownloadStatus(FileDownloadManager.STATUS_PROGRESS);

		long currentSize = manager.getDownloadCurrentSize();
		long totalSize = manager.getDownloadTotalSize();

		if (totalSize > 1024) {
			text_Current.setText("" + (currentSize / 1024));
			text_Total.setText("" + (totalSize / 1024));
		} else {
			text_Current.setText("0");
			text_Total.setText("1");
		}

		double position = ((double) currentSize / (double) totalSize) * 100.0;
		progressBar1.setProgress((int) position);
		FileDownloadManager.setFileDownloadListener(VersionCheckActivity.this);
	}

	/** ѯ���Ƿ�װ���� */
	private void showDownloadFinishedDialog() {
		if (dialog_Downloading != null) {
			dialog_Downloading.cancel();
		}
		String filePath = FileDownloadManager.getInstance().getDownloadFilePath();
		if (filePath != null) {
			downloadFile = new File(filePath);
		}

		Resources resources = getResources();
		String str_Title = "软件更新";
		String str_Message = "文件下载成功，是否现在安装？";
		String str_Install = "安装";
		String str_Cancel = "取消";

		// ���ȷ�Ͽ�
		AlertDialog.Builder builder = new Builder(VersionCheckActivity.this);
		builder.setTitle(str_Title);
		builder.setMessage(str_Message);
		builder.setPositiveButton(str_Install,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// ��ʼ��װ
						if (downloadFile != null) {
							Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.setDataAndType(Uri.fromFile(downloadFile),
									"application/vnd.android.package-archive");
							startActivity(intent);
						}
						FileDownloadManager.getInstance().setDownloadStatus(
								FileDownloadManager.STATUS_NOREADY);
						VersionCheckActivity.this.finish();
					}
				});
		builder.setNegativeButton(str_Cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// ����װ����
						FileDownloadManager.getInstance().setDownloadStatus(
								FileDownloadManager.STATUS_NOREADY);
						VersionCheckActivity.this.finish();
					}
				});
		dialog_DownloadFinished = builder.create();
		dialog_DownloadFinished.show();
		dialog_DownloadFinished.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					// ����װ����
					FileDownloadManager.getInstance().setDownloadStatus(
							FileDownloadManager.STATUS_NOREADY);
					VersionCheckActivity.this.finish();
				}
				return false;
			}
		});
	}

	private void showErrorDialog() {
		Resources resources = getResources();
		String str_Title = "软件更新";
		String str_Confirm = "确认";

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(str_Title);
		builder.setMessage("�޷����£����������Ƿ���");
		builder.setPositiveButton(str_Confirm,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						finish();
						FileDownloadManager.getInstance().setDownloadStatus(
								FileDownloadManager.STATUS_NOREADY);
					}
				});
		dialog_Error = builder.create();
		dialog_Error.show();
		dialog_Error.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					VersionCheckActivity.this.finish();
				}
				return false;
			}
		});
	}



	@Override
	public void downloadError(int code, String msg) {
	}

	@Override
	public void downloadFinish(File downloadFile) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				// setFinishStatus();
				if (isActivityRunning) {
					showDownloadFinishedDialog();
				} else {
					Intent intent = new Intent(VersionCheckActivity.this,
							VersionCheckActivity.class);
					VersionCheckActivity.this.startActivity(intent);
				}
			}
		});
	}

	@Override
	public void downloadProgress(final long currentSize, final long totalSize) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				
				if (totalSize > 1024) {
					text_Current.setText("" + (currentSize / 1024));
					text_Total.setText("" + (totalSize / 1024));
				} else {
					text_Current.setText("0");
					text_Total.setText("1");
				}

				double position = ((double) currentSize / (double) totalSize) * 100.0;
				progressBar1.setProgress((int) position);
			}
		});
	}

	@Override
	public void downloadStart() {
	}

	@Override
	public void downloadCancel() {
	}

	/** �������жԻ��� */
	private void releaseDialogs() {
		if (dialog_Downloading != null && dialog_Downloading.isShowing()) {
			dialog_Downloading.cancel();
		}
		if (dialog_DownloadFinished != null && dialog_DownloadFinished.isShowing()) {
			dialog_DownloadFinished.cancel();
		}
		if (dialog_Asking != null && dialog_Asking.isShowing()) {
			dialog_Asking.cancel();
		}
		if (dialog_Error != null && dialog_Error.isShowing()) {
			dialog_Error.cancel();
		}
	}

	private static boolean isActivityRunning;

	protected void onResume() {
		isActivityRunning = true;
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		isActivityRunning = false;
		releaseDialogs();
		super.onDestroy();
	}

	class FileDownloadThread extends Thread {
		private boolean running = false;
		private String fileUrl;
		private long fileSize;

		public FileDownloadThread(String fileUrl, long fileSize) {
			this.fileUrl = fileUrl;
			this.fileSize = fileSize;
		}

		@Override
		public void run() {
			super.run();
			running = true;
			FileDownloadManager manager = FileDownloadManager.getInstance();
			manager.setDownloadCurrentSize(0);
			manager.setDownloadTotalSize(this.fileSize);
			manager.setDownloadStatus(FileDownloadManager.STATUS_PROGRESS);

			String fileName = "unknow.apk";
			String directory = FilePathDealWith.getStorageDirPath();
			int index = fileUrl.lastIndexOf("/");
			if (index != -1) {
				fileName = fileUrl.substring(index);
			}
			File tmpFile = new File(directory);
			if (!tmpFile.exists()) {
				tmpFile.mkdir();
			}
			final File file = new File(directory + "/" + fileName);
			if (file.exists()) {
				file.delete();
			}
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// ---�¼�֪ͨ---
			if (FileDownloadManager.getFileDownloadListener() != null) {
				FileDownloadManager.getFileDownloadListener().downloadStart();
			}
			// ------
			long downloadCount = 0;
			try {
				URL url = new URL(fileUrl);
				try {
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					InputStream is = conn.getInputStream();
					FileOutputStream fos = new FileOutputStream(file);
					byte[] buf = new byte[512 * 1024];
					conn.connect();
					double count = 0;
					if (conn.getResponseCode() >= 400) {
						// Toast.makeText(Main.this, "���ӳ�ʱ", Toast.LENGTH_SHORT).show();
					} else {
						// ���ص��ļ�
						while (count <= 100 && running) {
							if (is != null) {
								int numRead = is.read(buf);
								if (numRead <= 0) {
									break;
								} else {
									fos.write(buf, 0, numRead);
									downloadCount += numRead;
									manager.setDownloadCurrentSize(downloadCount);
									if (FileDownloadManager.getFileDownloadListener() != null) {
										FileDownloadManager.getFileDownloadListener()
												.downloadProgress(downloadCount, this.fileSize);
									}
								}
							} else {
								break;
							}
						}

						if (!running) { // �û������ȡ��
							manager.setDownloadStatus(FileDownloadManager.STATUS_NOREADY);
							if (FileDownloadManager.getFileDownloadListener() != null) {
								FileDownloadManager.getFileDownloadListener().downloadCancel();
							}
							if (file != null && file.exists()) {
								file.delete();
							}
						} else { // ���������
							manager.setDownloadStatus(FileDownloadManager.STATUS_FINISH);
							manager.setDownloadFilePath(file.getAbsolutePath());

							if (FileDownloadManager.getFileDownloadListener() != null) {
								FileDownloadManager.getFileDownloadListener().downloadFinish(
										file);
							}
						}
					}
					conn.disconnect();
					fos.close();
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					manager.setDownloadStatus(FileDownloadManager.STATUS_ERROR);
					if (FileDownloadManager.getFileDownloadListener() != null) {
						FileDownloadManager.getFileDownloadListener().downloadError(0,
								"exception");
					}
					if (file != null && file.exists()) {
						file.delete();
					}
				} finally {
					manager.setDownloadCurrentSize(0); // ���ؽ����Ϊ0���»���ͷ����
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		public boolean isRunning() {
			return running;
		}

		public void setRunning(boolean running) {
			this.running = running;
		}
	}
}