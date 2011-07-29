package com.n4systems.util.uri;

import java.net.URI;

import com.n4systems.model.BaseEntity;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;
import com.n4systems.util.StringUtils;

public class ActionURLBuilder extends InternalUrlBuilder {
	
	public static ActionURLBuilder newUrl(ConfigurationProvider configContext) {
		String domain = configContext.getString(ConfigEntry.SYSTEM_DOMAIN);
		String protocol = configContext.getString(ConfigEntry.SYSTEM_PROTOCOL);

		String baseUrl = String.format("%s://www.%s/fieldid/", protocol, domain);
		return new ActionURLBuilder(URI.create(baseUrl), configContext);
	}
	
	private String action;
	private BaseEntity entity;
	private String parameters;

	public ActionURLBuilder(URI baseUri, ConfigurationProvider configContext) {
		super(baseUri, configContext);
	}

	@Override
	protected String path() {
		if (StringUtils.isNotEmpty(action)) {
			return getActionPath();
		}
		return "";
	}

	private String getActionPath() {
		String path = action + ".action";

		if (parameters != null) {
			path += "?" + parameters;
		}

		if (entity != null && !entity.isNew()) {
			addParameter("uniqueID", entity.getId());
		}
		return path;
	}

	public String getAction() {
		return action;
	}

	public ActionURLBuilder setAction(String action) {
		this.action = action;
		return this;
	}

	public ActionURLBuilder setParameters(String parameters) {
		this.parameters = parameters;
		return this;
	}

	public ActionURLBuilder setEntity(BaseEntity entity) {
		this.entity = entity;
		return this;
	}

	public BaseEntity getEntity() {
		return entity;
	}

}
