package com.n4systems.fieldid.wicket.pages.setup.sso;

import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.sso.SsoEntity;
import com.n4systems.model.sso.SsoSpMetadata;
import com.n4systems.fieldid.sso.SsoMetadataServices;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by agrabovskis on 2018-07-19.
 */
public class GenerateSpMetadata extends FieldIDTemplatePage {

    static public Logger logger = LoggerFactory.getLogger(GenerateSpMetadata.class);

    @SpringBean
    private SsoMetadataServices metadataServices;

    private FeedbackPanel feedbackPanel;

    public GenerateSpMetadata(final PageParameters parameters) {
        super(parameters);
        addComponents();
    }

    private void addComponents() {

        addFeedbackPanel();

        String tenantName = getTenant().getName();
        add(new Label("tenantName", tenantName));

        Link backLink = new Link("backLink") {
            public void onClick() {
                getRequestCycle().setResponsePage(SsoSettingsPage.class);
            }
        };
        add(backLink);

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
                originalUrl.getHost() +
                ":" + originalUrl.getPort() + getRequest().getContextPath()).toString();

        SsoSpMetadata spMetadata = new SsoSpMetadata();
        spMetadata.setSsoEntity(new SsoEntity("urn:test:arvid:sandbox:" + tenantName));
        spMetadata.setEntityBaseURL(baseUrl); //http://localhost:8080/sso-sandbox");
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


        SpMetadataPanel spPanel = new SpMetadataPanel("spMetatdata", spMetadata, false) {
            @Override
            void submitForm(SsoSpMetadata submittedSpMetadata) {
                saveMetadata(submittedSpMetadata);
            }
        };
        add(spPanel);
    }

    private void saveMetadata(SsoSpMetadata spMetadata) {

        spMetadata.setLocal(true);
        metadataServices.addSp(spMetadata);

        getRequestCycle().setResponsePage(SsoSettingsPage.class);
    }

    private void addFeedbackPanel() {
         /* Existing top feedback panel is in the correct place for our messages but doesn't
            get recognized as a feedback panel for our messages so we need to add our own. */
        remove(getTopFeedbackPanel());
        feedbackPanel = new FeedbackPanel("topFeedbackPanel");
        feedbackPanel.add(new AttributeAppender("style", new Model("text-align: center; color:red; padding: 0px 10px"), " "));
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);
    }

}
