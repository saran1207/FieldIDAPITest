package com.n4systems.fieldid.ws.v2.resources.customerdata.proceduredefinition;

import com.n4systems.fieldid.ws.v2.resources.customerdata.asset.attributevalues.ApiAttributeValue;
import com.n4systems.fieldid.ws.v2.resources.model.ApiReadonlyModel;

import java.util.List;

public class ApiDeviceDescription extends ApiReadonlyModel {

    private String freeformDescription;
    private Long assetTypeSid;
    private List<ApiAttributeValue> attributes;

    public Long getAssetTypeSid() {
        return assetTypeSid;
    }

    public void setAssetTypeSid(Long assetTypeSid) {
        this.assetTypeSid = assetTypeSid;
    }

    public String getFreeformDescription() {
        return freeformDescription;
    }

    public void setFreeformDescription(String freeformDescription) {
        this.freeformDescription = freeformDescription;
    }

    public List<ApiAttributeValue> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ApiAttributeValue> attributes) {
        this.attributes = attributes;
    }
}
