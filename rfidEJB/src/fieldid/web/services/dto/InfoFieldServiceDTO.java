package fieldid.web.services.dto;

import java.util.ArrayList;
import java.util.Collection;

public class InfoFieldServiceDTO extends LegacyBaseServiceDTO  {

	
	
	private String name;
	private Long r_productInfo;
	private Collection<InfoOptionServiceDTO> infoOptions;
	private Boolean required;
	private Boolean usedInTemplate;
	private Boolean usingUnitOfMeasure;
	private Long r_unitOfMeasure;
	private String fieldType;
	private String valueType;
	/**
	 * since version 5
	 */
	private boolean retired;
	
	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public InfoFieldServiceDTO(){
		this.infoOptions = new ArrayList<InfoOptionServiceDTO>();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setR_productInfo(Long r_productInfo) {
		this.r_productInfo = r_productInfo;
	}
	public Long getR_productInfo() {
		return r_productInfo;
	}
	public void setInfoOptions(Collection<InfoOptionServiceDTO> infoOptions) {
		this.infoOptions = infoOptions;
	}
	public Collection<InfoOptionServiceDTO> getInfoOptions() {
		return infoOptions;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setUsedInTemplate(Boolean usedInTemplate) {
		this.usedInTemplate = usedInTemplate;
	}

	public Boolean getUsedInTemplate() {
		return usedInTemplate;
	}

	public Boolean getUsingUnitOfMeasure() {
		return usingUnitOfMeasure;
	}

	public void setUsingUnitOfMeasure(Boolean usingUnitOfMeasure) {
		this.usingUnitOfMeasure = usingUnitOfMeasure;
	}

	public Long getR_unitOfMeasure() {
		return r_unitOfMeasure;
	}

	public void setR_unitOfMeasure(Long r_unitofmeasure) {
		this.r_unitOfMeasure = r_unitofmeasure;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldType() {
		return fieldType;
	}

	public boolean isRetired() {
		return retired;
	}

	public void setRetired(boolean retired) {
		this.retired = retired;
	}
	
}
