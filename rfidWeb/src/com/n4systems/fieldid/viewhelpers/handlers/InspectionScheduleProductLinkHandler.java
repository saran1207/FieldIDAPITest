package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.persistence.QueryBuilder;

public class InspectionScheduleProductLinkHandler implements OutputHandler {
	private final PersistenceManager persistenceManager;
	private final QueryBuilder<Long> builder = new QueryBuilder<Long>(InspectionSchedule.class, new OpenSecurityFilter());
	
	public InspectionScheduleProductLinkHandler() {
		persistenceManager = ServiceLocator.getPersistenceManager();
		builder.setSimpleSelect("product.id");
	}
	
	public String handle(Long entityId, Object value) {
		// look up the id of the product for this inspection, so that we can create the link		
		return "<a href=\"product.action?uniqueID=" + getProductId(entityId) + "\" >" + (String)value + "</a>";
		
	}

	public boolean isLabel() {
		return false;
	}
	
	private Long getProductId(Long inspectionId) {
		// we need to clean out the previous param before running this query
		builder.getWhereParameters().clear();
		return persistenceManager.find(builder.addSimpleWhere("id", inspectionId));
	}
}
