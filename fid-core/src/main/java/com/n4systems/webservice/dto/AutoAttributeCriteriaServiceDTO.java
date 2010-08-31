package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class AutoAttributeCriteriaServiceDTO extends AbstractBaseServiceDTO {

	private long productTypeId;
	private List<Long> inputInfoFields = new ArrayList<Long>();
	private List<Long> outputInfoFields = new ArrayList<Long>();
	
	public long getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(long productTypeId) {
		this.productTypeId = productTypeId;
	}
	public List<Long> getInputInfoFields() {
		return inputInfoFields;
	}
	public void setInputInfoFields(List<Long> inputInfoFields) {
		this.inputInfoFields = inputInfoFields;
	}
	public List<Long> getOutputInfoFields() {
		return outputInfoFields;
	}
	public void setOutputInfoFields(List<Long> outputInfoFields) {
		this.outputInfoFields = outputInfoFields;
	}
}
