package com.n4systems.fieldid.wicket.model.columnlayout;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.columns.ColumnMapping;
import com.n4systems.model.columns.ColumnMappingGroup;
import com.n4systems.model.columns.CustomColumnCategory;
import com.n4systems.model.columns.ReportType;
import com.n4systems.model.columns.loader.CustomColumnsLoader;
import com.n4systems.model.security.SecurityFilter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.ArrayList;
import java.util.List;

public class CustomColumnsModel extends LoadableDetachableModel<List<ColumnMapping>> {

    private ReportType reportType;
    private CustomColumnCategory category;
    private IModel<List<ColumnMapping>> inUseColumns;

    public CustomColumnsModel(ReportType reportType, CustomColumnCategory category, IModel<List<ColumnMapping>> inUseColumns) {
        this.reportType = reportType;
        this.category = category;
        this.inUseColumns = inUseColumns;
    }

    @Override
    protected List<ColumnMapping> load() {
        SecurityFilter securityFilter = FieldIDSession.get().getSessionUser().getSecurityFilter();
        List<ColumnMapping> customColumns = new CustomColumnsLoader(securityFilter).reportType(reportType).category(category).load();
        List<ColumnMapping> unusedColumns = new ArrayList<ColumnMapping>();

        List<ColumnMapping> inUseColumnList = inUseColumns.getObject();

//        ColumnMappingGroup customGroup = new ColumnMappingGroup();
//        customGroup.setGroupKey(CUSTOM_GROUP_KEY);
        // Apply a dummy custom group to the custom columns so we can draw them properly (assign the right css color)
        for (ColumnMapping column : customColumns) {
            if (!inUseColumnList.contains(column)) {
//                column.setGroup(customGroup);
                unusedColumns.add(column);
            }
        }

        return unusedColumns;
    }

}
