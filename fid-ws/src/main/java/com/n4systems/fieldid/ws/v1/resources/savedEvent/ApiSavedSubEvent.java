package com.n4systems.fieldid.ws.v1.resources.savedEvent;

/**
 * Created by rrana on 2016-02-16.
 */
public class ApiSavedSubEvent extends ApiSavedEvent {

    private String masterEventSid;

    public String getMasterEventSid() {
        return masterEventSid;
    }

    public void setMasterEventSid(String masterEventSid) {
        this.masterEventSid = masterEventSid;
    }
}
