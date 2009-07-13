package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class CriteriaServiceDTO extends AbstractBaseServiceDTO {

	private String displayText;	
	private boolean principal;
	private Long criteriaSectionId;
	private Long stateSetId;
	private List<ObservationServiceDTO> recommendations = new ArrayList<ObservationServiceDTO>();
	private List<ObservationServiceDTO> deficiencies = new ArrayList<ObservationServiceDTO>();
	
	public String getDisplayText() {
		return displayText;
	}
	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}
	public boolean isPrincipal() {
		return principal;
	}
	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}
	public Long getCriteriaSectionId() {
		return criteriaSectionId;
	}
	public void setCriteriaSectionId(Long criteriaSectionId) {
		this.criteriaSectionId = criteriaSectionId;
	}
	public Long getStateSetId() {
		return stateSetId;
	}
	public void setStateSetId(Long stateSetId) {
		this.stateSetId = stateSetId;
	}
	public List<ObservationServiceDTO> getRecommendations() {
		return recommendations;
	}
	public void setRecommendations(List<ObservationServiceDTO> recommendations) {
		this.recommendations = recommendations;
	}
	public List<ObservationServiceDTO> getDeficiencies() {
		return deficiencies;
	}
	public void setDeficiencies(List<ObservationServiceDTO> deficiencies) {
		this.deficiencies = deficiencies;
	}		
}
