package com.n4systems.fieldid.wicket.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.asset.LocationWebModel;
import com.n4systems.fieldid.reporting.service.EventColumnsService;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroupView;
import com.n4systems.fieldid.viewhelpers.ColumnMappingView;
import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import com.n4systems.fieldid.viewhelpers.ReportConfiguration;
import com.n4systems.fieldid.viewhelpers.handlers.AssetLinkHandler;
import com.n4systems.fieldid.viewhelpers.handlers.AssignedToUpdateHandler;
import com.n4systems.fieldid.viewhelpers.handlers.DateTimeHandler;
import com.n4systems.fieldid.viewhelpers.handlers.EnumHandler;
import com.n4systems.fieldid.viewhelpers.handlers.EventRfidNumberHandler;
import com.n4systems.fieldid.viewhelpers.handlers.EventSerialNumberHandler;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.reporting.columns.display.AssetLinkPropertyColumn;
import com.n4systems.fieldid.wicket.components.reporting.columns.display.AssignedToUpdatePropertyColumn;
import com.n4systems.fieldid.wicket.components.reporting.columns.display.DateTimePropertyColumn;
import com.n4systems.fieldid.wicket.components.reporting.columns.display.EventRfidNumberPropertyColumn;
import com.n4systems.fieldid.wicket.components.reporting.columns.display.EventSerialNumberPropertyColumn;
import com.n4systems.fieldid.wicket.components.reporting.columns.display.ReflectorPropertyColumn;
import com.n4systems.fieldid.wicket.components.reporting.columns.display.StringOrDatePropertyColumn;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.assettype.GroupedAssetTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.reporting.EventReportCriteriaModel;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.Project;
import com.n4systems.model.Status;
import com.n4systems.model.location.Location;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.LoaderFactory;

public class ReportFormatConverter {

    public List<IColumn> convertColumns(EventReportCriteriaModel criteriaModel) {
        List<IColumn> convertedColumns = new ArrayList<IColumn>();
        List<ColumnMappingView> enabledColumns = criteriaModel.getSortedStaticAndDynamicColumns();

        for (ColumnMappingView enabledColumn : enabledColumns) {
            String columnOutputHandler = enabledColumn.getOutputHandler();
            String pathExpression = enabledColumn.getPathExpression();
            FIDLabelModel columnLabelModel = new FIDLabelModel(enabledColumn.getLabel());
            // For wicket's sort property, we'll use a String representation of the column ID
            // Since this is the only information we'll get from wicket when the user clicks a sort link,
            // but we'll need more info than that to actually sort our search (sort join, sort expr).
            String dbColumnIdStr = enabledColumn.getDbColumnId() == null ? null : enabledColumn.getDbColumnId().toString();

            if (dbColumnIdStr == null || "custom_created_columns".equals(enabledColumn.getGroupKey())) {
                convertedColumns.add(new ReflectorPropertyColumn(columnLabelModel, pathExpression));
            } else if (AssetLinkHandler.class.getName().equals(columnOutputHandler)) {
                AssetLinkPropertyColumn assetLinkPropertyColumn = new AssetLinkPropertyColumn(columnLabelModel, pathExpression);
                convertedColumns.add(assetLinkPropertyColumn);
            } else if (EventSerialNumberHandler.class.getName().equals(columnOutputHandler)) {
                convertedColumns.add(new EventSerialNumberPropertyColumn(columnLabelModel, dbColumnIdStr, pathExpression));
            } else if (DateTimeHandler.class.getName().equals(columnOutputHandler)) {
                convertedColumns.add(new DateTimePropertyColumn(columnLabelModel, dbColumnIdStr, pathExpression));
            } else if (EventRfidNumberHandler.class.getName().equals(columnOutputHandler)) {
                convertedColumns.add(new EventRfidNumberPropertyColumn(columnLabelModel, dbColumnIdStr, pathExpression));
            } else if (EnumHandler.class.getName().equals(columnOutputHandler)) {
                convertedColumns.add(new EventRfidNumberPropertyColumn(columnLabelModel, dbColumnIdStr, pathExpression));
            } else if (AssignedToUpdateHandler.class.getName().equals(columnOutputHandler)) {
                convertedColumns.add(new AssignedToUpdatePropertyColumn(columnLabelModel, pathExpression));
            } else {
                if (enabledColumn.isSortable()) {
                    convertedColumns.add(new StringOrDatePropertyColumn(columnLabelModel, dbColumnIdStr, pathExpression));
                } else {
                    convertedColumns.add(new StringOrDatePropertyColumn(columnLabelModel, pathExpression));
                }

            }

        }

        return convertedColumns;
    }

    public EventSearchContainer convertCriteria(EventReportCriteriaModel criteriaModel) {
        SecurityFilter securityFilter = FieldIDSession.get().getSessionUser().getSecurityFilter();
        EventSearchContainer container = new EventSearchContainer(securityFilter, new LoaderFactory(securityFilter));

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

    public EventReportCriteriaModel convertCriteria(EventSearchContainer container, PersistenceManager pm, AssetManager assetManager) {
        EventReportCriteriaModel criteriaModel = new EventReportCriteriaModel();

        criteriaModel.setSerialNumber(container.getSerialNumber());
        criteriaModel.setAssetStatus(find(pm, AssetStatus.class, container.getAssetStatus()));
        criteriaModel.setAssetType(find(pm, AssetType.class, container.getAssetType()));
        criteriaModel.setAssetTypeGroup(find(pm, AssetTypeGroup.class, container.getAssetTypeGroup()));
        criteriaModel.setAssignedTo(find(pm, User.class, container.getAssignedUser()));
        criteriaModel.setEventBook(find(pm, EventBook.class, container.getEventBook()));
        criteriaModel.setEventType(find(pm, EventType.class, container.getEventType()));
        criteriaModel.setEventTypeGroup(find(pm, EventTypeGroup.class, container.getEventTypeGroup()));
        criteriaModel.setFromDate(container.getFromDate());
        criteriaModel.setToDate(container.getToDate());
        criteriaModel.setIncludeSafetyNetwork(container.isIncludeNetworkResults());
        criteriaModel.setJob(find(pm, Project.class, container.getJob()));
        criteriaModel.setOrderNumber(container.getOrderNumber());
        criteriaModel.setOwner(container.getOwner());
        criteriaModel.setPerformedBy(find(pm, User.class, container.getPerformedBy()));
        criteriaModel.setPurchaseOrder(container.getPurchaseOrder());
        criteriaModel.setReferenceNumber(container.getReferenceNumber());
        criteriaModel.setRfidNumber(container.getRfidNumber());
        criteriaModel.setResult(container.getStatus() == null ? null : Status.valueOf(container.getStatus()));
        criteriaModel.setSelection(container.getMultiIdSelection());

        LocationWebModel locationWebModel = container.getLocation();
        Location location = new Location(locationWebModel.getPredefinedLocation(),  locationWebModel.getFreeformLocation());
        criteriaModel.setLocation(location);

        ReportConfiguration reportConfiguration = new EventColumnsService().getReportConfiguration(FieldIDSession.get().getSessionUser().getSecurityFilter());
        criteriaModel.setColumnGroups(reportConfiguration.getColumnGroups());

        final IModel<AssetTypeGroup> assetTypeGroupModel = new PropertyModel<AssetTypeGroup>(criteriaModel, "assetTypeGroup");
        final IModel<AssetType> assetTypeModel = new PropertyModel<AssetType>(criteriaModel, "assetType");
        final IModel<List<AssetType>> availableAssetTypesModel = new GroupedAssetTypesForTenantModel(assetTypeGroupModel);
        final IModel<List<ColumnMappingGroupView>> dynamicAssetColumns = new PropertyModel<List<ColumnMappingGroupView>>(criteriaModel, "dynamicAssetColumnGroups");

        DynamicColumnsConverter.updateDynamicAssetColumns(assetManager, dynamicAssetColumns, assetTypeModel, availableAssetTypesModel);

        final IModel<EventTypeGroup> eventTypeGroupModel = new PropertyModel<EventTypeGroup>(criteriaModel, "eventTypeGroup");
        final IModel<EventType> eventTypeModel = new PropertyModel<EventType>(criteriaModel, "eventType");
        final IModel<List<EventType>> availableEventTypesModel = new EventTypesForTenantModel(eventTypeGroupModel);
        final IModel<List<ColumnMappingGroupView>> dynamicEventColumns = new PropertyModel<List<ColumnMappingGroupView>>(criteriaModel, "dynamicEventColumnGroups");

        DynamicColumnsConverter.updateDynamicEventColumns(pm, dynamicEventColumns, eventTypeModel, availableEventTypesModel);

        criteriaModel.enableSelectedColumns(container);
        criteriaModel.setSortColumnFromContainer(container);

        return criteriaModel;
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
