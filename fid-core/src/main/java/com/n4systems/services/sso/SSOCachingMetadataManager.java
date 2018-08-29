package com.n4systems.services.sso;


import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.util.resource.ResourceException;
import org.opensaml.xml.parse.ParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.springframework.security.saml.metadata.CachingMetadataManager;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by agrabovskis on 2018-07-11.
 */

public class SSOCachingMetadataManager extends CachingMetadataManager implements Serializable {

    /*@Autowired
    private MetadataServices metadataServices;*/

    /*@Resource(name="parserPool")
    private ParserPool parserPool;*/

    public SSOCachingMetadataManager() throws MetadataProviderException, ResourceException, XMLParserException {
        super(new ArrayList());
    }

    @PostConstruct
    private void init() throws MetadataProviderException, ResourceException, XMLParserException {
       // System.out.println("SSOCachingMetadataManager.init, parserPool " + parserPool);
      /*  List<MetadataProvider> providers = new ArrayList();
        for (SsoIdpMetadataDto idp : metadataServices.getIdps()) {
            final String serializedMetadata = idp.getSerializedMetadata();
            MetadataProvider provider = new DatabaseIdpMetadataProvider(metadataServices, idp.getEntityId(), parserPool) {
                @Override
                protected String getSerializedIdpMetadata() {
                    return serializedMetadata;
                }
            };
            providers.add(provider);
        }
        for (SsoSpMetadataDto sp : metadataServices.getSps()) {
            DatabaseSpMetadataProvider provider = new DatabaseSpMetadataProvider(metadataServices, sp.getEntityId(), parserPool);
            ExtendedMetadataDelegate delegate = new ExtendedMetadataDelegate(provider, provider.getExtendedMetadata(sp.getEntityId()));
            providers.add(delegate);
        }
        setProviders(providers);*/
    }

   /* @Override
    public ExtendedMetadata getExtendedMetadata(String entityID) throws MetadataProviderException {
        ExtendedMetadata result = super.getExtendedMetadata(entityID);
        System.out.println("Attempt to get metadata for " + entityID + " returned " + result);
        return result;
    }

    @Override
    public String getHostedSPName() {
        //return "urn:test:arvid:toronto";
        String spName = super.getHostedSPName();
        System.out.println("HostedSPName from metadataManager is '" + spName + "'");
        return spName;
    }

    @Override
    public void setHostedSPName(String hostedSPName) {
        System.out.println("hostedSPName being set to '" + hostedSPName + "'");
        super.setHostedSPName(hostedSPName);
    }*/
}
