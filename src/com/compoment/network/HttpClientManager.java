package com.compoment.network;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.http.conn.ConnectTimeoutException;

import android.util.Log;



public class HttpClientManager {

	private Map<String, String> heads = new HashMap<String, String>();
	private Map<String, String> cookies = new HashMap<String, String>();

	/**访问服务器正常  请求有响应*/
	public static String connect000000="000000";
	/**抛出此类异常，表示连接丢失，也就是说网络连接的另一端非正常关闭连接（可能是主机断电、网线出现故障等导致）*/
	public static String connect000001="000001";
	/**URL协议、格式或者路径错误*/
	public static String connect000002="000002";
	/**连接服务器超时 连不上服务器（网络异常或服务器没开）*/
	public static String connect000003="000003";
	/**服务器响应超时 */
	public static String connect000004="000004";
	/**IOException*/
	public static String connect000005="000005";
	/**其它错误*/
	public static String connect000006="000006";

	
	
	public HttpClientManager() {

	}



	public NetErrBean httpGet(String url) {
		HttpURLConnection urlCon = null;
		InputStream is = null;  
		NetErrBean netErrBean=new NetErrBean();
		try {
			urlCon = (HttpURLConnection) (new URL(url)).openConnection();
			urlCon.setConnectTimeout(3000);
			urlCon.setReadTimeout(3000);
		
			urlCon.setDoInput(true); //允许输入流，即允许下载  
			urlCon.setDoOutput(true); //允许输出流，即允许上传  
			urlCon.setUseCaches(false); //不使用缓冲  
			urlCon.setRequestMethod("GET"); //使用get请求  
			//urlCon.setRequestProperty("Charset", "utf-8");
			

			
			 is = urlCon.getInputStream(); 
			InputStreamReader in = new InputStreamReader(is);
			BufferedReader bufferedReader = new BufferedReader(in);
			StringBuffer resultBuffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				resultBuffer.append(line);
			}
		
			
			

			netErrBean.errorCode=connect000000;
			netErrBean.errorMsg="访问服务器正常";
			netErrBean.errorType="01";
			netErrBean.returnData=resultBuffer.toString();
			return netErrBean;
		}
		
		catch (EOFException e) {
			//抛出此类异常，表示连接丢失，也就是说网络连接的另一端非正常关闭连接（可能是主机断电、网线出现故障等导致）
			e.printStackTrace();
			netErrBean.errorCode=connect000001;
			netErrBean.errorMsg="抛出此类异常，表示连接丢失，也就是说网络连接的另一端非正常关闭连接（可能是主机断电、网线出现故障等导致）";
			netErrBean.errorType="01";
			netErrBean.returnData="";
			return netErrBean;
		
			
		}
		catch (MalformedURLException e) {
			//1、URL协议、格式或者路径错误， 好好检查下你程序中的代码
			//如果是路径问题，最好不要包含中文路径，因为有时中文路径会乱码，导致无法识别
			e.printStackTrace();
			
			netErrBean.errorCode=connect000002;
			netErrBean.errorMsg="URL协议、格式或者路径错误";
			netErrBean.errorType="01";
			netErrBean.returnData="";
			return netErrBean;
		}
		catch (ConnectTimeoutException e) {
			//连接服务器超时
			e.printStackTrace();
			
			netErrBean.errorCode=connect000003;
			netErrBean.errorMsg="连接服务器超时";
			netErrBean.errorType="01";
			netErrBean.returnData="";
			return netErrBean;
		} 
		catch (SocketTimeoutException e) {
			//服务器响应超时
			e.printStackTrace();
			netErrBean.errorCode=connect000004;
			netErrBean.errorMsg="服务器响应超时";
			netErrBean.errorType="01";
			netErrBean.returnData="";
			return netErrBean;
		} 
		
		
		catch (IOException e) {
			e.printStackTrace();
			netErrBean.errorCode=connect000005;
			netErrBean.errorMsg="IOException";
			netErrBean.errorType="01";
			netErrBean.returnData="";
			return netErrBean;
		} catch (Exception e) {
			e.printStackTrace();
			netErrBean.errorCode=connect000006;
			netErrBean.errorMsg="其它错误";
			netErrBean.errorType="01";
			netErrBean.returnData="";
			return netErrBean;
		}finally{  
            if(is != null){  
                try {  
                    is.close();  
                } catch (IOException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }  
            }  
            if(urlCon != null){  
            	urlCon.disconnect();  
            }  
        }  
	
	}

	public byte[] httpPost(String url, byte[] postContent) {
		HttpURLConnection urlCon = null;
		try {
			urlCon = (HttpURLConnection) (new URL(url)).openConnection();
			urlCon.setConnectTimeout(15 * 1000);
			urlCon.setReadTimeout(15 * 1000);
			urlCon.setUseCaches(false);
			urlCon.setDoInput(true);
			urlCon.setDoOutput(true);
			urlCon.setRequestMethod("POST");
			// HttpsURLConnection.setFollowRedirects(true);

			// System.out.println("@@@ postContent="+new String(postContent));
			urlCon.getOutputStream().write(postContent, 0, postContent.length);
			urlCon.getOutputStream().flush();
			urlCon.getOutputStream().close();



			InputStream input = urlCon.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// System.out.println("@@@ response code="+urlCon.getResponseCode());
			// baos.write
			byte[] buffer = new byte[1024];
			int count = -1;
			while ((count = input.read(buffer, 0, 1024)) != -1) {
				baos.write(buffer, 0, count);
			}
			Map<String, List<String>> fileds = urlCon.getHeaderFields();
			Set<String> keys = fileds.keySet();
			for (Iterator<String> it = keys.iterator(); it.hasNext();) {
				String key = it.next();
				List<String> values = fileds.get(key);
				for (int i = 0; i < values.size(); i++) {
					String value = values.get(i);
					// System.out.println("+++++ head, key="+key+", value="+value);
					if ("Set-Cookie".equals(key)) {
						String nameValueStr = value.split(";")[0];
						String[] nameValue = nameValueStr.split("=");
						cookies.put(nameValue[0], nameValue[1]);
						// System.out.println("add cookie, name="+nameValue[0]+", value="+nameValue[1]);
					}
				}

			}
			return baos.toByteArray();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	

	public void addHead(String name, String value) {
		if (heads == null) {
			heads = new HashMap<String, String>();
		}
		heads.put(name, value);
	}

	public Map<String, String> getCookies() {
		return cookies;
	}
	
	public class NetErrBean
	{
		public String returnData;
		public String errorMsg;
		public String errorCode;
		public String errorType;
	}

}

