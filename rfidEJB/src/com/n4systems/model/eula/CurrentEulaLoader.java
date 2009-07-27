package com.n4systems.model.eula;

import java.util.Date;

import javax.persistence.EntityManager;

import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class CurrentEulaLoader  extends Loader<EULA>   {

	public CurrentEulaLoader() {}
	
	@Override
	protected EULA load(EntityManager em) {
		QueryBuilder<EULA> builder = new QueryBuilder<EULA>(EULA.class);
		builder.addWhere(Comparator.LE, "effectiveDate", "effectiveDate", new Date());
		builder.addOrder("effectiveDate", false);
		
		EULA eula = builder.getResultList(em, 0, 1).get(0);
		return eula; 
	}

}
