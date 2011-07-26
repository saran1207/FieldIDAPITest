package com.n4systems.fieldid.wicket.util;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.permissions.SerializableSecurityGuard;
import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.reporting.columns.display.FieldIdPropertyColumn;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.LoaderFactory;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;

import java.util.ArrayList;
import java.util.List;

public class ReportFormatConverter {

    public List<IColumn> convertColumns(EventReportCriteriaModel criteriaModel) {
        List<IColumn> convertedColumns = new ArrayList<IColumn>();
        List<ColumnMappingView> enabledColumns = criteriaModel.getSortedStaticAndDynamicColumns();

        int index = 0;
        for (ColumnMappingView enabledColumn : enabledColumns) {
            FIDLabelModel columnLabelModel = new FIDLabelModel(enabledColumn.getLabel());

            if (enabledColumn.isSortable()) {
                convertedColumns.add(new FieldIdPropertyColumn(columnLabelModel, enabledColumn, index, true));
            } else {
                convertedColumns.add(new FieldIdPropertyColumn(columnLabelModel, enabledColumn, index));
            }

            index++;
        }

        return convertedColumns;
    }

    public EventSearchContainer convertCriteria(EventReportCriteriaModel criteriaModel) {
        SecurityFilter securityFilter = FieldIDSession.get().getSessionUser().getSecurityFilter();
        EventSearchContainer container = new EventSearchContainer(securityFilter, new LoaderFactory(securityFilter), new SerializableSecurityGuard(FieldIDSession.get().getTenant()));

        container.setSerialNumber(criteriaModel.getSerialNumber());
        container.setAssetStatus(getId(criteriaModel.getAssetStatus()));
        container.setAssetType(getId(criteriaModel.getAssetType()));
        container.setAssetTypeGroup(getId(criteriaModel.getAssetTypeGroup()));
        container.setAssignedUser(getId(criteriaModel.getAssignedTo()));
        container.setEventBook(getId(criteriaModel.getEventBook()));
        container.setEventType(getId(criteriaModel.getEventType()));
        container.setEventTypeGroup(getId(criteriaModel.getEventTypeGroup()));
        container.setFromDate(criteriaModel.getFromDate());
        container.setToDate(criteriaModel.getToDate());
        container.setIncludeNetworkResults(criteriaModel.isIncludeSafetyNetwork());
        container.setJob(getId(criteriaModel.getJob()));
        container.setOrderNumber(criteriaModel.getOrderNumber());
        container.setOwner(criteriaModel.getOwner());
        container.setPerformedBy(getId(criteriaModel.getPerformedBy()));
        container.setPurchaseOrder(criteriaModel.getPurchaseOrder());
        container.setReferenceNumber(criteriaModel.getReferenceNumber());
        container.setRfidNumber(criteriaModel.getRfidNumber());
        container.setStatus(criteriaModel.getResult() == null ? null : criteriaModel.getResult().name());
        container.setSelectedColumns(convertSelectedColumns(criteriaModel));
        container.setMultiIdSelection(criteriaModel.getSelection());
        container.getLocation().setFreeformLocation(criteriaModel.getLocation().getFreeformLocation());
        container.getLocation().setPredefinedLocation(criteriaModel.getLocation().getPredefinedLocation());

        container.setSortColumn(criteriaModel.getSortColumn() == null ? null : criteriaModel.getSortColumn().getSortExpression());
        container.setSortDirection(criteriaModel.getSortDirection() == null ? null : criteriaModel.getSortDirection().getDisplayName());

        return container;
    }

    private List<String> convertSelectedColumns(EventReportCriteriaModel criteriaModel) {
        List<ColumnMappingView> sortedEnabledColumns = criteriaModel.getSortedStaticAndDynamicColumns();
        List<String> convertedColumns = new ArrayList<String>(sortedEnabledColumns.size());
        for (ColumnMappingView sortedEnabledColumn : sortedEnabledColumns) {
            convertedColumns.add(sortedEnabledColumn.getId());
        }
        return convertedColumns;
    }

    protected Long getId(BaseEntity entity) {
        if (entity == null) {
            return null;
        }
        return entity.getId();
    }

    protected <T extends BaseEntity> T find(PersistenceManager pm, Class<T> klass, Long id) {
        if (id == null) {
            return null;
        }
        return pm.find(klass, id);
    }

}
