package com.compoment.login;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.compoment.network.HttpClientManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

//http://www.ithouge.com/memory-leak.html
public class LoginRequest {
	private String SENT = "SMS_SENT";
	private String DELIVERED = "SMS_DELIVERED";
	

	
    
	Context context=null;

	String imsi;// SIM卡序列号
	String iccid;// 手机序列号
	


	
	public LoginRequest(Context context)
	{
		
		this.context=context;
	}
	

	// （用户名，密码）方式登录
	public void loginCheck_username_password(String userName, String password) {

	}

	// (Sim卡序列号，手机序列号) 方式登录
	public void loginCheck_imsi_iccid(final String imsi, final String iccid) {
		Thread t = new Thread(new Runnable() {
			public void run() {
				if (!isSimCardExist()) {
					boolean isICCIDExist = isEmptyValid(iccid);
					boolean isIMSIExist = isEmptyValid(imsi);
					
					// 情况1：手机序列号不存在 sim卡序列号不存在
					if (!isICCIDExist && !isIMSIExist) {
						if (hasProfile()) {
							enterMainActivity();
						} else {
							enterRegisterActivity();
						}

						return;
					}

					// updateUserInfoByHttp(phone);
					// saveUserUpdateInProfile(iccid);
					// showNoNetworkConnectionDialog();
					// String phone = checkUserRegisterByHttp(iccid);

				}
			}
		});
		t.start();

	}


	private void saveUserUpdateInProfile(String iccid) {

	}

	private boolean hasProfile() {
		return true;
	}

	private void enterMainActivity() {
		// context.startService(new Intent(ServiceManager.INTENTS));
	}

	private void enterNewUpdateIntroductionActivity() {
	}

	private void enterRegisterActivity() {

	}

	public static String getImsiFromProfile(Context context) {
		return "";
		// return SettingUtils.getSetting(context,
		// SHARE_PREFERENCES_ITEM_IMSI,null);
	}

	private void showNoNetworkConnectionDialog() {
		
	}

	private void sendSms(String destination, String content) {
		if (null != destination && null != content) {
			SmsManager smsManager = SmsManager.getDefault();
			Intent sendIntent = new Intent(SENT);
			Intent deliveredIntent = new Intent(DELIVERED);
			PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
					sendIntent, 0);
			PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
					deliveredIntent, 0);

			smsManager.sendTextMessage("10086", null, content, sentPI,
					deliveredPI);
		} else {
			// showDialog(getTaskId());
		}
	}

	private String checkUserLoginByHttp(String iccid) {
		HttpClientManager httpClientManager = new HttpClientManager();
		String url = "http://120.197.230.39:9080/Web_Mgr/CheckIsRegister"
				+ "?imsiNum=" + iccid;
		// System.out.println("+++++++++++++++ check register url=" + url);
		byte[] resp = httpClientManager.httpGet2(url);
		if (resp != null) {
			System.out.println("+++++++++++++++ check register resp="
					+ new String(resp));
			try {
				ByteArrayInputStream bais = new ByteArrayInputStream(resp);
				UserLoginParser parser = new UserLoginParser(bais);
				UserBean userRegister = parser.getUserRegister();
				String phone = userRegister.getPhone();

				if (phone != null && !phone.trim().equals("")) {
					Pattern pattern = Pattern.compile("[0-9]*");
					Matcher isNum = pattern.matcher(phone);
					if (isNum.matches()) {
						return phone;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return "NETWORK_UNREACHABLE";
		}
		return null;
	}



	public String readLocalMacAddress() {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	private String readImsiNum() {
		TelephonyManager telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsiNum = telManager.getSubscriberId();

		return imsiNum;
	}

	private TelephonyManager telManager;

	private TelephonyManager getTelephonyManager() {
		if (telManager == null) {
			telManager = (TelephonyManager) this.context
					.getSystemService(Context.TELEPHONY_SERVICE);
		}
		return telManager;
	}

	public String getICCID() {
		return getTelephonyManager().getSimSerialNumber();
	}

	public String getIMSI() {
		return getTelephonyManager().getSubscriberId();
	}

	
	/* APP 版本号 */
	private String getCurrentAppVersion() {
		int id = context.getResources().getIdentifier("app_version", "string",
				context.getPackageName());
		return context.getResources().getString(id);

	}

	private void toastShort(final String text) {
	
				Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
			
	}

	private void toastLong(final String text) {
	
				Toast.makeText(context, text, Toast.LENGTH_LONG).show();
			
	}

	/* SIM卡是否存在 */
	public boolean isSimCardExist() {
		boolean isSimCardExist = true;
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
			isSimCardExist = false;
		}
		return isSimCardExist;
	}

	private boolean isEmptyValid(String text) {
		return !(text == null || "".equals(text.trim()));
	}

	public boolean isICCIDValid(String iccid) {
		if (isEmptyValid(iccid) && !(iccid.length() < 11)) {
			return true;
		} else {
			return false;
		}
	}

	
	/* 内核版本 */
	private String readSysKernel() {
		String kernel = "";

		String str1 = "/proc/version";
		String str2;
		String[] arrayOfString;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();

			if (str2 != null) {
				arrayOfString = str2.split("\\s+");
				kernel = arrayOfString[2];// KernelVersion
			}

			localBufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return kernel;
	}

}