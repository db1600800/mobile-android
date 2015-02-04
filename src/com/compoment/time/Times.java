package com.compoment.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Times {
	public static String getCurrentDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new Date());

		return date;
	}


	public static boolean compareDate(String lastTime, String currentTime) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date lastDate = sdf.parse(lastTime);
			Date currentDate = sdf.parse(currentTime);

			if (lastDate.getHours() == currentDate.getHours()) {

				if (lastDate.getMinutes() == currentDate.getMinutes()) {
					return true;
				}
              
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}
}
