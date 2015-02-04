package com.compoment.downloading_breakpoint_continue.util;

import java.io.File;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Environment;
import android.telephony.TelephonyManager;

public class Urls {
	// 新太内部测试服务器
	// private static final String SERVER_ADDRESS = "http://10.200.100.225:9080";
	// 南方基地服务器
	private static final String SERVER_ADDRESS = "http://120.197.230.39:9080";

	// public static final String UPDATE_URL =
	// "http://59.41.223.229/mwayadmin/interface/version.jsp";
	/** 检查更新的API */
	public static final String UPDATE_URL = SERVER_ADDRESS
			+ "/mwayadmin/interface/version.jsp";
	/** 检查更新的API @since 1.1 */
	public static final String UPDATE_URL_2 = SERVER_ADDRESS
			+ "/mwayadmin/appUpdate";
	
	public static final String BEAT_URL = SERVER_ADDRESS + "/Web_Mgr/beat";

	// public static final String UPDATE_USERINFO_URL =
	// "http://59.41.223.229/webmgr/UpdateUserInfo";
	public static final String UPDATE_USERINFO_URL = SERVER_ADDRESS
			+ "/Web_Mgr/UpdateUserInfo";

	public static final String SRC_ID = "106586983";

	public static final String BUSINESS_OPEN_RECEIVE_NUMBER = "10086";
	// 查询用户状态
	public static final String USERSTATUS_URL = SERVER_ADDRESS
			+ "/Web_Mgr/queryuserstatus";
	// 发送短信
	public static final String SMS_URL = SERVER_ADDRESS
			+ "/Web_Mgr/SendInviteMsg?mobile=";

	// 新太科技留言服务器
	// public static final String MESSAGE_SERVER =
	// "http://59.41.223.229/VoipMsgService/";

	// 南方基地留言服务器
	public static final String MESSAGE_SERVER = SERVER_ADDRESS
			+ "/VoipMsgService/";

	// 查询用户是否注册
	public static final String USER_REGISTER_SERVER = SERVER_ADDRESS
			+ "/Web_Mgr/CheckIsRegister";

	// 新用户注册接口
	public static final String USER_REGISTER_API = SERVER_ADDRESS
			+ "/Web_Mgr/checkAndAddRegister";

	// i9000热点
	// public static final String MESSAGE_SERVER =
	// "http://192.168.43.156:9080/VoipMsgService/";

	/**
	 * 同步登陆状态、检查登陆状态的接口。
	 * 
	 * 参考：
	 * 
	 * 同步登陆状态：http://120.197.230.39:9080/Web_Mgr/msgUserStatus?type=update&
	 * myMobile=13800138000&status=1
	 * 
	 * 查询登陆状态：http://120.197.230.39:9080/Web_Mgr/msgUserStatus?type=check&myMobile
	 * =13800138000&checkMobile=12345678901
	 * */
	public static final String CHECK_OR_UPDATE_STATUS_API = SERVER_ADDRESS
			+ "/Web_Mgr/msgUserStatus";

	public static final String SEND_MISS_MESSAGE_OR_CALL_SMS_API = SERVER_ADDRESS
			+ "/Web_Mgr/smsNotice";

	
	
	
	
	public static final int NETWORK_TYPE_UNKNOWN = -1;
	public static final int NETWORK_TYPE_WIFI = 1;
	public static final int NETWORK_TYPE_2G = 2;
	public static final int NETWORK_TYPE_3G = 3;
	
	
	public static String getUploadURL2(String snderNumber, String rcverNumber,
			int duration, long fileLength, String type, String fileName) {
		type = (Util.MESSAGE_TYPE_VIDEO.equals(type) ? "v" : "a");

		// 断点续传Servlet
		// String url2 = Constants.MESSAGE_SERVER + "NewUploadServlet2?FILE_LENGTH="
		// + fileLength + "&DURATION=" + duration + "&t=" + type
		// + "&RECEIVER_P_NUM=" + rcverNumber + "&SENDER_P_NUM=" + snderNumber
		// + "&FILE_NAME=" + fileName;

		String url2 = Urls.MESSAGE_SERVER
				+ "UploadMultiReceiverServlet?FILE_LENGTH=" + fileLength + "&DURATION="
				+ duration + "&t=" + type + "&RECEIVER_P_NUM=" + rcverNumber
				+ "&SENDER_P_NUM=" + snderNumber + "&FILE_NAME=" + fileName + "&m=t";

		return url2;
	}

	public static String getForwardURL(String snderNumber, String rcverNumber,
			String msgRcvId) {
		String url = Urls.MESSAGE_SERVER + "ForwardServlet?SENDER_P_NUM="
				+ snderNumber + "&RECEIVER_P_NUM=" + rcverNumber + "&MSG_ID="
				+ msgRcvId + "&m=t";

		return url;
	}

	
	public static String getPreVideoImageUrl(String path) {
		return Urls.MESSAGE_SERVER + "GetPrevImageRecordServlet?Path=" + path;

		// return Constants.MESSAGE_SERVER
		// + "getAudioRecordAction!getPrevImageRecord.action?Path=" + path;
	}


	public static String getCheckNewMsgURL(String rcverNumber) {
		String url = Urls.MESSAGE_SERVER
				+ "CheckNewMsgServlet?RECEIVER_P_NUM=" + rcverNumber;

		return url;
	}
	
	
	public static String getMarkAsReadedURL(String msg_id) {
		return Urls.MESSAGE_SERVER + "MarkAsReadedServlet?MSG_ID=" + msg_id;
	}
	
	
	/** 返回是否有网络连接。如果WiFi和GPRS都无，则返回false。 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		State gprsState = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		State wifiState = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();

		if (State.CONNECTED != gprsState && State.CONNECTED != wifiState) {
			return false;
		} else {
			return true;
		}
	}

	/** 获取当前网络连接的类型 */
	public static int getActiveNetworkType(Context context) {
		if (isWiFiConnected(context)) { // WiFi
			return NETWORK_TYPE_WIFI;
		} else if (isMobileConnected(context)) { // 移动网络，2G或3G
			return getMobileNetworkType(context);
		} else { // 无网络连接
			return NETWORK_TYPE_UNKNOWN;
		}
	}

	/** 是否已连接WiFi */
	public static boolean isWiFiConnected(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State wifiState = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (android.net.NetworkInfo.State.CONNECTED != wifiState) {
			return false;
		} else {
			return true;
		}
	}

	/** 是否已连接移动网络 */
	public static boolean isMobileConnected(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobileState = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		if (android.net.NetworkInfo.State.CONNECTED != mobileState) {
			return false;
		} else {
			return true;
		}
	}

	/** 是否已连接3G网络 */
	public static boolean isMobile3GConnected(Context context) {
		if (isMobileConnected(context)
				&& getMobileNetworkType(context) == NETWORK_TYPE_3G) {
			return true;
		} else {
			return false;
		}
	}

	public static int getMobileNetworkType(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		int networkType = manager.getNetworkType();

		switch (networkType) {
		case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2G
		case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2G、联通2G
		case TelephonyManager.NETWORK_TYPE_GPRS:
			return NETWORK_TYPE_2G;
		case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3G
		case TelephonyManager.NETWORK_TYPE_1xRTT: // CDMA2000
		case TelephonyManager.NETWORK_TYPE_HSDPA: // WCDMA联通的3G
		case TelephonyManager.NETWORK_TYPE_HSPA: // 3G
		case TelephonyManager.NETWORK_TYPE_UMTS: // 3G
		default:
			return NETWORK_TYPE_3G;
		}
	}
	
	
}
