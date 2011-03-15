package com.n4systems.model.columns.saver;

import com.n4systems.model.columns.ColumnLayout;
import com.n4systems.model.columns.loader.ColumnLayoutLoader;
import com.n4systems.model.security.SecurityFilter;

public class ColumnLayoutService {

    public void saveLayout(SecurityFilter filter, ColumnLayout layout) {
        ColumnLayout existingLayout = new ColumnLayoutLoader(filter).reportType(layout.getReportType()).includeDefaultLayout(false).load();
        if (existingLayout != null) {
            new ColumnLayoutSaver().remove(existingLayout);
        }

        new ColumnLayoutSaver().save(layout);
    }

}
