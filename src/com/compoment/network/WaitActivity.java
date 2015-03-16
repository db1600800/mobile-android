package com.compoment.network;



import org.json.JSONException;
import org.json.JSONObject;
import com.android_demonstrate_abstractcode.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

/**
 * 
 */
public class WaitActivity extends Activity {

	private Thread mThread;
	String url;
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
				if (netErrBean != null && netErrBean.errorCode.equals("000000")) {// 有数据返回

					String jsonStr = netErrBean.returnData;

					try {
						JSONObject jsonObject = new JSONObject(jsonStr);

						boolean returnCode  = jsonObject.optBoolean("returnCode");
						String dataJson = jsonObject.getString("returnData");
						if (returnCode) {// 调用成功

							JSONObject returnData = new JSONObject(dataJson);

							if (returnData != null && returnData.has("HEAD")) {

								JSONObject HEAD = returnData
										.optJSONObject("HEAD");
								final String HOST_RET_ERR = HEAD
										.optString("RET_ERR");
								final String HOST_RET_MSG = HEAD
										.optString("RET_MSG");
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
								} else {// 报文头 0000000 表示成功获取内容

									bundle.putString("result", jsonStr);

								}

								// mHandler.sendEmptyMessage(100);

							} else {// 没有报文头

								// "交易报文出错
							}

						} else {// 调用失败

							runOnUiThread(new Runnable() {
								@Override
								public void run() {

								}
							});
						}
					} catch (JSONException e) {// json格式出错
						e.printStackTrace();
					}

				} else if (netErrBean != null
						&& !netErrBean.errorCode.equals("000000")) {// 没数据返回

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
