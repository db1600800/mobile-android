package com.compoment.contact;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;



import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

//http://blog.csdn.net/nicholas6lee/article/details/7633708

public class ContactUtil {
	Context context;

	static String[] pinyin = { "a", "ai", "an", "ang", "ao", "ba", "bai",
			"ban", "bang", "bao", "bei", "ben", "beng", "bi", "bian", "biao",
			"bie", "bin", "bing", "bo", "bu", "ca", "cai", "can", "cang",
			"cao", "ce", "ceng", "cha", "chai", "chan", "chang", "chao", "che",
			"chen", "cheng", "chi", "chong", "chou", "chu", "chuai", "chuan",
			"chuang", "chui", "chun", "chuo", "ci", "cong", "cou", "cu",
			"cuan", "cui", "cun", "cuo", "da", "dai", "dan", "dang", "dao",
			"de", "deng", "di", "dian", "diao", "die", "ding", "diu", "dong",
			"dou", "du", "duan", "dui", "dun", "duo", "e", "en", "er", "fa",
			"fan", "fang", "fei", "fen", "feng", "fo", "fou", "fu", "ga",
			"gai", "gan", "gang", "gao", "ge", "gei", "gen", "geng", "gong",
			"gou", "gu", "gua", "guai", "guan", "guang", "gui", "gun", "guo",
			"ha", "hai", "han", "hang", "hao", "he", "hei", "hen", "heng",
			"hong", "hou", "hu", "hua", "huai", "huan", "huang", "hui", "hun",
			"huo", "ji", "jia", "jian", "jiang", "jiao", "jie", "jin", "jing",
			"jiong", "jiu", "ju", "juan", "jue", "jun", "ka", "kai", "kan",
			"kang", "kao", "ke", "ken", "keng", "kong", "kou", "ku", "kua",
			"kuai", "kuan", "kuang", "kui", "kun", "kuo", "la", "lai", "lan",
			"lang", "lao", "le", "lei", "leng", "li", "lia", "lian", "liang",
			"liao", "lie", "lin", "ling", "liu", "long", "lou", "lu", "lv",
			"luan", "lue", "lun", "luo", "ma", "mai", "man", "mang", "mao",
			"me", "mei", "men", "meng", "mi", "mian", "miao", "mie", "min",
			"ming", "miu", "mo", "mou", "mu", "na", "nai", "nan", "nang",
			"nao", "ne", "nei", "nen", "neng", "ni", "nian", "niang", "niao",
			"nie", "nin", "ning", "niu", "nong", "nu", "nv", "nuan", "nue",
			"nuo", "o", "ou", "pa", "pai", "pan", "pang", "pao", "pei", "pen",
			"peng", "pi", "pian", "piao", "pie", "pin", "ping", "po", "pu",
			"qi", "qia", "qian", "qiang", "qiao", "qie", "qin", "qing",
			"qiong", "qiu", "qu", "quan", "que", "qun", "ran", "rang", "rao",
			"re", "ren", "reng", "ri", "rong", "rou", "ru", "ruan", "rui",
			"run", "ruo", "sa", "sai", "san", "sang", "sao", "se", "sen",
			"seng", "sha", "shai", "shan", "shang", "shao", "she", "shen",
			"sheng", "shi", "shou", "shu", "shua", "shuai", "shuan", "shuang",
			"shui", "shun", "shuo", "si", "song", "sou", "su", "suan", "sui",
			"sun", "suo", "ta", "tai", "tan", "tang", "tao", "te", "teng",
			"ti", "tian", "tiao", "tie", "ting", "tong", "tou", "tu", "tuan",
			"tui", "tun", "tuo", "wa", "wai", "wan", "wang", "wei", "wen",
			"weng", "wo", "wu", "xi", "xia", "xian", "xiang", "xiao", "xie",
			"xin", "xing", "xiong", "xiu", "xu", "xuan", "xue", "xun", "ya",
			"yan", "yang", "yao", "ye", "yi", "yin", "ying", "yo", "yong",
			"you", "yu", "yuan", "yue", "yun", "za", "zai", "zan", "zang",
			"zao", "ze", "zei", "zen", "zeng", "zha", "zhai", "zhan", "zhang",
			"zhao", "zhe", "zhen", "zheng", "zhi", "zhong", "zhou", "zhu",
			"zhua", "zhuai", "zhuan", "zhuang", "zhui", "zhun", "zhuo", "zi",
			"zong", "zou", "zu", "zuan", "zui", "zun", "zuo" };

	public ContactUtil(Context context) {
		this.context = context;
	}



	/**
	 * 
	 * @param str
	 *            搜索字符�?
	 * @param exp
	 *            追加的正则表达式
	 * @return 拼音搜索正则表达�?
	 */
	public String getPYSearchRegExp(String str, String exp) {
		int start = 0;
		String regExp = "";
		str = str.toLowerCase();
		boolean isFirstSpell = true;
		for (int i = 0; i < str.length(); ++i) {
			String tmp = str.substring(start, i + 1);
			isFirstSpell = binSearch(tmp) ? false : true;

			if (isFirstSpell) {
				regExp += str.substring(start, i) + exp;
				start = i;
			} else {
				isFirstSpell = true;
			}

			if (i == str.length() - 1)
				regExp += str.substring(start, i + 1) + exp;
		}
		return regExp;
	}
	
	/*
	 * �Ƿ�Ϊƴ�����
	 */
	public boolean isSimpleFormPingyin(String str) {
		
		str = str.toLowerCase();
		boolean isSimpleForm = true;
		isSimpleForm = binSearch(str) ? false : true;

		return isSimpleForm;
	}

	/**
	 * str �Ƿ��� ƴ��������
	 * 
	 * @param str
	 *           
	 * @return 
	 */
	public boolean binSearch(String str) {
		
		if(str.equals("*") || str.equals("'") || str.equals("+") || str.equals("(") || 
				str.equals(")") || str.equals("{") || str.equals("}") || str.equals("`") || str.equals("~") || str.equals("?") || str.equals("&")
				|| str.equals("%") || str.equals("$") 
				)
		{
			return false;
		}
		
		
		int mid = 0;
		int start = 0;
		int end = pinyin.length - 1;

		while (start < end) {
			mid = start + ((end - start) / 2);
			if (pinyin[mid].matches(str + "[a-zA-Z]*"))
				return true;

			if (pinyin[mid].compareTo(str) < 0)
				start = mid + 1;
			else
				end = mid - 1;
		}
		return false;
	}

	/**
	 * 拼音匹配
	 * 
	 * @param src
	 *            含有中文的字符串
	 * @param des
	 *            查询的拼�?
	 * @return 是否能匹配拼�?
	 */
	public boolean pyMatches(String src, String des) {
		if (src != null) {
			src = src.replaceAll("[^ a-zA-Z]", "").toLowerCase();
			src = src.replaceAll("[ ]+", " ");
			String condition = getPYSearchRegExp(des, "[a-zA-Z]* ");

			String[] tmp = condition.split("[ ]");
			String[] tmp1 = src.split("[ ]");

			for (int i = 0; i + tmp.length <= tmp1.length; ++i) {
				String str = "";
				for (int j = 0; j < tmp.length; j++)
					str += tmp1[i + j] + " ";
				if (str.matches(condition))
					return true;
			}
		}
		return false;
	}

	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	public boolean isPinYin(String str) {
		Pattern pattern = Pattern.compile("[ a-zA-Z]*");
		return pattern.matcher(str).matches();
	}

	public boolean containCn(String str) {
		Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]");
		return pattern.matcher(str).find();
	}
}
