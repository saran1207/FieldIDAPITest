package com.n4systems.fieldid.utils;

import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.DateHelper;

public class SavedReportSearchCriteriaConverter {

	private final LoaderFactory loaderFactory;
	private final SecurityFilter filter;
		
	public SavedReportSearchCriteriaConverter(LoaderFactory loaderFactory, SecurityFilter filter) {
		this.loaderFactory = loaderFactory;
		this.filter = filter;
	}
	 
	public EventSearchContainer convert(SavedReport savedReport) {
		EventSearchContainer container = new EventSearchContainer(filter, loaderFactory);
		container.setSelectedColumns(savedReport.getColumns());
		container.setSortColumn(savedReport.getSortColumn());
		container.setSortDirection(savedReport.getSortDirection());
		
		container.setPurchaseOrder(savedReport.getStringCriteria(SavedReport.PURCHASE_ORDER_NUMBER));
		container.setOrderNumber(savedReport.getStringCriteria(SavedReport.ORDER_NUMBER));
		container.setRfidNumber(savedReport.getStringCriteria(SavedReport.RFID_NUMBER));
		container.setSerialNumber(savedReport.getStringCriteria(SavedReport.SERIAL_NUMBER));
		container.setReferenceNumber(savedReport.getStringCriteria(SavedReport.REFERENCE_NUMBER));
		container.getLocation().setFreeformLocation(savedReport.getStringCriteria(SavedReport.LOCATION));
		container.getLocation().setPredefinedLocationId(savedReport.getLongCriteria(SavedReport.PREDEFINED_LOCATION_ID));
		container.setEventBook(savedReport.getLongCriteria(SavedReport.EVENT_BOOK));
		container.setEventTypeGroup(savedReport.getLongCriteria(SavedReport.EVENT_TYPE_GROUP));
		container.setPerformedBy(savedReport.getLongCriteria(SavedReport.PERFORMED_BY));
		container.setAssignedUser(savedReport.getLongCriteria(SavedReport.ASSIGNED_USER));
		container.setAssetStatus(savedReport.getLongCriteria(SavedReport.ASSET_STATUS));
		container.setStatus(savedReport.getStringCriteria(SavedReport.EVENT_STATUS));
		container.setAssetType(savedReport.getLongCriteria(SavedReport.ASSET_TYPE));
		container.setAssetTypeGroup(savedReport.getLongCriteria(SavedReport.ASSET_TYPE_GROUP));
		container.setJob(savedReport.getLongCriteria(SavedReport.JOB_ID));
		
		container.setFromDate(DateHelper.string2Date(SavedReport.DATE_FORMAT, savedReport.getCriteria().get(SavedReport.FROM_DATE)));
		container.setToDate(DateHelper.string2Date(SavedReport.DATE_FORMAT, savedReport.getCriteria().get(SavedReport.TO_DATE)));
		
		container.setSavedReportId(savedReport.getId());
		container.setSavedReportModified(false);
		if (savedReport.getLongCriteria(SavedReport.OWNER_ID) != null) {
			container.setOwner(loaderFactory.createEntityByIdLoader(BaseOrg.class).setId(savedReport.getLongCriteria(SavedReport.OWNER_ID)).load());
		}
		
		return container;
	}
	
	
	public SavedReport convertInto(EventSearchContainer container, SavedReport report) {
		
		
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
		report.setInCriteria(SavedReport.EVENT_BOOK, container.getEventBook());
		report.setInCriteria(SavedReport.EVENT_TYPE_GROUP, container.getEventTypeGroup());
		report.setInCriteria(SavedReport.PERFORMED_BY, container.getPerformedBy());
		report.setInCriteria(SavedReport.ASSET_STATUS, container.getAssetStatus());
		report.setInCriteria(SavedReport.EVENT_STATUS, container.getStatus());
		report.setInCriteria(SavedReport.ASSET_TYPE, container.getAssetType());
		report.setInCriteria(SavedReport.ASSET_TYPE_GROUP, container.getAssetTypeGroup());
		report.setInCriteria(SavedReport.ASSIGNED_USER, container.getAssignedUser());
		report.setInCriteria(SavedReport.JOB_ID, container.getJob());
		report.setInCriteria(SavedReport.LOCATION, container.getLocation().getFreeformLocation());
		report.setInCriteria(SavedReport.PREDEFINED_LOCATION_ID, container.getLocation().getPredefinedLocationId());
		
		if (container.getFromDate() != null) {
			report.setInCriteria(SavedReport.FROM_DATE, DateHelper.date2String(SavedReport.DATE_FORMAT, container.getFromDate()));
		}
			
		if (container.getToDate() != null) {
			report.setInCriteria(SavedReport.TO_DATE, DateHelper.date2String(SavedReport.DATE_FORMAT, container.getToDate()));
		}

		return report;
	}

}
