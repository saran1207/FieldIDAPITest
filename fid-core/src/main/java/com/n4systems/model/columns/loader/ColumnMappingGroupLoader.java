package com.n4systems.model.columns.loader;

import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.columns.ColumnMapping;
import com.n4systems.model.columns.ColumnMappingGroup;
import com.n4systems.model.columns.ReportType;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

public class ColumnMappingGroupLoader extends ListLoader<ColumnMappingGroup> {

    private ReportType reportType;
    private PrimaryOrg primaryOrg;

    public ColumnMappingGroupLoader(PrimaryOrg primaryOrg) {
        super(new OpenSecurityFilter());
        this.primaryOrg = primaryOrg;
    }

    @Override
    protected List<ColumnMappingGroup> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<ColumnMappingGroup> query = new QueryBuilder<ColumnMappingGroup>(ColumnMappingGroup.class, filter);

        if (reportType != null)
            query.addSimpleWhere("type", reportType);

        query.addPostFetchPaths("columnMappings");

        List<ColumnMappingGroup> groups = query.getResultList(em);
        if (primaryOrg != null) {
            groups = filterColumnsWeDontHaveRequiredFeatureFor(groups);
        }
        return groups;
    }

    private List<ColumnMappingGroup> filterColumnsWeDontHaveRequiredFeatureFor(List<ColumnMappingGroup> groups) {
        List<ColumnMappingGroup> filteredGroups = new ArrayList<ColumnMappingGroup>();

        for (ColumnMappingGroup group : groups) {
            List<ColumnMapping> filteredMappings = new ArrayList<ColumnMapping>();
            for (ColumnMapping mapping : group.getColumnMappings()) {
                ExtendedFeature requiredFeature = mapping.getRequiredExtendedFeature();
                if (requiredFeature == null || primaryOrg.getExtendedFeatures().contains(requiredFeature)) {
                    filteredMappings.add(mapping);
                }
            }
            group.setColumnMappings(filteredMappings);
            if (!group.getColumnMappings().isEmpty()) {
                filteredGroups.add(group);
            }
        }

        return filteredGroups;
    }

    public ColumnMappingGroupLoader reportType(ReportType type) {
        this.reportType = type;
        return this;
    }

}
