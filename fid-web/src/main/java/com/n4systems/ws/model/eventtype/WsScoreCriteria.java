package com.n4systems.ws.model.eventtype;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

public class WsScoreCriteria extends WsCriteria {
	
	private List<WsScore> scores = new ArrayList<WsScore>();
	
	public WsScoreCriteria() {
		setCriteriaType("SCORE");
	}
	
	public void setScores(List<WsScore> scores) {
		this.scores = scores;
	}

	@XmlElement(name="Scores")
	public List<WsScore> getScores() {
		return scores;
	}
}
