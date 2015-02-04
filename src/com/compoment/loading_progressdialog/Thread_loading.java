package com.compoment.loading_progressdialog;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class Thread_loading extends Activity {
	
	LoadingProgressDialog loading;
	
	Thread thread;
	
	
	
	Handler progressDialogHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				loading.cancleProgressDialog();
				break;
			default:
				break;
			}
		}
	};

	public void progressDialog() {
		if (loading == null) {
			loading = new LoadingProgressDialog();
		}

		loading.showProgressDailg("title", "msg", this);

		if (thread != null && thread.isAlive())
			return;

		thread = new Thread() {

			@Override
			public void run() {
				Looper.prepare();// 需加 不然loading不出来

				progressDialogHandler.sendEmptyMessage(0);
			}

		};
		thread.start();// 不能用run
	}
}
