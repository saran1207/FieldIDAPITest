package com.n4systems.fieldid.ws.v1.resources.eventtype;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

public class ApiEventForm extends ApiReadonlyModel {
	private List<ApiCriteriaSection> sections = new ArrayList<ApiCriteriaSection>();
	private boolean useScoreForResult;

	public List<ApiCriteriaSection> getSections() {
		return sections;
	}

	public void setSections(List<ApiCriteriaSection> sections) {
		this.sections = sections;
	}

	public boolean isUseScoreForResult() {
		return useScoreForResult;
	}

	public void setUseScoreForResult(boolean useScoreForResult) {
		this.useScoreForResult = useScoreForResult;
	}

}
