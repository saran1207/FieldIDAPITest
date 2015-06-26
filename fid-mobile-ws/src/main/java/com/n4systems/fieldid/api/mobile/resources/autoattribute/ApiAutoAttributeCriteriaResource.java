package com.n4systems.fieldid.api.mobile.resources.autoattribute;

import com.n4systems.fieldid.api.mobile.resources.SetupDataResource;
import com.n4systems.model.AutoAttributeCriteria;
import org.springframework.stereotype.Component;
import rfid.ejb.entity.InfoFieldBean;

import javax.ws.rs.Path;

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
		apiCriteria.setModified(criteria.getModified());
		
		for (InfoFieldBean field : criteria.getInputs()) {
			apiCriteria.getInputs().add(field.getUniqueID());
		}
		
		for(InfoFieldBean field: criteria.getOutputs()) {
			apiCriteria.getOutputs().add(field.getUniqueID());
		}
		
		return apiCriteria;
	}
}
