package com.n4systems.util.uri;

import java.net.URI;

import com.n4systems.model.BaseEntity;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.StringUtils;

public class ActionURLBuilder extends BaseUrlBuilder {
	private String action;
	
	private BaseEntity entity;
	
	public ActionURLBuilder(URI baseUri, ConfigContext configContext) {
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
		if (entity != null && !entity.isNew()) {
			path += "?uniqueID=" + entity.getId();
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

	public ActionURLBuilder setEntity(BaseEntity entity) {
		this.entity = entity;
		return this;
	}

	public BaseEntity getEntity() {
		return entity;
	}

	
}
