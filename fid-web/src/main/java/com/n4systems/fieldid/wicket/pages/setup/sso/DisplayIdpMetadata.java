package com.n4systems.fieldid.wicket.pages.setup.sso;

import com.n4systems.sso.dao.SsoMetadataDao;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.sso.SsoIdpMetadata;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by agrabovskis on 2018-07-19.
 */
public class DisplayIdpMetadata extends FieldIDTemplatePage {

    static public Logger logger = LoggerFactory.getLogger(DisplayIdpMetadata.class);

    static public String ENTITY_ID_KEY = "entityId";

    @SpringBean
    private SsoMetadataDao ssoMetadataDao;

    public DisplayIdpMetadata(final PageParameters parameters) {
        super(parameters);
        String entityId = parameters.get(ENTITY_ID_KEY).toString();
        addComponents(entityId);
    }

    private void addComponents(String entityId) {

        SsoIdpMetadata idpMetadata = ssoMetadataDao.getIdp(entityId);
        add(new Label("metadata.idpEntityId", Model.of(idpMetadata.getSsoEntity().getEntityId())));
        add(new Label("metadata.idpUrl", Model.of(idpMetadata.getIdpUrl())));

        TextArea serializedMetadataField = new TextArea<String>("serializedMetadata",
                Model.of(idpMetadata.getSerializedMetadata()));
        add(serializedMetadataField);

        Link backLink = new Link("backLink") {
            public void onClick() {
                getRequestCycle().setResponsePage(SsoSettingsPage.class);
            }
        };
        add(backLink);
    }

}
