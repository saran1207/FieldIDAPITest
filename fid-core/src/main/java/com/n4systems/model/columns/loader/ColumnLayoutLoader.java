package com.n4systems.model.columns.loader;

import com.n4systems.model.columns.ColumnLayout;
import com.n4systems.model.columns.ReportType;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;

import javax.persistence.EntityManager;

public class ColumnLayoutLoader extends SecurityFilteredLoader<ColumnLayout> {

    private ReportType reportType;
    private boolean includeDefaultLayout = true;

    public ColumnLayoutLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected ColumnLayout load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<ColumnLayout> query = new QueryBuilder<ColumnLayout>(ColumnLayout.class, filter);

        query.addSimpleWhere("reportType", reportType);
        addFiltersAndFetches(query);

        ColumnLayout layout = query.getSingleResult(em);

        if (layout == null && includeDefaultLayout) {
            // Load default layout
            query = new QueryBuilder<ColumnLayout>(ColumnLayout.class, new OpenSecurityFilter());

            query.addSimpleWhere("reportType", reportType);
            query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NULL, "tenant", null));
            addFiltersAndFetches(query);
            layout = query.getSingleResult(em);
        }

        return layout;
    }

    private void addFiltersAndFetches(QueryBuilder<ColumnLayout> query) {
        query.addSimpleWhere("reportType", reportType);
        query.addPostFetchPaths("columnMappingList");
        query.addPostFetchPaths("columnMappingList.mapping.group");
    }

    public ColumnLayoutLoader reportType(ReportType reportType) {
        this.reportType = reportType;
        return this;
    }

    public ColumnLayoutLoader includeDefaultLayout(boolean include) {
        this.includeDefaultLayout = include;
        return this;
    }


}
