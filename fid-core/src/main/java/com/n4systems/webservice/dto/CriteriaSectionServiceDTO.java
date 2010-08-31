package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class CriteriaSectionServiceDTO extends AbstractBaseServiceDTO {

	private String title;
	private Long inspectionTypeId;
	private List<CriteriaServiceDTO> criteria = new ArrayList<CriteriaServiceDTO>();
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<CriteriaServiceDTO> getCriteria() {
		return criteria;
	}
	public void setCriteria(List<CriteriaServiceDTO> criteria) {
		this.criteria = criteria;
	}
	public Long getInspectionTypeId() {
		return inspectionTypeId;
	}
	public void setInspectionTypeId(Long inspectionTypeId) {
		this.inspectionTypeId = inspectionTypeId;
	}
	
	
}
