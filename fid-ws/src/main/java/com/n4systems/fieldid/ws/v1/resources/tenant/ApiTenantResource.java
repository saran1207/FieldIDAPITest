package com.n4systems.fieldid.ws.v1.resources.tenant;

import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;

public class ApiTenantResource extends ApiResource<ApiTenant, PrimaryOrg> {

	@Override
	public ApiTenant convertEntityToApiModel(PrimaryOrg primaryOrg) {
		ApiTenant apiTenant = new ApiTenant();
		apiTenant.setSid(primaryOrg.getTenant().getId());
		apiTenant.setUsingAssignedTo(primaryOrg.hasExtendedFeature(ExtendedFeature.AssignedTo));
		return apiTenant;
	}
}
