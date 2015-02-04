package com.compoment.prompt;

import android.content.Context;
import android.widget.Toast;

public class ToastPrompt1 {
	
	
	public void toastShort(Context context, final String text) {

		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

	}

	public void toastLong(Context context, final String text) {

		Toast.makeText(context, text, Toast.LENGTH_LONG).show();

	}
	
	
}
