package com.n4systems.fieldid.service.tenant;

import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.security.AccountPolicy;
import com.n4systems.model.security.PasswordPolicy;
import com.n4systems.model.tenant.TenantSettings;
import com.n4systems.model.tenant.UserLimits;

@Transactional
public class TenantSettingsService extends FieldIdPersistenceService {

	public TenantSettings getTenantSettings() {
		return persistenceService.find(createTenantSecurityBuilder(TenantSettings.class));
	}
	
	public void updateTenantAccountPolicySettings(AccountPolicy accountPolicy) { 
		TenantSettings tenantSettings = getTenantSettings();
		tenantSettings.setAccountPolicy(accountPolicy);
		persistenceService.update(tenantSettings);
	}
	
	public void updateTenantPasswordPolicySettings(PasswordPolicy passwordPolicy) { 
		TenantSettings tenantSettings = getTenantSettings();
		tenantSettings.setPasswordPolicy(passwordPolicy);
		persistenceService.update(tenantSettings);
	}	
	
	public void updateUserLimits(UserLimits userLimits) {
		TenantSettings tenantSettings = getTenantSettings();
		tenantSettings.setUserLimits(userLimits);
		persistenceService.update(tenantSettings);
	}
	
	public void updateGpsCapture(boolean gpsCapture) {
		TenantSettings tenantSettings = getTenantSettings();
		tenantSettings.setGpsCapture(gpsCapture);
		persistenceService.update(tenantSettings);
	}
}
