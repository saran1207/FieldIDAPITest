package com.n4systems.services.sso;

import com.n4systems.model.sso.IdpProvidedMetadata;
import com.n4systems.model.sso.SsoIdpMetadata;

/**
 * Created by agrabovskis on 2018-08-28.
 */
public interface SsoMetadataServices {

    public IdpProvidedMetadata getMetadataFromIdp(String idpUrl, int timeout);

    public SsoIdpMetadata addIdp(SsoIdpMetadata idpMetadataDto);

    public void deleteIdp(String entityId);
}
