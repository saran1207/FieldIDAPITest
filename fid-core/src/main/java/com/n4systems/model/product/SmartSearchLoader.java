package com.n4systems.model.product;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Product;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class SmartSearchLoader extends ListLoader<Product> {

	private String searchText;
	private boolean useSerialNumber = true;
	private boolean useRfidNumber = true;
	private boolean useRefNumber = true;
	
	public SmartSearchLoader(SecurityFilter filter) {
		super(filter);
	}

	protected QueryBuilder<Product> createQuery(SecurityFilter filter) {
		QueryBuilder<Product> builder = new QueryBuilder<Product>(Product.class, filter);
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
	public List<Product> load(EntityManager em, SecurityFilter filter) {
		List<Product> products = createQuery(filter).getResultList(em);
		return products;
	}

	public SmartSearchLoader setSearchText(String searchText) {
		this.searchText = searchText;
		return this;
	}
	
}
