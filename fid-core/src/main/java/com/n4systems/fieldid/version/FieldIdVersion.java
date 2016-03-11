package com.n4systems.fieldid.version;

import org.apache.log4j.Logger;

import java.util.Properties;

public class FieldIdVersion {
	private static Logger logger = Logger.getLogger(FieldIdVersion.class);
	
	private static String version;
	
	static {
		try {
            Properties props = new Properties();
            props.load(FieldIdVersion.class.getResourceAsStream("/com/package.properties"));
            version = props.getProperty("app.versionnumber");
        } catch (Exception e) {
            version = "UNKNOWN";
        }
	}
	
	public static String getVersion() {
		return version;
	}

    public static String getWebVersionDescription() {
        return "Web, Version: " + getVersion();
    }
}
