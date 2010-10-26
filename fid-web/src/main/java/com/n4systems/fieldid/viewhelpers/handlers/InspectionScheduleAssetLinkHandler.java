package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.persistence.QueryBuilder;

public class InspectionScheduleAssetLinkHandler extends WebOutputHandler {
	private final PersistenceManager persistenceManager;
	private final QueryBuilder<Long> builder = new QueryBuilder<Long>(InspectionSchedule.class, new OpenSecurityFilter());
	
	public InspectionScheduleAssetLinkHandler(AbstractAction action) {
		super(action);
		persistenceManager = ServiceLocator.getPersistenceManager();
		builder.setSimpleSelect("asset.id");
	}
	
	public String handleWeb(Long entityId, Object value) {
		// look up the id of the asset for this inspection, so that we can create the link
		return "<a href=\"asset.action?uniqueID=" + getAssetId(entityId) + "\" >" + (String)value + "</a>";
		
	}
	
	private Long getAssetId(Long inspectionId) {
		// we need to clean out the previous param before running this query
		builder.getWhereParameters().clear();
		return persistenceManager.find(builder.addSimpleWhere("id", inspectionId));
	}

	public Object handleExcel(Long entityId, Object value) {
		return value;
	}
}
