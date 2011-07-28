package com.n4systems.model.tenant.extendedfeatures;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.notificationsettings.NotificationSettingByTenantListLoader;
import com.n4systems.model.notificationsettings.NotificationSettingSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.Transaction;

// Use ExtendedFeatureService
@Deprecated
public class EmailAlertsSwitch extends ExtendedFeatureSwitch {

	protected EmailAlertsSwitch(PrimaryOrg primaryOrg) {
		super(primaryOrg, ExtendedFeature.EmailAlerts);
	}

	@Override
	protected void featureSetup(Transaction transaction) {
	}

	@Override
	protected void featureTearDown(Transaction transaction) {
		
		NotificationSettingByTenantListLoader loader = new  NotificationSettingByTenantListLoader(new TenantOnlySecurityFilter(primaryOrg.getTenant().getId()));
		
		NotificationSettingSaver saver = new NotificationSettingSaver();
		
		for (NotificationSetting setting: loader.load(transaction)) {
			saver.remove(transaction, setting);
		}
	}

}
