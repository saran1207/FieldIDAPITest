package com.n4systems.fieldid.viewhelpers.handlers;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.service.download.TableGenerationContext;
import com.n4systems.fieldid.service.download.WebOutputHandler;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.persistence.QueryBuilder;

public class EventScheduleAssetLinkHandler extends WebOutputHandler {
	private final PersistenceManager persistenceManager;
	private final QueryBuilder<Long> builder = new QueryBuilder<Long>(EventSchedule.class, new OpenSecurityFilter());
	
	public EventScheduleAssetLinkHandler(TableGenerationContext action) {
		super(action);
		persistenceManager = ServiceLocator.getPersistenceManager();
		builder.setSimpleSelect("asset.id");
	}
	
	public String handleWeb(Long entityId, Object value) {
		// look up the id of the asset for this event, so that we can create the link
		return "<a class=\"identifierLink\" href=\"/fieldid/asset.action?uniqueID=" + getAssetId(entityId) + "\" >" + (String)value + "</a>";
		
	}
	
	private Long getAssetId(Long eventId) {
		// we need to clean out the previous param before running this query
		builder.getWhereParameters().clear();
		return persistenceManager.find(builder.addSimpleWhere("id", eventId));
	}

	public Object handleExcel(Long entityId, Object value) {
		return value;
	}
}
