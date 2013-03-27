package com.n4systems.fieldid.ws.v1.resources.procedure;

import com.n4systems.fieldid.ws.v1.resources.model.ApiReadonlyModel;

public class ApiDeviceDescription extends ApiReadonlyModel {

    private Long assetTypeSid;

    public Long getAssetTypeSid() {
        return assetTypeSid;
    }

    public void setAssetTypeSid(Long assetTypeSid) {
        this.assetTypeSid = assetTypeSid;
    }
}
