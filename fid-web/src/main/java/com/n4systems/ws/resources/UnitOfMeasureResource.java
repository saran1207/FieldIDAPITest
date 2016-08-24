package com.n4systems.ws.resources;

import com.n4systems.model.UnitOfMeasure;
import com.n4systems.ws.model.unitofmeasure.WsUnitOfMeasure;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("/UnitOfMeasure")
public class UnitOfMeasureResource extends BaseResource<UnitOfMeasure, WsUnitOfMeasure> {

	public UnitOfMeasureResource(@Context UriInfo uriInfo) {
		super(uriInfo, new UnitOfMeasureResourceDefiner());
	}

}
