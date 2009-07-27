package com.n4systems.model.division;

import javax.persistence.EntityManager;

import com.n4systems.model.Division;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class DivisionUniqueNameUsedLoader extends SecurityFilteredLoader<Boolean> {
	private Long id = null;
	private String uniqueName;

	public DivisionUniqueNameUsedLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected Boolean load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Division> nameAvaliableQuery = new QueryBuilder<Division>(Division.class, filter.prepareFor(Division.class));
		nameAvaliableQuery.addWhere(Comparator.EQ, "divisionID", "divisionID", uniqueName, WhereParameter.IGNORE_CASE);
		if (id != null) {
			nameAvaliableQuery.addWhere(Comparator.NE, "id", "id", id);
		}
		return (0 != nameAvaliableQuery.getCount(em));
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
