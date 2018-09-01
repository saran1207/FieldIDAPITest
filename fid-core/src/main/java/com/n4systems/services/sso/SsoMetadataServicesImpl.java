package com.n4systems.services.sso;

import com.n4systems.dao.SsoMetadataDao;
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
import org.opensaml.xml.parse.StaticBasicParserPool;
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
    @Transactional
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
            logger.info("Added IDP '" + idpMetadata.getSsoEntity().getEntityId() + "' for tenant " +
                idpMetadata.getTenant().getName());
        }
        catch (MetadataProviderException ex) {
            logger.error("Unable to add IDP", ex);
            /* This is not an expected exception so show user an Oops error */
            throw new RuntimeException(ex);
        }
        return idpMetadata;
    }

    @Override
    @Transactional
    public void deleteIdp(String entityId) {

        Iterator<MetadataProvider> iter = metadataManager.getProviders().iterator();
        while (iter.hasNext()) {
            MetadataProvider provider = iter.next();
            try {
                if (provider.getEntityDescriptor(entityId) != null) {
                    metadataManager.removeMetadataProvider(provider);
                    //metadataManager.setHostedSPName(null);
                    ssoMetadataDao.deleteIdp(entityId);
                    metadataManager.setRefreshRequired(true);
                    metadataManager.refreshMetadata();
                    logger.info("Deleted IDP '" + entityId + "'");
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

    @Override
    public SsoSpMetadata addSp(SsoSpMetadata spMetadata) {
        spMetadata.setSerializedMetadata(generateSp(spMetadata));

        try {
            ssoMetadataDao.addSp(spMetadata);

            DatabaseSpMetadataProvider provider =
                    new DatabaseSpMetadataProvider(ssoMetadataDao, spMetadata.getSsoEntity().getEntityId(), parserPool);
            ExtendedMetadataDelegate delegate =
                    new ExtendedMetadataDelegate(provider, provider.getExtendedMetadata(spMetadata.getSsoEntity().getEntityId()));
            delegate.initialize();
            metadataManager.addMetadataProvider(delegate);
            metadataManager.setHostedSPName(spMetadata.getSsoEntity().getEntityId());
            metadataManager.setRefreshRequired(true);
            metadataManager.refreshMetadata();
            logger.info("Added SP '" + spMetadata.getSsoEntity().getEntityId() + "' for tenant " +
                    spMetadata.getTenant().getName());
        }
        catch(MetadataProviderException ex) {
            logger.error("Unable to add SP metadata",ex);
            throw new RuntimeException(ex);
        }

        return spMetadata;
    }

    @Override
    @Transactional
    public SsoSpMetadata updateSp(SsoSpMetadata spMetadata) {
        logger.info("Revising SP '" + spMetadata.getSsoEntity().getEntityId() + "' for tenant '" + spMetadata.getTenant().getName() + "'");
        deleteSp(spMetadata.getSsoEntity().getEntityId());
        addSp(spMetadata);
        return spMetadata;
    }

    @Override
    @Transactional
    public void deleteSp(String entityId) {
        Iterator<MetadataProvider> iter = metadataManager.getProviders().iterator();
        while (iter.hasNext()) {
            MetadataProvider provider = iter.next();
            try {
                if (provider.getEntityDescriptor(entityId) != null) {
                    metadataManager.removeMetadataProvider(provider);
                    //metadataManager.setHostedSPName(null);
                    ssoMetadataDao.deleteSp(entityId);
                    metadataManager.setRefreshRequired(true);
                    metadataManager.refreshMetadata();
                    logger.info("Deleted SP '" + entityId + "'");
                }
            }
            catch(MetadataProviderException ex) {
                logger.error("Unable to delete SP", ex);
                /* This is not an expected error so show user an Oops error */
                throw new RuntimeException(ex);
            }
        }
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
}
