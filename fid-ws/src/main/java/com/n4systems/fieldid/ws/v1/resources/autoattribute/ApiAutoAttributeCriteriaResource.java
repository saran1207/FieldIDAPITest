package com.n4systems.fieldid.ws.v1.resources.autoattribute;

import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

import rfid.ejb.entity.InfoFieldBean;

import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.model.AutoAttributeCriteria;

@Component
@Path("autoAttributeCriteria")
public class ApiAutoAttributeCriteriaResource extends SetupDataResource<ApiAutoAttributeCriteria, AutoAttributeCriteria> {

	public ApiAutoAttributeCriteriaResource() {
		super(AutoAttributeCriteria.class, true);
	}

	@Override
	protected ApiAutoAttributeCriteria convertEntityToApiModel(AutoAttributeCriteria criteria) {
		ApiAutoAttributeCriteria apiCriteria = new ApiAutoAttributeCriteria();
		apiCriteria.setSid(criteria.getId());
		apiCriteria.setAssetTypeId(criteria.getAssetType().getId());
		
		for (InfoFieldBean field : criteria.getInputs()) {
			apiCriteria.getInputs().add(field.getUniqueID());
		}
		
		return apiCriteria;
	}
}
