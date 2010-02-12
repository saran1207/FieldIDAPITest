package com.n4systems.model.orgs.external;

import javax.persistence.EntityManager;

import com.n4systems.model.orgs.ExternalOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class ExternalOrgCodeExistsLoader extends SecurityFilteredLoader<Boolean> {
	private final Class<? extends ExternalOrg> orgClass;
	private String code;
	private Long filterOutId;
	private String filterOutGlobalId;
	
	public ExternalOrgCodeExistsLoader(SecurityFilter filter, Class<? extends ExternalOrg> orgClass) {
		super(filter);
		this.orgClass = orgClass;
	}

	@Override
	protected Boolean load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<ExternalOrg> builder = new QueryBuilder<ExternalOrg>(orgClass, new TenantOnlySecurityFilter(filter.getTenantId()));
		builder.addSimpleWhere("code", code);
		
		if (filterOutId != null) {
			builder.addWhere(Comparator.NE, "filter_out_id", "id", filterOutId);
		}
		
		if (filterOutGlobalId != null) {
			builder.addWhere(Comparator.NE, "filter_out_global_id", "globalId", filterOutGlobalId);
		}
		
		boolean exists = builder.entityExists(em);
		return exists;
	}

	public ExternalOrgCodeExistsLoader setCode(String code) {
		this.code = code;
		return this;
	}

	public ExternalOrgCodeExistsLoader setFilterOutId(Long filterOutId) {
		this.filterOutId = filterOutId;
		return this;
	}
	
	public ExternalOrgCodeExistsLoader setFilterOutGlobalId(String filterOutExternalId) {
		this.filterOutGlobalId = filterOutExternalId;
		return this;
	}

}
