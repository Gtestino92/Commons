package com.mongo.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

	public static Date getDateCorrectGMT() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
		calendar.setTimeZone(TimeZone.getTimeZone("America/Buenos_Aires"));
		return calendar.getTime();
	}

}
