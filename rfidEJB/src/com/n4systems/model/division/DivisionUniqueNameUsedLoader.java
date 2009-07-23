package com.n4systems.model.division;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Division;
import com.n4systems.persistence.loaders.legacy.SecuredLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class DivisionUniqueNameUsedLoader extends SecuredLoader<Boolean> {

	private Long id = null;
	private String uniqueName;
	
	public DivisionUniqueNameUsedLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	@Override
	protected Boolean load(PersistenceManager pm, SecurityFilter filter) {
		QueryBuilder<Division> nameAvaliableQuery = new QueryBuilder<Division>(Division.class, filter.prepareFor(Division.class));
		nameAvaliableQuery.addWhere(Comparator.EQ, "divisionID", "divisionID", uniqueName, WhereParameter.IGNORE_CASE);
		if (id != null) {
			nameAvaliableQuery.addWhere(Comparator.NE, "id", "id", id);
		}
		return (0 != pm.findCount(nameAvaliableQuery));
	}
	
	public DivisionUniqueNameUsedLoader exceptForId(Long id) {
		this.id = id;
		return this;
	}
	
	public DivisionUniqueNameUsedLoader setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName.trim().toLowerCase();
		return this;
	}
}
