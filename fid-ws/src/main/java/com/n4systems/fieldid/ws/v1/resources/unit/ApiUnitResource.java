package com.n4systems.fieldid.ws.v1.resources.unit;

import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.model.UnitOfMeasure;

@Component
@Path("unit")
public class ApiUnitResource extends SetupDataResource<ApiUnit, UnitOfMeasure> {

	public ApiUnitResource() {
		super(UnitOfMeasure.class);
	}

	@Override
	protected ApiUnit convertEntityToApiModel(UnitOfMeasure unit) {
		ApiUnit apiUnit = new ApiUnit();
		apiUnit.setSid(unit.getId());
		apiUnit.setActive(true);
		apiUnit.setModified(unit.getModified());
		apiUnit.setName(unit.getName());
		apiUnit.setShortName(unit.getShortName());
		apiUnit.setSelectable(unit.isSelectable());
		if (unit.getChild() != null) {
			apiUnit.setChild(convertEntityToApiModel(unit.getChild()));
		}
		return apiUnit;
	}

}
