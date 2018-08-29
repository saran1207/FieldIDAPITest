package com.n4systems.dao;

import com.n4systems.model.sso.SsoIdpMetadata;

/**
 * Data access object to interfact with the SSO objects in the database.
 */
public interface SsoMetadataDao {

    public SsoIdpMetadata getIdp(String entityId);
    public SsoIdpMetadata getIdpByTenant(long tenantId);
    public SsoIdpMetadata addIdp(SsoIdpMetadata idpMetadata);
    public SsoIdpMetadata updateIdp(SsoIdpMetadata idpMetadata);
    public void deleteIdp(String entityId);
}
