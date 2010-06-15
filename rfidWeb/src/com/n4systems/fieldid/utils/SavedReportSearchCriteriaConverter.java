package com.n4systems.fieldid.utils;

import com.n4systems.fieldid.viewhelpers.InspectionSearchContainer;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.util.DateHelper;

public class SavedReportSearchCriteriaConverter {

	private final FilteredIdLoader<BaseOrg> loader;
	private final SecurityFilter filter;
	

		
	public SavedReportSearchCriteriaConverter(FilteredIdLoader<BaseOrg> loader, SecurityFilter filter) {
		this.loader = loader;
		this.filter = filter;
	}



	public InspectionSearchContainer convert(SavedReport savedReport) {
		InspectionSearchContainer container = new InspectionSearchContainer(filter);
		container.setSelectedColumns(savedReport.getColumns());
		container.setSortColumn(savedReport.getSortColumn());
		container.setSortDirection(savedReport.getSortDirection());
		
		container.setPurchaseOrder(savedReport.getStringCriteria(SavedReport.PURCHASE_ORDER_NUMBER));
		container.setOrderNumber(savedReport.getStringCriteria(SavedReport.ORDER_NUMBER));
		container.setRfidNumber(savedReport.getStringCriteria(SavedReport.RFID_NUMBER));
		container.setSerialNumber(savedReport.getStringCriteria(SavedReport.SERIAL_NUMBER));
		container.setReferenceNumber(savedReport.getStringCriteria(SavedReport.REFERENCE_NUMBER));
		container.setLocation(savedReport.getStringCriteria(SavedReport.LOCATION));
		
			
		container.setInspectionBook(savedReport.getLongCriteria(SavedReport.INSPECTION_BOOK));
		container.setInspectionTypeGroup(savedReport.getLongCriteria(SavedReport.INSPECTION_TYPE_GROUP));
		container.setPerformedBy(savedReport.getLongCriteria(SavedReport.PERFORMED_BY));
		container.setAssignedUser(savedReport.getLongCriteria(SavedReport.ASSIGNED_USER));
		container.setProductStatus(savedReport.getLongCriteria(SavedReport.PRODUCT_STATUS));
		container.setProductType(savedReport.getLongCriteria(SavedReport.PRODUCT_TYPE));
		container.setProductTypeGroup(savedReport.getLongCriteria(SavedReport.PRODUCT_TYPE_GROUP));
		container.setJob(savedReport.getLongCriteria(SavedReport.JOB_ID));
		
		container.setFromDate(DateHelper.string2Date(SavedReport.DATE_FORMAT, savedReport.getCriteria().get(SavedReport.FROM_DATE)));
		container.setToDate(DateHelper.string2Date(SavedReport.DATE_FORMAT, savedReport.getCriteria().get(SavedReport.TO_DATE)));
		
		container.setSavedReportId(savedReport.getId());
		container.setSavedReportModified(false);
		if (savedReport.getLongCriteria(SavedReport.OWNER_ID) != null) {
			container.setOwner(loader.setId(savedReport.getLongCriteria(SavedReport.OWNER_ID)).load());
		}
		
		return container;
	}
	
	
	public SavedReport convertInto(InspectionSearchContainer container, SavedReport report) {
		
		
		report.setSortColumn(container.getSortColumn());
		report.setSortDirection(container.getSortDirection());
		report.setColumns(container.getSelectedColumns());
			
		report.getCriteria().clear();
			
		report.setInCriteria(SavedReport.PURCHASE_ORDER_NUMBER, container.getPurchaseOrder());
		report.setInCriteria(SavedReport.ORDER_NUMBER, container.getOrderNumber());
		report.setInCriteria(SavedReport.RFID_NUMBER, container.getRfidNumber());
		report.setInCriteria(SavedReport.SERIAL_NUMBER, container.getSerialNumber());
		report.setInCriteria(SavedReport.OWNER_ID, container.getOwnerId());
		report.setInCriteria(SavedReport.REFERENCE_NUMBER, container.getReferenceNumber());
		report.setInCriteria(SavedReport.INSPECTION_BOOK, container.getInspectionBook());
		report.setInCriteria(SavedReport.INSPECTION_TYPE_GROUP, container.getInspectionTypeGroup());
		report.setInCriteria(SavedReport.PERFORMED_BY, container.getPerformedBy());
		report.setInCriteria(SavedReport.PRODUCT_STATUS, container.getProductStatus());
		report.setInCriteria(SavedReport.PRODUCT_TYPE, container.getProductType());
		report.setInCriteria(SavedReport.PRODUCT_TYPE_GROUP, container.getProductTypeGroup());
		report.setInCriteria(SavedReport.ASSIGNED_USER, container.getAssignedUser());
		report.setInCriteria(SavedReport.JOB_ID, container.getJob());
		report.setInCriteria(SavedReport.LOCATION, container.getLocation());

		if (container.getFromDate() != null) {
			report.setInCriteria(SavedReport.FROM_DATE, DateHelper.date2String(SavedReport.DATE_FORMAT, container.getFromDate()));
		}
			
		if (container.getToDate() != null) {
			report.setInCriteria(SavedReport.TO_DATE, DateHelper.date2String(SavedReport.DATE_FORMAT, container.getToDate()));
		}

		return report;
	}

}
