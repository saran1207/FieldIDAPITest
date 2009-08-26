package com.n4systems.model.promocode;

import javax.persistence.EntityManager;

import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

public class PromoCodeByCodeLoader extends Loader<PromoCode> {

	private String code;
	
	@Override
	protected PromoCode load(EntityManager em) {
		
		QueryBuilder<PromoCode> builder = new QueryBuilder<PromoCode>(PromoCode.class);
		builder.addSimpleWhere("code", code);		
		
		return builder.getSingleResult(em);
	}

	public void setCode(String code) {
		this.code = code;
	}
}
