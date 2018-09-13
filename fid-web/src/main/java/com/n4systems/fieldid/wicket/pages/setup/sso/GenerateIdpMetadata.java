package com.n4systems.fieldid.wicket.pages.setup.sso;

import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.sso.IdpProvidedMetadata;
import com.n4systems.model.sso.SsoEntity;
import com.n4systems.model.sso.SsoIdpMetadata;
import com.n4systems.fieldid.sso.SsoMetadataServices;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
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

    private FeedbackPanel feedbackPanel;

    public GenerateIdpMetadata(final PageParameters parameters) {
        super(parameters);
        addComponents();
     }

    private void addComponents() {

        addFeedbackPanel();

        final Model<String> idpUrlModel = Model.of("");
        final Model<String> loadTimeoutModel = Model.of("15000");
        final Model<String> entityIdModel = Model.of("");
        final Model<String> serializedMetadataModel = Model.of("");

        final TextField entityIdField = new TextField<String>("entityId", entityIdModel);
        entityIdField.setRequired(true);
        entityIdField.setOutputMarkupId(true);

        final TextArea serializedMetadataField = new TextArea<String>("serializedMetadata",
                serializedMetadataModel);
        serializedMetadataField.setRequired(true);
        serializedMetadataField.setOutputMarkupId(true);

        Link backLink = new Link("backLink") {
            public void onClick() {
                setResponsePage(SsoSettingsPage.class);
            }
        };

        Form metadataForm = new Form("metadataForm") {
            @Override
            protected void onSubmit() {
                SsoIdpMetadata idpMetadata = new SsoIdpMetadata();
                idpMetadata.setTenant(getTenant());
                idpMetadata.setSsoEntity(new SsoEntity(entityIdModel.getObject()));
                idpMetadata.setSerializedMetadata(serializedMetadataModel.getObject());
                metadataServices.addIdp(idpMetadata);
                getRequestCycle().setResponsePage(SsoSettingsPage.class);
            }
        };
        add(metadataForm);
        TextField idpLoadUrl = new TextField<String>("idpLoadUrl", idpUrlModel);
        idpLoadUrl.setOutputMarkupId(true);
        idpLoadUrl.add(new AjaxFormComponentUpdatingBehavior("onChange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                // Do nothing, we just need model updated.
                }
            }
        );
        metadataForm.add(idpLoadUrl);
        NumberTextField refreshInterval = new NumberTextField("refreshInterval", loadTimeoutModel);
        refreshInterval.setOutputMarkupId(true);
        refreshInterval.add(new AjaxFormComponentUpdatingBehavior("onChange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                // Do nothing, we just need model updated.
                }
            }
        );
        metadataForm.add(refreshInterval);
        metadataForm.add(entityIdField);
        metadataForm.add(serializedMetadataField);
        metadataForm.add(backLink);

        metadataForm.add(new AjaxLink("getMetadata") {
            @Override
            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                metadataForm.clearInput();
                feedbackPanel.getFeedbackMessages().clear();
                ajaxRequestTarget.add(feedbackPanel);
                try {
                    IdpProvidedMetadata metadata = metadataServices.getMetadataFromIdp(idpUrlModel.getObject(),
                            new Integer(loadTimeoutModel.getObject()));
                    entityIdModel.setObject(metadata.getEntityId());
                    serializedMetadataModel.setObject(metadata.getSerializedMetadata());
                    ajaxRequestTarget.add(entityIdField);
                    ajaxRequestTarget.add(serializedMetadataField);
                }
                catch(Throwable t) {
                    logger.error("Unable to get metadata from IDP", t);
                    error("Unable to download IDP metadata from specified URL");
                }
            }
        });
    }

    private void addFeedbackPanel() {
         /* Existing top feedback panel is in the correct place for our messages but doesn't
            get recognized as a feedback panel for our messages. */
        remove(getTopFeedbackPanel());
        feedbackPanel = new FeedbackPanel("topFeedbackPanel");
        feedbackPanel.add(new AttributeAppender("style", new Model("text-align: center; color:red; padding: 0px 10px"), " "));
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);
    }
}
