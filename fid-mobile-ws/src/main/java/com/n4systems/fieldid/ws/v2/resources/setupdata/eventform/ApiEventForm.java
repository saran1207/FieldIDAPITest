package com.n4systems.fieldid.ws.v2.resources.setupdata.eventform;

import com.n4systems.fieldid.ws.v2.resources.model.ApiReadOnlyModel;

import java.util.ArrayList;
import java.util.List;

public class ApiEventForm extends ApiReadOnlyModel {
	private List<ApiCriteriaSection> sections = new ArrayList<>();
	private boolean useScoreForResult;
    private boolean useObservationCountForResult;

    //Score pass/fail fields.
    private String scoreCalculationType;
    private ApiResultRange scorePassRange = new ApiResultRange();
    private ApiResultRange scoreFailRange = new ApiResultRange();

    //Observation Count pass/fail fields.
    private String observationPassCalculationType;
    private ApiResultRange observationPassRange = new ApiResultRange();

    private String observationFailCalcaulationType;
    private ApiResultRange observationFailRange = new ApiResultRange();

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

    public boolean isUseObservationCountForResult() {
        return useObservationCountForResult;
    }

    public String getScoreCalculationType() {
        return scoreCalculationType;
    }

    public void setScoreCalculationType(String scoreCalculationType) {
        this.scoreCalculationType = scoreCalculationType;
    }

    public ApiResultRange getScorePassRange() {
        return scorePassRange;
    }

    public void setScorePassRange(ApiResultRange scorePassRange) {
        this.scorePassRange = scorePassRange;
    }

    public ApiResultRange getScoreFailRange() {
        return scoreFailRange;
    }

    public void setScoreFailRange(ApiResultRange scoreFailRange) {
        this.scoreFailRange = scoreFailRange;
    }

    public String getObservationPassCalculationType() {
        return observationPassCalculationType;
    }

    public void setObservationPassCalculationType(String observationPassCalculationType) {
        this.observationPassCalculationType = observationPassCalculationType;
    }

    public ApiResultRange getObservationPassRange() {
        return observationPassRange;
    }

    public void setObservationPassRange(ApiResultRange observationPassRange) {
        this.observationPassRange = observationPassRange;
    }

    public String getObservationFailCalcaulationType() {
        return observationFailCalcaulationType;
    }

    public void setObservationFailCalcaulationType(String observationFailCalcaulationType) {
        this.observationFailCalcaulationType = observationFailCalcaulationType;
    }

    public ApiResultRange getObservationFailRange() {
        return observationFailRange;
    }

    public void setObservationFailRange(ApiResultRange observationFailRange) {
        this.observationFailRange = observationFailRange;
    }
}
