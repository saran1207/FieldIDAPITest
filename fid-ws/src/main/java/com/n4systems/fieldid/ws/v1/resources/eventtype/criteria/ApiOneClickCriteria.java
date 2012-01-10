package com.n4systems.fieldid.ws.v1.resources.eventtype.criteria;

import java.util.ArrayList;
import java.util.List;

public class ApiOneClickCriteria extends ApiCriteria {
	private boolean principal;
	private List<ApiOneClickState> states = new ArrayList<ApiOneClickState>();

	public ApiOneClickCriteria(boolean principal, List<ApiOneClickState> states) {
		this();
		this.principal = principal;
		this.states = states;
	}
	
	public ApiOneClickCriteria() {
		setCriteriaType("ONECLICK");
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	public List<ApiOneClickState> getStates() {
		return states;
	}

	public void setStates(List<ApiOneClickState> states) {
		this.states = states;
	}
}
