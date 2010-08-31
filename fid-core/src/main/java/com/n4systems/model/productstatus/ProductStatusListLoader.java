package com.n4systems.model.productstatus;

import java.util.List;

import javax.persistence.EntityManager;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class ProductStatusListLoader extends ListLoader<ProductStatusBean> {

	public ProductStatusListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<ProductStatusBean> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<ProductStatusBean> builder = new QueryBuilder<ProductStatusBean>(ProductStatusBean.class, filter);
		builder.addOrder("name");
		
		List<ProductStatusBean> productStati = builder.getResultList(em);
		return productStati;
	}

}
