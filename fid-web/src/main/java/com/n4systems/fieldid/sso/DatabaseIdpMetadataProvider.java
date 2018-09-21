package com.n4systems.fieldid.sso;

import com.n4systems.sso.dao.SsoMetadataDao;
import org.apache.log4j.Logger;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.ParserPool;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class DatabaseIdpMetadataProvider extends DatabaseMetadataProvider {

    private static final Logger logger = Logger.getLogger(DatabaseIdpMetadataProvider.class);

    private SsoMetadataDao ssoMetadataDao;

    public DatabaseIdpMetadataProvider(SsoMetadataDao ssoMetadataDao, String entityId, ParserPool parserPool) {
        super(entityId);
        this.ssoMetadataDao = ssoMetadataDao;
        setParserPool(parserPool);
    }

    @Override
    protected XMLObject doGetMetadata() throws MetadataProviderException {

        try {
            String metadataStr = getSerializedIdpMetadata();
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

    protected String getSerializedIdpMetadata() {
        String metadataStr = ssoMetadataDao.getIdpByEntityId(getEntityId()).getSerializedMetadata();
        return metadataStr;
    }
}
