package com.n4systems.fieldid.sso;


import com.n4systems.sso.dao.SsoMetadataDao;
import com.n4systems.model.sso.SsoIdpMetadata;
import com.n4systems.model.sso.SsoSpMetadata;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.util.resource.ResourceException;
import org.opensaml.xml.parse.ParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.saml.metadata.CachingMetadataManager;
import org.springframework.security.saml.metadata.ExtendedMetadataDelegate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The main manager for the IDP and SP definitions used by the Spring SAML framework. All IDP's and SP's
 * defined here are the live objects accessed directly by the framework.
 */

public class SSOCachingMetadataManager extends CachingMetadataManager implements Serializable {

    static public Logger logger = LoggerFactory.getLogger(SSOCachingMetadataManager.class);

    @Autowired
    private SsoMetadataDao ssoMetadataDao;

    @Resource(name="parserPool")
    private ParserPool parserPool;

    public SSOCachingMetadataManager() throws MetadataProviderException, ResourceException, XMLParserException {
        super(new ArrayList());
    }

    /**
     * Take the IDP and SP definitions stored in the database and make them live objects used by the Spring SAML framework.
     * @throws MetadataProviderException
     * @throws ResourceException
     * @throws XMLParserException
     */
    @PostConstruct
    private void init() throws MetadataProviderException, ResourceException, XMLParserException {
        List<MetadataProvider> providers = new ArrayList();
        for (SsoIdpMetadata idp : ssoMetadataDao.getIdp()) {
            try {
                final String serializedMetadata = idp.getSerializedMetadata();
                MetadataProvider provider = new DatabaseIdpMetadataProvider(ssoMetadataDao, idp.getSsoEntity().getEntityId(), parserPool) {
                    @Override
                    protected String getSerializedIdpMetadata() {
                        return serializedMetadata;
                    }
                };
                providers.add(provider);
                logger.info("Added SSO IDP '" + idp.getSsoEntity().getEntityId() + "' for tenant '" + idp.getTenant().getName() + "'");
            }
            catch(Exception ex) {
                logger.error("Attempt to add SSO IDP '" + idp.getSsoEntity().getEntityId() + "' failed", ex);
            }
        }
        for (SsoSpMetadata sp : ssoMetadataDao.getSpByEntityId()) {
            try {
                DatabaseSpMetadataProvider provider = new DatabaseSpMetadataProvider(ssoMetadataDao, sp.getSsoEntity().getEntityId(), parserPool);
                ExtendedMetadataDelegate delegate = new ExtendedMetadataDelegate(provider, provider.getExtendedMetadata(sp.getSsoEntity().getEntityId()));
                providers.add(delegate);
                logger.info("Added SSO SP '" + sp.getSsoEntity().getEntityId() + "' for tenant '" + sp.getTenant().getName() + "'");
            }
            catch(Exception ex) {
                logger.error("Attempt to add SSO SP '" + sp.getSsoEntity().getEntityId() + "' failed", ex);
            }
        }
        setProviders(providers);
    }

}
