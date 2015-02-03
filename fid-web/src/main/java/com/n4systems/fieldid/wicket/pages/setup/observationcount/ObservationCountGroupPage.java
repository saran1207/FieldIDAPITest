package com.n4systems.fieldid.wicket.pages.setup.observationcount;

import com.n4systems.fieldid.service.event.ObservationCountService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.TextFieldWithDescription;
import com.n4systems.fieldid.wicket.components.eventform.EditCopyDeleteItemPanel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.observationCount.ObservationCountGroupCopyUtil;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.eventform.ObservationCountGroupsForTenantModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.fieldid.wicket.pages.setup.score.ScoreGroupsPage;
import com.n4systems.fieldid.wicket.util.NoBarsValidator;
import com.n4systems.fieldid.wicket.util.NoColonsValidator;
import com.n4systems.model.ObservationCountGroup;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

public class ObservationCountGroupPage extends FieldIDFrontEndPage {

    @SpringBean
    private ObservationCountService observationCountService;

    private WebMarkupContainer blankSlate;
    private WebMarkupContainer groupsAndObservationsContainer;
    private FIDFeedbackPanel feedbackPanel;
    private ObservationCountGroupPanel observationCountGroupPanel;

    private int currentlySelectedIndex = -1;

    public ObservationCountGroupPage() {

        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

        boolean hasGroups = observationCountService.countObservationCountGroups() > 0;

        add(blankSlate = new BlankSlate("blankSlatePanel"));
        blankSlate.setVisible(!hasGroups);

        add(groupsAndObservationsContainer = new WebMarkupContainer("groupsAndObservationsContainer"));
        groupsAndObservationsContainer.setOutputMarkupPlaceholderTag(true);
        groupsAndObservationsContainer.setVisible(hasGroups);

        groupsAndObservationsContainer.add(new ListView<ObservationCountGroup>("group", new ObservationCountGroupsForTenantModel()) {
            @Override
            protected void populateItem(ListItem<ObservationCountGroup> item) {
                item.setOutputMarkupId(true);
                EditCopyDeleteItemPanel editCopyDeleteItemPanel;
                item.add(editCopyDeleteItemPanel = new EditCopyDeleteItemPanel("editCopyDeletePanel", new PropertyModel<String>(item.getModel(), "name")) {
                    {
                        setStoreLabel(new FIDLabelModel("label.save"));
                        setEditMaximumLength(1024);
                    }

                    @Override
                    protected void onViewLinkClicked(AjaxRequestTarget target) {
                        currentlySelectedIndex = item.getIndex();
                        target.add(groupsAndObservationsContainer);
                        onGroupSelected(item.getModel(), target);
                    }

                    @Override
                    protected void onDeleteButtonClicked(AjaxRequestTarget target) {
                        observationCountService.archive(item.getModelObject());
                        if (item.getIndex() == currentlySelectedIndex) {
                            onGroupSelected(new Model<ObservationCountGroup>(null), target);
                            currentlySelectedIndex = -1;
                        } else if (currentlySelectedIndex != -1 && item.getIndex() < currentlySelectedIndex) {
                            currentlySelectedIndex = currentlySelectedIndex - 1;
                        }
                        getModel().detach();
                        target.add(groupsAndObservationsContainer);
                    }

                    @Override
                    protected void onCopyLinkClicked(AjaxRequestTarget target) {
                        ObservationCountGroup copiedGroup = new ObservationCountGroupCopyUtil().copy(item.getModelObject());
                        observationCountService.saveOrUpdate(copiedGroup);
                        getModel().detach();
                        target.add(groupsAndObservationsContainer);
                    }

                    @Override
                    protected void onStoreLinkClicked(AjaxRequestTarget target) {
                        observationCountService.saveOrUpdate(item.getModelObject());
                    }

                    @Override
                    protected void onFormValidationError(AjaxRequestTarget target) {
                        onValidationError(target);
                    }

                    @Override
                    public int getTextDisplayLimit() {
                        return 50;
                    }
                });

                editCopyDeleteItemPanel.getTextField().add(new NoBarsValidator());
                editCopyDeleteItemPanel.getTextField().add(new NoColonsValidator());
            }
        });

        groupsAndObservationsContainer.add(observationCountGroupPanel = new ObservationCountGroupPanel("observationCountGroup", new Model<ObservationCountGroup>()));
        groupsAndObservationsContainer.add(new NewObservationCountGroupForm("newObservationGroupForm"));

    }

    private void onGroupSelected(IModel<ObservationCountGroup> model, AjaxRequestTarget target) {
        groupsAndObservationsContainer.remove("observationCountGroup");
        groupsAndObservationsContainer.add(new ObservationCountGroupPanel("observationCountGroup", model));

        target.add(groupsAndObservationsContainer);
        target.add(feedbackPanel);


    }

    private void onValidationError(AjaxRequestTarget target) {
        target.add(feedbackPanel);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/scoreGroups.css");
        response.renderCSSReference("style/legacy/newCss/component/header_reorder_link_button.css");
        response.renderCSSReference("style/legacy/newCss/component/buttons.css");
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("nav.observation_groups"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                NavigationItemBuilder.aNavItem().page("eventTypes.action").label("nav.view_all").build(),
                NavigationItemBuilder.aNavItem().page("buttonGroups.action").label("nav.button_groups").build(),
                NavigationItemBuilder.aNavItem().page(ObservationCountGroupPage.class).label("nav.observation_groups").build(),
                NavigationItemBuilder.aNavItem().page(ScoreGroupsPage.class).label("nav.score_groups").build()));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, AssetsAndEventsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    private class NewObservationCountGroupForm extends Form {

        private String name;

        public NewObservationCountGroupForm(String id) {
            super(id);
            TextField<String> groupNameField;
            add(groupNameField = new TextFieldWithDescription("name", new PropertyModel<String>(this, "name"),
                    new FIDLabelModel("label.sample_observation_group_name")));

            groupNameField.add(new StringValidator.MaximumLengthValidator(1024));
            groupNameField.add(new NoBarsValidator());
            groupNameField.add(new NoColonsValidator());

            add(new AjaxSubmitLink("submitLink") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    ObservationCountGroup group = new ObservationCountGroup();
                    group.setTenant(getTenant());
                    group.setName(name);
                    observationCountService.saveOrUpdate(group);
                    FieldIDSession.get().info(new FIDLabelModel("label.group_saved").getObject());
                    setResponsePage(ObservationCountGroupPage.class);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    onValidationError(target);
                }
            });

        }
    }

    private class BlankSlate extends Fragment {

        public BlankSlate(String id) {
            super(id, "blankSlate", ObservationCountGroupPage.this);
            setOutputMarkupPlaceholderTag(true);

            add(new AjaxLink<Void>("createFirstGroupLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    blankSlate.setVisible(false);
                    groupsAndObservationsContainer.setVisible(true);
                    target.add(blankSlate,groupsAndObservationsContainer);
                }
            });
        }
    }

}
