package com.n4systems.model.eventtype;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.model.EventType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class CommonEventAttributeNameListLoader extends ListLoader<String> {

	public CommonEventAttributeNameListLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected List<String> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Long> builder = new QueryBuilder<Long>(EventType.class, filter);
		Long eventTypeCount = builder.setCountSelect().getSingleResult(em);
		
		Query query = em.createQuery("SELECT f FROM " + EventType.class.getName() + " i, IN(i.infoFieldNames) f WHERE i.tenant.id = :tenantId GROUP BY f HAVING COUNT(*) = :eventTypeCount");
		query.setParameter("tenantId", filter.getTenantId());
		query.setParameter("eventTypeCount", eventTypeCount);
		
		List<String> uniqueNames = (List<String>)query.getResultList();
		
		if (uniqueNames.isEmpty()) {
			return new ArrayList<String>();
		}
		
		return uniqueNames;
	}

}
