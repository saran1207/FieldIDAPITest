package com.n4systems.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.log4j.Logger;

public class StreamHelper {
	private static Logger logger = Logger.getLogger(StreamHelper.class);
	
	private StreamHelper() {}
	
	public static InputStream openQuietly(File file) {
		if (file == null || !file.canRead()) {
			logger.warn(String.format("Unable to open stream.  Could not access file [%s]", file));
		}
		
		InputStream imageStream = null;
		try {
			imageStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {}
		return imageStream;
	}
	
}
