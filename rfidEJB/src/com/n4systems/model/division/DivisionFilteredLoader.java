package com.n4systems.model.division;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Division;
import com.n4systems.persistence.loaders.legacy.SecuredLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

/**
 * @deprecated Use {@link FilteredIdLoader} instead.
 */
@Deprecated
public class DivisionFilteredLoader extends SecuredLoader<Division> {
	private Long id;
	
	public DivisionFilteredLoader(SecurityFilter filter) {
	    super(filter);
    }

	public DivisionFilteredLoader(PersistenceManager pm, SecurityFilter filter) {
	    super(pm, filter);
    }
	
	@Override
    protected Division load(PersistenceManager pm, SecurityFilter filter) {
		QueryBuilder<Division> builder = new QueryBuilder<Division>(Division.class, filter.prepareFor(Division.class));
		
		builder.addSimpleWhere("id", id);
		
		Division division = pm.find(builder);
		
	    return division;
    }

	public DivisionFilteredLoader setId(Long id) {
		this.id = id;
		return this;
	}
}
