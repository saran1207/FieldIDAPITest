package com.n4systems.model.tenant.extendedfeatures;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.TenantOrganization;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.notificationsettings.NotificationSettingByTenantListLoader;
import com.n4systems.model.notificationsettings.NotificationSettingSaver;
import com.n4systems.util.SecurityFilter;

public class EmailAlertsSwitch extends ExtendedFeatureSwitch {

	protected EmailAlertsSwitch(TenantOrganization tenant, PersistenceManager persistenceManager) {
		super(tenant, persistenceManager, ExtendedFeature.EmailAlerts);
	}

	@Override
	protected void featureSetup() {
	}

	@Override
	protected void featureTearDown() {
		// removes all current notification settings for this tenant
		NotificationSettingByTenantListLoader loader = new  NotificationSettingByTenantListLoader(persistenceManager, new SecurityFilter(tenant.getId()));
		loader.setTenantId(tenant.getId());
		
		NotificationSettingSaver deleter = new NotificationSettingSaver(persistenceManager);
		
		for (NotificationSetting setting: loader.load()) {
			deleter.remove(setting);
		}
	}

}
