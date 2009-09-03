package com.n4systems.model.tenant.extendedfeatures;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.notificationsettings.NotificationSettingByTenantListLoader;
import com.n4systems.model.notificationsettings.NotificationSettingSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.Transaction;
import com.n4systems.util.SecurityFilter;

public class EmailAlertsSwitch extends ExtendedFeatureSwitch {

	protected EmailAlertsSwitch(PrimaryOrg primaryOrg) {
		super(primaryOrg, ExtendedFeature.EmailAlerts);
	}

	@Override
	protected void featureSetup(Transaction transaction) {
	}

	@Override
	protected void featureTearDown(Transaction transaction) {
		// removes all current notification settings for this tenant
		NotificationSettingByTenantListLoader loader = new  NotificationSettingByTenantListLoader(new SecurityFilter(primaryOrg.getTenant().getId()));
		
		NotificationSettingSaver saver = new NotificationSettingSaver();
		
		for (NotificationSetting setting: loader.load(transaction)) {
			saver.remove(transaction, setting);
		}
	}

}
