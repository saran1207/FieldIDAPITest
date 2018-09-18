package com.n4systems.fieldid.wicket.pages.setup.sso;

import com.n4systems.fieldid.wicket.pages.FieldIDTemplateWithFeedbackPage;
import com.n4systems.model.sso.SsoEntity;
import com.n4systems.model.sso.SsoSpMetadata;
import com.n4systems.fieldid.sso.SsoMetadataServices;
import com.n4systems.sso.dao.SsoDuplicateEntityIdException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Created by agrabovskis on 2018-07-19.
 */
public class GenerateSpMetadata extends FieldIDTemplateWithFeedbackPage {

    static public Logger logger = LoggerFactory.getLogger(GenerateSpMetadata.class);

    @SpringBean
    private SsoMetadataServices metadataServices;

    public GenerateSpMetadata(final PageParameters parameters) {
        super(parameters);
        addComponents();
    }

    private void addComponents() {

        String tenantName = getTenant().getName();
        add(new Label("tenantName", tenantName));

        /* Spring SAML sample app sets initial values for these fields:
            sets entity base URL
            signing key - apollo
            encryption key - apollo
            Signature security profile - MetaIOP
            SSL/TLS security profile - PKIX
            SSL/TLS hostname verification - Standard hostname verifier
            Sign sent AuthNR:requests - Yes
            Single sign-on bindings - SSO HTTP-POST (Default and included), SSO Artifact (included)
            Supported NameIDs (all)
            Enable IDP discovery profile (yes)
         */

        Url originalUrl = getRequest().getOriginalUrl();
        String baseUrl = (originalUrl.getProtocol() + "://" +
                originalUrl.getHost() + getRequest().getContextPath()).toString();

        SsoSpMetadata spMetadata = new SsoSpMetadata();
        spMetadata.setSsoEntity(new SsoEntity(baseUrl));
        spMetadata.setEntityBaseURL(baseUrl);
        spMetadata.setMatchOnUserId(true);
        spMetadata.setUserIdAttributeName("UserID");
        spMetadata.setMatchOnEmailAddress(true);
        spMetadata.setEmailAddressAttributeName("EmailAddress");
        spMetadata.setAlias(getTenant().getName());

        spMetadata.setTenant(getTenant());
        spMetadata.setTlsKey(null);
        spMetadata.setSignMetadata(Boolean.FALSE);
        spMetadata.setSigningAlgorithm(null);
        spMetadata.setRequestSigned(Boolean.FALSE);
        spMetadata.setWantAssertionSigned(Boolean.TRUE);
        spMetadata.setRequireLogoutRequestSigned(Boolean.TRUE);
        spMetadata.setRequireLogoutResponseSigned(Boolean.FALSE);
        spMetadata.setRequireArtifactResolveSigned(Boolean.FALSE);

        spMetadata.setSecurityProfile("metaiop");
        spMetadata.setSslSecurityProfile("pkix");
        spMetadata.setSslHostnameVerification("default");

        Collection<String> nameId = new ArrayList();
        nameId.add("urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress");
        nameId.add("urn:oasis:names:tc:SAML:2.0:nameid-format:transient");
        nameId.add("urn:oasis:names:tc:SAML:2.0:nameid-format:persistent");
        nameId.add("urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified");
        nameId.add("urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName");
        spMetadata.setNameID(nameId);

        Collection<String> bindingsSSO = new ArrayList<>();
        bindingsSSO.add("urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST");
        bindingsSSO.add("urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact");
        spMetadata.setBindingsSSO(bindingsSSO);

        Collection<String> bindingsHoKSSO = new ArrayList();
        spMetadata.setBindingsHoKSSO(bindingsHoKSSO);

        Collection<String> bindingsSLO = new ArrayList();
        spMetadata.setBindingsSLO(bindingsSLO);

        spMetadata.setAssertionConsumerIndex(0);

        spMetadata.setIncludeDiscoveryExtension(false);
        spMetadata.setIdpDiscoveryEnabled(false);
        spMetadata.setIdpDiscoveryURL(null);

        SpMetadataPanel spPanel = new SpMetadataPanel("spMetatdata", spMetadata, false) {
            @Override
            void cancel() {
                getRequestCycle().setResponsePage(SsoSettingsPage.class);
            }
            @Override
            void submitForm(SsoSpMetadata submittedSpMetadata) {
                saveMetadata(submittedSpMetadata);
            }
        };
        add(spPanel);
    }

    private void saveMetadata(SsoSpMetadata spMetadata) {

        try {
            spMetadata.setLocal(true);
            metadataServices.addSp(spMetadata);

            getRequestCycle().setResponsePage(SsoSettingsPage.class);
        }
        catch(SsoDuplicateEntityIdException ex) {
            error("Save of Service Provider failed - duplicate entity id");
        }
    }

}
