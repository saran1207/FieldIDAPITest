package com.n4systems.fieldid.ws.v2.resources.setupdata.eventform;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadonlyModel;

import java.util.ArrayList;
import java.util.List;

public class ApiEventForm extends ApiReadonlyModel {
	private List<ApiCriteriaSection> sections = new ArrayList<>();
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
