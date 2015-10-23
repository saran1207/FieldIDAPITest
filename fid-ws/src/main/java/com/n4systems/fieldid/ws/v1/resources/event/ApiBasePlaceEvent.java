package com.n4systems.fieldid.ws.v1.resources.event;

/**
 * This is the base class for Place Events, holding the ID field of the Place/BaseOrg.  Both ApiPlaceEvent and
 * ApiSavedPlaceEvent extend this class.
 *
 * Created by Jordan Heath on 2015-10-22.
 */
public abstract class ApiBasePlaceEvent extends ApiBaseEvent {
    private Long placeId;

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }
}
