package com.n4systems.api.model;

import java.io.Serializable;

import com.n4systems.exporting.beanutils.ExportField;

@SuppressWarnings("serial")
public abstract class ExternalModelView implements Serializable {
	
	@ExportField(title="System ID", order = 9999999)
	private String globalId;
	
	public ExternalModelView() {}
	
	public String getGlobalId() {
		return globalId;
	}

	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}
	
	public boolean isNew() {
		return (globalId == null);
	}
}
