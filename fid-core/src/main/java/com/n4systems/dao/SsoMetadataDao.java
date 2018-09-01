package com.n4systems.dao;

import com.n4systems.model.sso.SsoIdpMetadata;
import com.n4systems.model.sso.SsoSpMetadata;

import java.util.List;

/**
 * Data access object to interfact with the SSO objects in the database.
 */
public interface SsoMetadataDao {

    public SsoIdpMetadata getIdp(String entityId);
    public SsoIdpMetadata getIdpByTenant(long tenantId);
    public List<SsoIdpMetadata> getIdp();
    public SsoIdpMetadata addIdp(SsoIdpMetadata idpMetadata);
    public SsoIdpMetadata updateIdp(SsoIdpMetadata idpMetadata);
    public void deleteIdp(String entityId);

    public SsoSpMetadata getSp(String entityId);
    public SsoSpMetadata getSpByTenant(long tenantId);
    public List<SsoSpMetadata> getSp();
    public SsoSpMetadata addSp(SsoSpMetadata spMetadata);
    public SsoSpMetadata updateSp(SsoSpMetadata spMetadata);
    public void deleteSp(String entityId);
}
