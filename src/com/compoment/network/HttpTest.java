package com.compoment.network;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.compoment.network.HttpClientManager.NetErrBean;
import com.google.gson.Gson;

public class HttpTest {
	String baseurl = "https://ip:port/front/macula-gbss-mobile/";


	private String http() {
		HttpClientManager httpClientManager = new HttpClientManager();
		String url = baseurl + "gbss-mobile/product/series/getSeries"
				+ "?lastUpdateTime=" + "";

		NetErrBean netErrBean = httpClientManager.httpGet(url);
		if (netErrBean != null && netErrBean.errorCode.equals("000000")) {

			try {
	

				//value2Bean
//				Gson gson = new Gson();
//				Bean bean = gson.fromJson(sb.toString(), Bean.class);
//				if(bean==null)
//				{
//		        System.out.println(bean.toString());
//				}
				return "";

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return "NETWORK_UNREACHABLE";
		}
		return null;
	}
}
