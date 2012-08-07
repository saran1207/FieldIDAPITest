package com.n4systems.fieldid.wicket.util;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.viewhelpers.AssetSearchContainer;
import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import com.n4systems.fieldid.viewhelpers.SearchContainer;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.reporting.columns.display.FieldIdPropertyColumn;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.views.RowView;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;

import java.util.ArrayList;
import java.util.List;

public class ReportFormatConverter {

    private SystemSecurityGuard securityGuard;
    
    public ReportFormatConverter(SystemSecurityGuard securityGuard) { 
    	this.securityGuard = securityGuard;
    }

	public List<IColumn<RowView>> convertColumns(SearchCriteria criteriaModel) {
        List<IColumn<RowView>> convertedColumns = new ArrayList<IColumn<RowView>>();
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

    public EventSearchContainer convertCriteria(EventReportCriteria criteriaModel) {
        SecurityFilter securityFilter = FieldIDSession.get().getSessionUser().getSecurityFilter();
        EventSearchContainer container = new EventSearchContainer(securityFilter, new LoaderFactory(securityFilter), securityGuard);

        container.setIdentifier(criteriaModel.getIdentifier());
        container.setAssetStatus(getId(criteriaModel.getAssetStatus()));
        container.setAssetType(getId(criteriaModel.getAssetType()));
        container.setAssetTypeGroup(getId(criteriaModel.getAssetTypeGroup()));
        container.setAssignedUser(getId(criteriaModel.getAssignedTo()));
        container.setEventBook(getId(criteriaModel.getEventBook()));
        container.setEventType(getId(criteriaModel.getEventType()));
        container.setEventTypeGroup(getId(criteriaModel.getEventTypeGroup()));
        if (criteriaModel.getDateRange() != null) {
            // should this be in UTC...or have a TimeZone???
            container.setFromDate(criteriaModel.getDateRange().calculateFromDate());
            container.setToDate(criteriaModel.getDateRange().calculateToDate());
        }
        container.setDateRange(criteriaModel.getDateRange());
        container.setIncludeNetworkResults(criteriaModel.isIncludeSafetyNetwork());        
        container.setJob(getId(criteriaModel.getJob()));
        container.setOrderNumber(criteriaModel.getOrderNumber());
        container.setOwner(criteriaModel.getOwner());
        container.setPerformedBy(getId(criteriaModel.getPerformedBy()));
        container.setPurchaseOrder(criteriaModel.getPurchaseOrder());
        container.setReferenceNumber(criteriaModel.getReferenceNumber());
        container.setRfidNumber(criteriaModel.getRfidNumber());
        container.setStatus(criteriaModel.getResult() == null ? null : criteriaModel.getResult().name());
        container.getLocation().setFreeformLocation(criteriaModel.getLocation().getFreeformLocation());
        container.getLocation().setPredefinedLocation(criteriaModel.getLocation().getPredefinedLocation());

        setSortAndColumnParameters(container, criteriaModel);

        return container;
    }

    public AssetSearchContainer convertCriteria(AssetSearchCriteria criteriaModel) {
        SecurityFilter securityFilter = FieldIDSession.get().getSessionUser().getSecurityFilter();
        AssetSearchContainer container = new AssetSearchContainer(securityFilter, new LoaderFactory(securityFilter), securityGuard);

        container.setIdentifier(criteriaModel.getIdentifier());
        container.setOwner(criteriaModel.getOwner());
        container.setAssetStatus(getId(criteriaModel.getAssetStatus()));
        container.setAssetType(getId(criteriaModel.getAssetType()));
        container.setAssignedUser(getId(criteriaModel.getAssignedTo()));
        container.setAssetTypeGroup(getId(criteriaModel.getAssetTypeGroup()));

        // DD : timezone's don't really make sense for floating date ranges here so i'll just use defaults.
        //   confirm...not really sure about the usage of this class w.r.t date ranges. i think it only dealt with
        //   specific dates in the past.
        container.setFromDate(criteriaModel.getDateRange().calculateFromDate());
        container.setToDate(criteriaModel.getDateRange().calculateToDate());
        container.setOrderNumber(criteriaModel.getOrderNumber());
        container.setRfidNumber(criteriaModel.getRfidNumber());
        container.setReferenceNumber(criteriaModel.getReferenceNumber());
        container.setRfidNumber(criteriaModel.getRfidNumber());
        container.setPurchaseOrder(criteriaModel.getPurchaseOrder());

        setSortAndColumnParameters(container, criteriaModel);

        return container;
    }

    protected void setSortAndColumnParameters(SearchContainer container, SearchCriteria criteriaModel) {
        container.setSelectedColumns(convertSelectedColumns(criteriaModel));
        container.setMultiIdSelection(criteriaModel.getSelection());
        container.setSortColumn(criteriaModel.getSortColumn() == null ? null : criteriaModel.getSortColumn().getSortExpression());
        container.setSortDirection(criteriaModel.getSortDirection() == null ? null : criteriaModel.getSortDirection().getDisplayName());
        container.setSortJoinExpression(criteriaModel.getSortColumn() == null ? null : criteriaModel.getSortColumn().getJoinExpression());
    }

    private List<String> convertSelectedColumns(SearchCriteria criteriaModel) {
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
