package com.compoment.video_record_play.util;

import java.util.Random;

public class DigitDealWith {
	/** 生成id */
	public static String createId() {
		return generateRandomNumericString(15);
	}
	
	/** 生成随机数 */
	public static String generateRandomNumericString(int length) {
		length = Math.abs(length);
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int oneRandomNumericCharacter = random.nextInt(9);
			sb.append(oneRandomNumericCharacter);
		}
		return sb.toString();
	}
}
