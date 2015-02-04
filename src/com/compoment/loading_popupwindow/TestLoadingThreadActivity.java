package com.compoment.loading_popupwindow;

import com.android_demonstrate_abstractcode.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TestLoadingThreadActivity extends Activity {

	private TextView layout;
	LoadingPopupWindow progress=null;
	boolean firstTime=true;
	LayoutInflater lInflater = getLayoutInflater();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loading_test_activity);
		layout = (TextView) findViewById(R.id.text);		

		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		int selectType = bundle.getInt("SelectType");
	
		//TextView titleTextView = (TextView) findViewById(R.id.title);
		//txtOverlay = (TextView)lInflater.inflate(R.layout.popup_char_textview, null);
	
	}



	@Override  
	public void onWindowFocusChanged(boolean hasFocus) {  
	    // TODO Auto-generated method stub  
	    super.onWindowFocusChanged(hasFocus);  
	    if(firstTime && progress == null){
	    	firstTime=false;
			progress = new LoadingPopupWindow(this);
			progress.show(layout);
		}
	}  
	
	private class ThreadsTask extends AsyncTask<Void, Void, Cursor> {
     	
		
     	Handler activityHandler;
     	
		public ThreadsTask(Context context,View view,Handler activityHandler) {
          this.activityHandler=activityHandler;
		}

		@Override
		protected Cursor doInBackground(Void... params) {
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Cursor curs) {
			if(progress != null){
				progress.hidden();
				progress = null;
			}
			
			Message msg=new Message();
			msg.what=1;
			msg.obj=null;
			activityHandler.sendMessage(msg);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_BACK) {

			return true;
		}

		return true;
	}

	private Handler activityHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// Intent intent = new Intent();
			// intent.setClass(LoginActivity.this, ActivityMain.class);
			// Bundle data = new Bundle();
			// data.putBoolean("logined", true);
			// switch (msg.what) {
			// case H_USE_NOW:
			// if (ifFinish) {
			// break;
			// }
			// data.putBoolean("isFirstTime", true);
			//
			// intent.putExtras(data);
			// startActivity(intent);
			// break;
			// default:
			// break;
		}

	};
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) { // resultCode
		case 10://
			break;
		default:
			break;
		}
	}

}
