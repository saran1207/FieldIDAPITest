package com.n4systems.fieldid.ws.v1.resources.event;

/**
 * This class holds a few fields shared by ApiThingEvent and ApiSavedThingEvent.
 *
 * Created by Jordan Heath on 2015-10-22.
 */
public class ApiBaseThingEvent extends ApiBaseEvent {
    private String assetId;
    private Long assetStatusId;

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public Long getAssetStatusId() {
        return assetStatusId;
    }

    public void setAssetStatusId(Long assetStatusId) {
        this.assetStatusId = assetStatusId;
    }
}
