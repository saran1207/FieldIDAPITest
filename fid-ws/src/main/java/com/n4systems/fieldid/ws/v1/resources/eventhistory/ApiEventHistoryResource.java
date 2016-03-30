package com.n4systems.fieldid.ws.v1.resources.eventhistory;

import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.model.SubEvent;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.WorkflowState;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("eventHistory")
public class ApiEventHistoryResource extends ApiResource<ApiEventHistory, ThingEvent> {

    private static final Logger logger = Logger.getLogger(ApiEventHistoryResource.class);

    public List<ApiEventHistory> findAllEventHistory(String assetId) {
		QueryBuilder<ThingEvent> builder = createUserSecurityBuilder(ThingEvent.class);
        builder.addWhere(WhereClauseFactory.create("workflowState", WorkflowState.COMPLETED));
		builder.addWhere(WhereClauseFactory.create("asset.mobileGUID", assetId));
		builder.addOrder("completedDate", false);

		// FIMB-1039: Mobile only displays the last 3 events.  Pulling the entire event history was causing performance problems.
		List<ThingEvent> events = persistenceService.findAll(builder, 0, 3);
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
        apiEventHistory.setSubEvents(event.getSubEvents().stream().map(subEvent -> {
            SubEvent convertMe = persistenceService.find(SubEvent.class, subEvent.getId());

            return convertSubEventToApiModel(convertMe,
                                             //There's a little bit of information we only find in the MasterEvent...
                                             //...assuming I'm right that we need this.

                                             event.getDate(),
                                             event.getPerformedBy().getDisplayName(),
                                             event.getEventResult().getDisplayName(),
                                             event.isPrintable());
        }).collect(Collectors.toList()));

		return apiEventHistory;
	}

    private ApiEventHistory convertSubEventToApiModel(SubEvent event, Date completionDate, String performedBy, String status, boolean printable) {
        ApiEventHistory apiEventHistory = new ApiEventHistory();
        apiEventHistory.setAssetId(event.getAsset().getMobileGUID());
        apiEventHistory.setAssetTypeId(event.getType().getId());
        apiEventHistory.setEventTypeName(event.getType().getName());
        apiEventHistory.setEventDate(completionDate);
        apiEventHistory.setEventId(event.getMobileGUID());
        apiEventHistory.setEventTypeId(event.getType().getId());
        apiEventHistory.setPerformedBy(performedBy);

        //Is this right??  I'm not 100% sure this is the value we want here.
        apiEventHistory.setStatus(status);

        //I'm assuming we show the same Printable field from the Master event...
        apiEventHistory.setPrintable(printable);

        return apiEventHistory;
    }
}

