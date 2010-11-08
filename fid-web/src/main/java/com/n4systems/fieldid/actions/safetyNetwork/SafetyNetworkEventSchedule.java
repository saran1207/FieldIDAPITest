package com.n4systems.fieldid.actions.safetyNetwork;

import com.n4systems.ejb.EventScheduleManager;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.fieldid.actions.EventScheduleCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;

public class SafetyNetworkEventSchedule extends EventScheduleCrud {

	public SafetyNetworkEventSchedule(
			LegacyAsset legacyAssetManager,
			PersistenceManager persistenceManager,
			EventScheduleManager eventScheduleManager) {
		super(legacyAssetManager, persistenceManager, eventScheduleManager);
	}
	
	@Override
	@SkipValidation
	@UserPermissionFilter(open=true)
	public String doList() {
		setPageType("network_asset", "event_schedules");
		testRequiredEntities(false, false);
		return SUCCESS;
	}
	
	@Override
	public boolean isInVendorContext() {
		return true;
	}
}
