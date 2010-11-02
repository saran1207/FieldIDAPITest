package com.n4systems.fieldid.viewhelpers;


import com.n4systems.fieldid.utils.SavedReportSearchCriteriaConverter;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.LoaderFactory;

public class SavedReportHelper {

	public static boolean isModified(EventSearchContainer eventSearchContainer, SavedReport originalReport, SecurityFilter filter, LoaderFactory loaderFactory) {
		SavedReport report = new SavedReportSearchCriteriaConverter(loaderFactory, filter).convertInto(eventSearchContainer, new SavedReport());
		return areReportsDifferent(originalReport, report);
	}
	
	private static boolean areReportsDifferent(SavedReport report1, SavedReport report2) {
		boolean different = false;
		
		if (!report1.getSortColumn().equals(report2.getSortColumn())) {
			
			different = true;
		} else if (!report1.getSortDirection().equals(report2.getSortDirection())) {
			
			different = true;
		} else if (!report1.getColumns().containsAll(report2.getColumns()) || !report2.getColumns().containsAll(report1.getColumns())) {
			
			different = true;
		}else if (!report1.getCriteria().equals(report2.getCriteria())) {
			
			different = true;
		}
		
		return different;
	}
	
	/**
	 * Creates a new SharedReport based on an existing report.  The non-entity values are fist copied to a new
	 * report object.  Owner information is then set on the new report and security is overridden based on the
	 * toUser's security level.  The original report will not be modified.
	 * @param report	The report
	 * @param fromUser	The user sharing this report
	 * @param toUser	The user to share this report to
	 * @return			A new SavedReport
	 */
	public static SavedReport createdSharedReport(SavedReport report, User fromUser, User toUser) {
		SavedReport sharedReport = copyNonEntityReportFields(report);
		
		// override the owner and sharedBy name
		sharedReport.setSharedByName(fromUser.getDisplayName());
		sharedReport.setUser(toUser);
		
		
		// if the user is a customer/division user, we need to ensure the division gets properly overridden.
		// for employee users we will let the original customer/division properties continue
		if (toUser.getOwner().isExternal()) {
			sharedReport.setInCriteria(SavedReport.OWNER_ID, toUser.getOwner().getId());
		}

		return sharedReport;
	}
	
	/**
	 * Copies the non-entity fields (ie fields that are not id, created, modifiedBy, etc) to a new SavedReport object.  The
	 * Tenant is the one exception.
	 * @param report	The report to copy
	 * @return			The new Report
	 */
	private static SavedReport copyNonEntityReportFields(SavedReport report) {
		SavedReport newReport = new SavedReport();
		
		newReport.getColumns().addAll(report.getColumns());
		newReport.getCriteria().putAll(report.getCriteria());
		newReport.setName(report.getName());
		newReport.setUser(report.getUser());
		newReport.setSharedByName(report.getSharedByName());
		newReport.setSortColumn(report.getSortColumn());
		newReport.setSortDirection(report.getSortDirection());
		newReport.setTenant(report.getTenant());
		
		return newReport;
	}
}
