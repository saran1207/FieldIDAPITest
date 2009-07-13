package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class StateSetListResponse extends AbstractListResponse {

	private List<StateSetServiceDTO> stateSets = new ArrayList<StateSetServiceDTO>();

	public List<StateSetServiceDTO> getStateSets() {
		return stateSets;
	}

	public void setStateSets(List<StateSetServiceDTO> stateSets) {
		this.stateSets = stateSets;
	}
	
}
