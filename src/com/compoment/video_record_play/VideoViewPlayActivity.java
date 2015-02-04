package com.compoment.video_record_play;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import com.android_demonstrate_abstractcode.R;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;



public class VideoViewPlayActivity extends Activity {
	// 显示组件
	private VideoView videoView1;
	// 播放文件绝对路径
	private String path;

	// private AudioManager audioManager;
	// private int originVolume;
	// private int maxVolume;
	// private boolean isVolumeChanged;

	private MediaController mediaController;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.video_play);

		mediaController = new MediaController(this);
		// 不自动隐藏MediaController
		// mediaController.show(0);

		videoView1 = (VideoView) findViewById(R.id.videoView1);
		videoView1.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				finish();
			}
		});

		Intent intent = getIntent();
		path = intent.getStringExtra("path");

		// audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		// originVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		// isVolumeChanged = false;
		// if (!(audioManager.isWiredHeadsetOn() ||
		// audioManager.isBluetoothA2dpOn())) {
		// maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		// // 调大音量
		// audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume,
		// AudioManager.FLAG_PLAY_SOUND);
		// isVolumeChanged = true;
		// }

		playVideo(path);
	}

	/** 播放视频，path是本地文件绝对路径 */
	private boolean playVideo(String path) {
		videoView1.setVideoURI(Uri.parse(path));

		if (videoView1 != null) {
			videoView1.setMediaController(mediaController);
			videoView1.requestFocus();
			videoView1.start();

			return true;
		} else {
			return false;
		}
	}

	protected void onDestroy() {
		// if (isVolumeChanged) {
		// // 恢复原始音量
		// audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originVolume,
		// AudioManager.FLAG_PLAY_SOUND);
		// }
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