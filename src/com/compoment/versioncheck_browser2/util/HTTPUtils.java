package com.compoment.versioncheck_browser2.util;



import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

/**
 * <p>Title: iSoftStone</p>
 * <p>Description: </p>
 * HTTP访问工具
 * <p>Copyright: Copyright (c) 2012</p>
 * <p>Company: iSoftStone</p>
 *
 * @author Lilibo
 * @version 1.0
 */
public class HTTPUtils {
	// 网络状态
	private static boolean netStatus = false;
	private static String TAG = "net";
	
	
	public static void main(String[] args) {
		/*String urlstr = "http://gd.10086.cn/tryme/version.action?platform=iPhone&product=酷信&resolution=480x800";
		HTTPUtils.httpDoGet(urlstr);*/

		String urlstr = "http://gd.10086.cn/tryme/version.action";
		Map<String, String> params = new HashMap<String, String>();
		params.put("platform", "Android 2.2/2.3");
		params.put("product", "号携");
		params.put("resolution", "480x800");
		HTTPUtils.httpDoPost(urlstr, params);
	}
	


	/**
	 * HTTP GET请求
	 * 
	 * @param urlstr 请求URL
	 * @return 返回内容
	 */
	public static String httpDoGet(String urlstr) {
		System.out.println("------------ HTTPUtil httpDoGet Start ------------");
		System.out.println("------------ HTTPUtil httpDoGet urlstr : " + urlstr);
		String response = null;
		InputStreamReader resinput = null;
		HttpURLConnection httpurlconn = null;
		try {
			URL url = new URL(urlstr);
			httpurlconn = (HttpURLConnection) url.openConnection();
			httpurlconn.setConnectTimeout(1000 * 3);
			httpurlconn.setReadTimeout(1000 * 3);
			httpurlconn.setRequestMethod("GET");

			int icode = httpurlconn.getResponseCode();
			System.out.println("Response Code : " + icode + " (" + httpurlconn.getResponseMessage() + ") ");
			if (icode == HttpURLConnection.HTTP_OK) {
				int length = httpurlconn.getContentLength();
				if (length <= 0) {
					return null;
				}
				resinput = new InputStreamReader(httpurlconn.getInputStream(), "GBK");
				StringBuffer strbuf = new StringBuffer(length);
				char[] charbuff = new char[length];
		        int i;
		        while ((i = resinput.read(charbuff, 0, length - 1)) != -1) {
		        	strbuf.append(charbuff, 0, i);
		        }
		        response = strbuf.toString();
				System.out.println("Response Content : \n" + response);
			}
		} catch (Exception e) {
			response = null;
			System.out.println("------------ HTTPUtil httpDoGet Exception " + e);
		} finally {
			if (null != resinput) {
				try {
					resinput.close();
				} catch (Exception e) {
					System.out.println("------------ HTTPUtil httpDoGet InputStreamReader Close Exception " + e);
				}
			}
			if (null != httpurlconn) {
				try {
					httpurlconn.disconnect();
				} catch (Exception e) {
					System.out.println("------------ HTTPUtil httpDoGet HttpURLConnection Close Exception " + e);
				}
			}
			System.out.println("------------ HTTPUtil httpDoGet finally ------------");
		}
		System.out.println("------------ HTTPUtil httpDoGet End ------------");
		return response;
	}

	/**
	 * HTTP POST请求
	 * 
	 * @param urlstr 请求URL
	 * @param params 请求参数
	 * @return 返回内容
	 */
	public static String httpDoPost(String urlstr, Map<String, String> params) {
		System.out.println("------------ HTTPUtil httpDoPost Start ------------");
		System.out.println("------------ HTTPUtil httpDoPost urlstr : " + urlstr);
		String response = null;
		InputStreamReader resinput = null;
		HttpURLConnection httpurlconn = null;
		try {
			URL url = new URL(urlstr);
			httpurlconn = (HttpURLConnection) url.openConnection();
			httpurlconn.setConnectTimeout(1000 * 3);
			httpurlconn.setReadTimeout(1000 * 3);
			httpurlconn.setRequestMethod("POST");
			httpurlconn.setDoOutput(true);
			if (null != params) {
				OutputStreamWriter resoutput = null;
				try {
					resoutput = new OutputStreamWriter(httpurlconn.getOutputStream(), "UTF-8");
					StringBuffer strbuf = new StringBuffer();
					for (String key : params.keySet()) {
						strbuf.append(key).append("=").append(params.get(key)).append("&");
					}
					String postparams = strbuf.toString();
					postparams = postparams.substring(0, postparams.length() -1);
					System.out.println("postparams : " + postparams);
					resoutput.write(postparams);
					resoutput.flush();
				} catch (Exception e) {
					System.out.println("------------ HTTPUtil postParams Exception " + e);
				} finally {
					if (null != resoutput) {
						try {
							resoutput.close();
						} catch (Exception e) {
							System.out.println("------------ HTTPUtil postParams OutputStreamWriter Close Exception " + e);
						}
					}
				}
			}

			int icode = httpurlconn.getResponseCode();
			System.out.println("Response Code : " + icode + " (" + httpurlconn.getResponseMessage() + ") ");
			if (icode == HttpURLConnection.HTTP_OK) {
				int length = httpurlconn.getContentLength();
				if (length <= 0) {
					return null;
				}
				resinput = new InputStreamReader(httpurlconn.getInputStream(), "GBK");
				StringBuffer strbuf = new StringBuffer(length);
				char[] charbuff = new char[length];
		        int i;
		        while ((i = resinput.read(charbuff, 0, length - 1)) != -1) {
		        	strbuf.append(charbuff, 0, i);
		        }
		        response = strbuf.toString();
				System.out.println("Response Content : \n" + response);
			}
		} catch (Exception e) {
			response = null;
			System.out.println("------------ HTTPUtil httpDoPost Exception " + e);
		} finally {
			if (null != resinput) {
				try {
					resinput.close();
				} catch (Exception e) {
					System.out.println("------------ HTTPUtil httpDoPost InputStreamReader Close Exception " + e);
				}
			}
			if (null != httpurlconn) {
				try {
					httpurlconn.disconnect();
				} catch (Exception e) {
					System.out.println("------------ HTTPUtil httpDoPost HttpURLConnection Close Exception " + e);
				}
			}
			System.out.println("------------ HTTPUtil httpDoPost finally ------------");
		}
		System.out.println("------------ HTTPUtil httpDoPost End ------------");
		return response;
	}

	
	/**
	 * 检测网络是否通
	 * @param context
	 * @return 网络通，返回true，否则返回false
	 */
	public synchronized static boolean checkNetStatus(Context context) {
		try {
			netStatus = false;
			ConnectivityManager connManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			connManager.getActiveNetworkInfo();
			if (connManager.getActiveNetworkInfo() != null) {
				netStatus = connManager.getActiveNetworkInfo().isConnected();
			}
			if (!netStatus) {
				Log.e(TAG, "Network Connected:false");
			}
		} catch (Exception e) {
			e.printStackTrace();
			netStatus = false;
		} 
		return netStatus;
	}
	
	
}