package com.n4systems.util.properties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.n4systems.exceptions.UncheckedLoadingException;
import com.n4systems.model.Tenant;
import com.n4systems.reporting.PathHandler;


public class HirarchicalPropertiesLoader {
	private static final String KEY_VALUE_EXT = ".properties";
	private static final String XML_EXT = ".xml";
	
	// methods are static, hide constructor
	private HirarchicalPropertiesLoader() {}
	
	/**
	 * Loads HirarchicalProperties for a given Class.  Property files must be named
	 * the same as the Class and in the same Package.  Looks for .properties files first and then .xml.
	 * @see Class#getResourceAsStream(String)
	 * @see #load(Class, Properties)
	 * @see HierarchicalProperties
	 * @param clazz		Class to load properties file for
	 * @return			HirarchicalProperties
	 * @throws			UncheckedLoadingException on any IOException while loading
	 */
	public static HierarchicalProperties load(Class<?> clazz) {
		return load(clazz, (Properties)null);
	}
	
	/**
	 * Loads an overriding properties file using the resource base properties file for defaults.
	 * @param clazz		Class of properties file to load
	 * @return			HirarchicalProperties with global overrides
	 */
    public static HierarchicalProperties loadGlobal(Class<?> clazz) {		
		return load(clazz);
	}
	
	/**
	 * Loads HirarchicalProperties for a given Class with provided defaults.  Property files must be named
	 * the same as the Class and in the same Package.  Looks for .properties files first and then .xml.
	 * @see Class#getResourceAsStream(String)
	 * @see HierarchicalProperties
	 * @param clazz		Class to load properties file for
	 * @param defaults	Optional default Properties
	 * @return			HirarchicalProperties
	 * @throws			UncheckedLoadingException on any IOException while loading
	 */
	public static HierarchicalProperties load(Class<?> clazz, Properties defaults) {
		HierarchicalProperties properties = new HierarchicalProperties(defaults);
		
		InputStream in = null;
		try {
			in = clazz.getResourceAsStream(clazz.getSimpleName() + KEY_VALUE_EXT);
			if (in != null) {
				// if we found a properties file, load it.
				properties.load(in);
				
			} else {
				// otherwise look for an xml properties file
				in = clazz.getResourceAsStream(clazz.getSimpleName() + XML_EXT);
				if(in != null) {
					// try loading from xml
					properties.loadFromXML(in);
				}
			}
		} catch(IOException e) {
			throw new UncheckedLoadingException(e);
		} finally {
			IOUtils.closeQuietly(in);
		}
		
		return properties;
	}
}
