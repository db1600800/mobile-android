/**
 * @Title:文件的名称
 * @Description:Sip协议封装
 * @Copyright: Copyright (c) 2012
 * @Company: iSoftStone
 * @author lipei
 * @version 1.0
 */

package com.compoment.widget;

import com.android_demonstrate_abstractcode.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

/* 在AndroidMainfest.xml
 		<receiver android:name=".Widget" 
			>
			<meta-data android:name="android.appwidget.provider"
				android:resource="@xml/widget_info" />
			<intent-filter>
				<action android:name="android.appwidget.action.APPWIDGET_UPDATE"></action>
			</intent-filter>
		</receiver>
 */
public class Widget extends AppWidgetProvider{

	/*
	onDisabled(Context context) 最后一个App Widget实例删除后调用此方法
	onEnabled(Context context) App WIdget实例第一次被创建是调用此方法
	*/
	
	
	// 删除App Widget时调用此方法
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

	
	//接收广播事件
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}

	
	//到达指定更新时间或用户向桌面添加了App Widget时调用此方法
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		for(int i = 0; i < appWidgetIds.length; i++){
			int widgetId = appWidgetIds[i];
			updateAppWidget(context, appWidgetManager, widgetId);
		}
	}
	
	public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
			int appWidgetId){
		RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget);
		
		//Bundle bundle = new Bundle();
		Intent intentApk = new Intent();
		//intentApk.setClass(context, ActivityWidgetApk.class);
		intentApk.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intentApk.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		//bundle.putString(ActivityFileManager.FILE_TYPE, ActivityFileManager.FILE_APK);
		//intentApk.putExtras(bundle);
        PendingIntent apkPending = PendingIntent.getActivity(context, 0, intentApk, 0);
		views.setOnClickPendingIntent(R.id.imgApk, apkPending);
		
		appWidgetManager.updateAppWidget(appWidgetId, views);
	}
}
