package com.n4systems.fieldid.ws.v1.resources.eventtype.criteria;

import java.util.ArrayList;
import java.util.List;

public class ApiScoreCriteria extends ApiCriteria {
	private List<ApiScore> scores = new ArrayList<ApiScore>();

	public ApiScoreCriteria(List<ApiScore> scores) {
		this();
		this.scores = scores;
	}

	public ApiScoreCriteria() {
		setCriteriaType("SCORE");
	}

	public void setScores(List<ApiScore> scores) {
		this.scores = scores;
	}

	public List<ApiScore> getScores() {
		return scores;
	}
}
