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

    public ApiEventForm getForm() {
        return form;
    }

    public void setForm(ApiEventForm form) {
        this.form = form;
    }
}
