package com.n4systems.fieldid.service.tenant;

import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.security.AccountPolicy;
import com.n4systems.model.security.PasswordPolicy;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.tenant.TenantSettings;
import com.n4systems.model.tenant.UserLimits;

@Transactional
public class TenantSettingsService extends FieldIdPersistenceService {

	public TenantSettings getTenantSettings() {
		return persistenceService.find(createTenantSecurityBuilder(TenantSettings.class));
	}
	
	public void update(TenantSettings tenantSettings) {
		persistenceService.update(tenantSettings);
	}
	
	// uggh...this is used when the security context was initialized when the tenantId was not known, but later on during the request it is. 
	//  .: it must be passed as a parameter
	public TenantSettings getTenantSettings(Long tenantId) {
		securityContext.setTenantSecurityFilter(new TenantOnlySecurityFilter(tenantId));
		return getTenantSettings();
	}
	
	public void updateTenantAccountPolicySettings(AccountPolicy accountPolicy) { 
		TenantSettings tenantSettings = getTenantSettings();
		tenantSettings.setAccountPolicy(accountPolicy);
		update(tenantSettings);
	}
	
	public void updateTenantPasswordPolicySettings(PasswordPolicy passwordPolicy) { 
		TenantSettings tenantSettings = getTenantSettings();
		tenantSettings.setPasswordPolicy(passwordPolicy);
		update(tenantSettings);
	}	
	
	public void updateUserLimits(UserLimits userLimits) {
		TenantSettings tenantSettings = getTenantSettings();
		tenantSettings.setUserLimits(userLimits);
		update(tenantSettings);
	}
	
	public void updateGpsCapture(boolean gpsCapture) {
		TenantSettings tenantSettings = getTenantSettings();
		tenantSettings.setGpsCapture(gpsCapture);
		persistenceService.update(tenantSettings);
	}

	public void updateGpsCaptureAndSupportUrl(boolean gpsCapture, String supportUrl) {
		TenantSettings tenantSettings = getTenantSettings();
		tenantSettings.setGpsCapture(gpsCapture);
		tenantSettings.setSupportUrl(supportUrl);
		persistenceService.update(tenantSettings);				
	}

}
