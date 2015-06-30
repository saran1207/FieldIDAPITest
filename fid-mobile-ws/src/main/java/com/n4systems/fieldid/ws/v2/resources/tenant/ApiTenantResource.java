package com.n4systems.fieldid.ws.v2.resources.tenant;

import com.n4systems.fieldid.ws.v2.resources.ApiResource;
import com.n4systems.fieldid.ws.v2.resources.procedure.ApiLockoutReason;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.procedure.LockoutReason;
import com.n4systems.model.procedure.LotoSettings;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.springframework.stereotype.Component;
import rfid.ejb.entity.IdentifierCounter;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApiTenantResource extends ApiResource<ApiTenant, PrimaryOrg> {

	@Override
	public ApiTenant convertEntityToApiModel(PrimaryOrg primaryOrg) {
		ApiTenant apiTenant = new ApiTenant();
		apiTenant.setSid(primaryOrg.getTenant().getMobileId());
		apiTenant.setName(primaryOrg.getTenant().getName());
		apiTenant.setActive(!primaryOrg.getTenant().isDisabled());
		apiTenant.setModified(primaryOrg.getTenant().getSettings().getModified());
		apiTenant.setSerialNumberLabel(primaryOrg.getIdentifierLabel());
		apiTenant.setSerialNumberFormat(primaryOrg.getIdentifierFormat());

		IdentifierCounter identifierCounter = persistenceService.find(createTenantSecurityBuilder(IdentifierCounter.class));
		apiTenant.setSerialNumberDecimalFormat(identifierCounter.getDecimalFormat());

		LotoSettings lotoSettings = persistenceService.find(createTenantSecurityBuilder(LotoSettings.class));
		//Default values for all of the fields
		if(lotoSettings == null) {
			apiTenant.setApplicationProcess(ProcedureDefinition.LOCKOUT_APPLICATION_PROCESS);
			apiTenant.setRemovalProcess(ProcedureDefinition.LOCKOUT_REMOVAL_PROCESS);
			apiTenant.setTestingAndVerification(ProcedureDefinition.TESTING_AND_VERIFICATION_REQUIREMENTS);
		} else {
			//Default values for specific fields
			if(lotoSettings.getApplicationProcess() == null) {
				apiTenant.setApplicationProcess(ProcedureDefinition.LOCKOUT_APPLICATION_PROCESS);
			} else {
				apiTenant.setApplicationProcess(lotoSettings.getApplicationProcess());
			}

			if(lotoSettings.getRemovalProcess() == null){
				apiTenant.setRemovalProcess(ProcedureDefinition.LOCKOUT_REMOVAL_PROCESS);
			} else {
				apiTenant.setRemovalProcess(lotoSettings.getRemovalProcess());
			}

			if(lotoSettings.getTestingAndVerification() == null) {
				apiTenant.setTestingAndVerification(ProcedureDefinition.TESTING_AND_VERIFICATION_REQUIREMENTS);
			} else {
				apiTenant.setTestingAndVerification(lotoSettings.getTestingAndVerification());
			}
		}

		//Populating LOTO lockout reasons for the tenant
		List<LockoutReason> reasons = persistenceService.findAll(createTenantSecurityBuilder(LockoutReason.class));
		List<ApiLockoutReason> apiReasons = new ArrayList<>();
		for(LockoutReason reason:reasons) {
			ApiLockoutReason temp = new ApiLockoutReason();
			temp.setName(reason.getName());
			apiReasons.add(temp);
		}

		apiTenant.setLockoutReasonList(apiReasons);
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
