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

public class SmartSearchListLoader extends ListLoader<Product> {

	private String searchText;
	
	public SmartSearchListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<Product> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Product> builder = new QueryBuilder<Product>(Product.class, filter);
		
		WhereParameterGroup whereGroup = new WhereParameterGroup("search_group");
		
		whereGroup.addClause(WhereClauseFactory.create("serialNumber", searchText, WhereParameter.IGNORE_CASE, ChainOp.OR));
		whereGroup.addClause(WhereClauseFactory.create("rfidNumber", searchText, WhereParameter.IGNORE_CASE, ChainOp.OR));
		whereGroup.addClause(WhereClauseFactory.create("customerRefNumber", searchText, WhereParameter.IGNORE_CASE, ChainOp.OR));
		
		builder.addWhere(whereGroup);
		builder.addOrder("created");
				
		List<Product> products = builder.getResultList(em);
		return products;
	}

	public SmartSearchListLoader setSearchText(String searchText) {
		this.searchText = searchText;
		return this;
	}
	
}
