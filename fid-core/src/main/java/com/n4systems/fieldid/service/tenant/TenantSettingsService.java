package com.n4systems.fieldid.service.tenant;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.event.UsageBasedEventThresholdAlert;
import com.n4systems.model.security.AccountPolicy;
import com.n4systems.model.security.OfflinePolicy;
import com.n4systems.model.security.PasswordPolicy;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.tenant.TenantSettings;
import com.n4systems.model.tenant.UserLimits;
import com.n4systems.model.user.Assignable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class TenantSettingsService extends FieldIdPersistenceService {

    @Autowired
    private UsageBasedEventThresholdAlert notifyUsageBasedThresholdAlert;

	public TenantSettings getTenantSettings() {
		return persistenceService.find(createTenantSecurityBuilder(TenantSettings.class));
	}
	
	public TenantSettings update(TenantSettings tenantSettings) {
		return persistenceService.update(tenantSettings);
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

	public void updateTenantOfflinePolicySettings(OfflinePolicy offlinePolicy) {
		TenantSettings tenantSettings = getTenantSettings();
		tenantSettings.setOfflinePolicy(offlinePolicy);
		update(tenantSettings);
	}
	
	public void updateUserLimits(UserLimits userLimits) {
		TenantSettings tenantSettings = getTenantSettings();
		tenantSettings.setUserLimits(userLimits);
		update(tenantSettings);
	}

    public void decrementUsageBasedEventCount() {
        decrementUsageBasedEventCount(1);
    }

    public void decrementUsageBasedEventCount(int count) {
        TenantSettings tenantSettings = getTenantSettings();
        int eventCount = tenantSettings.getUserLimits().getUsageBasedUserEvents() - count;
        tenantSettings.getUserLimits().setUsageBasedUserEvents(eventCount);
        update(tenantSettings);
        notifyUsageBasedThresholdAlert.sendAlert(tenantSettings.getTenant(), eventCount);
    }
	
	public void updateGpsCapture(boolean gpsCapture) {
		TenantSettings tenantSettings = getTenantSettings();
		tenantSettings.setGpsCapture(gpsCapture);
		persistenceService.update(tenantSettings);
	}

	public void updateGpsCaptureAndUrls(boolean gpsCapture, String supportUrl, String logoutUrl) {
		TenantSettings tenantSettings = getTenantSettings();
		tenantSettings.setGpsCapture(gpsCapture);
		tenantSettings.setSupportUrl(supportUrl);
        tenantSettings.setLogoutUrl(logoutUrl);
		persistenceService.update(tenantSettings);
	}

}
