package com.n4systems.fieldid.selenium.util;

import org.apache.log4j.Logger;

public class TimeLogger {
	private final long startTime = System.currentTimeMillis();
	private final Logger logger;
	private final String name;
	
	public TimeLogger(Logger logger, String format, Object ... args) {
		this(logger, String.format(format, args));
	}
	
	public TimeLogger(Logger logger, String name) {
		this.logger = logger;
		this.name = name;
	}
	
	public void stop() {
		long elapsed = System.currentTimeMillis() - startTime;
		String message = name + ": " + elapsed + "ms";
		
		if (elapsed > 1000)
			logger.error(message);
		else
			logger.info(message);
	}
}
