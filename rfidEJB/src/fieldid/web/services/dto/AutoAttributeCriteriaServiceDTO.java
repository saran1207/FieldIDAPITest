package fieldid.web.services.dto;

import java.util.Collection;

public class AutoAttributeCriteriaServiceDTO extends BaseServiceDTO {

	private String productTypeId;
	private Collection<String> inputInfoFields;
	private Collection<String> outputInfoFields;
	
	private Collection<AutoAttributeDefinitionServiceDTO> definitions;
	
	
	public String getProductTypeId() {
		return productTypeId;
	}
	public void setProductTypeId(String productTypeId) {
		this.productTypeId = productTypeId;
	}
	public Collection<String> getInputInfoFields() {
		return inputInfoFields;
	}
	public void setInputInfoFields(Collection<String> inputInfoFields) {
		this.inputInfoFields = inputInfoFields;
	}
	public Collection<String> getOutputInfoFields() {
		return outputInfoFields;
	}
	public void setOutputInfoFields(Collection<String> outputInfoFields) {
		this.outputInfoFields = outputInfoFields;
	}
	public Collection<AutoAttributeDefinitionServiceDTO> getDefinitions() {
		return definitions;
	}
	public void setDefinitions(	Collection<AutoAttributeDefinitionServiceDTO> definitions ) {
		this.definitions = definitions;
	}
	
	
	
}
