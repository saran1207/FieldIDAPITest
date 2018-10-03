package com.n4systems.fieldid.sso;

import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.sso.SsoIdpMetadata;
import com.n4systems.model.sso.SsoSpMetadata;
import com.n4systems.model.user.User;
import com.n4systems.sso.dao.SsoMetadataDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLAuthenticationToken;
import org.springframework.security.saml.context.SAMLMessageContext;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import java.util.List;

public class ApplicationAuthenticationProvider extends SAMLAuthenticationProvider {

    private static final Logger logger = Logger.getLogger(ApplicationAuthenticationProvider.class);

    @Autowired
    private SsoMetadataDao ssoMetadataDao;

    @Autowired
    private UserService userService;

    @Autowired
    private OrgService orgService;

    @Autowired
    private TenantSettingsService tenantSettingsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.info("SSO authenticate request received " + ((authentication != null) ? authentication.getName() : ""));
        Authentication authenticatedBySaml = super.authenticate(authentication);
        verifyUserExistsInFieldId(authentication, (SamlUserDetails) authenticatedBySaml.getDetails());
        return authenticatedBySaml;
    }

    private void verifyUserExistsInFieldId(Authentication authentication, SamlUserDetails userDetails) {
        SAMLAuthenticationToken token = (SAMLAuthenticationToken) authentication;
        SAMLMessageContext context = token.getCredentials();
        String localEntityId = context.getLocalEntityId(); /* Service Provider entity id */
        String peerEntityId = context.getPeerEntityId(); /* IDP entity id */
        SsoSpMetadata ssoSpMetadata = ssoMetadataDao.getSpByEntityId(localEntityId);
        if (ssoSpMetadata == null)
            throw new ProviderNotFoundException(
                    "Incoming SSO authentication request referenced non existent service provider '" + localEntityId + "'");

        /* Verify that this authentication request is coming from the IDP associated with this SP */
        Tenant expectedTenant = ssoSpMetadata.getTenant();
        SsoIdpMetadata ssoIdpMetadata = ssoMetadataDao.getIdpByTenant(expectedTenant.getId());
        if (!peerEntityId.equals(ssoIdpMetadata.getSsoEntity().getEntityId()))
            throw new SessionAuthenticationException("Incoming SSO authentication request for SP '" + localEntityId +
                    "' should be from IDP '" + ssoIdpMetadata.getSsoEntity().getEntityId() +
                    "' but it is instead from IDP '" + peerEntityId + "'");

        /* Verify that this tenant is allowed to use SSO */
        PrimaryOrg primaryOrg = orgService.getPrimaryOrgForTenantNoSecurityFilter(expectedTenant.getId());
        if (primaryOrg != null) {
            if (!primaryOrg.hasExtendedFeature(ExtendedFeature.SSO))
                throw new SessionAuthenticationException("Incoming SSO authentication request for SP '" + localEntityId +
                        "' from IDP '" + ssoIdpMetadata.getSsoEntity().getEntityId() +
                        "' but tenant '" + expectedTenant.getName() + "' does not have SSO extended feature");
        }
        else
            throw new SessionAuthenticationException("Incoming SSO authentication request for SP '" + localEntityId +
                    "' from IDP '" + ssoIdpMetadata.getSsoEntity().getEntityId() +
                    "' but tenant '" + expectedTenant.getName() + "' has no primary org");

        /* Verify tenant has SSO set as active */
        if (!tenantSettingsService.getTenantSettings(expectedTenant.getId()).isSsoEnabled())
            throw new SessionAuthenticationException("Incoming SSO authentication request for SP '" + localEntityId +
                    "' from IDP '" + ssoIdpMetadata.getSsoEntity().getEntityId() +
                    "' but tenant '" + expectedTenant.getName() + "' does not have SSO activated in SSO settings");

        /* Get the corresponding FieldId user (if it exists) */
        User fieldIdUser;
        if (ssoSpMetadata.isMatchOnUserId() && ssoSpMetadata.isMatchOnEmailAddress())
            fieldIdUser = userService.findUserByUserIDAndEmailAddress(expectedTenant.getName(), userDetails.getUserId(), userDetails.getEmailAddress());
        else
        if (ssoSpMetadata.isMatchOnUserId())
            fieldIdUser = userService.findUserByUserID(expectedTenant.getName(), userDetails.getUserId());
        else
        if (ssoSpMetadata.isMatchOnEmailAddress()) {
            List<User> users = userService.findUsersByEmailAddress(expectedTenant.getName(), userDetails.getEmailAddress());
            /* Want single match, if multiple users have the same email its not a match */
            if (users != null && users.size() == 1) {
                fieldIdUser = users.get(0);
                userDetails.setUserId(fieldIdUser.getUserID());
            }
            else
                fieldIdUser = null;
        }
        else
            throw new SessionAuthenticationException("Incoming SSO authentication request for SP '" + localEntityId +
                    "' has no criteria to match with user in FieldId");

        if (fieldIdUser == null)
            throw new SessionAuthenticationException("Incoming SSO authentication request for SP '" + localEntityId +
                    "' tenant '" + expectedTenant.getName() +
                    "' userId '" + userDetails.getUserId() + "', email '" + userDetails.getEmailAddress() +
                    "' has no match in FieldId");

        userDetails.setUserIdInFieldId(fieldIdUser.getId());

        if (fieldIdUser.isLocked())
            throw new SessionAuthenticationException("Incoming SSO authentication request for SP '" + localEntityId +
                "' tenant '" + expectedTenant.getName() +
                "' userId '" + userDetails.getUserId() + "', email '" + userDetails.getEmailAddress() +
                "' was matched in FieldIs but the userid is locked");

        if (!fieldIdUser.isActive())
            throw new SessionAuthenticationException("Incoming SSO authentication request for SP '" + localEntityId +
                    "' tenant '" + expectedTenant.getName() +
                    "' userId '" + userDetails.getUserId() + "', email '" + userDetails.getEmailAddress() +
                    "' was matched in FieldIs but the userid is not active");

        userDetails.setAuthenticated(true);
        logger.info("Incoming SSO authentication request for SP '" + localEntityId +
                "' tenant '" + expectedTenant.getName() +
                "' userId '" + userDetails.getUserId() + "', email '" + userDetails.getEmailAddress() +
                "' was matched in FieldId");
    }
}
