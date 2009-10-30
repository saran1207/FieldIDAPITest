package com.n4systems.fieldid;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

	protected String propertyFileName;
	protected Properties p;

	public PropertyLoader(Class<?> c) {
		propertyFileName = c.getSimpleName() + ".properties";
		p = new Properties();
		InputStream in = c.getResourceAsStream(propertyFileName);
		try {
			p.load(in);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public String getProperty(String key) {
		return p.getProperty(key);
	}
}
