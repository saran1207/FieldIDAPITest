package com.n4systems.fieldid.sso;

import com.n4systems.model.sso.IdpProvidedMetadata;
import com.n4systems.model.sso.SsoIdpMetadata;
import com.n4systems.model.sso.SsoSpMetadata;
import com.n4systems.sso.dao.SsoDuplicateEntityIdException;

/**
 * Created by agrabovskis on 2018-08-28.
 */
public interface SsoMetadataServices {

    public IdpProvidedMetadata getMetadataFromIdp(String idpUrl, int timeout);

    public SsoIdpMetadata addIdp(SsoIdpMetadata idpMetadataDto) throws SsoDuplicateEntityIdException;

    public void deleteIdp(String entityId);

    public SsoSpMetadata addSp(SsoSpMetadata spMetadata) throws SsoDuplicateEntityIdException;

    public SsoSpMetadata updateSp(SsoSpMetadata spMetadata) throws SsoDuplicateEntityIdException;

    public void deleteSp(String entityId);
}
