package fieldid.web.services.dto;

import java.util.Collection;

public class AutoAttributeDefinitionServiceDTO extends BaseServiceDTO {
	
	private String autoAttributeCriteriaId;
	
	private Collection<String> inputInfoOptions;
	private Collection<InfoOptionServiceDTO> outputInfoOptions;
	
	
	public String getAutoAttributeCriteriaId() {
		return autoAttributeCriteriaId;
	}
	public Long getAutoAttributeCriteriaIdLong() {
		return convertToLong( autoAttributeCriteriaId );
	}
	public void setAutoAttributeCriteriaId(String r_criteria) {
		this.autoAttributeCriteriaId = r_criteria;
	}
	public void setAutoAttributeCritieraId(Long r_criteria) {
		this.autoAttributeCriteriaId = r_criteria.toString();
	}
	public Collection<String> getInputInfoOptions() {
		return inputInfoOptions;
	}
	public void setInputInfoOptions(Collection<String> inputInfoOptions) {
		this.inputInfoOptions = inputInfoOptions;
	}
	public Collection<InfoOptionServiceDTO> getOutputInfoOptions() {
		return outputInfoOptions;
	}
	public void setOutputInfoOptions(Collection<InfoOptionServiceDTO> outputInfoOptions) {
		this.outputInfoOptions = outputInfoOptions;
	}
	
	
}
