package com.n4systems.model.eula;

import java.util.Date;
import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.persistence.loaders.legacy.EntityLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;



public class CurrentEulaLoader  extends EntityLoader<EULA>   {

	public CurrentEulaLoader() {
		super();
	}

	public CurrentEulaLoader(PersistenceManager pm) {
		super(pm);
	}
	
	@Override
	protected EULA load(PersistenceManager pm) {
		QueryBuilder<EULA> query = new QueryBuilder<EULA>(EULA.class);
		query.addWhere(Comparator.LE, "effectiveDate", "effectiveDate", new Date());
		query.addOrder("effectiveDate", false);
		
		return pm.findAll(query, 0, 1).get(0);
	}

}
