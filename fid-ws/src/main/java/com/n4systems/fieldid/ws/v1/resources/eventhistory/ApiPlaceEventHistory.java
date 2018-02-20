package com.n4systems.fieldid.ws.v1.resources.eventhistory;

import com.n4systems.fieldid.ws.v1.resources.savedEvent.ApiSavedPlaceEvent;

import java.util.List;

/**
 * Created by Jordan Heath on 2015-10-23.
 */
public class ApiPlaceEventHistory {
    private List<ApiPlaceEventInfo> placeEvents;
    private List<ApiSavedPlaceEvent> events;
    private List<ApiSavedPlaceEvent> schedules;

    public List<ApiPlaceEventInfo> getPlaceEvents() {
        return placeEvents;
    }

    public void setPlaceEvents(List<ApiPlaceEventInfo> placeEvents) {
        this.placeEvents = placeEvents;
    }

    public List<ApiSavedPlaceEvent> getEvents() {
        return events;
    }

    public void setEvents(List<ApiSavedPlaceEvent> events) {
        this.events = events;
    }

    public List<ApiSavedPlaceEvent> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<ApiSavedPlaceEvent> schedules) {
        this.schedules = schedules;
    }

}
