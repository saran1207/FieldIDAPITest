package com.n4systems.fieldid.wicket.pages.setup.sso;

import com.n4systems.sso.dao.SsoMetadataDao;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.sso.SsoSpMetadata;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;

import java.util.HashMap;
import java.util.Map;


public class DisplaySpMetadata extends FieldIDTemplatePage {

    public static final String ENTITY_ID_KEY = "entityId";

    @SpringBean
    private SsoMetadataDao metadataDao;

    public DisplaySpMetadata(final PageParameters parameters) {
        super(parameters);
        String entityId = parameters.get("entityId").toString();
        addComponents(entityId);
    }

    private void addComponents(String entityId) {
        SsoSpMetadata spMetadata = metadataDao.getSp(entityId);

        add(new Label("entityId", Model.of(spMetadata.getSsoEntity().getEntityId())));

        add(new Label("matchOnUserId", Model.of(spMetadata.isMatchOnUserId())));
        add(new Label("userIdMatchAttribute", Model.of(spMetadata.getUserIdAttributeName())));
        add(new Label("matchOnEmailAddress", Model.of(spMetadata.isMatchOnEmailAddress())));
        add(new Label("emailAddressMatchAttribute", Model.of(spMetadata.getEmailAddressAttributeName())));

        add(new Label("entityBaseUrl", Model.of(spMetadata.getEntityBaseURL())));
        add(new Label("alias", Model.of(spMetadata.getAlias())));

        final Map<String, String> securityProfileOptions = new HashMap<String, String>();
        securityProfileOptions.put("metaiop", "MetaIOP");
        securityProfileOptions.put("pkix","PKIX");

        add(new Label("securityProfile", Model.of(securityProfileOptions.get(spMetadata.getSecurityProfile()))));

        final Map<String, String> sslSecurityProfileOptions = new HashMap<String, String>();
        sslSecurityProfileOptions.put("pkix","PKIX");
        sslSecurityProfileOptions.put("metaiop", "MetaIOP");

        add(new Label("sslSecurityProfile", Model.of(sslSecurityProfileOptions.get(spMetadata.getSslSecurityProfile()))));

        final Map<String, String> sslHostnameVerificationOptions = new HashMap();
        sslHostnameVerificationOptions.put("default","Standard hostname verifier");
        sslHostnameVerificationOptions.put("defaultAndLocalhost", "Standard hostname verifier (skips verification for localhost)");
        sslHostnameVerificationOptions.put("strict","Strict hostname verifier");
        sslHostnameVerificationOptions.put("allowAll","Disable hostname verification (allow all)");

        add(new Label("sslHostnameVerification", Model.of(sslHostnameVerificationOptions.get(spMetadata.getSslHostnameVerification()))));

        add(new Label("wantAssertionSigned", Model.of(spMetadata.isWantAssertionSigned())));

        TextArea serializedMetadataField = new TextArea<String>("serializedMetadata", Model.of(spMetadata.getSerializedMetadata()));
        add(serializedMetadataField);

        Link<Void> streamDownloadLink = new Link<Void>("downloadMetadata") {

            @Override
            public void onClick() {

                AbstractResourceStreamWriter rStream = new AbstractResourceStreamWriter() {
                    @Override
                    public void write(Response output) {
                        output.write(spMetadata.getSerializedMetadata());
                    }
                };

                ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(rStream, entityId + "_metadata.xml");
                getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
            }
        };

        add(streamDownloadLink);

        Link backLink = new Link("backLink") {
            public void onClick() {
                getRequestCycle().setResponsePage(SsoSettingsPage.class);
            }
        };
        add(backLink);


    }
}
