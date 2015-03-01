package com.compoment.versioncheck_browser2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.compoment.loading_progressdialog.LoadingProgressBar;
import com.compoment.versioncheck_browser2.util.HTTPUtils;
import com.compoment.versioncheck_browser2.util.PackageInfo;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;




public class VersionCheck {
	
	Context context;
	Handler handler;
	boolean isMust = false;//默认不是强制更新
	public static final String VersionCheckCfg = "VersionCheck";
	private boolean hasConfigFile;
	private boolean isInBackground;
	private String saveFileName;
	
	private static final String UPDATE_MUST = "must";

	private Dialog noticeDialog;
	
	private static final String AUTO_UPDATE = "auto_update";
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	
	static String ERROR_CODE = "301"; // 产品不存在
	static String PRODUCT_EXIST = "401";// 已经是最新版本
	boolean apkNotExist = false;
	
	public VersionCheck(Context context,Handler handler) {
		this.context = context;
		this.handler = handler;
		
	}

	/**
	 * 下载apk
	 * 
	 * @param url
	 */

	private void downloadApk() {
		if (hasConfigFile) {
			getVersionFromNet(new VersionBean());
		}
		VersionBean vb = readFromVersionFile();
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(vb.getPkg_url()));
		context.startActivity(intent);
	}

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		context.startActivity(intent);

	}

	/**
	 * 获取网络最新版本数据（正在使用的函数）
	 * @param vBean
	 * @return
	 */
	public Boolean getVersionFromNet(VersionBean vBean) {
		if (HTTPUtils.checkNetStatus(context)) {
			try {
				String versionCheckBaseUrl = "http://gd.10086.cn/tryme/versionUpdate.do";
				Map<String, String> mapParams = new HashMap<String, String>();

				String isPublish = PackageInfo.getValue(context, "isPublish");
				isPublish = isPublish.substring(0, isPublish.length() - 1);
				
				String seriesCode = PackageInfo.getValue(context, "seriesCode");
				String versionCode = PackageInfo.getValue(context, "versionCode");
				String isCopressed = PackageInfo.getValue(context, "isCopressed");
				versionCode = versionCode.substring(0, versionCode.length() - 1);
				isCopressed = isCopressed.substring(0, isCopressed.length() - 1);

				WindowManager wm = (WindowManager) context
						.getSystemService(Context.WINDOW_SERVICE);
				DisplayMetrics metrics = new DisplayMetrics();
				wm.getDefaultDisplay().getMetrics(metrics);

				mapParams.put("seriesCode", seriesCode);
				mapParams.put("versionCode", versionCode);
				mapParams.put("isCopressed", isCopressed);
				mapParams.put("isPublish", isPublish);

				String content = HTTPUtils.httpDoPost(versionCheckBaseUrl,
						mapParams);
				Log.e("more", "get value from api:" + content);
				Log.e("mapParams", mapParams.toString());
				
				// 产品已是最新版本
				if(PRODUCT_EXIST.equals(content)){
					return true;
				}
				
				if(ERROR_CODE.equals(content)){
					apkNotExist = true;
					return null;
				}
				JSONObject obj = new JSONObject(content);
				JSONArray array = (JSONArray) obj.get("results");

				JSONObject temp = array.getJSONObject(0);
				String updateMode = temp.getString("updateMode");
//				update = "must";
				String file_url = temp.getString("file_url");
				String update_content = temp.getString("update_content");
				String rVersionCode = temp.getString("versionCode");
				
				if (rVersionCode != null && rVersionCode.compareTo(versionCode)>0) {
					// 写入配置文件
					writeToVersionFile(new VersionBean(updateMode, file_url, 
							update_content, rVersionCode));

					// 保存从网络解释后的数据
					vBean.setPkg_url(file_url);
					vBean.setUpdate(updateMode);
					vBean.setUpdate_content(update_content);
					vBean.setVersion(rVersionCode);
				
					return false;
				} else {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(e);
				return null;
			}
		}
		return null;
	}

	/**
	 * 写入版本信息配置文件。
	 * 
	 * @param versionBean
	 */
	private void writeToVersionFile(VersionBean versionBean) {
		// TODO Auto-generated method stub
		// 获取只能被本应用程序读、写的SharedPreferences对象
//		preferences = context.getSharedPreferences("productsUpdated",
//				Context.MODE_WORLD_READABLE);
		preferences = context.getSharedPreferences(VersionCheckCfg,
				Context.MODE_WORLD_READABLE);
		editor = preferences.edit();
		boolean nextTimeNotRemind = preferences.getBoolean("nextTimeNotRemind", false);
		editor.clear();
		// 存入最新的版本信息
		editor.putString("update", versionBean.getUpdate());
		editor.putString("pkg_url", versionBean.getPkg_url());
		editor.putString("update_content", versionBean.getUpdate_content());
		editor.putString("version", versionBean.getVersion());
		
		//如果不是强制更新,那么保存之前的值
		if(!UPDATE_MUST.equals(versionBean.getUpdate())){
			editor.putBoolean("nextTimeNotRemind", nextTimeNotRemind);
		}
		editor.putBoolean("nextTimeNotRemind", nextTimeNotRemind);
		editor.commit();
	}

	/**
	 * 读取版本信息配置文件。
	 * 
	 * @param versionBean
	 */
	private VersionBean readFromVersionFile() {
		// TODO Auto-generated method stub
		// 获取只能被本应用程序读、写的SharedPreferences对象
		if(preferences == null){
//			preferences = context.getSharedPreferences("productsUpdated",
//					Context.MODE_WORLD_READABLE);
			preferences = context.getSharedPreferences(VersionCheckCfg,
					Context.MODE_WORLD_READABLE);
		}
		// 读取时间数据
		String update = preferences.getString("update", null);
		String pkg_url = preferences.getString("pkg_url", null);
		String update_content = preferences.getString("update_content", null);
		String version = preferences.getString("version", null);

		return new VersionBean(update, pkg_url, update_content, version);
	}

	/**
	 * 首先从配置文件当中判断是否有更新
	 * 
	 * @throws Exception
	 */
	public boolean hasUpdated() throws Exception {
		VersionBean versionBean = readFromVersionFile();
		String appVerson = PackageInfo.getValue(context, "version");
		appVerson = appVerson.substring(0, appVerson.length() - 1);
		String configVersion = versionBean.getVersion();
		//if (configVersion != null && !configVersion.equals(appVerson)) {此句有bug，因为如果出现覆盖安装的话，并不会删除sharedParence信息文件....
		if (configVersion != null && Long.parseLong(configVersion) > Long.parseLong(appVerson)) {
			return true;
		}
		return false;
	}

	private void showToast(String content) {
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}



	/**
	 * 弹出对话框
	 */

	private void showPopWindow(final boolean isMust,VersionBean vBean) {
		// TODO Auto-generated method stub
		
		
		 if(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(AUTO_UPDATE, false)){
//			 如已在上次检查版本时 选择不再提醒
			 showDownloadDialog(isMust);
		 }else{
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				View view = inflater.inflate(R.layout.layout_pop_version_update, null);

				Builder builder = new AlertDialog.Builder(context);

				if (isMust) {
//					lys 强制更新
					builder.setTitle("您必须下载安装该软件的新版本才能继续使用！");
					noticeDialog = builder
							.setPositiveButton("马上更新",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialoginterface, int i) {
											noticeDialog.dismiss();
											showDownloadDialog(isMust);
										}
									})
							.setNegativeButton("退出",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialoginterface, int i) {
											android.os.Process
													.killProcess(android.os.Process
															.myPid());
										}
									}).show();
				} else {
					builder.setTitle("版本更新");
					builder.setMessage("检测到新版本，是否更新？");
					
					noticeDialog = builder
							.setPositiveButton("马上更新",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialoginterface, int i) {
											
											noticeDialog.dismiss();
											showDownloadDialog(isMust);
										}
									})
							.setNegativeButton("以后再说",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialoginterface, int i) {
											
											
											noticeDialog.dismiss();
										}
									}).show();
				}

				noticeDialog.setOnKeyListener(new OnKeyListener() {

					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						// TODO Auto-generated method stub

						if (event.getAction() == KeyEvent.ACTION_DOWN
								&& keyCode == KeyEvent.KEYCODE_BACK) {
							return true;
						}
						return false;
					}
				});
		 }

	}




	
	private void showDownloadDialog(boolean isMust) {
		if (!HTTPUtils.checkNetStatus(context)) {
			showToast("网络不稳定,下载失败,请重新尝试");
			return;
		}
		


		downloadApk();
	}

	
	

	

}
