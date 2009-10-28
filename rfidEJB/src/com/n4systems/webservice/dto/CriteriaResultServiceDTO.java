package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class CriteriaResultServiceDTO extends AbstractBaseServiceDTO {

	private long criteriaId;
	private long stateId;
	private long inspectionId;
	private List<ObservationResultServiceDTO> recommendations = new ArrayList<ObservationResultServiceDTO>();
	private List<ObservationResultServiceDTO> deficiencies = new ArrayList<ObservationResultServiceDTO>();

	public long getCriteriaId() {
		return criteriaId;
	}
	public void setCriteriaId(long criteriaId) {
		this.criteriaId = criteriaId;
	}
	public long getStateId() {
		return stateId;
	}
	public void setStateId(long stateId) {
		this.stateId = stateId;
	}
	public long getInspectionId() {
		return inspectionId;
	}
	public void setInspectionId(long inspectionId) {
		this.inspectionId = inspectionId;
	}
	public List<ObservationResultServiceDTO> getRecommendations() {
		return recommendations;
	}
	public void setRecommendations(List<ObservationResultServiceDTO> recommendations) {
		this.recommendations = recommendations;
	}
	public List<ObservationResultServiceDTO> getDeficiencies() {
		return deficiencies;
	}
	public void setDeficiencies(List<ObservationResultServiceDTO> deficiencies) {
		this.deficiencies = deficiencies;
	}	
}
