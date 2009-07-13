package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class AutoAttributeCriteriaListResponse extends AbstractListResponse {

	private List<AutoAttributeCriteriaServiceDTO> autoAttributeCriteria = new ArrayList<AutoAttributeCriteriaServiceDTO>();

	public List<AutoAttributeCriteriaServiceDTO> getAutoAttributeCriteria() {
		return autoAttributeCriteria;
	}

	public void setAutoAttributeCriteria(
			List<AutoAttributeCriteriaServiceDTO> autoAttributeCriteria) {
		this.autoAttributeCriteria = autoAttributeCriteria;
	}
}
