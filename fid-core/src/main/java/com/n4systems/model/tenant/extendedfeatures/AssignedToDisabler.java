package com.n4systems.model.tenant.extendedfeatures;

import java.util.List;

import com.n4systems.model.EventType;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.eventtype.EventTypeListLoader;
import com.n4systems.model.eventtype.EventTypeSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.model.savedreports.SavedReportAssignedToTrimmer;
import com.n4systems.model.savedreports.SavedReportListLoader;
import com.n4systems.model.savedreports.SavedReportSaver;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.Transaction;

public class AssignedToDisabler extends ExtendedFeatureDisabler {

	protected AssignedToDisabler(PrimaryOrg primaryOrg) {
		super(primaryOrg, ExtendedFeature.AssignedTo);
	}

	@Override
	protected void disableFeature(Transaction transaction) {
		removeSavedReportsUsingAssignedTo(transaction);
		disableAssignedToForAllEventTypes(transaction);
	}

	private void removeSavedReportsUsingAssignedTo(Transaction transaction) {
		SavedReportListLoader loader = new SavedReportListLoader(new TenantOnlySecurityFilter(primaryOrg.getTenant()));
		SavedReportSaver saver = new SavedReportSaver();
		List<SavedReport> reportsToRemove = SavedReportAssignedToTrimmer.extractAssignedToReferences(loader.load(transaction));

		if (!reportsToRemove.isEmpty()) {
			for (SavedReport report : reportsToRemove) {
				saver.remove(transaction, report);
			}
		}
	}

	private void disableAssignedToForAllEventTypes(Transaction transaction) {
		EventTypeListLoader loader = new EventTypeListLoader(new TenantOnlySecurityFilter(primaryOrg.getTenant()));
		EventTypeSaver saver = new EventTypeSaver();
		List<EventType> eventTypesToToggle = loader.load(transaction);

		for (EventType eventType : eventTypesToToggle) {
			eventType.removeAssignedTo();
			saver.update(transaction, eventType);

		}
	}
}
