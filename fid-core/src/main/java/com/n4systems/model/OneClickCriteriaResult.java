package com.n4systems.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "oneclick_criteriaresults")
@PrimaryKeyJoinColumn(name="id")
public class OneClickCriteriaResult extends CriteriaResult {

	@ManyToOne(cascade= CascadeType.REFRESH, fetch= FetchType.EAGER, optional=false)
	private State state;

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@Override
	public EventResult getResult() {
		return ((OneClickCriteria)criteria).isPrincipal() ? state.getEventResult() : null;
	}

	@Override
	public String getResultString() {
		return state != null ? state.getDisplayText() : "";
	}

	@Override
	public String toString() {
		return super.toString() + "[state=" + state + "]";
	}
	
}
