package com.n4systems.handlers.remover;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.handlers.remover.summary.SavedReportDeleteSummary;
import com.n4systems.model.EventType;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.model.savedreports.SavedReportListLoader;
import com.n4systems.model.savedreports.SavedReportSaver;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.Transaction;

public class SavedReportDeleteHandlerImpl implements SavedReportDeleteHandler {
	
	private EventType eventType;

	@Override
	public SavedReportDeleteHandler forEventType(EventType eventType) {
		this.eventType = eventType;
		return this;
	}
	
	private List<SavedReport> getSavedReportsWithEventTypeCriteria() {
		List<SavedReport> savedReportsforRemoval = new ArrayList<SavedReport>();
			
		List<SavedReport> allTenantSavedReports = new SavedReportListLoader(new TenantOnlySecurityFilter(eventType.getTenant())).load();
		
		for (SavedReport report: allTenantSavedReports) {
			if(report.getCriteria().containsKey("eventTypeId") 
					&& report.getCriteria().get("eventTypeId").equals(eventType.getId().toString())) {
				savedReportsforRemoval.add(report);
			}
		}		
		return savedReportsforRemoval;
	}

	@Override
	public void remove(Transaction transaction) {
		SavedReportSaver saver = new SavedReportSaver();
		for (SavedReport report: getSavedReportsWithEventTypeCriteria()) {
			saver.remove(transaction, report);
		}
	}

	@Override
	public SavedReportDeleteSummary summary(Transaction transaction) {
		return new SavedReportDeleteSummary(getSavedReportsWithEventTypeCriteria().size());
	}

}

