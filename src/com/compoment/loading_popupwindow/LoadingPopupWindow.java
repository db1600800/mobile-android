package com.compoment.loading_popupwindow;


import com.android_demonstrate_abstractcode.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;



public class LoadingPopupWindow {
	private PopupWindow menuPopUp;
	private LayoutInflater inflater;
	private ImageView loadImageView;

	public LoadingPopupWindow(Context contxt) {
		try {
			inflater = LayoutInflater.from(contxt);

			View view = inflater.inflate(R.layout.loading, null);
			loadImageView = (ImageView) view.findViewById(R.id.LoadingImgeView);
			loadImageView.post(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					AnimationDrawable anim = (AnimationDrawable)loadImageView.getBackground();
					anim.start();
				}
			});


			menuPopUp = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, true);
			menuPopUp.setOutsideTouchable(false);
			view.setFocusableInTouchMode(true);
			view.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {

					return true;
				}

			});
		} catch (Exception e) {

		}
	}


	public void show(View contain) {
		System.out.println("menuPopUp.isShowing():"+menuPopUp.isShowing());
		if (!menuPopUp.isShowing()) {
			menuPopUp.showAtLocation(contain, Gravity.CENTER, 0, 0);
		}
	}


	public void hidden() {
		if (menuPopUp.isShowing()) {
			menuPopUp.dismiss();

		}
	}
}
