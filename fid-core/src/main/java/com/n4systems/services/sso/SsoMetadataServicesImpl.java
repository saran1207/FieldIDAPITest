package com.n4systems.services.sso;

import com.n4systems.dao.SsoMetadataDao;
import com.n4systems.model.sso.IdpProvidedMetadata;
import com.n4systems.model.sso.SsoIdpMetadata;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.provider.HTTPMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.parse.ParserPool;
import org.opensaml.xml.parse.StaticBasicParserPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.saml.key.KeyManager;
import org.springframework.security.saml.metadata.ExtendedMetadata;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.security.saml.util.SAMLUtil;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Timer;

/**
 * Provide various services for SSO metadata
 */
@Component
public class SsoMetadataServicesImpl implements SsoMetadataServices {

    private static final Logger logger = Logger.getLogger(SsoMetadataServicesImpl.class);

    @Autowired
    private SsoMetadataDao ssoMetadataDao;

    @Autowired
    private MetadataManager metadataManager;

    @Autowired
    private ParserPool parserPool;


    @Autowired
    private KeyManager keyManager;

    @Override
    public SsoIdpMetadata addIdp(SsoIdpMetadata idpMetadata) {

        ssoMetadataDao.addIdp(idpMetadata);

        try {
            MetadataProvider provider = new DatabaseIdpMetadataProvider(
                    ssoMetadataDao,
                    idpMetadata.getSsoEntity().getEntityId(),
                    parserPool);
            ((DatabaseIdpMetadataProvider) provider).initialize();
            metadataManager.addMetadataProvider(provider);
            metadataManager.setRefreshRequired(true);
            metadataManager.refreshMetadata();
        }
        catch (MetadataProviderException ex) {
            logger.error("Unable to add IDP", ex);
            /* This is not an expected exception so show user an Oops error */
            throw new RuntimeException(ex);
        }
        return idpMetadata;
    }

    @Override
    public void deleteIdp(String entityId) {

        Iterator<MetadataProvider> iter = metadataManager.getProviders().iterator();
        while (iter.hasNext()) {
            MetadataProvider provider = iter.next();
            System.out.println("Looking at provider: " + provider);
            try {
                if (provider.getEntityDescriptor(entityId) != null) {
                    System.out.println("... its descriptor: " + provider.getEntityDescriptor(entityId));
                    metadataManager.removeMetadataProvider(provider);
                    metadataManager.setHostedSPName(null);
                    ssoMetadataDao.deleteIdp(entityId);
                    metadataManager.setRefreshRequired(true);
                    metadataManager.refreshMetadata();
                }
            }
            catch(MetadataProviderException ex) {
                logger.error("Unable to delete IDP", ex);
                /* This is not an expected error so show user an Oops error */
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public IdpProvidedMetadata getMetadataFromIdp(String idpUrl, int timeout) {
        try {
            EntityDescriptor entityDescriptor = getIdpMetadata(idpUrl, timeout);
            String serializedMetadata = SAMLUtil.getMetadataAsString(metadataManager, keyManager, entityDescriptor,
                    new ExtendedMetadata());
            return new IdpProvidedMetadata(entityDescriptor.getEntityID(), serializedMetadata);
        }
        catch (MarshallingException | MetadataProviderException ex) {
            logger.error("Attempt to get serialized metadata failed on ", ex);
            return null;
        }
    }

    private EntityDescriptor getIdpMetadata(String idpUrl, int requestTimeout) throws MetadataProviderException {

        HttpClientParams clientParams = new HttpClientParams();
        clientParams.setSoTimeout(requestTimeout);
        HttpClient httpClient = new HttpClient(clientParams);
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(requestTimeout);
        HTTPMetadataProvider provider = new HTTPMetadataProvider(new Timer(true), httpClient, idpUrl + "/idp-meta.xml");
        provider.setParserPool(parserPool);
        provider.initialize();
        System.out.println("IDP Metadata URI:" + provider.getMetadataURI());
        EntityDescriptor entityDescriptor = provider.getEntityDescriptor(idpUrl);
        System.out.println("entity descriptor: " + entityDescriptor.toString());
        provider.destroy();
        return entityDescriptor;
    }
}
