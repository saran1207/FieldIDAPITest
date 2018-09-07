package com.n4systems.fieldid.sso;

import com.n4systems.sso.dao.SsoMetadataDao;
import com.n4systems.model.sso.SsoSpMetadata;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.ParserPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.saml.metadata.ExtendedMetadata;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by agrabovskis on 2018-08-07.
 */
public class DatabaseSpMetadataProvider extends DatabaseMetadataProvider implements MetadataProvider {

    static public Logger logger = LoggerFactory.getLogger(DatabaseSpMetadataProvider.class);

    private SsoMetadataDao ssoMetadataDao;

    public DatabaseSpMetadataProvider(SsoMetadataDao ssoMetadataDao, String entityId, ParserPool parserPool) {
        super(entityId);
        this.ssoMetadataDao = ssoMetadataDao;
        setParserPool(parserPool);
    }

    @Override
    protected XMLObject doGetMetadata() throws MetadataProviderException {
        try {
            String metadataStr = ssoMetadataDao.getSp(getEntityId()).getSerializedMetadata();
            InputStream is = new ByteArrayInputStream(metadataStr.getBytes());
            XMLObject metadata = unmarshallMetadata(is);
            return metadata;
        }
        catch (UnmarshallingException e) {
            String errMsg = "Unable to read IDP metadata";
            logger.error(errMsg, e);
            throw new MetadataProviderException(errMsg, e);
        }
    }

    public ExtendedMetadata getExtendedMetadata(String entityID) throws MetadataProviderException {

        SsoSpMetadata spMetadata = ssoMetadataDao.getSp(entityID);
        ExtendedMetadata extendedMetadata = new ExtendedMetadata();

        extendedMetadata.setLocal(true); // SP is always local
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
        return extendedMetadata;
    }

}
