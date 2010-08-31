package com.n4systems.webservice.dto;

import java.util.ArrayList;
import java.util.List;

public class AutoAttributeDefinitionServiceDTO extends AbstractBaseServiceDTO {
	
	private long autoAttributeCriteriaId;	
	private List<Long> inputInfoOptions = new ArrayList<Long>();
	private List<InfoOptionServiceDTO> outputInfoOptions = new ArrayList<InfoOptionServiceDTO>();

	public long getAutoAttributeCriteriaId() {
		return autoAttributeCriteriaId;
	}
	public void setAutoAttributeCriteriaId(long autoAttributeCriteriaId) {
		this.autoAttributeCriteriaId = autoAttributeCriteriaId;
	}
	public List<Long> getInputInfoOptions() {
		return inputInfoOptions;
	}
	public void setInputInfoOptions(List<Long> inputInfoOptions) {
		this.inputInfoOptions = inputInfoOptions;
	}
	public List<InfoOptionServiceDTO> getOutputInfoOptions() {
		return outputInfoOptions;
	}
	public void setOutputInfoOptions(List<InfoOptionServiceDTO> outputInfoOptions) {
		this.outputInfoOptions = outputInfoOptions;
	}

}
