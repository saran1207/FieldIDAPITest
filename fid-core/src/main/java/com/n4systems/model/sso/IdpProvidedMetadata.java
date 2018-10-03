package com.n4systems.model.sso;

import java.io.Serializable;

public class IdpProvidedMetadata implements Serializable {

    private String entityId;
    private String serializedMetadata;

    public IdpProvidedMetadata(String entityId, String serializedMetadata) {
        this.entityId = entityId;
        this.serializedMetadata = serializedMetadata;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getSerializedMetadata() {
        return serializedMetadata;
    }

    public void setSerializedMetadata(String serializedMetadata) {
        this.serializedMetadata = serializedMetadata;
    }
}
