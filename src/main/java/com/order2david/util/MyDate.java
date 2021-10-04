package com.order2david.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyDate {
	
	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public static String toDay(LocalDateTime dateTime) {
		return dateTime.format(formatter);
	}


}
