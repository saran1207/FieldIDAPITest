package com.n4systems.fieldid.ws.v1.resources.eventhistory;

import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.model.Event;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import java.util.List;

@Component
@Path("eventHistory")
public class ApiEventHistoryResource extends ApiResource<ApiEventHistory, Event> {
	
	public List<ApiEventHistory> findAllEventHistory(String assetId) {
		QueryBuilder<Event> builder = createUserSecurityBuilder(Event.class);
        builder.addWhere(WhereClauseFactory.create("workflowState", Event.WorkflowState.COMPLETED));
		builder.addWhere(WhereClauseFactory.create("asset.mobileGUID", assetId));
		builder.addOrder("completedDate", false);

		List<Event> events = persistenceService.findAll(builder);
		List<ApiEventHistory> apiEventHistory = convertAllEntitiesToApiModels(events);
		return apiEventHistory;
	}
	
	@Override
	protected ApiEventHistory convertEntityToApiModel(Event event) {
		ApiEventHistory apiEventHistory = new ApiEventHistory();
		apiEventHistory.setAssetId(event.getAsset().getMobileGUID());
		apiEventHistory.setAssetTypeId(event.getType().getId());
		apiEventHistory.setEventTypeName(event.getType().getName());
		apiEventHistory.setEventDate(event.getDate());
		apiEventHistory.setEventId(event.getMobileGUID());
		apiEventHistory.setEventTypeId(event.getType().getId());
		apiEventHistory.setPerformedBy(event.getPerformedBy().getDisplayName());
		apiEventHistory.setStatus(event.getEventResult().getDisplayName());
		apiEventHistory.setPrintable(event.isPrintable());
		return apiEventHistory;
	}
}
