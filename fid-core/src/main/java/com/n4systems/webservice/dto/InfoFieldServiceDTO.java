package com.n4systems.webservice.dto;

import java.util.List;

public class InfoFieldServiceDTO extends AbstractBaseServiceDTO {

	private String name;
	private String fieldType;
	private Long weight;
	private boolean required;
	private boolean usingUnitOfMeasure;
	private Long defaultUnitOfMeasureId;
	private Long productTypeId;
	private List<InfoOptionServiceDTO> infoOptions;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public Long getWeight() {
		return weight;
	}
	public void setWeight(Long weight) {
		this.weight = weight;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public boolean isUsingUnitOfMeasure() {
		return usingUnitOfMeasure;
	}
	public void setUsingUnitOfMeasure(boolean usingUnitOfMeasure) {
		this.usingUnitOfMeasure = usingUnitOfMeasure;
	}
	public List<InfoOptionServiceDTO> getInfoOptions() {
		return infoOptions;
	}
	public void setInfoOptions(List<InfoOptionServiceDTO> infoOptions) {
		this.infoOptions = infoOptions;
	}
	public Long getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}
	public Long getDefaultUnitOfMeasureId() {
		return defaultUnitOfMeasureId;
	}
	public void setDefaultUnitOfMeasureId(Long defaultUnitOfMeasureId) {
		this.defaultUnitOfMeasureId = defaultUnitOfMeasureId;
	}
	
	
}
