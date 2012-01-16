package com.n4systems.fieldid.ws.v1.resources.eventtype.criteria;

import java.util.ArrayList;
import java.util.List;

public class ApiScoreCriteria extends ApiCriteria {
	private List<ApiScore> scores;

	public ApiScoreCriteria(List<ApiScore> scores) {
		super("SCORE");
		this.scores = scores;
	}

	public ApiScoreCriteria() {
		this(new ArrayList<ApiScore>());
	}

	public void setScores(List<ApiScore> scores) {
		this.scores = scores;
	}

	public List<ApiScore> getScores() {
		return scores;
	}
}
