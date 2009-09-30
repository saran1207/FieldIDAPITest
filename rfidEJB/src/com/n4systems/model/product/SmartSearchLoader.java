package com.n4systems.model.product;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Product;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameterGroup;
import com.n4systems.util.persistence.WhereClause.ChainOp;

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
		
		WhereParameterGroup whereGroup = new WhereParameterGroup("search_group");
		
		if (useSerialNumber) {
			whereGroup.addClause(WhereClauseFactory.create("serialNumber", searchText, WhereParameter.IGNORE_CASE, ChainOp.OR));
		}
		
		if (useRfidNumber) {
			whereGroup.addClause(WhereClauseFactory.create("rfidNumber", searchText, WhereParameter.IGNORE_CASE, ChainOp.OR));
		}
		
		if (useRefNumber) {
			whereGroup.addClause(WhereClauseFactory.create("customerRefNumber", searchText, WhereParameter.IGNORE_CASE, ChainOp.OR));
		}
		
		builder.addWhere(whereGroup);
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
