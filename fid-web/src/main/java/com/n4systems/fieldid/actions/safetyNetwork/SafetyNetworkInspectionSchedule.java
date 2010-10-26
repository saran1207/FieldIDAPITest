package com.n4systems.fieldid.actions.safetyNetwork;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.fieldid.actions.InspectionScheduleCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;

public class SafetyNetworkInspectionSchedule extends InspectionScheduleCrud{

	public SafetyNetworkInspectionSchedule(
			LegacyProductSerial legacyProductManager,
			PersistenceManager persistenceManager,
			InspectionScheduleManager inspectionScheduleManager) {
		super(legacyProductManager, persistenceManager, inspectionScheduleManager);
	}
	
	@Override
	@SkipValidation
	@UserPermissionFilter(open=true)
	public String doList() {
		setPageType("network_asset", "inspection_schedules");
		testRequiredEntities(false, false);
		return SUCCESS;
	}
	
	@Override
	public boolean isInVendorContext() {
		return true;
	}
}
