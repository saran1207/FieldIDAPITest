package com.n4systems.fieldid.ws.v1.resources.eventtype.criteria;

import java.util.ArrayList;
import java.util.List;

public class ApiOneClickCriteria extends ApiCriteria {
	private boolean principal;
	private List<ApiOneClickState> states;

	public ApiOneClickCriteria(boolean principal, List<ApiOneClickState> states) {
		super("ONECLICK");
		this.principal = principal;
		this.states = states;
	}
	
	public ApiOneClickCriteria() {
		this(false, new ArrayList<ApiOneClickState>());
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
