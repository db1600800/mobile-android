package com.compoment.versioncheck_browser2.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class PackageInfo {
	
	/*
	 * String isPublish = PackageInfo.getValue(context, "isPublish");
					
				String seriesCode = PackageInfo.getValue(context, "seriesCode");
				String versionCode = PackageInfo.getValue(context, "versionCode");
				String isCopressed = PackageInfo.getValue(context, "isCopressed");

	 */
	
	public static String getValue(Context context, String key) throws Exception{
			PackageManager pm = context.getPackageManager();
			ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(),
					PackageManager.GET_META_DATA);
			Bundle bundle = ai.metaData;
			return bundle.getString(key);
		}

}

