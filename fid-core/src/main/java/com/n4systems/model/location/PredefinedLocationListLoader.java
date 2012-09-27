package com.n4systems.model.location;

import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;
import java.util.List;

public class PredefinedLocationListLoader extends ListLoader<PredefinedLocation> {

	private boolean parentFirstOrdering;
	private boolean archivedState;
	private SecurityFilter filter;
	
	public PredefinedLocationListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<PredefinedLocation> load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<PredefinedLocation> builder = createQueryBuilder(filter);
        if (filter.getOwner()!=null && !filter.getOwner().isPrimary()) {
            builder.applyFilter(new OwnerAndDownFilter(filter.getOwner()));
        }

		if (parentFirstOrdering) {
			builder.addOrder("id");
		}
		
		return builder.getResultList(em);
	}
	
	protected QueryBuilder<PredefinedLocation> createQueryBuilder(SecurityFilter filter) {
        QueryBuilder<PredefinedLocation> query = new QueryBuilder<PredefinedLocation>(PredefinedLocation.class, filter);
        return query;
	}
	

	public PredefinedLocationListLoader withParentFirstOrder() {
		parentFirstOrdering = true;
		return this;
	}

	public PredefinedLocationListLoader withArchivedState() {
		archivedState = true;
		return this;
	}

    public SecurityFilter getFilter() {
        return filter;
    }
}
