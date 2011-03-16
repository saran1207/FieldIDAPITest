package com.n4systems.fieldid.selenium;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

public class SeleniumConfigLoader {
	private static final Logger logger = Logger.getLogger(SeleniumConfigLoader.class);
    private static final String OVERRIDE_CONFIG_FILE_LOCATION = "/var/fieldid/selenium.properties";
    private static final String DEFAULT_CONFIG_RESOURCE_PATH = "/default-selenium.properties";

    public SeleniumConfig loadConfig() {
        try {
            File overrideFile = new File(OVERRIDE_CONFIG_FILE_LOCATION);
            if (overrideFile.exists()) {
                return loadConfigFrom(new FileInputStream(overrideFile));
            } else {
                return loadConfigFrom(getClass().getResourceAsStream(DEFAULT_CONFIG_RESOURCE_PATH));
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private SeleniumConfig loadConfigFrom(InputStream configStream) throws Exception {
        Properties props = new Properties();
        props.load(configStream);

        SeleniumConfig config = new SeleniumConfig();

        config.setActionDelay(props.getProperty("actionDelay"));
        config.setInitCompany(props.getProperty("initCompany"));
        config.setProtocol(props.getProperty("protocol"));
        config.setSeleniumBrowser(props.getProperty("seleniumBrowser"));
        config.setSeleniumServerHost(props.getProperty("seleniumServerHost"));
        config.setSeleniumServerPort(Integer.parseInt(props.getProperty("seleniumServerPort")));
        config.setTestServerContextRoot(props.getProperty("testServerContextRoot"));
        config.setTestServerDomain(props.getProperty("testServerDomain"));
        config.setDatabaseUrl(props.getProperty("databaseUrl"));
        config.setDatabaseUser(props.getProperty("databaseUser"));
        config.setDatabasePassword(props.getProperty("databasePassword"));
        config.setMailServerPort(Integer.parseInt(props.getProperty("embeddedMailServerPort")));

        return config;
    }

    public static void main(String[] args) throws Exception {
        SeleniumConfig config = new SeleniumConfigLoader().loadConfig();
        logger.info(ToStringBuilder.reflectionToString(config));
    }

}

