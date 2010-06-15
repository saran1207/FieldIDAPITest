package com.n4systems.model.tenant.extendedfeatures;

import java.util.List;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.model.savedreports.SavedReportAssignedToTrimmer;
import com.n4systems.model.savedreports.SavedReportListLoader;
import com.n4systems.model.savedreports.SavedReportSaver;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.Transaction;

public class AssignedToSwitch extends ExtendedFeatureSwitch {

	protected AssignedToSwitch(PrimaryOrg primaryOrg) {
		super(primaryOrg, ExtendedFeature.AssignedTo);
	}

	@Override
	protected void featureSetup(Transaction transaction) {

	}

	@Override
	protected void featureTearDown(Transaction transaction) {
		removeSavedReportsUsingAssignedTo(transaction);
	}
	
	private void removeSavedReportsUsingAssignedTo(Transaction transaction){
		SavedReportListLoader loader = new SavedReportListLoader(new TenantOnlySecurityFilter(primaryOrg.getTenant()));
		SavedReportSaver saver = new SavedReportSaver();
		List<SavedReport> reportsToRemove = SavedReportAssignedToTrimmer.extractAssignedToReferences(loader.load()); 
		
		if(!reportsToRemove.isEmpty()){
			for (SavedReport report : reportsToRemove){
				saver.remove(report);
			}
		}
	}
}
