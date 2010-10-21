package com.n4systems.model.product;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class SmartSearchLoader extends ListLoader<Asset> {

	private String searchText;
	private boolean useSerialNumber = true;
	private boolean useRfidNumber = true;
	private boolean useRefNumber = true;
	
	public SmartSearchLoader(SecurityFilter filter) {
		super(filter);
	}

	protected QueryBuilder<Asset> createQuery(SecurityFilter filter) {
		QueryBuilder<Asset> builder = new QueryBuilder<Asset>(Asset.class, filter);
		builder.addWhere(new SmartSearchWhereClause(searchText, useSerialNumber, useRfidNumber, useRefNumber));
		builder.addOrder("created");
		
		return builder;
	}

	public void setUseSerialNumber(boolean useSerialNumber) {
		this.useSerialNumber = useSerialNumber;
	}

	public void setUseRfidNumber(boolean useRfidNumber) {
		this.useRfidNumber = useRfidNumber;
	}

	public void setUseRefNumber(boolean useRefNumber) {
		this.useRefNumber = useRefNumber;
	}

	@Override
	public List<Asset> load(EntityManager em, SecurityFilter filter) {
		List<Asset> assets = createQuery(filter).getResultList(em);
		return assets;
	}

	public SmartSearchLoader setSearchText(String searchText) {
		this.searchText = searchText;
		return this;
	}
	
}
