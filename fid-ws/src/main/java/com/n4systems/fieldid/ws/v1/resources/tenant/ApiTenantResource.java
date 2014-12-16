package com.n4systems.fieldid.ws.v1.resources.tenant;

import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import rfid.ejb.entity.IdentifierCounter;

public class ApiTenantResource extends ApiResource<ApiTenant, PrimaryOrg> {

	@Override
	public ApiTenant convertEntityToApiModel(PrimaryOrg primaryOrg) {
		ApiTenant apiTenant = new ApiTenant();
		apiTenant.setSerialNumberLabel(primaryOrg.getIdentifierLabel());
		apiTenant.setSerialNumberFormat(primaryOrg.getIdentifierFormat());

		IdentifierCounter identifierCounter = persistenceService.find(createTenantSecurityBuilder(IdentifierCounter.class));
		apiTenant.setSerialNumberDecimalFormat(identifierCounter.getDecimalFormat());

		apiTenant.setUsingAssignedTo(primaryOrg.hasExtendedFeature(ExtendedFeature.AssignedTo));
		apiTenant.setUsingJobSites(primaryOrg.hasExtendedFeature(ExtendedFeature.JobSites));
		apiTenant.setUsingAdvancedLocation(primaryOrg.hasExtendedFeature(ExtendedFeature.AdvancedLocation));
		apiTenant.setUsingOrderDetails(primaryOrg.hasExtendedFeature(ExtendedFeature.OrderDetails));
        apiTenant.setUsingLoto(primaryOrg.getTenant().getSettings().isLotoEnabled());
		apiTenant.setUsingEvents(primaryOrg.getTenant().getSettings().isInspectionsEnabled());
		apiTenant.setUsingGpsCapture(primaryOrg.getTenant().getSettings().isGpsCapture());
		apiTenant.setLockoutDuration(primaryOrg.getTenant().getSettings().getAccountPolicy().getLockoutDuration());
		apiTenant.setMaxAttempts(primaryOrg.getTenant().getSettings().getAccountPolicy().getMaxAttempts());
		apiTenant.setLockoutOnMobile(primaryOrg.getTenant().getSettings().getAccountPolicy().isLockoutOnMobile());
		return apiTenant;
	}
}
