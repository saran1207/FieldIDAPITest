package com.n4systems.fieldid.ws.v2.resources.setupdata.unit;

import com.n4systems.fieldid.ws.v2.resources.setupdata.SetupDataResourceReadOnly;
import com.n4systems.model.UnitOfMeasure;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Component
@Path("unit")
public class ApiUnitResource extends SetupDataResourceReadOnly<ApiUnit, UnitOfMeasure> {

	public ApiUnitResource() {
		super(UnitOfMeasure.class, false);
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
