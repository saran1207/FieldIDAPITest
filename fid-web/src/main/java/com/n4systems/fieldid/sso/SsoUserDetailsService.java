package com.n4systems.fieldid.sso;

import com.n4systems.model.sso.SsoSpMetadata;
import com.n4systems.sso.dao.SsoMetadataDao;
import org.apache.log4j.Logger;
import org.opensaml.saml2.core.Attribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;

import java.util.List;

/**
 * Take the SAMLCredential object provided by Spring SAML and extract the userid and/or email address object required
 * for further validation and logon.
 */
public class SsoUserDetailsService implements SAMLUserDetailsService {

    private static final Logger logger = Logger.getLogger(SsoUserDetailsService.class);

    @Autowired
    private SsoMetadataDao ssoMetadataDao;

    @Override
    public Object loadUserBySAML(SAMLCredential credential) throws UsernameNotFoundException {

        String spEntityId = credential.getLocalEntityID();
        SsoSpMetadata spMetadata = ssoMetadataDao.getSpByEntityId(spEntityId);
        if (spMetadata == null)
            throw new ProviderNotFoundException(
                    "Incoming SSO authentication request referenced non existent service provider '" + spEntityId + "'");

        boolean missingAttributes = false;
        String userId = null;
        if (spMetadata.isMatchOnUserId()) {
            userId = credential.getAttributeAsString(spMetadata.getUserIdAttributeName());
            if (userId == null)
                missingAttributes = true;
        }

        String emailAddress = null;
        if (spMetadata.isMatchOnEmailAddress()) {
            emailAddress = credential.getAttributeAsString(spMetadata.getEmailAddressAttributeName());
            if (emailAddress == null)
                missingAttributes = true;
        }

        if (missingAttributes) {
            /* Display the attribute names in the log to make for easier debugging */
            List<Attribute> attributes = credential.getAttributes();
            String msg = "SsoUserDetailsService received " + attributes.size() + " attributes";
            for (Attribute attribute: attributes) {
                msg+= ", Attribute: '" + attribute.getName() + "'";
            }
            logger.error(msg);
        }

        return new SamlUserDetails(spMetadata.getTenant().getName(), userId, emailAddress, credential.getLocalEntityID(), credential.getRemoteEntityID(), false);
    }
}
