package com.n4systems.model.eula;

import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;

import javax.persistence.EntityManager;
import java.util.Date;

public class CurrentEulaLoader  extends Loader<EULA>   {

	public CurrentEulaLoader() {}
	
	@Override
	public EULA load(EntityManager em) {
		QueryBuilder<EULA> builder = new QueryBuilder<EULA>(EULA.class, new OpenSecurityFilter());
		builder.addWhere(Comparator.LE, "effectiveDate", "effectiveDate", new Date());
		builder.addOrder("effectiveDate", false);
		
		EULA eula = builder.getResultList(em, 0, 1).get(0);
		return eula; 
	}

}
