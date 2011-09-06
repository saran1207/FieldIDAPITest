package com.n4systems.servicedto.converts;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.webservice.dto.TenantServiceDTO;

public class PrimaryOrgToServiceDTOConverter {

	
	private TenantServiceDTO tenantService;

	public TenantServiceDTO convert(PrimaryOrg primaryOrg) {
		createNewDTO();
		setBaseInformation(primaryOrg);
		addExtendedFeatures(primaryOrg);

		return tenantService;
	}

	private void createNewDTO() {
		tenantService = new TenantServiceDTO();
	}

	private void setBaseInformation(PrimaryOrg primaryOrg) {
		tenantService.setId(primaryOrg.getTenant().getId());
		tenantService.setName(primaryOrg.getTenant().getName());
		tenantService.setDisplayName(primaryOrg.getName());
		tenantService.setSerialNumberFormat(primaryOrg.getIdentifierFormat());
		tenantService.setSerialNumberLabel(primaryOrg.getIdentifierLabel());
		tenantService.setUsingSerialNumber(primaryOrg.isUsingSerialNumber());
		tenantService.setGpsCapture(primaryOrg.getTenant().getSettings().isGpsCapture());
	}

	private void addExtendedFeatures(PrimaryOrg primaryOrg) {
		tenantService.setUsingJobs(primaryOrg.hasExtendedFeature(ExtendedFeature.Projects));
		tenantService.setUsingJobSites(primaryOrg.hasExtendedFeature(ExtendedFeature.JobSites));
		tenantService.setUsingAssignedTo(primaryOrg.hasExtendedFeature(ExtendedFeature.AssignedTo));
		tenantService.setUsingIntegration(primaryOrg.hasExtendedFeature(ExtendedFeature.Integration));
		tenantService.setUsingAdvancedLocation(primaryOrg.hasExtendedFeature(ExtendedFeature.AdvancedLocation));
		tenantService.setUsingOrderDetails(primaryOrg.hasExtendedFeature(ExtendedFeature.OrderDetails));
	}
}
