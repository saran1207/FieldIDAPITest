package com.n4systems.model.location;

import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.model.security.OwnerAndDownWithPrimaryFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;
import java.util.List;

public class PredefinedLocationListLoader extends ListLoader<PredefinedLocation> {

	private boolean archivedState;
	private SecurityFilter filter;
    private boolean primaryOrgFiltering = false;

    public PredefinedLocationListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<PredefinedLocation> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<PredefinedLocation> builder = createQueryBuilder(new TenantOnlySecurityFilter(filter.getTenantId()));
        // Caveat : you don't want to load primary org locations when editing.
        if (isPrimaryOrgFiltering()) {
            builder.applyFilter(new OwnerAndDownWithPrimaryFilter(filter.getOwner()));
        } else {
            builder.applyFilter(new OwnerAndDownFilter(filter.getOwner()));
        }
        builder.addOrder("id");

		return builder.getResultList(em);
	}

    private boolean isPrimaryOrgFiltering() {
        return primaryOrgFiltering;
    }

    public PredefinedLocationListLoader withPrimaryOrgFiltering() {
        primaryOrgFiltering = true;
        return this;
    }

    protected QueryBuilder<PredefinedLocation> createQueryBuilder(SecurityFilter filter) {
        QueryBuilder<PredefinedLocation> query = new QueryBuilder<PredefinedLocation>(PredefinedLocation.class, filter);
        return query;
	}
	

	public PredefinedLocationListLoader withArchivedState() {
		archivedState = true;
		return this;
	}

    public SecurityFilter getFilter() {
        return filter;
    }
}
