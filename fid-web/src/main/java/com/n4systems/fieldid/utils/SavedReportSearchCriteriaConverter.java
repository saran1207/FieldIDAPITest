package com.n4systems.fieldid.utils;

import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import com.n4systems.model.columns.ColumnMapping;
import com.n4systems.model.columns.ColumnMappingGroup;
import com.n4systems.model.columns.ReportType;
import com.n4systems.model.columns.SystemColumnMapping;
import com.n4systems.model.columns.loader.ColumnMappingGroupLoader;
import com.n4systems.model.columns.loader.CustomColumnsLoader;
import com.n4systems.model.columns.loader.SystemColumnMappingLoader;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.DateHelper;

import java.util.ArrayList;
import java.util.List;

public class SavedReportSearchCriteriaConverter {

	private final LoaderFactory loaderFactory;
	private final SecurityFilter filter;
	private final SystemSecurityGuard systemSecurityGuard;
		
	public SavedReportSearchCriteriaConverter(LoaderFactory loaderFactory, SecurityFilter filter, SystemSecurityGuard systemSecurityGuard) {
		this.loaderFactory = loaderFactory;
		this.filter = filter;
		this.systemSecurityGuard = systemSecurityGuard;
	}
	 
	public EventSearchContainer convert(SavedReport savedReport) {
		EventSearchContainer container = new EventSearchContainer(filter, loaderFactory, systemSecurityGuard);
        verifySavedColumnsAreAllStillPresent(savedReport.getColumns());
		container.setSelectedColumns(savedReport.getColumns());
		container.setSortColumn(savedReport.getSortColumn());
        container.setSortColumnId(savedReport.getSortColumnId());
		container.setSortDirection(savedReport.getSortDirection());

        if (savedReport.getSortColumnId() != null) {
            SystemColumnMapping mapping = new SystemColumnMappingLoader().id(savedReport.getSortColumnId()).load();
            container.setSortJoinExpression(mapping.getJoinExpression());
        }

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
        container.setEventType(savedReport.getLongCriteria(SavedReport.EVENT_TYPE));
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
        report.setSortColumnId(container.getSortColumnId());
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
        report.setInCriteria(SavedReport.EVENT_TYPE, container.getEventType());
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

    private void verifySavedColumnsAreAllStillPresent(List<String> columnIds) {
        List<String> allColumnIds = getListOfAllColumnIds();
        for (String columnId : columnIds) {
            if (!isDyanmic(columnId) && !allColumnIds.contains(columnId)) {
                throw new MissingEntityException("Column from saved report was missing, must have been deleted. ID: " + columnId);
            }
        }
    }

    private boolean isDyanmic(String columnId) {
        return columnId.startsWith("event_search_infooption_") || columnId.startsWith("event_search_insattribute_");
    }

    protected List<String> getListOfAllColumnIds() {
        List<String> allColumnIds = new ArrayList<String>();
        List<ColumnMappingGroup> groups = new ColumnMappingGroupLoader(loaderFactory.getSecurityFilter().getOwner().getPrimaryOrg()).load();
        for (ColumnMappingGroup group : groups) {
            for (ColumnMapping columnMapping : group.getColumnMappings()) {
                allColumnIds.add(columnMapping.getName());
            }
        }
        CustomColumnsLoader customColumnsLoader = new CustomColumnsLoader(loaderFactory.getSecurityFilter()).reportType(ReportType.EVENT);
        for (ColumnMapping mapping : customColumnsLoader.load()) {
            allColumnIds.add(mapping.getName());
        }
        return allColumnIds;
    }

}
