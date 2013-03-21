package com.n4systems.fieldid.ws.v1.resources.assettype.attributevalues;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rfid.ejb.entity.InfoOptionBean;

public class ApiAttributeValueResource {
	
	public ApiAttributeValue convertInfoOption(InfoOptionBean infoOption) {
		ApiAttributeValue attribValue = new ApiAttributeValue();
		attribValue.setAttributeId(infoOption.getInfoField().getUniqueID());
		
		if (infoOption.getInfoField().isDateField()) {
			if (infoOption.getName() != null) {
				attribValue.setValue(new Date(Long.parseLong(infoOption.getName())));
			}
		} else {
			attribValue.setValue(infoOption.getName());
		}
		return attribValue;
	}
	
	// I had to explicitly write this method instead of extending ApiResource<A, E extends AbstractEntity> 
	// Because InfoOptionBean doesn't extend AbstractEntity. Not sure if we need to adjust the model at this point.
	public List<ApiAttributeValue> convertInfoOptions(List<InfoOptionBean> infoOptions) {
		List<ApiAttributeValue> attribValues = new ArrayList<ApiAttributeValue>();
		for (InfoOptionBean infoOption: infoOptions) {
			attribValues.add(convertInfoOption(infoOption));
		}
		return attribValues;
	}
}
