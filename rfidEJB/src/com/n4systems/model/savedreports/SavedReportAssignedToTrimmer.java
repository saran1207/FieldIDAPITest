package com.n4systems.model.savedreports;

import java.util.ArrayList;
import java.util.List;

public class SavedReportAssignedToTrimmer {

	public static List<SavedReport> extractAssignedToReferences(List<SavedReport> savedReports){
		List<SavedReport> savedReportsToRemove = new ArrayList<SavedReport>();
		
		for (SavedReport report : savedReports){
			if (report.getColumns().contains("inspection_search_assignedto") || report.getCriteria().containsKey("assignedUser")){
				savedReportsToRemove.add(report);
			}
		}
		
		return savedReportsToRemove;
	}
	
}
