package com.n4systems.fieldid.api.mobile.resources.eventtype;

import com.n4systems.fieldid.api.mobile.resources.model.ApiReadonlyModel;

import java.util.ArrayList;
import java.util.List;

public class ApiEventForm extends ApiReadonlyModel {
	private List<ApiCriteriaSection> sections = new ArrayList<ApiCriteriaSection>();
	private boolean useScoreForResult;
    private boolean useObservationCountForResult;

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

    public boolean getUseObservationCountForResult() {
        return useObservationCountForResult;
    }

    public void setUseObservationCountForResult(boolean useObservationCountForResult) {
        this.useObservationCountForResult = useObservationCountForResult;
    }
}
