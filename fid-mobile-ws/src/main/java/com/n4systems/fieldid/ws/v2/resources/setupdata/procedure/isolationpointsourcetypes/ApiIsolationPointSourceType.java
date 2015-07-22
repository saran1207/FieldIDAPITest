package com.n4systems.fieldid.ws.v2.resources.setupdata.procedure.isolationpointsourcetypes;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadonlyModel;

public class ApiIsolationPointSourceType extends ApiReadonlyModel{
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
