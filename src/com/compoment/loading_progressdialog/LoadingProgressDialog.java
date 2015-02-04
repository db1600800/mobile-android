package com.compoment.loading_progressdialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

public class LoadingProgressDialog {
	/**
	 * 显示进度条
	 */
	ProgressDialog progressDialog;
	
	public LoadingProgressDialog()
	{
		
	}
	
	public void showProgressDailg(String title, String msg, Context context) {
		try {
			progressDialog = new ProgressDialog(context);
			// 实例化
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// 设置进度条风格，风格为圆形，旋转的
			progressDialog.setTitle(title);//title=="" 时不显示title部分
			// 设置ProgressDialog 标题
			progressDialog.setMessage(msg);
			progressDialog.setIndeterminate(false);
			// 设置ProgressDialog 的进度条是否不明确
			progressDialog.setCancelable(true);
			progressDialog.show();
		} catch (Exception e) {
			Log.e("showProgressDailg", e.getMessage());
		}
	}

	/**
	 * 取消进度条
	 */
	public void cancleProgressDialog() {
		if (null != progressDialog && progressDialog.isShowing()) {
			try {
				progressDialog.dismiss();
			} catch (Exception e) {
			}
		}
	}
}
