package com.n4systems.fieldid.api.mobile.resources.eventhistory;

import com.n4systems.fieldid.api.mobile.resources.ApiResource;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.WorkflowState;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import java.util.List;

@Component
@Path("eventHistory")
public class ApiEventHistoryResource extends ApiResource<ApiEventHistory, ThingEvent> {
	
	public List<ApiEventHistory> findAllEventHistory(String assetId) {
		QueryBuilder<ThingEvent> builder = createUserSecurityBuilder(ThingEvent.class);
        builder.addWhere(WhereClauseFactory.create("workflowState", WorkflowState.COMPLETED));
		builder.addWhere(WhereClauseFactory.create("asset.mobileGUID", assetId));
		builder.addOrder("completedDate", false);

		List<ThingEvent> events = persistenceService.findAll(builder);
		List<ApiEventHistory> apiEventHistory = convertAllEntitiesToApiModels(events);
		return apiEventHistory;
	}
	
	@Override
	protected ApiEventHistory convertEntityToApiModel(ThingEvent event) {
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
