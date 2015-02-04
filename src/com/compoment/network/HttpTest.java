package com.compoment.network;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.google.gson.Gson;

public class HttpTest {
	String baseurl = "https://ip:port/front/macula-gbss-mobile/";


	private String productSeriesGetSeries() {
		HttpClientManager httpClientManager = new HttpClientManager();
		String url = baseurl + "gbss-mobile/product/series/getSeries"
				+ "?lastUpdateTime=" + "";

		byte[] resp = httpClientManager.httpGet2(url);
		if (resp != null) {

			try {
				ByteArrayInputStream bais = new ByteArrayInputStream(resp);
				String str = "";
				// 默认以utf-8形式
				String encode = "utf-8";
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(bais, encode));
				StringBuffer sb = new StringBuffer();

				while ((str = reader.readLine()) != null) {
					sb.append(str);
				}

				//value2Bean
//				Gson gson = new Gson();
//				Bean bean = gson.fromJson(sb.toString(), Bean.class);
//				if(bean==null)
//				{
//		        System.out.println(bean.toString());
//				}
				return sb.toString();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return "NETWORK_UNREACHABLE";
		}
		return null;
	}
}
