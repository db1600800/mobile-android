package com.compoment.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedPreferencesStorage {


	public static String getSetting(Context context, String name,
			String defaultValue) {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);  //getSharedPreferences("myfile", Context.MODE_PRIVATE);
		String value = prefs.getString(name, defaultValue);
		return value;
	}

	public static boolean setSetting(Context context, String name, String value) {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putString(name, value);
		return editor.commit();
	}



}
