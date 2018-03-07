package com.n4systems.fieldid.ws.v1.resources.eventhistory;

import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.fieldid.ws.v1.resources.eventtype.ApiEventType;
import com.n4systems.fieldid.ws.v1.resources.eventtype.ApiPlaceEventTypeResource;
import com.n4systems.model.PlaceEvent;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.persistence.utils.PostFetcher;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.search.PostfetchingDefiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
