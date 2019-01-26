package com.n4systems.fieldid.ws.v1.resources.eventhistory;

import com.n4systems.fieldid.ws.v1.resources.FieldIdPersistenceServiceWithNewRelicLogging;
import com.n4systems.fieldid.ws.v1.resources.eventtype.ApiPlaceEventTypeResource;
import com.n4systems.fieldid.ws.v1.resources.savedEvent.ApiSavedPlaceEventResource;
import com.newrelic.api.agent.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * This service returns the event history for one Place/Org.
 *
 * Created by Jordan Heath on 2015-10-23.
 */
@Component
@Path("placeEventHistory")
public class ApiPlaceEventHistoryResource extends FieldIdPersistenceServiceWithNewRelicLogging {

    @Autowired
    private ApiPlaceEventInfoResource placeEventResource;

    @Autowired
    private ApiSavedPlaceEventResource savedPlaceEventResource;

    @Autowired
    private ApiPlaceEventTypeResource placeEventTypeResource;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Trace  (dispatcher=true)
    public ApiPlaceEventHistory findAllEventHistory(@QueryParam("id") Long placeId) {

        setNewRelicWithAppInfoParameters();
        ApiPlaceEventHistory apiEventHistory = new ApiPlaceEventHistory();

        apiEventHistory.setEventHistory(placeEventResource.getPlaceEvents(placeId));
        apiEventHistory.setEvents(savedPlaceEventResource.findLastEventOfEachType(placeId));
        apiEventHistory.setSchedules(savedPlaceEventResource.findAllOpenEvents(placeId));
        apiEventHistory.setEventTypes(placeEventTypeResource.getPlaceEventTypes(placeId));
        return apiEventHistory;
    }
}
