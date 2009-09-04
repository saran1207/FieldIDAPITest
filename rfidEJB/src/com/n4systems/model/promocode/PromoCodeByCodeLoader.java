package com.n4systems.model.promocode;

import javax.persistence.EntityManager;

import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class PromoCodeByCodeLoader extends Loader<PromoCode> {

	private String code;
	
	@Override
	protected PromoCode load(EntityManager em) {
		if (code == null || code.trim().length() == 0) {
			return null;
		}
		
		QueryBuilder<PromoCode> builder = new QueryBuilder<PromoCode>(PromoCode.class);
		builder.addWhere(Comparator.EQ, "code", "code", code, WhereParameter.IGNORE_CASE);		
		
		return builder.getSingleResult(em);
	}

	public PromoCodeByCodeLoader setCode(String code) {
		this.code = code;
		return this;
	}
}
