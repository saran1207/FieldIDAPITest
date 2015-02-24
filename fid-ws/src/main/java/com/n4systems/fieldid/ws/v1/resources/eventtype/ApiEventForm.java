package com.n4systems.fieldid.ws.v1.resources.eventtype;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

import java.util.ArrayList;
import java.util.List;

public class ApiEventForm extends ApiReadonlyModel {
	private List<ApiCriteriaSection> sections = new ArrayList<ApiCriteriaSection>();
	private boolean useScoreForResult;
    private Boolean useObservationCountForResult;

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

    public Boolean getUseObservationCountForResult() {
        return useObservationCountForResult;
    }

    public void setUseObservationCountForResult(Boolean useObservationCountForResult) {
        this.useObservationCountForResult = useObservationCountForResult;
    }
}
