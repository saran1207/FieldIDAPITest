package com.n4systems.model.orgs;

import javax.persistence.EntityManager;

import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class SecondaryOrgByNameLoader extends SecurityFilteredLoader<SecondaryOrg> {
	
	private String name;
	private Long filterOutId;
	private boolean caseInsensitive;
	
	public SecondaryOrgByNameLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected SecondaryOrg load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<SecondaryOrg> builder = new QueryBuilder<SecondaryOrg>(SecondaryOrg.class, filter.prepareFor(SecondaryOrg.class));
		
		Integer opts = null;
		if (caseInsensitive) {
			opts = WhereParameter.IGNORE_CASE;
		}
		
		builder.addWhere(Comparator.EQ, "name", "name", name, opts);
		
		if (filterOutId != null) {
			builder.addWhere(Comparator.NE, "id", "id", filterOutId);
		}
	
		SecondaryOrg secOrg = builder.getSingleResult(em);
		return secOrg;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFilterOutId(Long filterOutId) {
		this.filterOutId = filterOutId;
	}

	public void setCaseInsensitive(boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}
	
}
