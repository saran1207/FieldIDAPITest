package com.n4systems.fieldid.api.mobile.resources.autoattribute;

import com.n4systems.fieldid.api.mobile.resources.SetupDataResource;
import com.n4systems.fieldid.api.mobile.resources.assettype.attributevalues.ApiAttributeValueResource;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rfid.ejb.entity.InfoOptionBean;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

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
		apiDefinition.setModified(definition.getModified());
		
		for (InfoOptionBean option: definition.getInputs()) {
			apiDefinition.getInputs().add(option.getUniqueID());
		}
		
		apiDefinition.setOutputs(apiAttributeValueResource.convertInfoOptions(definition.getOutputs()));
		
		return apiDefinition;
	}
	
	@GET
	@Path("list")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(readOnly = true)
	public List<Long> findAllIds(@QueryParam("criteriaId") Long criteriaId ) {
		QueryBuilder<AutoAttributeDefinition> builder = createTenantSecurityBuilder(AutoAttributeDefinition.class, true);
		builder.addWhere(WhereClauseFactory.create("criteria.id", criteriaId));
		List<AutoAttributeDefinition> definitions = persistenceService.findAll(builder);
		
		List<Long> result = new ArrayList<Long>();
		for(AutoAttributeDefinition definition : definitions) {
			result.add(definition.getId());
		}
		
		return result;
	}
}
