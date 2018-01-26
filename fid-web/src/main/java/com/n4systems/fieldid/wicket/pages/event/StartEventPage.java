package com.n4systems.fieldid.wicket.pages.event;

import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.wicket.behavior.SetFocusOnLoadBehavior;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.search.SmartSearchListPage;
import com.n4systems.security.Permissions;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.ConfigurationProvider;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@UserPermissionFilter(userRequiresOneOf={Permissions.CREATE_EVENT, Permissions.EDIT_EVENT})
public class StartEventPage extends FieldIDFrontEndPage {

    private FeedbackPanel feedbackPanel;

    public StartEventPage(PageParameters params) {

        super(params);
        addComponents();
        addFeedbackPanel();
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

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.start_event"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/pageStyles/events.css");
        response.renderCSS("li .feedbackPanelINFO {padding: 10px 0px 10px 0px;\n" +
                "text-align: center;\n" +
                "border: 1px solid #5fb336;\n" +
                "background-color: #e3f4db;\n" +
                "font-size: 13px;\n" +
                "display: block;\n" +
                "color: #333333;}", null);
        response.renderCSS("li .feedbackPanelERROR {text-align: center: display:block; color: red;}", null);
    }

    private void addComponents() {

        add(new Label("label_multi_event.full", new StringResourceModel("label.multi_event.full",
                 this,null, new Object[]{getMaxAssetsFromMassEvent()})));
        final TextField<String> searchTerm = new TextField<String>("singleAssetSearchText", Model.of(""));
        searchTerm.setOutputMarkupId(true);
        searchTerm.add(new SetFocusOnLoadBehavior());
        Form singleAssetSearchForm = new Form("singleAssetSearchForm") {
          @Override
          protected void onSubmit() {
              if (searchTerm.getModelObject() == null) {
                  error(new FIDLabelModel("error.enterSmartSearchTermForSingleAsset").getObject());
              }
              else {
                  setResponsePage(SmartSearchListPage.class,
                          PageParametersBuilder.param("searchTerm",
                                  searchTerm.getModelObject()));
              }
          }
        };
        singleAssetSearchForm.add(searchTerm);
        add(singleAssetSearchForm);

        boolean includeMultipleProofTests = getSecurityGuard().isProofTestIntegrationEnabled();
        WebMarkupContainer multipleProofTestsContainer = new WebMarkupContainer("multipleProofTestsContainer");
        multipleProofTestsContainer.setVisible(includeMultipleProofTests);
        add(multipleProofTestsContainer);
        multipleProofTestsContainer.add(new Label("label_multi_proof_test_event.full",
                new StringResourceModel("label.multi_proof_test.full",
                this,null, new Object[]{getMaxAssetsFromMassEvent()})));
    }

    private Long getMaxAssetsFromMassEvent() {
        Long maxAssetsFromMassEvent = getConfigContext().getLong(ConfigEntry.MASS_ACTIONS_LIMIT, getTenantId());
        return maxAssetsFromMassEvent != null ? maxAssetsFromMassEvent : 250;
    }

    private ConfigurationProvider getConfigContext() {
        return ConfigService.getInstance();
    }

}
