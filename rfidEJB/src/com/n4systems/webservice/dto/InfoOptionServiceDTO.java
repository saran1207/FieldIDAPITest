package com.n4systems.webservice.dto;

public class InfoOptionServiceDTO extends AbstractBaseServiceDTO {

	private String name;
	private Long weight;
	private boolean staticData;
	private Long infoFieldId;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getWeight() {
		return weight;
	}
	public void setWeight(Long weight) {
		this.weight = weight;
	}
	public boolean isStaticData() {
		return staticData;
	}
	public void setStaticData(boolean staticData) {
		this.staticData = staticData;
	}
	public Long getInfoFieldId() {
		return infoFieldId;
	}
	public void setInfoFieldId(Long infoFieldId) {
		this.infoFieldId = infoFieldId;
	}
	
	

}
