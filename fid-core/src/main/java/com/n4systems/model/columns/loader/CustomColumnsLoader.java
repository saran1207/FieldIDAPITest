package com.n4systems.model.columns.loader;

import com.n4systems.model.columns.ColumnMapping;
import com.n4systems.model.columns.CustomColumnCategory;
import com.n4systems.model.columns.CustomColumnMapping;
import com.n4systems.model.columns.ReportType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

import javax.persistence.EntityManager;

public class CustomColumnsLoader extends ListLoader<ColumnMapping> {

    private ReportType reportType;
    private CustomColumnCategory category;

    public CustomColumnsLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected List<ColumnMapping> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<ColumnMapping> query = new QueryBuilder<ColumnMapping>(CustomColumnMapping.class, filter);

        query.addSimpleWhere("reportType", reportType);
        query.addSimpleWhere("tenant", filter.getOwner().getPrimaryOrg().getTenant());
        if (category != null) query.addSimpleWhere("category", category);

        return query.getResultList(em);
    }

    public CustomColumnsLoader reportType(ReportType reportType) {
        this.reportType = reportType;
        return this;
    }

    public CustomColumnsLoader category(CustomColumnCategory category) {
        this.category = category;
        return this;
    }

}
