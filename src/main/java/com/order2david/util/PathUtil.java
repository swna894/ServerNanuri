package com.order2david.util;

import java.io.File;

import org.springframework.stereotype.Component;

@Component
public class PathUtil {

	public static Boolean isExist(String path) {
		File file = new File(path);
		if(!file.exists()) {
			file.mkdirs();
		}
		return file.exists();
	}
	
	public static Boolean isFile(String path) {
		File file = new File(path);
		return file.isFile() && file.exists();
	}
}
