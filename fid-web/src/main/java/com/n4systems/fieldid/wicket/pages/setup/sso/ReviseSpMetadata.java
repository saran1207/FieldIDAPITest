package com.n4systems.fieldid.wicket.pages.setup.sso;

import com.n4systems.sso.dao.SsoMetadataDao;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.sso.SsoSpMetadata;
import com.n4systems.fieldid.sso.SsoMetadataServices;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by agrabovskis on 2018-07-19.
 */
public class ReviseSpMetadata extends FieldIDTemplatePage {

    static public Logger logger = LoggerFactory.getLogger(ReviseSpMetadata.class);

    public static String ENTITY_ID_KEY = "entityId";

    @SpringBean
    private SsoMetadataDao metadataDao;

    @SpringBean
    private SsoMetadataServices ssoMetadataServices;

    private FeedbackPanel feedbackPanel;

    public ReviseSpMetadata(final PageParameters parameters) {
        super(parameters);
        String entityId = parameters.get(ENTITY_ID_KEY).toString();
        addFeedbackPanel();
        addComponents(entityId);
    }

    private void addComponents(String entityId) {

        SsoSpMetadata spMetadata = metadataDao.getSp(entityId);
        String tenantName = getTenant().getName();
        add(new Label("tenantName", tenantName));

        SpMetadataPanel spPanel = new SpMetadataPanel("spMetatdata", spMetadata, true) {
            @Override
            void cancel() {
                getRequestCycle().setResponsePage(SsoSettingsPage.class);
            }
            @Override
            void submitForm(SsoSpMetadata sp) {
                updateMetadata(sp);
            }
        };
        add(spPanel);
    }

    private void updateMetadata(SsoSpMetadata spMetadata) {

        spMetadata.setLocal(true);
        ssoMetadataServices.updateSp(spMetadata);

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
