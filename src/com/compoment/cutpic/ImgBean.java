package com.compoment.cutpic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.Color;

public class ImgBean {

	public Bitmap img;

	public Bitmap getImg() {
		return img;
	}

	public void setImg(Bitmap img) {
		this.img =img;
		getBackgroundRgb16();
		this.img =null;
	}

	public int x;
	public int y;
	public int w;
	public int h;
	public String rgb16;
	
	public String enName;
	
	public String cnName;
	
	public String controlType;//控件类型
	
	public ImgBean controlBelong;//控件属于哪个控件
	
	public String id;
	
	public int red10value =0;
	public  int green10value =0;
	public  int blue10Value = 0;
	
	public ImgBean()
	{
		id=getCharAndNumr(3)+"-"+getCharAndNumr(2)+"-"+getCharAndNumr(3);
	}

	public String getRBG( int x, int y) {
		// 10进制值
		int rgb10value = img.getPixel(x, y);
		// 16进制值
		String rgb16value = Integer.toHexString(rgb10value);

	   red10value = Color.red(rgb10value);
	   green10value = Color.green(rgb10value);
	   blue10Value = Color.blue(rgb10value);
		return rgb16value;
	}

	public String getBackgroundRgb16() {

		List xy5point = new ArrayList();

		for (int i = 0; i <  5; i++) {
			xy5point.add(getRBG( i, 0));
		}

		for (int j = 0; j <  5; j++) {
			xy5point.add(getRBG( 0, j));
		}

		final int size =xy5point.size();
	
		LinkedHashMap<String, Integer> result = mostEle((String[]) xy5point
				.toArray(new String[size]));

		List c = new ArrayList(result.values());
		Set<String> s = result.keySet();

		int p = 0;
		for (Iterator it = result.keySet().iterator(); it.hasNext();)

		{

			Object key = it.next();
			if (p == 0) {
				rgb16 = (String) key;
			}

		}

		return rgb16;
		// System.out.print("一共有" + result.size() + "元素最多。它们分别是");
		// System.out.print(s);
		// System.out.println("，分别出现了" + c.get(0) + "次。");
	}

	private static LinkedHashMap<String, Integer> map;

	public static LinkedHashMap<String, Integer> mostEle(String[] strArray) {
		map = new LinkedHashMap<String, Integer>();

		String str = "";
		int count = 0;
		int result = 0;

		for (int i = 0; i < strArray.length; i++)
			str += strArray[i];

		for (int i = 0; i < strArray.length; i++) {
			String temp = str.replaceAll(strArray[i], "");
			count = (str.length() - temp.length()) / strArray[i].length();

			if (count > result) {
				map.clear();
				map.put(strArray[i], count);
				result = count;
			} else if (null == map.get(strArray[i]) && count == result)
				map.put(strArray[i], count);
		}
		return map;
	}
	
	public String getCharAndNumr(int length)     
	{     
		//只想要大写的字母 可以使 int choice =65； 只想要小写的字母，就 int choice =97；注：1.方法的参数 length 是生成的随机数的长度。
		
	    String val = "";     
	             
	    Random random = new Random();     
	    for(int i = 0; i < length; i++)     
	    {     
	        String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字     
	                 
	        if("char".equalsIgnoreCase(charOrNum)) // 字符串     
	        {     
	            int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; //取得大写字母还是小写字母     
	            val += (char) (choice + random.nextInt(26));     
	        }     
	        else if("num".equalsIgnoreCase(charOrNum)) // 数字     
	        {     
	            val += String.valueOf(random.nextInt(10));     
	        }     
	    }     
	             
	    return val;     
	}   


}
