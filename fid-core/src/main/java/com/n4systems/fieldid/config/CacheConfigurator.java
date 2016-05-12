package com.n4systems.fieldid.config;

import net.sf.ehcache.CacheManager;
import org.apache.log4j.Logger;
import org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public class CacheConfigurator {
	private static Logger logger = Logger.getLogger(CacheConfigurator.class);
	private static final String ehCacheConfigPath = "/var/fieldid/etc/ehcache.xml";

	private static boolean cacheEnabled = false;

	public static boolean isCacheEnabled() {
		return cacheEnabled;
	}

	public static synchronized void initAndSetJPAProperties(Map<String, String> properties) {
		File ehCacheConfig = new File(ehCacheConfigPath);

		if (ehCacheConfig.exists()) {
			logger.info("Cache config found.  Enabling Caching");
            /*
            Force initialization of the CacheManager using a config file from the filesystem rather than classpath
            This needs to be performed prior to EntityManagerFactory creation
            */
			try (InputStream fis = new FileInputStream(ehCacheConfig.getAbsolutePath())) {
				CacheManager.create(fis);
				properties.put("hibernate.cache.use_second_level_cache", "true");
				properties.put("hibernate.cache.use_query_cache", "true");
				properties.put("hibernate.cache.region.factory_class", SingletonEhCacheRegionFactory.class.getName());
				logger.info("Caching Enabled");
				cacheEnabled = true;
			} catch (Exception e){
				cacheEnabled = false;
				throw new RuntimeException("Unable to load ehcache configuration", e);
			}
		} else {
			properties.put("hibernate.cache.use_second_level_cache", "false");
			properties.put("hibernate.cache.use_query_cache", "false");
			logger.info("Cache config not found.  Caching Disabled");
		}
	}
}
