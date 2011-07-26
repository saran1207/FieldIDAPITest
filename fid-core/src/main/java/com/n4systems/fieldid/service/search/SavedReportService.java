package com.n4systems.fieldid.service.search;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.service.event.EventTypeService;
import com.n4systems.fieldid.service.search.columns.DynamicColumnsService;
import com.n4systems.fieldid.service.search.columns.EventColumnsService;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.Project;
import com.n4systems.model.Status;
import com.n4systems.model.columns.ColumnMapping;
import com.n4systems.model.columns.SystemColumnMapping;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.model.search.ColumnMappingConverter;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.model.search.ReportConfiguration;
import com.n4systems.model.user.User;
import com.n4systems.util.DateHelper;
import com.n4systems.util.persistence.search.SortDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class SavedReportService extends FieldIdPersistenceService {

    private @Autowired DynamicColumnsService dynamicColumnsService;
    private @Autowired AssetTypeService assetTypeService;
    private @Autowired EventTypeService eventTypeService;

    @Transactional(readOnly = true)
    public SavedReport getSavedReport(Long savedReportId) {
        return persistenceService.find(SavedReport.class, savedReportId);
    }

    @Transactional(readOnly = true)
    public EventReportCriteriaModel convertToCriteria(Long savedReportId) {
        SavedReport savedReport = getSavedReport(savedReportId);

        EventReportCriteriaModel criteriaModel = new EventReportCriteriaModel();
        criteriaModel.setReportAlreadyRun(true);

        ReportConfiguration reportConfiguration = new EventColumnsService().getReportConfiguration(securityContext.getUserSecurityFilter());

		criteriaModel.setSortDirection(SortDirection.ASC.getDisplayName().equals(savedReport.getSortDirection()) ? SortDirection.ASC : SortDirection.DESC);

        if (savedReport.getSortColumnId() != null) {
            ColumnMapping mapping = persistenceService.findUnsecured(SystemColumnMapping.class, savedReport.getSortColumnId());
            ColumnMappingView mappingView = new ColumnMappingConverter().convert(mapping);
            criteriaModel.setSortColumn(mappingView);
        }

		criteriaModel.setPurchaseOrder(savedReport.getStringCriteria(SavedReport.PURCHASE_ORDER_NUMBER));
		criteriaModel.setOrderNumber(savedReport.getStringCriteria(SavedReport.ORDER_NUMBER));
		criteriaModel.setRfidNumber(savedReport.getStringCriteria(SavedReport.RFID_NUMBER));
		criteriaModel.setSerialNumber(savedReport.getStringCriteria(SavedReport.SERIAL_NUMBER));
		criteriaModel.setReferenceNumber(savedReport.getStringCriteria(SavedReport.REFERENCE_NUMBER));

		criteriaModel.getLocation().setFreeformLocation(savedReport.getStringCriteria(SavedReport.LOCATION));
		criteriaModel.getLocation().setPredefinedLocation(findEntity(PredefinedLocation.class, savedReport.getLongCriteria(SavedReport.PREDEFINED_LOCATION_ID)));

		criteriaModel.setEventBook(findEntity(EventBook.class, savedReport.getLongCriteria(SavedReport.EVENT_BOOK)));
		criteriaModel.setEventTypeGroup(findEntity(EventTypeGroup.class, savedReport.getLongCriteria(SavedReport.EVENT_TYPE_GROUP)));
		criteriaModel.setPerformedBy(findEntity(User.class, savedReport.getLongCriteria(SavedReport.PERFORMED_BY)));
		criteriaModel.setAssignedTo(findEntity(User.class, savedReport.getLongCriteria(SavedReport.ASSIGNED_USER)));
		criteriaModel.setAssetStatus(findEntity(AssetStatus.class, savedReport.getLongCriteria(SavedReport.ASSET_STATUS)));
        String statusString = savedReport.getStringCriteria(SavedReport.EVENT_STATUS);
        criteriaModel.setResult(statusString == null ? null : Status.valueOf(statusString));
        criteriaModel.setEventType(findEntity(EventType.class, savedReport.getLongCriteria(SavedReport.EVENT_TYPE)));
		criteriaModel.setAssetType(findEntity(AssetType.class, savedReport.getLongCriteria(SavedReport.ASSET_TYPE)));
		criteriaModel.setAssetTypeGroup(findEntity(AssetTypeGroup.class, savedReport.getLongCriteria(SavedReport.ASSET_TYPE_GROUP)));
		criteriaModel.setJob(findEntity(Project.class, savedReport.getLongCriteria(SavedReport.JOB_ID)));

		criteriaModel.setFromDate(DateHelper.string2Date(SavedReport.DATE_FORMAT, savedReport.getCriteria().get(SavedReport.FROM_DATE)));
		criteriaModel.setToDate(DateHelper.string2Date(SavedReport.DATE_FORMAT, savedReport.getCriteria().get(SavedReport.TO_DATE)));

		criteriaModel.setSavedReportId(savedReport.getId());
		if (savedReport.getLongCriteria(SavedReport.OWNER_ID) != null) {
			criteriaModel.setOwner(persistenceService.find(BaseOrg.class, savedReport.getLongCriteria(SavedReport.OWNER_ID)));
		}

        AssetTypeGroup assetTypeGroup = criteriaModel.getAssetTypeGroup();
        List<ColumnMappingGroupView> dynamicAssetColumns = dynamicColumnsService.getDynamicAssetColumns(criteriaModel.getAssetType(), assetTypeService.getAssetTypes(assetTypeGroup == null ? null : assetTypeGroup.getId()));
        List<ColumnMappingGroupView> dynamicEventColumns = dynamicColumnsService.getDynamicEventColumns(criteriaModel.getEventType(), eventTypeService.getEventTypes(assetTypeGroup == null ? null : assetTypeGroup.getId()));

        criteriaModel.setDynamicAssetColumnGroups(dynamicAssetColumns);
        criteriaModel.setDynamicEventColumnGroups(dynamicEventColumns);
        criteriaModel.setColumnGroups(reportConfiguration.getColumnGroups());

        enableSelectedColumns(criteriaModel, savedReport);

		return criteriaModel;
    }

    private void enableSelectedColumns(EventReportCriteriaModel criteriaModel, SavedReport savedReport) {
        List<String> columns = savedReport.getColumns();
        for (ColumnMappingView columnMappingView : criteriaModel.getSortedStaticAndDynamicColumns(false)) {
            columnMappingView.setEnabled(columns.contains(columnMappingView.getId()));
        }
    }

    private <T extends EntityWithTenant> T findEntity(Class<T> klass, Long id) {
        if (id == null) {
            return null;
        }

        return persistenceService.find(klass, id);
    }

}
