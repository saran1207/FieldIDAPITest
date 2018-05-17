package com.n4systems.fieldid.wicket.util;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.wicket.components.reporting.columns.display.FieldIdPropertyColumn;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.SearchCriteria;
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
        return convertColumns(criteriaModel, false);
    }

	public List<IColumn<RowView>> convertColumns(SearchCriteria criteriaModel, boolean disableSorting) {
        List<IColumn<RowView>> convertedColumns = new ArrayList<IColumn<RowView>>();
        List<ColumnMappingView> enabledColumns = criteriaModel.getSortedStaticAndDynamicColumns();

        int index = 0;
        for (ColumnMappingView enabledColumn : enabledColumns) {
            FIDLabelModel columnLabelModel = new FIDLabelModel(enabledColumn.getLabel());

            if (!disableSorting && enabledColumn.isSortable()) {
                convertedColumns.add(createSortableColumn(columnLabelModel, enabledColumn, index));
            } else {
                convertedColumns.add(createNonSortableColumn(columnLabelModel, enabledColumn, index));
            }

            index++;
        }

        return convertedColumns;
    }

    protected FieldIdPropertyColumn createSortableColumn(
            FIDLabelModel columnLabelModel, ColumnMappingView enabledColumn, int index) {
        return new FieldIdPropertyColumn(columnLabelModel, enabledColumn, index, true);
    }

    protected FieldIdPropertyColumn createNonSortableColumn(
            FIDLabelModel columnLabelModel, ColumnMappingView enabledColumn, int index) {
        return new FieldIdPropertyColumn(columnLabelModel, enabledColumn, index);
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
