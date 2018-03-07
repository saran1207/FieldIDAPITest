package com.n4systems.fieldid.ws.v1.resources.eventhistory;

import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.model.PlaceEvent;
import com.n4systems.model.WorkflowState;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * This service returns the event history for one Place/Org.
 *
 * Created by Ruppi Rana
 */
@Component
@Path("placeEventHistoryV1")
public class ApiPlaceEventHistoryResourceV1 extends ApiResource<ApiPlaceEventHistoryV1, PlaceEvent> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ApiPlaceEventHistoryV1> findAllEventHistory(@QueryParam("id") Long placeId) {
        QueryBuilder<PlaceEvent> builder = createUserSecurityBuilder(PlaceEvent.class);
        builder.addWhere(WhereClauseFactory.create("workflowState", WorkflowState.COMPLETED));
        builder.addWhere(WhereClauseFactory.create("place.id", placeId));
        builder.addOrder("completedDate", false);

        List<PlaceEvent> events = persistenceService.findAll(builder);
        List<ApiPlaceEventHistoryV1> apiEventHistory = convertAllEntitiesToApiModels(events);
        return apiEventHistory;
    }

    @Override
    protected ApiPlaceEventHistoryV1 convertEntityToApiModel(PlaceEvent entityModel) {
        ApiPlaceEventHistoryV1 history = new ApiPlaceEventHistoryV1();

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
