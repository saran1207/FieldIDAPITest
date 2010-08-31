package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class AutoAttributeDefinitionListResponse extends AbstractListResponse {
	
	private List<AutoAttributeDefinitionServiceDTO> autoAttributeDefinitions = new ArrayList<AutoAttributeDefinitionServiceDTO>();

	public List<AutoAttributeDefinitionServiceDTO> getAutoAttributeDefinitions() {
		return autoAttributeDefinitions;
	}

	public void setAutoAttributeDefinitions(
			List<AutoAttributeDefinitionServiceDTO> autoAttributeDefinitions) {
		this.autoAttributeDefinitions = autoAttributeDefinitions;
	}
}
