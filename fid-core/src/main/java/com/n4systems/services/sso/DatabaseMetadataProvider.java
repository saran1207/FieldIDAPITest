package com.n4systems.services.sso;

import org.opensaml.saml2.metadata.provider.AbstractMetadataProvider;

abstract public class DatabaseMetadataProvider  extends AbstractMetadataProvider {

    final private String entityId;

    public DatabaseMetadataProvider(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityId() {
        return entityId;
    }
}
