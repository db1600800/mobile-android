package com.compoment.loading_progressdialog;

import com.android_demonstrate_abstractcode.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;




//http://my.oschina.net/amigos/blog/59871
public class LoadingProgressBar extends Activity{
	/**
	 * 显示进度条
	 */
	ProgressBar progressBar;

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.progressbar);
		
	}
    

}
