package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class StateSetServiceDTO extends AbstractBaseServiceDTO {
	
	private String name;
	
	private List<StateServiceDTO> states = new ArrayList<StateServiceDTO>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<StateServiceDTO> getStates() {
		return states;
	}

	public void setStates(List<StateServiceDTO> states) {
		this.states = states;
	}

}
