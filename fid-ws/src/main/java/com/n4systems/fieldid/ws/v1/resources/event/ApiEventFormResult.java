package com.n4systems.fieldid.ws.v1.resources.event;

import java.util.ArrayList;
import java.util.List;

public class ApiEventFormResult {
	private Long formId;
	private List<ApiCriteriaSectionResult> sections = new ArrayList<ApiCriteriaSectionResult>();

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public List<ApiCriteriaSectionResult> getSections() {
		return sections;
	}

	public void setSections(List<ApiCriteriaSectionResult> sections) {
		this.sections = sections;
	}

}
