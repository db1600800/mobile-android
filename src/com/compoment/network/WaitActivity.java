package com.compoment.network;



import org.json.JSONException;
import org.json.JSONObject;




import com.android_demonstrate_abstractcode.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

/**
 * <uses-permission android:name="android.permission.RESTART_PACKAGES" />
    <!-- 在SDCard中创建与删除文件权限 -->
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
    <!-- 往SDCard写入数据权限 -->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_LOCATION_EXTRA_COMMANDS" />
    <!-- 获取IMEI -->
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <!-- 网络状态 -->
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <!-- wifi权限 -->
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <!-- 震动 -->
    <uses-permission android:name="android.permission.VIBRATE" />
    <!-- 拨打电话 -->
    <uses-permission android:name="android.permission.CALL_PHONE" />
    <!-- 相机权限 -->
    <uses-permission android:name="android.permission.CAMERA" />

    <uses-feature android:name="android.hardware.camera" />
    <uses-feature android:name="android.hardware.camera.autofocus" />

    <uses-permission android:name="android.permission.FLASHLIGHT" />
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    <uses-permission android:name="android.permission.GET_TASKS" />
    <!-- 读取短信验证码 -->
    <uses-permission android:name="android.permission.READ_SMS" />
 */
public class WaitActivity extends Activity {

	private Thread mThread;
	String url;
	public static String urlbase="http://10.0.3.2:8080/web/Serverlet4453020";
	boolean isLog=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.progressbar);

		Bundle extras = this.getIntent().getExtras();
		if (extras == null)
			return;
		url = extras.getString("url");
		if (url == null)
			return;

		// 打印log
		if (isLog) {
			Log.i("hobby", url);
		}

		
		// {"returnCode": "0","returnData": {"HEAD": { "RET_MSG": "成功", "RET_ERR": "000000"}, "RECORDNUM": "3" ,"ASS_NO": ["", "", "" ],"MAXRECORD": "6"}}

		final Intent intent = new Intent();
		final Bundle bundle = new Bundle();

		Runnable requestRunnable = new Runnable() {

			@Override
			public void run() {

				// 发起请求
				HttpClientManager httpClientManager = new HttpClientManager();
				HttpClientManager.NetErrBean netErrBean = httpClientManager
						.httpGet(url);
				if (netErrBean != null && netErrBean.errorCode.equals(HttpClientManager.connect000000)) {// 有数据返回 数据对不对看下面

					String jsonStr = netErrBean.returnData;
					
					if(jsonStr==null||jsonStr.contains(""))
					{
						Toast.makeText(WaitActivity.this, "数据为空", Toast.LENGTH_SHORT).show();
						finish();
					}
		
					try {
    					JSONObject jsonObject = new JSONObject(jsonStr);

						String returnCode  = jsonObject.optString("returnCode");
						String dataJson = jsonObject.getString("returnData");
						if (returnCode.equals("000000")) {// 调用成功

							JSONObject returnData = new JSONObject(dataJson);

							if (returnData != null && returnData.has("head")) {

								JSONObject HEAD = returnData
										.optJSONObject("head");
								final String HOST_RET_ERR = HEAD
										.optString("ret_err");
								final String HOST_RET_MSG = HEAD
										.optString("ret_msg");
								if (!HOST_RET_ERR.equals("000000")) {

									if (HOST_RET_ERR.contains("2001")) {
										// token失效不做提示处理
									} else if (HOST_RET_ERR.contains("050008")) {
										// 交易代号为 4460140 并且
										// HOST_RET_ERR（错误响应吗）为“050008”(超时），屏蔽提示，交给具体调用的地方处理。
									} else {
										// 1、根据后台返回的错误描述查询数据库对应的转义描述
										// 2、如果查询结果为null,则根据"*"去查询通用的错误描述文字信息

										// 在此提示数据错误描述转义信息

									}
									
									Toast.makeText(WaitActivity.this, HOST_RET_MSG, Toast.LENGTH_SHORT).show();
									finish();
								} else 
								
								{// 报文头 0000000 表示成功获取内容

									bundle.putString("jsonString", jsonStr);

								}

							} else {// 没有报文头 head

								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(WaitActivity.this, "没有head", Toast.LENGTH_SHORT).show();
										finish();
									}
								});
							}

						} else {// returncode＝非 000000 调用失败

							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(WaitActivity.this, "returnCode=0失败", Toast.LENGTH_SHORT).show();
									finish();
								}
							});
						}
					} catch (JSONException e) {// json格式出错
						e.printStackTrace();
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(WaitActivity.this, "数据不符合Json格式", Toast.LENGTH_SHORT).show();
								finish();
							}
						});
						
					}

				} else if (netErrBean != null
						&& !netErrBean.errorCode.equals(HttpClientManager.connect000000)) {//网络不好 服务器没开  服务器太忙没响应

					Toast.makeText(WaitActivity.this, netErrBean.errorMsg, Toast.LENGTH_SHORT).show();
					finish();
				}

				intent.putExtras(bundle);
				setResult(0, intent);
				finish();// 此处一定要调用finish()方法
			}
		};
		mThread = new Thread(requestRunnable);
		mThread.start();

	}

}

