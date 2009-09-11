package com.n4systems.model.orgs;

import javax.persistence.EntityManager;

import com.n4systems.model.security.OwnerFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class ExternalOrgCodeExistsLoader<T extends ExternalOrg> extends SecurityFilteredLoader<Boolean> {
	private final Class<T> orgClass;
	private String code;
	private BaseOrg parentOrg;
	private Long filterOutId;
	
	public ExternalOrgCodeExistsLoader(SecurityFilter filter, Class<T> orgClass) {
		super(filter);
		this.orgClass = orgClass;
	}

	@Override
	protected Boolean load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<T> builder = new QueryBuilder<T>(orgClass, filter);
		builder.applyFilter(new OwnerFilter(parentOrg));
		builder.addSimpleWhere("code", code);
		
		if (filterOutId != null) {
			builder.addWhere(Comparator.NE, "filter_out_id", "id", filterOutId);
		}
		
		boolean exists = builder.entityExists(em);
		return exists;
	}

	public ExternalOrgCodeExistsLoader<T> setCode(String code) {
		this.code = code;
		return this;
	}

	public ExternalOrgCodeExistsLoader<T> setParentOrg(BaseOrg parentOrg) {
		this.parentOrg = parentOrg;
		return this;
	}

	public ExternalOrgCodeExistsLoader<T> setFilterOutId(Long filterOutId) {
		this.filterOutId = filterOutId;
		return this;
	}

}
