package com.compoment.prompt;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.android_demonstrate_abstractcode.R;

/*
private void showUpdateDialog() {
	Builder dialog = new AlertDialog.Builder(context);
	dialog.setTitle("检测到新版本，是否升级？");
	dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialoginterface, int i) {

		}
	}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialoginterface, int i) {
		}
	}).show();
}

private void showNoUpdateDialog() {
	Dialog dialog = new AlertDialog.Builder(context)
			.setTitle("已经是最新版本")
			.setPositiveButton("知道了",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

						}
					}).create();
	dialog.show();
}*/


public class DialogFactory {
	private Dialog dialog;
	private Context context;

	private int currentMsgIndex = 0;
	private TextView txtInfo;
	private Button confirm;

	public DialogFactory(Context context) {
		this.context = context;
	}


	public void createSingerButtonDialog(String title, String content,
			OnClickListener onclick) {
		
		dialog = new Dialog(context, R.style.SelfDialog);
	
	//	dialog.setContentView(R.layout.layout_dialog);

		confirm = (Button) dialog.findViewById(R.id.btn_confirm);
		confirm.setOnClickListener(onclick);


		dialog.show();
	}



	public void hideDialog() {
		if (dialog != null)
			dialog.hide();
	}

	
	public void dismiss() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	public boolean isShown() {
		if (dialog != null) {
			return dialog.isShowing();
		} else {
			return false;
		}
	}


	public void setOndismiss(final Activity activity) {
		if (dialog != null) {
			dialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface arg0) {
					if (activity != null) {
						activity.finish();
					}
				}
			});
		}
	}

}
