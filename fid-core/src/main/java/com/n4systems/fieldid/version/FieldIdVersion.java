package com.n4systems.fieldid.version;

import java.util.Properties;

import org.apache.log4j.Logger;

public class FieldIdVersion {
	private static Logger logger = Logger.getLogger(FieldIdVersion.class);
	
	private static String version;
	
	static {
		try {
            Properties props = new Properties();
            props.load(FieldIdVersion.class.getResourceAsStream("/com/package.properties"));
            version = props.getProperty("app.versionnumber");
            logger.info("Field ID Version: " + version);
        } catch (Exception e) {
        	logger.warn("Unable to load Field ID version", e);
            version = "UNKNOWN";
        }
	}
	
	public static String getVersion() {
		return version;
	}
}
