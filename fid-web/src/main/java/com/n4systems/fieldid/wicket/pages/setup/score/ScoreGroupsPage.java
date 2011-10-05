package com.n4systems.fieldid.wicket.pages.setup.score;

import com.n4systems.fieldid.service.event.ScoreService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.TextFieldWithDescription;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.eventform.ScoreGroupsForTenantModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDLoggedInPage;
import com.n4systems.model.ScoreGroup;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

public class ScoreGroupsPage extends FieldIDLoggedInPage {

    @SpringBean
    private ScoreService scoreService;

    private WebMarkupContainer groupsAndScoresContainer;
    private ScoreGroupPanel scoreGroupPanel;
    private ScoreBlankSlatePanel scoreBlankSlatePanel;
    private FIDFeedbackPanel feedbackPanel;

    public ScoreGroupsPage() {
        add(CSSPackageResource.getHeaderContribution("style/scoreGroups.css"));
        ScoreGroupsForTenantModel scoreGroupsForTenant = new ScoreGroupsForTenantModel();
        boolean hasScoreGroups = !scoreGroupsForTenant.getObject().isEmpty();

        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

        add(scoreBlankSlatePanel = new ScoreBlankSlatePanel("blankSlatePanel") {
            @Override
            protected void onCreateFirstGroupClicked(AjaxRequestTarget target) {
                groupsAndScoresContainer.setVisible(true);
                scoreBlankSlatePanel.setVisible(false);
                target.addComponent(scoreBlankSlatePanel);
                target.addComponent(groupsAndScoresContainer);
            }
        });
        scoreBlankSlatePanel.setVisible(!hasScoreGroups);
        add(groupsAndScoresContainer = new WebMarkupContainer("groupsAndScoresContainer"));
        groupsAndScoresContainer.setOutputMarkupPlaceholderTag(true);
        groupsAndScoresContainer.setVisible(hasScoreGroups);
        groupsAndScoresContainer.add(new ScoreGroupsListPanel("scoreGroups", scoreGroupsForTenant) {
            @Override
            protected void onScoreGroupSelected(IModel<ScoreGroup> model, AjaxRequestTarget target) {
                if (model == null) {
                    // clearing the selected score group
                    scoreGroupPanel.setVisible(false);
                } else {
                    groupsAndScoresContainer.remove("scoreGroup");
                    groupsAndScoresContainer.add(new ScoreGroupPanel("scoreGroup", model));
                }

                target.addComponent(groupsAndScoresContainer);
                target.addComponent(feedbackPanel);
            }

            @Override
            protected void onValidationError(AjaxRequestTarget target) {
                target.addComponent(feedbackPanel);
            }
        });
        groupsAndScoresContainer.add(scoreGroupPanel = new ScoreGroupPanel("scoreGroup", new Model<ScoreGroup>()));
        groupsAndScoresContainer.add(new NewScoreGroupForm("newScoreGroupForm"));
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("nav.score_groups"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                NavigationItemBuilder.aNavItem().page("eventTypes.action").label("nav.view_all").build(),
                NavigationItemBuilder.aNavItem().page("buttonGroups.action").label("nav.button_groups").build(),
                NavigationItemBuilder.aNavItem().page(ScoreGroupsPage.class).label("nav.score_groups").build(),
                NavigationItemBuilder.aNavItem().page("eventTypeAdd.action").label("nav.add").onRight().build()));
    }


    class NewScoreGroupForm extends Form {

        private String newGroupName;

        public NewScoreGroupForm(String id) {
            super(id);
            TextField<String> groupNameField;
            add(groupNameField = new TextFieldWithDescription("scoreGroupName", new PropertyModel<String>(this, "newGroupName"),
                    new FIDLabelModel("label.sample_score_group_name")));

            groupNameField.add(new StringValidator.MaximumLengthValidator(1024));

            add(new Button("submitButton"));
        }

        @Override
        protected void onSubmit() {
            ScoreGroup scoreGroup = new ScoreGroup();
            scoreGroup.setTenant(getTenant());
            scoreGroup.setName(newGroupName);
            scoreService.saveScoreGroup(scoreGroup);
            FieldIDSession.get().info(new FIDLabelModel("label.group_saved").getObject());
            setResponsePage(ScoreGroupsPage.class);
        }

    }

}
