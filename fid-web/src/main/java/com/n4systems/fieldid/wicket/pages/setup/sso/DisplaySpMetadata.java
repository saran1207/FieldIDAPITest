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

        add(new Label("metadata.local", Model.of(spMetadata.isLocal())));
        add(new Label("metadata.entityId", Model.of(spMetadata.getSsoEntity().getEntityId())));
        //add(new Label("metadata.alias", Model.of(spMetadataDto.getAlias())));
        add(new Label("matchOnUserId", Model.of(spMetadata.isMatchOnUserId())));
        add(new Label("matchOnEmailAddress", Model.of(spMetadata.isMatchOnEmailAddress())));
      /*  add(new Label("metadata.signingKey", Model.of(spMetadataDto.getSigningKey())));
        add(new Label("metadata.encryptionKey", Model.of(spMetadataDto.getEncryptionKey())));*/

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
