package com.n4systems.fieldid.ws.v2.resources.setupdata.procedure.isolationpointsourcetypes;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadOnlyModel;

public class ApiIsolationPointSourceType extends ApiReadOnlyModel {
	private String source;
	private String sourceText;

	public ApiIsolationPointSourceType() {}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceText() {
		return sourceText;
	}

	public void setSourceText(String sourceText) {
		this.sourceText = sourceText;
	}
}
