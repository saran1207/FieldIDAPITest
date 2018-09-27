package com.n4systems.fieldid.sso;

import com.n4systems.sso.dao.SsoDuplicateEntityIdException;
import com.n4systems.sso.dao.SsoMetadataDao;
import com.n4systems.model.sso.IdpProvidedMetadata;
import com.n4systems.model.sso.SsoIdpMetadata;
import com.n4systems.model.sso.SsoSpMetadata;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.provider.HTTPMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.parse.ParserPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.saml.key.KeyManager;
import org.springframework.security.saml.metadata.ExtendedMetadata;
import org.springframework.security.saml.metadata.ExtendedMetadataDelegate;
import org.springframework.security.saml.metadata.MetadataGenerator;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.security.saml.util.SAMLUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
    public SsoIdpMetadata addIdp(SsoIdpMetadata idpMetadata) throws SsoDuplicateEntityIdException {
        ssoMetadataDao.addIdp(idpMetadata);
        loadIdpProvider(idpMetadata.getSsoEntity().getEntityId());
        logger.info("Added IDP '" + idpMetadata.getSsoEntity().getEntityId() + "' for tenant " +
                idpMetadata.getTenant().getName());
        return idpMetadata;
    }

    @Override
    @Transactional
    public void deleteIdp(String entityId) {
        unloadProvider(entityId);
        ssoMetadataDao.deleteIdpByEntityId(entityId);
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
        HTTPMetadataProvider provider = new HTTPMetadataProvider(new Timer(true), httpClient, idpUrl);
        provider.setParserPool(parserPool);
        provider.initialize();
        logger.info("Obtaining IDP metadata from " + provider.getMetadataURI());
        EntityDescriptor entityDescriptor = (EntityDescriptor) provider.getMetadata();
        provider.destroy();
        return entityDescriptor;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SsoSpMetadata addSp(SsoSpMetadata spMetadata) throws SsoDuplicateEntityIdException {
        spMetadata.setSerializedMetadata(generateSp(spMetadata));
        ssoMetadataDao.addSp(spMetadata);
        loadSpProvider(spMetadata.getSsoEntity().getEntityId());
        return spMetadata;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SsoSpMetadata updateSp(SsoSpMetadata spMetadata) throws SsoDuplicateEntityIdException {
        SsoSpMetadata originalSp = ssoMetadataDao.getSpById(spMetadata.getId());
        unloadProvider(originalSp.getSsoEntity().getEntityId());
        deleteSp(originalSp.getSsoEntity().getEntityId());
        return addSp(spMetadata);
    }

    @Override
    @Transactional
    public void deleteSp(String entityId) {
        unloadProvider(entityId);
        ssoMetadataDao.deleteSpByEntityId(entityId);
    }

    private String generateSp(SsoSpMetadata spMetadata) {

        MetadataGenerator generator = new MetadataGenerator();

        generator.setAssertionConsumerIndex(spMetadata.getAssertionConsumerIndex());
        generator.setBindingsHoKSSO(spMetadata.getBindingsHoKSSO());
        generator.setBindingsSLO(spMetadata.getBindingsSLO());
        generator.setBindingsSSO(spMetadata.getBindingsSSO());
        generator.setEntityBaseURL(spMetadata.getEntityBaseURL());
        generator.setEntityId(spMetadata.getSsoEntity().getEntityId());
        generator.setIncludeDiscoveryExtension(spMetadata.isIncludeDiscoveryExtension());
        generator.setKeyManager(keyManager);
        generator.setNameID(spMetadata.getNameID());
        generator.setRequestSigned(spMetadata.isRequestSigned());
        generator.setWantAssertionSigned(spMetadata.isWantAssertionSigned());

        ExtendedMetadata extendedMetadata = new ExtendedMetadata();

        extendedMetadata.setSigningKey(spMetadata.getSigningKey());
        extendedMetadata.setEncryptionKey(spMetadata.getEncryptionKey());
        extendedMetadata.setTlsKey(spMetadata.getTlsKey());
        extendedMetadata.setIdpDiscoveryEnabled(spMetadata.isIdpDiscoveryEnabled());
        extendedMetadata.setIdpDiscoveryURL(spMetadata.getIdpDiscoveryURL());
        extendedMetadata.setIdpDiscoveryResponseURL(spMetadata.getIdpDiscoveryResponseURL());
        extendedMetadata.setAlias(spMetadata.getAlias());
        extendedMetadata.setSecurityProfile(spMetadata.getSecurityProfile());
        extendedMetadata.setSslSecurityProfile(spMetadata.getSslSecurityProfile());
        extendedMetadata.setRequireLogoutRequestSigned(spMetadata.isRequireLogoutRequestSigned());
        extendedMetadata.setRequireLogoutResponseSigned(spMetadata.isRequireLogoutResponseSigned());
        extendedMetadata.setRequireArtifactResolveSigned(spMetadata.isRequireArtifactResolveSigned());
        extendedMetadata.setSslHostnameVerification(spMetadata.getSslHostnameVerification());
        extendedMetadata.setSignMetadata(spMetadata.isSignMetadata());
        extendedMetadata.setSigningAlgorithm(spMetadata.getSigningAlgorithm());

        generator.setExtendedMetadata(extendedMetadata);

        final EntityDescriptor generatedDescriptor = generator.generateMetadata();
        final ExtendedMetadata generatedExtendedMetadata = generator.generateExtendedMetadata();

        String serializedMetadata = "";
        try {
            serializedMetadata = SAMLUtil.getMetadataAsString(metadataManager, keyManager, generatedDescriptor, generatedExtendedMetadata);
        }
        catch(MarshallingException ex) {
            logger.error("Attempt to get serialized metadata failed",ex);
            throw new RuntimeException(ex);
        }
        return serializedMetadata;
    }

    @Override
    public void loadIdpProvider(String entityId) {
        try {
            unloadProvider(entityId); // Shouldn't already exist except for error condition
            MetadataProvider provider = new DatabaseIdpMetadataProvider(
                    ssoMetadataDao,
                    entityId,
                    parserPool);
            ((DatabaseIdpMetadataProvider) provider).initialize();
            metadataManager.addMetadataProvider(provider);
            metadataManager.setRefreshRequired(true);
            metadataManager.refreshMetadata();
        }
        catch (MetadataProviderException ex) {
            logger.error("Unable to add provider '" + entityId + "'", ex);
            /* This is not an expected/recoverable exception so show user an Oops page */
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void loadSpProvider(String entityId) {
        try {
            unloadProvider(entityId);
            DatabaseSpMetadataProvider provider =
                new DatabaseSpMetadataProvider(ssoMetadataDao, entityId, parserPool);
            ExtendedMetadataDelegate delegate =
                new ExtendedMetadataDelegate(provider, provider.getExtendedMetadata(entityId));
            delegate.initialize();
            metadataManager.addMetadataProvider(delegate);
            metadataManager.setRefreshRequired(true);
            metadataManager.refreshMetadata();

        }
        catch(MetadataProviderException ex) {
            logger.error("Unable to add SP metadata",ex);
            /* This is not an expected/recoverable exception so give user an Oops page */
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean unloadProvider(String entityId) {
        Iterator<MetadataProvider> iter = metadataManager.getProviders().iterator();
        while (iter.hasNext()) {
            MetadataProvider provider = iter.next();
            try {
                if (provider.getEntityDescriptor(entityId) != null) {
                    metadataManager.removeMetadataProvider(provider);
                    metadataManager.setRefreshRequired(true);
                    metadataManager.refreshMetadata();
                    logger.info("Deleted provider '" + entityId + "'");
                    return true;
                }
            }
            catch(MetadataProviderException ex) {
                logger.error("Unable to delete provider", ex);
                /* This is not an expected error so show user an Oops error */
                throw new RuntimeException(ex);
            }
        }
        return false;
    }
}
