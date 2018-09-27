package com.n4systems.sso.dao;

import com.n4systems.model.sso.SsoGlobalSettings;
import com.n4systems.model.sso.SsoIdpMetadata;
import com.n4systems.model.sso.SsoSpMetadata;

import java.util.List;

/**
 * Data access object to interact with the SSO objects in the database.
 */
public interface SsoMetadataDao {

    public SsoIdpMetadata getIdpByEntityId(String entityId);
    public SsoIdpMetadata getIdpByTenant(long tenantId);
    public List<SsoIdpMetadata> getAllIdp();
    public SsoIdpMetadata addIdp(SsoIdpMetadata idpMetadata) throws SsoDuplicateEntityIdException;
    public void deleteIdpByEntityId(String entityId);

    public SsoSpMetadata getSpById(Long id);
    public SsoSpMetadata getSpByEntityId(String entityId);
    public SsoSpMetadata getSpByTenant(long tenantId);
    public List<SsoSpMetadata> getAllSp();
    public SsoSpMetadata addSp(SsoSpMetadata spMetadata) throws SsoDuplicateEntityIdException;
    public void deleteSpById(Long id);
    public void deleteSpByEntityId(String entityId);

    public SsoGlobalSettings getSsoGlobalSettings();
    public SsoGlobalSettings updateSsoGlobalSettings(SsoGlobalSettings ssoGlobalSettings);
}
