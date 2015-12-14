package com.n4systems.services.config;

import com.n4systems.fieldid.service.FieldIdService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConfigService extends FieldIdService implements ConfigurationProvider {
	private static Logger logger = Logger.getLogger(ConfigService.class);
	private static final File CONFIG_FILE = new File("/var/fieldid/etc/config.yml");
	private static final Long DEFAULT_CONFIG = -1L;

	protected volatile static ConfigService instance;

	/*
	This class is setup as a java singleton (rather than springleton) as it is required outside of spring scope and prior to spring context setup.
	It it ALSO configured as a spring bean (which delegates here).  Consumers within spring scope should autowire rather than using the static accessor.
	 */
	public static ConfigService getInstance() {
		if (instance == null) {
			synchronized (ConfigService.class) {
				if (instance == null) {
					instance = new ConfigService();
				}
			}
		}
		return instance;
	}

	private Map<Long, RootConfig> configMap = new HashMap<>();

	protected ConfigService() {
		reloadConfigurations();
	}

	public synchronized void reloadConfigurations() {
		try {
			Constructor constructor = new Constructor();
			constructor.addTypeDescription(new TypeDescription(MutableRootConfig.class, "!config"));

			Yaml yaml = new Yaml(constructor);
			List<MutableRootConfig> configs = (List<MutableRootConfig>) yaml.loadAs(new FileInputStream(CONFIG_FILE), List.class);

			// Convert to immutable configs and copy into a map of tenant id to config
			configMap = configs.stream()
					.map(c -> new RootConfig((MutableRootConfig) c))
					.collect(Collectors.toMap(RootConfig::getTenantId, Function.identity()));

			if (!configMap.containsKey(DEFAULT_CONFIG)) {
				throw new IllegalStateException(CONFIG_FILE + " must contain a default (tenantId: " + DEFAULT_CONFIG + ") entry");
			}

		} catch (IOException e) {
			String message = "Failed parsing config file: " + CONFIG_FILE;
			logger.error(message, e);
			throw new RuntimeException(message, e);
		}
	}

	public RootConfig getConfig() {
		// Use the tenant from the current security context if we have one
		if (securityContext != null && securityContext.hasTenantSecurityFilter()) {
			return getConfig(securityContext.getTenantSecurityFilter().getTenantId());
		} else {
			return getConfig(DEFAULT_CONFIG);
		}
	}

	public RootConfig getConfig(Long tenantId) {
		RootConfig conf = configMap.get(tenantId);
		if (conf == null) {
			conf = configMap.get(DEFAULT_CONFIG);
		}
		return conf;
	}

	@Deprecated
	protected Object getValue(ConfigEntry entry, Long tenantId) {
		return entry.getValue(getConfig(tenantId));
	}

	@Deprecated
	public String getString(ConfigEntry entry, Long tenantId) {
		Object value = getValue(entry, tenantId);
		if (value instanceof String) {
			return (String) value;
		} else {
			return (value == null) ? null : value.toString();
		}
	}

	@Deprecated
	public Integer getInteger(ConfigEntry entry, Long tenantId) {
		// Some numeric config entries are asked for as both ints and longs.  Some are also stored as strings.
		Object value = getValue(entry, tenantId);
		if (value instanceof Long) {
			return Math.toIntExact((Long) value);
		} else if (value instanceof String){
			return Integer.valueOf((String) value);
		} else {
			return (Integer) value;
		}
	}

	@Deprecated
	public Long getLong(ConfigEntry entry, Long tenantId) {
		// Some numeric config entries are asked for as both ints and longs.  Some are also stored as strings.
		Object value = getValue(entry, tenantId);
		if (value instanceof Integer) {
			return ((Integer) value).longValue();
		} else if (value instanceof String){
			return Long.valueOf((String) value);
		} else {
			return (Long) value;
		}
	}

	@Deprecated
	public Boolean getBoolean(ConfigEntry entry, Long tenantId) {
		Object value = getValue(entry, tenantId);
		if (value instanceof String) {
			return Boolean.valueOf((String) value);
		} else {
			return (Boolean) value;
		}
	}

}
