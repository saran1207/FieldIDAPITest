package com.n4systems.fieldid.ws.v1.resources.savedEvent;

import com.n4systems.fieldid.ws.v1.resources.event.ApiBasePlaceEvent;
import com.n4systems.fieldid.ws.v1.resources.eventtype.ApiEventForm;

/**
 * This is the Saved Place Event model, which really just serves to hold the Criteria half of the Event Form.
 *
 * Sorry, Mario, the Criteria Results are in another castle.
 *
 * Created by Jordan Heath on 2015-10-22.
 */
public class ApiSavedPlaceEvent extends ApiBasePlaceEvent {
    private ApiEventForm form;
    private String eventTypeName;
    private String ownerDisplayName;

    public ApiEventForm getForm() {
        return form;
    }

    public void setForm(ApiEventForm form) {
        this.form = form;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public String getOwnerDisplayName() {
        return ownerDisplayName;
    }

    public void setOwnerDisplayName(String ownerDisplayName) {
        this.ownerDisplayName = ownerDisplayName;
    }
}
