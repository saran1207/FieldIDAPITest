package com.n4systems.model.productstatus;

import javax.persistence.EntityManager;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class ProductStatusByNameLoader extends SecurityFilteredLoader<ProductStatusBean> {
	private String name;
	
	public ProductStatusByNameLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected ProductStatusBean load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<ProductStatusBean> builder = new QueryBuilder<ProductStatusBean>(ProductStatusBean.class, filter);
		builder.addWhere(WhereClauseFactory.create("name", name));
		
		ProductStatusBean status = builder.getSingleResult(em);
		return status;
	}

	public ProductStatusByNameLoader setName(String name) {
		this.name = name;
		return this;
	}
}
