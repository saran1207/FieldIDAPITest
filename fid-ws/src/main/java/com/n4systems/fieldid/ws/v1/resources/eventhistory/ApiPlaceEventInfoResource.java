package com.n4systems.fieldid.ws.v1.resources.eventhistory;

import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.model.PlaceEvent;
import com.n4systems.model.WorkflowState;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

import java.util.List;

/**
 * Created by rrana on 2018-02-19.
 */
public class ApiPlaceEventInfoResource extends ApiResource<ApiPlaceEventInfo, PlaceEvent> {

    protected List<ApiPlaceEventInfo> getPlaceEvents(Long placeId) {
        QueryBuilder<PlaceEvent> builder = createUserSecurityBuilder(PlaceEvent.class);
            builder.addWhere(WhereClauseFactory.create("workflowState",WorkflowState.COMPLETED));
            builder.addWhere(WhereClauseFactory.create("place.id",placeId));
            builder.addOrder("completedDate",false);

        List<PlaceEvent> events = persistenceService.findAll(builder);
        return convertAllEntitiesToApiModels(events);

    }

    @Override
    protected ApiPlaceEventInfo convertEntityToApiModel(PlaceEvent entityModel) {
        ApiPlaceEventInfo history = new ApiPlaceEventInfo();

        history.setPlaceId(entityModel.getPlace().getId());
        history.setEventId(entityModel.getMobileGUID());
        history.setEventTypeId(entityModel.getType().getId());
        history.setEventTypeName(entityModel.getEventType().getName());
        history.setEventDate(entityModel.getDate());
        history.setPerformedBy(entityModel.getPerformedBy().getId());
        history.setStatus(entityModel.getEventResult().getDisplayName());
        history.setPrintable(entityModel.isPrintable());

        return history;
    }
}
