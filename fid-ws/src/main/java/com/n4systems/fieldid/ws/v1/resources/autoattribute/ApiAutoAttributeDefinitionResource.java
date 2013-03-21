package com.n4systems.fieldid.ws.v1.resources.autoattribute;

import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.fieldid.ws.v1.resources.assettype.attributevalues.ApiAttributeValueResource;
import com.n4systems.model.AutoAttributeDefinition;

@Component
@Path("autoAttributeDefinition")
public class ApiAutoAttributeDefinitionResource extends SetupDataResource<ApiAutoAttributeDefinition, AutoAttributeDefinition> {
	@Autowired private ApiAttributeValueResource apiAttributeValueResource;

	public ApiAutoAttributeDefinitionResource() {
		super(AutoAttributeDefinition.class, true);
	}

	@Override
	protected ApiAutoAttributeDefinition convertEntityToApiModel(AutoAttributeDefinition definition) {
		ApiAutoAttributeDefinition apiDefinition = new ApiAutoAttributeDefinition();
		apiDefinition.setSid(definition.getId());
		apiDefinition.setCriteriaId(definition.getCriteria().getId());
		
		for (InfoOptionBean option: definition.getInputs()) {
			apiDefinition.getInputs().add(option.getUniqueID());
		}
		
		apiDefinition.setOutputs(apiAttributeValueResource.convertInfoOptions(definition.getOutputs()));
		
		return apiDefinition;
	}
}
