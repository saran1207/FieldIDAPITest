package com.n4systems.model.asset;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class SmartSearchCounter extends SecurityFilteredLoader<Long> {
	private String searchText;
	private boolean useIdentifier = true;
	private boolean useRfidNumber = true;
	private boolean useRefNumber = true;
	
	public SmartSearchCounter(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected Long load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Long> builder = new QueryBuilder<Long>(Asset.class, filter);
		builder.addWhere(new SmartSearchWhereClause(searchText, useIdentifier, useRfidNumber, useRefNumber));
		
		Long count = builder.getCount(em);
		return count;
	}

	public void setUseIdentifier(boolean useIdentifier) {
		this.useIdentifier = useIdentifier;
	}

	public void setUseRfidNumber(boolean useRfidNumber) {
		this.useRfidNumber = useRfidNumber;
	}

	public void setUseRefNumber(boolean useRefNumber) {
		this.useRefNumber = useRefNumber;
	}

	public SmartSearchCounter setSearchText(String searchText) {
		this.searchText = searchText;
		return this;
	}
	
}
