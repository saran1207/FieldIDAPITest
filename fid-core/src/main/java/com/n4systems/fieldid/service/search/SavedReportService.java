package com.n4systems.fieldid.service.search;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.asset.AssetTypeService;
import com.n4systems.fieldid.service.event.EventTypeService;
import com.n4systems.fieldid.service.search.columns.DynamicColumnsService;
import com.n4systems.fieldid.service.search.columns.EventColumnsService;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.columns.ColumnMapping;
import com.n4systems.model.columns.SystemColumnMapping;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.saveditem.SavedReportItem;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.model.search.ColumnMappingConverter;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.model.search.ReportConfiguration;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class SavedReportService extends FieldIdPersistenceService {

    private @Autowired DynamicColumnsService dynamicColumnsService;
    private @Autowired AssetTypeService assetTypeService;
    private @Autowired EventTypeService eventTypeService;

    @Transactional(readOnly = true)
    public SavedReport getSavedReport(Long savedReportId) {
        return persistenceService.find(SavedReport.class, savedReportId);
    }

    @Transactional
    public List<SavedItem> listSavedItems() {
        QueryBuilder<SavedItem> query = createUserSecurityBuilder(SavedItem.class);
        
        query.addOrder("name");

        return persistenceService.findAll(query);
    }

    @Transactional(readOnly = true)
    public EventReportCriteriaModel getConvertedReport(Long itemId) {
        final SavedReportItem savedItem = (SavedReportItem) persistenceService.find(SavedItem.class, itemId);
        final EventReportCriteriaModel report = savedItem.getReport();
        report.setReportAlreadyRun(true);

        ReportConfiguration reportConfiguration = new EventColumnsService().getReportConfiguration(securityContext.getUserSecurityFilter());

        AssetTypeGroup assetTypeGroup = report.getAssetTypeGroup();
        EventTypeGroup eventTypeGroup = report.getEventTypeGroup();
        List<ColumnMappingGroupView> dynamicAssetColumns = dynamicColumnsService.getDynamicAssetColumnsForReporting(report.getAssetType(), assetTypeService.getAssetTypes(assetTypeGroup == null ? null : assetTypeGroup.getId()));
        List<ColumnMappingGroupView> dynamicEventColumns = dynamicColumnsService.getDynamicEventColumnsForReporting(report.getEventType(), eventTypeService.getEventTypes(assetTypeGroup == null ? null : eventTypeGroup.getId()));

        report.setDynamicAssetColumnGroups(dynamicAssetColumns);
        report.setDynamicEventColumnGroups(dynamicEventColumns);
        report.setColumnGroups(reportConfiguration.getColumnGroups());

        enableSelectedColumns(report, report.getColumns());

        if (report.getSortColumnId() != null) {
            ColumnMapping mapping = persistenceService.findUnsecured(SystemColumnMapping.class, report.getSortColumnId());
            ColumnMappingView mappingView = new ColumnMappingConverter().convert(mapping);
            report.setSortColumn(mappingView);
        }

        return report;
    }

    @Transactional
    public void saveReport(EventReportCriteriaModel criteriaModel, boolean overwrite, String name) {
        boolean updating = overwrite && criteriaModel.getId() != null;
        final User user = getCurrentUser();

        SavedReportItem savedReportItem;

        if (updating) {
            savedReportItem = criteriaModel.getSavedReportItem();
        } else {
            savedReportItem = new SavedReportItem();
        }

        savedReportItem.setTenant(getCurrentTenant());
        savedReportItem.setName(name);
        savedReportItem.setReport(criteriaModel);

        storeSelectedColumns(criteriaModel);

        if (updating) {
            persistenceService.update(savedReportItem);
        } else {
            criteriaModel.reset();
            try {
                criteriaModel = (EventReportCriteriaModel) criteriaModel.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }

            persistenceService.save(criteriaModel);
            savedReportItem.setReport(criteriaModel);
            user.getSavedItems().add(savedReportItem);

            persistenceService.save(user);
        }

        
    }

    // TODO: Refactor these into model objects themselves after asset search saving implemented!
    private void enableSelectedColumns(EventReportCriteriaModel criteriaModel, List<String> columns) {
        for (ColumnMappingView columnMappingView : criteriaModel.getSortedStaticAndDynamicColumns(false)) {
            columnMappingView.setEnabled(columns.contains(columnMappingView.getId()));
        }
    }

    private void storeSelectedColumns(EventReportCriteriaModel criteriaModel) {
        List<String> selectedColumns = new ArrayList<String>();
        for (ColumnMappingView columnMappingView : criteriaModel.getSortedStaticAndDynamicColumns(true)) {
            selectedColumns.add(columnMappingView.getId());
        }
        criteriaModel.setColumns(selectedColumns);
    }

}

