package com.n4systems.fieldid.wicket.pages.setup.sso;

import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.sso.IdpProvidedMetadata;
import com.n4systems.model.sso.SsoEntity;
import com.n4systems.model.sso.SsoIdpMetadata;
import com.n4systems.fieldid.sso.SsoMetadataServices;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by agrabovskis on 2018-07-31.
 */
public class GenerateIdpMetadata extends FieldIDTemplatePage {

    static public Logger logger = LoggerFactory.getLogger(GenerateIdpMetadata.class);

    @SpringBean
    private SsoMetadataServices metadataServices;


    public GenerateIdpMetadata(final PageParameters parameters) {
        super(parameters);
        addComponents();
        //metadataManager = (MetadataManager) ((WicketApplication)getApplication()).getAppCtx().getBean("metadata");
    }

    private void addComponents() {

        Link backLink = new Link("backLink") {
            public void onClick() {
                setResponsePage(SsoSettingsPage.class);
            }
        };
        add(backLink);

        add(new FeedbackPanel("feedbackPanel"));

        final Model<String> idpUrlModel = Model.of("https://idp.ssocircle.com");
        final Model<String> loadTimeoutModel = Model.of("15000");
        final Model<String> entityIdModel = Model.of("");
        final Model<String> serializedMetadataModel = Model.of("");

        final TextField entityIdField = new TextField<String>("entityId", entityIdModel);
        entityIdField.setOutputMarkupId(true);

        final TextArea serializedMetadataField = new TextArea<String>("serializedMetadata",
                serializedMetadataModel);
        serializedMetadataField.setOutputMarkupId(true);

        Form metadataForm = new Form("metadataForm") {
            @Override
            protected void onSubmit() {
                SsoIdpMetadata idpMetadata = new SsoIdpMetadata();
                idpMetadata.setTenant(getTenant());
                idpMetadata.setIdpUrl(idpUrlModel.getObject());
                idpMetadata.setSsoEntity(new SsoEntity(entityIdModel.getObject()));
                idpMetadata.setSerializedMetadata(serializedMetadataModel.getObject());
                metadataServices.addIdp(idpMetadata);
                getRequestCycle().setResponsePage(SsoSettingsPage.class);
            }
        };
        add(metadataForm);
        metadataForm.add(new TextField<String>("idpUrl", idpUrlModel));
        metadataForm.add(new AjaxLink("getMetadata") {
            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                IdpProvidedMetadata metadata = metadataServices.getMetadataFromIdp(idpUrlModel.getObject(),
                        new Integer(loadTimeoutModel.getObject()));
                entityIdModel.setObject(metadata.getEntityId());
                serializedMetadataModel.setObject(metadata.getSerializedMetadata());
                ajaxRequestTarget.add(entityIdField);
                ajaxRequestTarget.add(serializedMetadataField);
            }
        });
        metadataForm.add(new NumberTextField("refreshInterval", loadTimeoutModel));
        metadataForm.add(entityIdField);
        metadataForm.add(serializedMetadataField);
    }

}
