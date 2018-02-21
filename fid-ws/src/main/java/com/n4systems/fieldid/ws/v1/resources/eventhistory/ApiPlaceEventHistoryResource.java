package com.n4systems.fieldid.ws.v1.resources.eventhistory;

import com.n4systems.fieldid.ws.v1.resources.eventtype.ApiPlaceEventTypeResource;
import com.n4systems.fieldid.ws.v1.resources.savedEvent.ApiSavedPlaceEventResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.stream.Collectors;

/**
 * This service returns the event history for one Place/Org.
 *
 * Created by Jordan Heath on 2015-10-23.
 */
@Component
@Path("placeEventHistory")
public class ApiPlaceEventHistoryResource {

    @Autowired
    private ApiPlaceEventInfoResource placeEventResource;

    @Autowired
    private ApiSavedPlaceEventResource savedPlaceEventResource;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ApiPlaceEventHistory findAllEventHistory(@QueryParam("id") Long placeId) {

        ApiPlaceEventHistory apiEventHistory = new ApiPlaceEventHistory();

        apiEventHistory.setEventTypes(placeEventResource.getPlaceEventTypes(placeId));
        apiEventHistory.setEventHistory(placeEventResource.getPlaceEvents(placeId));
        apiEventHistory.setEvents(savedPlaceEventResource.findLastEventOfEachType(placeId));
        apiEventHistory.setSchedules(savedPlaceEventResource.findAllOpenEvents(placeId));
        return apiEventHistory;
    }
}
