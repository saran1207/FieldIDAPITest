package com.n4systems.model.inspectiontype;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.model.InspectionType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class CommonInspectionAttributeNameListLoader extends ListLoader<String> {

	public CommonInspectionAttributeNameListLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected List<String> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Long> builder = new QueryBuilder<Long>(InspectionType.class, filter);
		Long inspectionTypes = builder.setCountSelect().getSingleResult(em);
		
		Query query = em.createQuery("SELECT f FROM " + InspectionType.class.getName() + " i, IN(i.infoFieldNames) f WHERE i.tenant.id = :tenantId GROUP BY f HAVING COUNT(*) = :inspectionTypeCount");
		query.setParameter("tenantId", filter.getTenantId());
		query.setParameter("inspectionTypeCount", inspectionTypes);
		
		List<String> uniqueNames = (List<String>)query.getResultList();
		
		if (uniqueNames.isEmpty()) {
			return new ArrayList<String>();
		}
		
		return uniqueNames;
	}

}
