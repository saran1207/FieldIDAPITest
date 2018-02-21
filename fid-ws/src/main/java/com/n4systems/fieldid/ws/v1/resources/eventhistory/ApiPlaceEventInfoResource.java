package com.n4systems.fieldid.ws.v1.resources.eventhistory;

import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.fieldid.ws.v1.resources.eventtype.ApiEventType;
import com.n4systems.fieldid.ws.v1.resources.eventtype.ApiPlaceEventTypeResource;
import com.n4systems.model.PlaceEvent;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rrana on 2018-02-19.
 */
public class ApiPlaceEventInfoResource extends ApiResource<ApiPlaceEventInfo, PlaceEvent> {

    @Autowired
    private ApiPlaceEventTypeResource eventTypeResource;

    protected List<ApiPlaceEventInfo> getPlaceEvents(Long placeId) {
        QueryBuilder<PlaceEvent> builder = createUserSecurityBuilder(PlaceEvent.class);
            builder.addWhere(WhereClauseFactory.create("workflowState",WorkflowState.COMPLETED));
            builder.addWhere(WhereClauseFactory.create("place.id",placeId));
            builder.addOrder("completedDate",false);

        List<PlaceEvent> events = persistenceService.findAll(builder);
        return convertAllEntitiesToApiModels(events);

    }

    protected List<ApiEventType> getPlaceEventTypes(Long placeId) {
        QueryBuilder<BaseOrg> query = new QueryBuilder<>(BaseOrg.class, securityContext.getTenantSecurityFilter());
        query.addSimpleWhere("id", placeId);
        BaseOrg place = persistenceService.find(query);

        return place.getEventTypes().stream().map(eventTypeResource::convertToApiPlaceEvent).collect(Collectors.toList());
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
