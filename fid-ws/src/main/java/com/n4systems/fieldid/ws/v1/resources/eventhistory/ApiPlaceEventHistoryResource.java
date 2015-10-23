package com.n4systems.fieldid.ws.v1.resources.eventhistory;

import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.model.PlaceEvent;
import com.n4systems.model.WorkflowState;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import java.util.List;

/**
 * Created by Jordan Heath on 2015-10-23.
 */
@Component
@Path("placeEventHistory")
public class ApiPlaceEventHistoryResource extends ApiResource<ApiPlaceEventHistory, PlaceEvent> {

    public List<ApiPlaceEventHistory> findAllEventHistory(Long placeId) {
        QueryBuilder<PlaceEvent> builder = createUserSecurityBuilder(PlaceEvent.class);
        builder.addWhere(WhereClauseFactory.create("workflowState", WorkflowState.COMPLETED));
        builder.addWhere(WhereClauseFactory.create("place.id", placeId));
        builder.addOrder("completedDate", false);

        List<PlaceEvent> events = persistenceService.findAll(builder);
        List<ApiPlaceEventHistory> apiEventHistory = convertAllEntitiesToApiModels(events);
        return apiEventHistory;
    }

    @Override
    protected ApiPlaceEventHistory convertEntityToApiModel(PlaceEvent entityModel) {
        return null;
    }
}
