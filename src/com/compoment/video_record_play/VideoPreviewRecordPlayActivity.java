package com.compoment.video_record_play;


import java.io.File;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import com.android_demonstrate_abstractcode.R;
import com.compoment.video_record_play.db.VideoBean;
import com.compoment.video_record_play.db.VideoBeanProvider;
import com.compoment.video_record_play.util.CalendarDealWith;
import com.compoment.video_record_play.util.DigitDealWith;
import com.compoment.video_record_play.util.FilePathDealWith;
import com.compoment.video_record_play.util.PictureDealWith;
import com.compoment.video_record_play.util.SensorUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Images;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



public class VideoPreviewRecordPlayActivity extends Activity {
	// 姓名和号码，从Intent获取或进入联系人选择
	private String number;
	// 显示组件
	private ImageButton imgBtn_Back;
	private ImageButton imgBtn_Record_Stop_Send; // 录制、停止、发送
	private ImageButton imgBtn_Exchange_Delete; // 切换前后摄像头、弹出PopupWindow
	private TextView readyTextView; // 录制状态
	private TextView timeTextView; // 录制时长
	private ProgressDialog dialog;

	private VideoCameraPreviewSurfaceView previewSurfaceView; // 预览组件

	private Handler handler;

	// 视频文件和音频文件
	private File myRecAudioFile;
	private File dir;

	// 视频录制器
	private MediaRecorder recorder;

	private String recordId = null;

	// 正在录制
	private int stateOfRecord = STATE_FREE;
	private static final int STATE_FREE = 0;
	private static final int STATE_RECORDING = 1;
	private static final int STATE_FINISHED = 2;

	private static int counter = 0; // 游标，单位：毫秒
	private static int duration = 0; // 录制时长，单位：毫秒
	private static final int MESSAGE_RECORD_CLEAR = 0;
	private static final int MESSAGE_RECORD_BEGIN = 1;
	private static final int MESSAGE_RECORD_INCREASE = 2;
	private static final int MESSAGE_RECORD_FINISHED = 3;
	private static final int MESSAGE_PLAY_BEGIN = 4;
	private static final int MESSAGE_UPDATE_IMAGE = 5;

	// 录制完成后，回放视频
	private RelativeLayout previewLayout;

	
	private ImageButton imgBtn_Play; // 回放按钮
	private boolean isPlaying = false; // 正在回放
	private Bitmap bitmap_PlayImage; // 视频截图

	// public MessageVideoScreen() {
	// super(SCREEN_TYPE.VIEW_T, VoipDialRecordScreen.class.getCanonicalName());
	// }

	private static final String SHARE_PREFERENCES_ITEM_MESSAGE_VIDEO_CAMERA_FLAG = "camera_flag";

	private static final String TAG = VideoPreviewRecordPlayActivity.class.getCanonicalName();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_preview_record_play);

		Intent intent = getIntent();
		number = intent.getStringExtra("number"); // getStringExtra()默认返回null

		// 保持屏幕常亮
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// 初始化显示组件
		imgBtn_Back = (ImageButton) findViewById(R.id.imageButton1);
		imgBtn_Record_Stop_Send = (ImageButton) findViewById(R.id.imageButton2);
		imgBtn_Exchange_Delete = (ImageButton) findViewById(R.id.imageButton3);
		
		
		readyTextView = (TextView) findViewById(R.id.readyTextView);
		timeTextView = (TextView) findViewById(R.id.timeTextView);

		previewLayout = (RelativeLayout) findViewById(R.id.previewLayout);
		imgBtn_Play = (ImageButton) findViewById(R.id.playBtn);
	

		// 更新UI的主线程
		handler = getUIHandler();

		// 创建文件夹存放视频
		String path = FilePathDealWith.getLocalVideoRecordDirPath();
		dir = new File(path);

		recorder = new MediaRecorder();
		imgBtn_Back.setOnClickListener(listenerFinish);
		imgBtn_Record_Stop_Send.setOnClickListener(listenerRecord_Stop_Send);
		imgBtn_Exchange_Delete.setOnClickListener(listenerExchangeCamera);
		imgBtn_Play.setOnClickListener(listenerPlay);

		imgBtn_Back.setEnabled(false);
		imgBtn_Record_Stop_Send.setEnabled(false);
		imgBtn_Exchange_Delete.setEnabled(false);
		imgBtn_Play.setEnabled(false);

		// 读取前后摄像头配置
		final int flag;
		if (CameraUtil.getNumberOfCameras() > 1) {
			
			final SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			flag = prefs.getInt(SHARE_PREFERENCES_ITEM_MESSAGE_VIDEO_CAMERA_FLAG, VideoCameraPreviewSurfaceView.FLAG_CAMERA_FRONT);
			
		} else {
			final SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			flag = prefs.getInt(SHARE_PREFERENCES_ITEM_MESSAGE_VIDEO_CAMERA_FLAG, VideoCameraPreviewSurfaceView.FLAG_CAMERA_BACK);
		
			Editor editor = prefs.edit();
			editor.putInt(SHARE_PREFERENCES_ITEM_MESSAGE_VIDEO_CAMERA_FLAG, VideoCameraPreviewSurfaceView.FLAG_CAMERA_BACK);
			editor.commit();
		}

		isFrontCamera = (flag == VideoCameraPreviewSurfaceView.FLAG_CAMERA_FRONT);
		handler.postDelayed(new Runnable() {
			public void run() {
				startPreview(flag);
			}
		}, 200);
		handler.postDelayed(new Runnable() {
			public void run() {
				imgBtn_Back.setEnabled(true);
				imgBtn_Record_Stop_Send.setEnabled(true);
				imgBtn_Exchange_Delete.setEnabled(true);
				imgBtn_Play.setEnabled(true);
			}
		}, 500);
	}

	// 结束Activity，释放资源
	private OnClickListener listenerFinish = new OnClickListener() {
		public void onClick(View arg0) {
			if (VideoPreviewRecordPlayActivity.this.myRecAudioFile != null
					&& VideoPreviewRecordPlayActivity.this.myRecAudioFile.exists()
					&& stateOfRecord == STATE_FINISHED) {
				// 录制消息保存在本地
				storeMessageRecord();
			}

			if (stateOfRecord == STATE_RECORDING) {
				cancelMaxRecordTimeTask();
			}

			finish();
		}
	};

	/** 录制消息保存在本地 */
	private synchronized VideoBean storeMessageRecord() {
		VideoBean messageRecord = new VideoBean();
		recordId = DigitDealWith.createId(); // 随机生成一个id
		messageRecord.setId(recordId);

		messageRecord
				.setFileLength(VideoPreviewRecordPlayActivity.this.myRecAudioFile.length());
		messageRecord.setType("video");
		messageRecord.setDuration(VideoPreviewRecordPlayActivity.duration);
		messageRecord.setRecordTime(Calendar.getInstance());
		// 视频路径
		String path = VideoPreviewRecordPlayActivity.this.myRecAudioFile.getAbsolutePath();
		messageRecord.setPath(path);

		// 为视频生成缩略图
		String imageName = PictureDealWith.saveToPng(path);
		messageRecord.setPreviewImagePath(FilePathDealWith
				.getPreviewImageRecordDirPath() + imageName);

		VideoBeanProvider manager = new VideoBeanProvider(
				VideoPreviewRecordPlayActivity.this);
		manager.addMessageRecord(messageRecord);

		return messageRecord;
	}

	// 用户短时间内多次点击发送按钮时，防止storeMessageRecord被执行多次
	private boolean isStored = false;

	// 切换前后置摄像头
	private OnClickListener listenerExchangeCamera = new OnClickListener() {
		public void onClick(View v) {
			// TODO 切换摄像头
			// Toast.makeText(MessageVideoScreen.this, "切换摄像头：未实现", 1000).show();

			if (CameraUtil.getNumberOfCameras() > 1) {
				v.setEnabled(false);
				final View v_ = v;
				handler.postDelayed(new Runnable() {
					public void run() {
						v_.setEnabled(true);
					}
				}, 1000);

				if (stateOfRecord == STATE_FREE) { // 录制前才可以切换摄像头
					switchCamera();
				}
			} else {
				Toast.makeText(VideoPreviewRecordPlayActivity.this, "您的手机暂时不支持切换摄像头",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	private OnClickListener listenerPlay = new OnClickListener() {
		public void onClick(View arg0) {
			String path = myRecAudioFile.getAbsolutePath();
			Intent intent = new Intent(VideoPreviewRecordPlayActivity.this,
					VideoViewPlayActivity.class);
			intent.putExtra("path", path);
			startActivity(intent);

		
		}
	};

	private OnClickListener listenerDelete = new OnClickListener() {
		public void onClick(View v) {
			// 获取资源文字
			//Resources resources = VideoPreviewRecordPlayActivity.this.getResources();
			String str_Tip = "提醒";
			String str_Confirm = "确认";
			String str_Cancel = "取消";
			String str_DeleteConfirm = "您确定要删除？";

			// 生成确认框
			AlertDialog.Builder builder = new Builder(VideoPreviewRecordPlayActivity.this);
			builder.setTitle(str_Tip);
			builder.setMessage(str_DeleteConfirm);
			builder.setPositiveButton(str_Confirm,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							handler.sendEmptyMessage(MESSAGE_RECORD_CLEAR); // 主线程更新UI
							imgBtn_Exchange_Delete.setOnClickListener(listenerExchangeCamera); // 切换摄像头的listener
							imgBtn_Exchange_Delete.setVisibility(View.VISIBLE);

							if (myRecAudioFile != null) {
								myRecAudioFile.delete(); // 删除视频文件
							}
							// 如果正在预览
							if (isPlaying) {
							
								handler.sendEmptyMessage(MESSAGE_RECORD_FINISHED);
							}
							imgBtn_Play.setVisibility(View.GONE);
							if (isFrontCamera) {
								startPreview(VideoCameraPreviewSurfaceView.FLAG_CAMERA_FRONT);
							} else {
								startPreview(VideoCameraPreviewSurfaceView.FLAG_CAMERA_BACK);
							}

							Toast.makeText(
									VideoPreviewRecordPlayActivity.this,"视频已放弃", Toast.LENGTH_SHORT)
									.show();
							stateOfRecord = STATE_FREE;
							dialog.cancel();
						}
					});
			builder.setNegativeButton(str_Cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
			builder.create().show();
		}
	};

	private OnClickListener listenerRecord_Stop_Send = new OnClickListener() {
		public void onClick(View arg0) {
			imgBtn_Record_Stop_Send.setEnabled(false);
			switch (stateOfRecord) {
			case STATE_FREE:
				imgBtn_Record_Stop_Send
						.setBackgroundResource(R.drawable.selector_video_recording);

				imgBtn_Exchange_Delete.setVisibility(View.GONE);
				imgBtn_Play.setBackgroundColor(Color.argb(0x00, 0x00, 0x00, 0x00));

				// 弹出进度条对话框，提示“正在启动摄像头”
				dialog = new ProgressDialog(VideoPreviewRecordPlayActivity.this);
				dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				dialog.setTitle("请稍等");
				dialog.setMessage("正在启动摄像头");
				dialog.setCancelable(true);

				dialog.show();

				// 启用新线程，开始录制
				new Thread(new Runnable() {
					public void run() {
						startRecording();
						dialog.cancel();
						handler.sendEmptyMessage(MESSAGE_RECORD_BEGIN); // 主线程更新UI
					}
				}).start();

				stateOfRecord = STATE_RECORDING;

				// 设置录制最长时间
				cancelMaxRecordTimeTask();
				schedualMaxRecordTimeTask(61 * 1000);

				break;
			case STATE_RECORDING:
				if (isFrontCamera && CameraUtil.getNumberOfCameras() > 1
						&& !canStopRecording) {
					break;
				}

				// 取消录制超时
				cancelMaxRecordTimeTask();

				// TODO
				// i9100前置摄像头录制留言后，马上播放视频，视频会黑屏，过一会播放就不会了。原因：摄像头没有完全关闭，预览的时候使用了OverLay播放的时候也要用到
				// 系统资源不够
				// if (isFrontCamera) {
				// final ProgressDialog progressDialog = new ProgressDialog(
				// MessageVideoScreen.this);
				// progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				// progressDialog.setTitle(MessageVideoScreen.this.getResources()
				// .getString(R.string.please_wait));
				// progressDialog.setMessage("正在准备预览");
				// progressDialog.setCancelable(true);
				//
				// progressDialog.show();
				//
				// imgBtn_Play.setEnabled(false);
				// handler.postDelayed(new Runnable() {
				// public void run() {
				// progressDialog.cancel();
				// imgBtn_Play.setEnabled(true);
				// }
				// }, 3000);
				// }

				// 停止录像，保存视频
				stopRecording();
				duration = counter;
				stopPreview();

				imgBtn_Play.setVisibility(View.VISIBLE);
				imgBtn_Exchange_Delete.setVisibility(View.VISIBLE);
				// 更新视频预览图
				if (pickImageThread != null && pickImageThread.isAlive()) {
					pickImageThread.interrupt();
				}
				pickImageThread = new Thread(new Runnable() {
					public void run() {
						// 用户点击过快时，myRecAudioFile为null，因此设置重试
						for (int i = 0; i < 10; i++) {
							if (myRecAudioFile != null) {
								String videoPath = myRecAudioFile.getAbsolutePath();
								bitmap_PlayImage = pickImageFromVideo(videoPath);
								if (stateOfRecord == STATE_FINISHED) {
									handler.sendEmptyMessage(MESSAGE_UPDATE_IMAGE);
								}
								break;
							} else {
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					}
				});
				pickImageThread.start();

				handler.sendEmptyMessage(MESSAGE_RECORD_FINISHED); // 主线程更新UI
				// 修改图片资源
				imgBtn_Record_Stop_Send
						.setBackgroundResource(R.drawable.selector_video_send);
				imgBtn_Exchange_Delete
						.setBackgroundResource(R.drawable.selector_video_delete);
				// 设置最右下角按钮的监听器
				imgBtn_Exchange_Delete.setOnClickListener(listenerDelete);

				stateOfRecord = STATE_FINISHED;
				isStored = false;
				break;
			case STATE_FINISHED:
				if (!isStored) {
					isStored = true;
					

					// 用户点击过快时，myRecAudioFile为null，因此设置重试
					for (int i = 0; i < 10; i++) {
						if (myRecAudioFile != null) {
							// 录制信息保存到本地
							storeMessageRecord();
							break;
						} else {
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}

//					if (MessageUploadThreadPool.hasUploadingThread()) {
//						Toast.makeText(VideoPreviewRecordPlayActivity.this, "正在上传其他留言，请稍后重试",
//								Toast.LENGTH_LONG).show();
//					} else 
					{
						// 发送
						startUploadingActivity();
					}

					finish();
				}
				break;
			default:
				break;
			}
			imgBtn_Record_Stop_Send.setEnabled(true);
		}
	};

	private void schedualMaxRecordTimeTask(long millsecond) {
		timerTask = new MaxRecordTimeTask();
		timer.schedule(timerTask, millsecond);
	}

	private void cancelMaxRecordTimeTask() {
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;
		}
	}

	private Timer timer = new Timer(true);
	private TimerTask timerTask;

	private class MaxRecordTimeTask extends TimerTask {
		public void run() {
			// 停止录像，保存视频
			stopRecording();
			duration = counter;
			handler.post(new Runnable() {
				public void run() {
					stopPreview();

					imgBtn_Play.setVisibility(View.VISIBLE);
					imgBtn_Exchange_Delete.setVisibility(View.VISIBLE);
					// 更新视频预览图
					if (pickImageThread != null && pickImageThread.isAlive()) {
						pickImageThread.interrupt();
					}
					pickImageThread = new Thread(new Runnable() {
						public void run() {
							if (myRecAudioFile != null) {
								String videoPath = myRecAudioFile.getAbsolutePath();
								bitmap_PlayImage = pickImageFromVideo(videoPath);
								if (stateOfRecord == STATE_FINISHED) {
									handler.sendEmptyMessage(MESSAGE_UPDATE_IMAGE);
								}
							}
						}
					});
					pickImageThread.start();

					handler.sendEmptyMessage(MESSAGE_RECORD_FINISHED); // 主线程更新UI
					// 修改图片资源
					imgBtn_Record_Stop_Send
							.setBackgroundResource(R.drawable.selector_video_send);
					imgBtn_Exchange_Delete
							.setBackgroundResource(R.drawable.selector_video_delete);
					// 设置最右下角按钮的监听器
					imgBtn_Exchange_Delete.setOnClickListener(listenerDelete);
				}
			});

			stateOfRecord = STATE_FINISHED;
			isStored = false;
		}
	}

	private Thread pickImageThread;
	private boolean isStartUploadingActivity = false; // 同步操作，同一时间只允许启动一个上传界面

	// 启动Activity，处理上传
	private void startUploadingActivity() {
		if (!isStartUploadingActivity) {
			isStartUploadingActivity = true;

			/*上传 有用 暂时不用
			Intent intent = new Intent(VideoPreviewRecordPlayActivity.this,
					MessageUploadingActivity.class);
			intent.putExtra("snderNumber", "158188");
			intent.putExtra("rcverNumber", number);
			intent.putExtra("duration", VideoPreviewRecordPlayActivity.duration);
			intent.putExtra("type", "video");
			intent.putExtra("path",
					VideoPreviewRecordPlayActivity.this.myRecAudioFile.getAbsolutePath());
			intent.putExtra("recordId", recordId);

			startActivity(intent);*/

			isStartUploadingActivity = false;
		}
	}

	private static boolean isFrontCamera = true;

	private synchronized void switchCamera() {
		stopPreview();

		if (isFrontCamera) {
			startPreview(VideoCameraPreviewSurfaceView.FLAG_CAMERA_BACK);
			isFrontCamera = false;
			
			final SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			Editor editor = prefs.edit();
			editor.putInt(SHARE_PREFERENCES_ITEM_MESSAGE_VIDEO_CAMERA_FLAG, VideoCameraPreviewSurfaceView.FLAG_CAMERA_BACK);
			editor.commit();
		
		} else {
			startPreview(VideoCameraPreviewSurfaceView.FLAG_CAMERA_FRONT);
			isFrontCamera = true;
			
			final SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			Editor editor = prefs.edit();
			editor.putInt(SHARE_PREFERENCES_ITEM_MESSAGE_VIDEO_CAMERA_FLAG, VideoCameraPreviewSurfaceView.FLAG_CAMERA_FRONT);
			 editor.commit();
			 
		}

		if (isMotorola()) {
			Toast.makeText(this, "切换成功，推荐使用横向录制", Toast.LENGTH_LONG).show();
		}
	}

	private int previewWidth = 0;
	private int previewHeight = 0;

	public void setPreviewSize(int previewWidth, int previewHeight) {
		this.previewWidth = previewWidth;
		this.previewHeight = previewHeight;

		// RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams)
		// imgBtn_Play
		// .getLayoutParams();
		// param.width = previewWidth;
		// param.height = previewHeight;
		//
		// imgBtn_Play.setLayoutParams(param);

		// param = (RelativeLayout.LayoutParams) playSurfaceView.getLayoutParams();
		// param.width = previewWidth;
		// param.height = previewHeight;
		// playSurfaceView.setLayoutParams(param);
	}

	/** 开启摄像头，新建SurfaceView，开始预览 */
	private synchronized void startPreview(int frontOrBack) {
		previewSurfaceView = new VideoCameraPreviewSurfaceView(
				VideoPreviewRecordPlayActivity.this, frontOrBack);

		// 这个判断用作减少计算量，但可能对前后摄像头支持不同分辨率的手机有问题。所以暂时不用
		// if (previewWidth == 0 || previewHeight == 0) {
		// }

		// 计算预览大小，不考虑相机是否支持。相机是否支持，在previewSurfaceView中处理
		DisplayMetrics metrics = VideoPreviewRecordPlayActivity.this.getResources()
				.getDisplayMetrics();
		Log.e("MessageVideoScreen", "screen width = " + metrics.widthPixels
				+ ", height = " + metrics.heightPixels);

		switch (metrics.heightPixels) {
		case 320:
			previewWidth = 352;
			previewHeight = 288;
			// previewWidth = 288;
			// previewHeight = 352;
			break;
		case 480:
		case 540:
			previewWidth = 640;
			previewHeight = 480;
			// previewWidth = 480;
			// previewHeight = 640;
			break;
		case 600:
			previewWidth = 768;
			previewHeight = 576;
			// previewWidth = 576;
			// previewHeight = 768;
			break;
		case 720:
			previewWidth = 960;
			previewHeight = 720;
			// previewWidth = 720;
			// previewHeight = 960;
			break;
		default:
			previewWidth = 640;
			previewHeight = 480;
			// previewWidth = 480;
			// previewHeight = 640;
			break;
		}

		previewLayout.addView(previewSurfaceView, previewWidth, previewHeight);
		
	}

	/** 关闭摄像头，销毁SurfaceView，停止预览 */
	private synchronized void stopPreview() {
		previewLayout.removeView(previewSurfaceView);
		
	}

	/** 录制过程中使用的Camera对象，只用于录制前unlock()和录制后reconnect()，不用作其他用途 */
	private Camera camera_Recording;

	/** */
	private static int shift = 0;

	/** 录像 */
	public synchronized void startRecording() {
		try {
			myRecAudioFile = new File(dir, FilePathDealWith.createLocalVideoFileName()); // 创建文件
			myRecAudioFile.createNewFile();
			camera_Recording = previewSurfaceView.getCamera();
			for (int i = 0; i < 10; i++) {
				if (camera_Recording == null) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					camera_Recording = previewSurfaceView.getCamera();
				} else {
					break;
				}
			}
			camera_Recording.unlock();

			recorder.setCamera(camera_Recording); // 开始录制前，预览
			recorder.setPreviewDisplay(previewSurfaceView.getHolder().getSurface()); // 预览
			recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA); // 视频源
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 录音源为麦克风
			recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // 输出格式为MPEG_4
			recorder.setVideoSize(640, 480); // 视频尺寸

			// 修改码率
			try {
				Method setVideoEncodingBitRateMethod = MediaRecorder.class.getMethod(
						"setVideoEncodingBitRate", new Class[] { int.class });
				if (setVideoEncodingBitRateMethod != null) {
					setVideoEncodingBitRateMethod.invoke(recorder, 1024000); // 码率 1MB
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264); // 视频编码
			recorder.setVideoFrameRate(30); // 视频帧频率
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // 音频编码
			recorder.setMaxDuration(1000000); // 最大期限
			recorder.setOutputFile(myRecAudioFile.getAbsolutePath()); // 保存路径

			if (!"motorola".equals(android.os.Build.MANUFACTURER)) { // moto手机无需旋转
				if (isFrontCamera && CameraUtil.getNumberOfCameras() > 1) { // 前置摄像头
					shift = 0; // 根据重力感应的旋转
					int orientation = SensorUtil.getCurrentDeviceOrientation(this);
					Log.e("", "orientation = " + orientation);
					switch (orientation) {
					case SensorUtil.ORIENTATION_0:
						// shift = 0; // protrial用
						shift = 270; // landscape用
						break;
					case SensorUtil.ORIENTATION_90:
						shift = 0; // landscape用
						// shift = 90; // protrial用
						break;
					case SensorUtil.ORIENTATION_180:
						// shift = 180; // protrial用
						shift = 90; // landscape用
						break;
					case SensorUtil.ORIENTATION_270:
						shift = 180; // landscape用
						// shift = 270; // protrial用
						break;
					}

					// 根据重力感应，设置预览图大小。横竖屏录制的视频，大小不一样
					/*
					 * protrial用 if (shift % 180 == 90) { // 横屏录制 int width =
					 * previewWidth; int height = previewHeight; DisplayMetrics metrics =
					 * MessageVideoScreen.this.getResources() .getDisplayMetrics();
					 * 
					 * if (height > metrics.widthPixels) { width = metrics.widthPixels;
					 * height = width * previewWidth / previewHeight; // 图片做了旋转 }
					 * 
					 * final RelativeLayout.LayoutParams param =
					 * (RelativeLayout.LayoutParams) imgBtn_Play .getLayoutParams();
					 * param.width = width; param.height = height;
					 * 
					 * handler.post(new Runnable() { public void run() {
					 * imgBtn_Play.setLayoutParams(param); } }); } else { // 竖屏录制 final
					 * RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams)
					 * imgBtn_Play .getLayoutParams(); param.width = previewWidth;
					 * param.height = previewHeight;
					 * 
					 * handler.post(new Runnable() { public void run() {
					 * imgBtn_Play.setLayoutParams(param); } }); }
					 */
					/* landscape用 */
					if (shift % 180 == 90) { // 竖屏录制
						int width = previewHeight;
						int height = previewWidth;
						DisplayMetrics metrics = VideoPreviewRecordPlayActivity.this.getResources()
								.getDisplayMetrics();

						if (height > metrics.heightPixels) {
							height = metrics.heightPixels;
							width = height * previewHeight / previewWidth; // 图片做了旋转
						}

						final RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) imgBtn_Play
								.getLayoutParams();
						param.width = width;
						param.height = height;

						handler.post(new Runnable() {
							public void run() {
								imgBtn_Play.setLayoutParams(param);
							}
						});
					} else { // 横屏录制
						final RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) imgBtn_Play
								.getLayoutParams();
						param.width = previewWidth;
						param.height = previewHeight;

						handler.post(new Runnable() {
							public void run() {
								imgBtn_Play.setLayoutParams(param);
							}
						});
					}

					try { // 2.3以下的系统，这里的反射方法会抛出异常。异常导致不旋转视频，软件可以继续使用
						if (!"motorola".equals(android.os.Build.MANUFACTURER)) { // moto手机无需旋转
							// 旋转
							setRecorderOrientationHint(
									recorder,
									(getRotation(this, CameraUtil.getNumberOfCameras() - 1,
											camera_Recording) + shift) % 360);
							Log.e("MessageVideoScreen.startRecording", "shift = " + shift
									+ ", isFrontCamera = " + isFrontCamera);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else { // 后置摄像头
					shift = 0; // 根据重力感应的旋转
					int orientation = SensorUtil.getCurrentDeviceOrientation(this);
					Log.e("", "orientation = " + orientation);
					switch (orientation) {
					case SensorUtil.ORIENTATION_0:
						// shift = 0; // protrial用
						shift = 90; // landscape用
						break;
					case SensorUtil.ORIENTATION_90:
						shift = 0; // landscape用
						// shift = 270; // protrial用
						break;
					case SensorUtil.ORIENTATION_180:
						// shift = 180; // protrial用
						shift = 270; // landscape用
						break;
					case SensorUtil.ORIENTATION_270:
						shift = 180; // landscape用
						// shift = 90; // protrial用
						break;
					}

					// 根据重力感应，设置预览图大小。横竖屏录制的视频，大小不一样
					/*
					 * protrial用 if (shift % 180 == 90) { // 横屏录制 int width =
					 * previewWidth; int height = previewHeight; DisplayMetrics metrics =
					 * MessageVideoScreen.this.getResources() .getDisplayMetrics();
					 * 
					 * if (height > metrics.widthPixels) { width = metrics.widthPixels;
					 * height = width * previewWidth / previewHeight; // 图片做了旋转 }
					 * 
					 * final RelativeLayout.LayoutParams param =
					 * (RelativeLayout.LayoutParams) imgBtn_Play .getLayoutParams();
					 * param.width = width; param.height = height;
					 * 
					 * handler.post(new Runnable() { public void run() {
					 * imgBtn_Play.setLayoutParams(param); } }); } else { // 竖屏录制 final
					 * RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams)
					 * imgBtn_Play .getLayoutParams(); param.width = previewWidth;
					 * param.height = previewHeight;
					 * 
					 * handler.post(new Runnable() { public void run() {
					 * imgBtn_Play.setLayoutParams(param); } }); }
					 */
					/* landscape用 */
					if (shift % 180 == 90) { // 竖屏录制
						int width = previewHeight;
						int height = previewWidth;
						DisplayMetrics metrics = VideoPreviewRecordPlayActivity.this.getResources()
								.getDisplayMetrics();

						if (height > metrics.heightPixels) {
							height = metrics.heightPixels;
							width = height * previewHeight / previewWidth; // 图片做了旋转
						}

						final RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) imgBtn_Play
								.getLayoutParams();
						param.width = width;
						param.height = height;

						handler.post(new Runnable() {
							public void run() {
								imgBtn_Play.setLayoutParams(param);
							}
						});
					} else { // 横屏录制
						final RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) imgBtn_Play
								.getLayoutParams();
						param.width = previewWidth;
						param.height = previewHeight;

						handler.post(new Runnable() {
							public void run() {
								imgBtn_Play.setLayoutParams(param);
							}
						});
					}

					try {// 2.3以下的系统，这里的反射方法会抛出异常。异常导致不旋转视频，软件可以继续使用
						setRecorderOrientationHint(recorder,
								(getRotation(this, 0, camera_Recording) + shift) % 360);
						Log.e("MessageVideoScreen.startRecording", "shift = " + shift
								+ ", isFrontCamera = false");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			recorder.prepare();

			if (isFrontCamera) {
				canStopRecording = false;
				new Timer().schedule(new TimerTask() {
					public void run() {
						canStopRecording = true;
					}
				}, 1000);
			}

			recorder.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 标志位，MediaRecorder在start之后，至少1000ms之后才能stop。因为 MediaRecorder 的 start
	 * 方法是异步的。如果start之后马上执行stop，会跑出stop failed的RuntimeException
	 */
	private boolean canStopRecording = false;

	private void setRecorderOrientationHint(MediaRecorder recorder, int degrees) {
		try {
			Method setOrientationHintMethod = MediaRecorder.class.getMethod(
					"setOrientationHint", new Class[] { int.class });
			Log.d(TAG, "*** setOrientationHintMethod=" + setOrientationHintMethod
					+ ", degrees=" + degrees);
			if (setOrientationHintMethod != null) {
				setOrientationHintMethod.invoke(recorder, degrees);
				Log.d(TAG, "*** invoke success");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int getRotation(Activity activity, int cameraId,
			android.hardware.Camera camera) {
		int result = CameraUtil.getRotation(activity, cameraId);
		return result;
	}

	public synchronized void stopRecording() {
		try {
			Log.e("MessageVideoScreen", "recorder stop()");
			recorder.stop();
			recorder.reset();

			camera_Recording.reconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.e("MessageVideoScreen", "recorder stop finished()");
	}


	/** 返回更新UI的Handler */
	private Handler getUIHandler() {
		return new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MESSAGE_RECORD_BEGIN:
					counter = 0;
					if (!isPlaying) { // 播放时不改变duration
						duration = 0;
					}
					readyTextView.setText("正在录制");
					timeTextView.setText("00:00");
					sendEmptyMessageDelayed(MESSAGE_RECORD_INCREASE, 1000);
					break;
				case MESSAGE_RECORD_INCREASE:
					counter += 1000;
					timeTextView.setText(CalendarDealWith.formatTime1(counter));
					sendEmptyMessageDelayed(MESSAGE_RECORD_INCREASE, 1000);
					break;
				case MESSAGE_RECORD_FINISHED:
					removeMessages(MESSAGE_RECORD_BEGIN);
					removeMessages(MESSAGE_PLAY_BEGIN);
					removeMessages(MESSAGE_RECORD_INCREASE);
					readyTextView.setText("录制完成");
					timeTextView.setText(CalendarDealWith.formatTime1(duration));
					break;
				case MESSAGE_RECORD_CLEAR:
					removeMessages(MESSAGE_RECORD_BEGIN);
					removeMessages(MESSAGE_RECORD_INCREASE);
					removeMessages(MESSAGE_RECORD_FINISHED);
					removeMessages(MESSAGE_PLAY_BEGIN);

					// 修改图片资源
					imgBtn_Record_Stop_Send
							.setBackgroundResource(R.drawable.selector_video_record);
					imgBtn_Exchange_Delete
							.setBackgroundResource(R.drawable.selector_video_exchange);
					readyTextView.setText("准备就绪");
					counter = 0;
					timeTextView.setText("00:00");
				case MESSAGE_UPDATE_IMAGE:
					imgBtn_Play
							.setBackgroundDrawable(new BitmapDrawable(bitmap_PlayImage));
					break;
				case MESSAGE_PLAY_BEGIN:
					counter = 0;
					if (!isPlaying) { // 播放时不改变duration
						duration = 0;
					}
					readyTextView.setText("正在播放");
					timeTextView.setText("00:00");
					sendEmptyMessageDelayed(MESSAGE_RECORD_INCREASE, 1000);
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
	}

	private static boolean isMotorola() {
		return "motorola".equals(android.os.Build.MANUFACTURER);
	}

	/** 获取视频缩略图 */
	public static Bitmap pickImageFromVideo(String videoPath) {
		Bitmap bm = android.media.ThumbnailUtils.createVideoThumbnail(videoPath,
				Images.Thumbnails.MINI_KIND);

		if (!isMotorola()) {
			if (shift % 180 == 90) { // 横屏录制
				// bm = ThumbnailUtils.extractThumbnail(bm, 640, 480); // portrial用
				bm = ThumbnailUtils.extractThumbnail(bm, 480, 640); // landscape用
			} else { // 竖屏录制
				// bm = ThumbnailUtils.extractThumbnail(bm, 480, 640); // portrial用
				bm = ThumbnailUtils.extractThumbnail(bm, 640, 480); // landscape用
			}
		} else {
			bm = ThumbnailUtils.extractThumbnail(bm, 640, 480);
		}

		return bm;
	}

	/** 释放播放器资源 */
	private void releaseMediaRecorder() {
		try {
			if (recorder != null) {
				if (stateOfRecord == STATE_RECORDING) {
					recorder.stop();
					recorder.reset();
					// Toast.makeText(MessageVideoScreen.this,
					// "文件保存在 " + myRecAudioFile.getAbsolutePath(), Toast.LENGTH_SHORT)
					// .show();
				}
				recorder.release();
				recorder = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (VideoPreviewRecordPlayActivity.this.myRecAudioFile != null
					&& VideoPreviewRecordPlayActivity.this.myRecAudioFile.exists()
					&& stateOfRecord == STATE_FINISHED) {
				// 录制消息保存在本地
				storeMessageRecord();
			}

			if (stateOfRecord == STATE_RECORDING) {
				cancelMaxRecordTimeTask();
			}

			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/** 离开Activity时注销MediaRecorder */
	protected void onDestroy() {
		if (dialog != null && dialog.isShowing()) {
			dialog.cancel();
		}

		try {
			releaseMediaRecorder();
		} catch (Exception e) {
			e.printStackTrace();
		}

		handler.sendEmptyMessage(MESSAGE_RECORD_CLEAR);
		if (pickImageThread != null && !pickImageThread.isInterrupted()) {
			pickImageThread.interrupt();
		}
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!FilePathDealWith.isSDCardExist()) {
			Toast.makeText(VideoPreviewRecordPlayActivity.this, "请插入SD卡", Toast.LENGTH_SHORT)
					.show();
			finish();
		} else if (FilePathDealWith.isNotEnoughMemory()) {
			Toast.makeText(VideoPreviewRecordPlayActivity.this, "无法录制视频：SD卡空间不足",
					Toast.LENGTH_LONG).show();
			finish();
		} else {
		
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
}
