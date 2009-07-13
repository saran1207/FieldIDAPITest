package com.n4systems.model.inspectiontype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.InspectionType;
import com.n4systems.persistence.loaders.legacy.ListLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

public class CommonInspectionAttributeNameListLoader extends ListLoader<String> {

	
	public CommonInspectionAttributeNameListLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

	public CommonInspectionAttributeNameListLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	protected List<String> load(PersistenceManager pm, SecurityFilter filter) {
		QueryBuilder<Long> builder = new QueryBuilder<Long>(InspectionType.class, filter.prepareFor(InspectionType.class));
		Long inspectionTypes = pm.find(builder.setCountSelect());
		
		String query = "SELECT f FROM " + InspectionType.class.getName() + " i, IN(i.infoFieldNames) f WHERE i.tenant.id = :tenantId GROUP BY f HAVING COUNT(*) = :inspectionTypeCount";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tenantId", filter.getTenantId());
		paramMap.put("inspectionTypeCount", inspectionTypes);
		
		List<String> uniqueNames = pm.passThroughFindAll(query, paramMap);
		
		if (uniqueNames.isEmpty()) {
			return new ArrayList<String>();
		}
		
		return uniqueNames;
	}

}
