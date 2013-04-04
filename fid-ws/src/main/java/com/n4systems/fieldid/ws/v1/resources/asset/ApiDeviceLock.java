package com.n4systems.fieldid.ws.v1.resources.asset;

import com.n4systems.fieldid.ws.v1.resources.assettype.attributevalues.ApiAttributeValue;

import java.util.List;

public class ApiDeviceLock {

    private String identifier;
    private String rfidNumber;
    private Long typeId;
    private List<ApiAttributeValue> attributeValues;
    private boolean device;
    private boolean lock;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getRfidNumber() {
        return rfidNumber;
    }

    public void setRfidNumber(String rfidNumber) {
        this.rfidNumber = rfidNumber;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public List<ApiAttributeValue> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(List<ApiAttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
    }

    public boolean isDevice() {
        return device;
    }

    public void setDevice(boolean device) {
        this.device = device;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }
}
