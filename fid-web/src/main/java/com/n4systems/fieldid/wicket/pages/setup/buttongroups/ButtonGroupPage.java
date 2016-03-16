package com.n4systems.fieldid.wicket.pages.setup.buttongroups;

import com.n4systems.fieldid.service.event.ButtonGroupService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.setup.buttongroups.ButtonGroupListPanel;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.ButtonGroupDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.fieldid.wicket.pages.setup.observationcount.ObservationCountGroupPage;
import com.n4systems.fieldid.wicket.pages.setup.score.ScoreGroupsPage;
import com.n4systems.model.ButtonGroup;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the Setup page for Button Groups.  It lists all available button groups and allows the users to edit them
 * and create new button groups.  Once you've made a button group, however, it's there forever.  You should be
 * punished for even thinking about deleting them.  Shame on you.
 *
 * Created by Jordan Heath on 2016-03-11.
 */
public class ButtonGroupPage extends FieldIDTemplatePage {
    private static final Logger logger = Logger.getLogger(ButtonGroupPage.class);

    @SuppressWarnings("unused")
    @SpringBean
    private ButtonGroupService buttonGroupService;

    private ButtonGroupDataProvider dataProvider;

    private FIDFeedbackPanel feedbackPanel;

    private AjaxSubmitLink saveButton;

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

        Form buttonGroupForm = new Form("controlForm") {
            @Override
            protected void onValidate() {
                if(getDataProvider().getCurrentGroupList().stream().filter(group -> group.getButtons().stream().filter(button -> !button.isRetired()).count() == 0).count() > 0 ||
                        getDataProvider().getCurrentGroupList().stream().filter(group -> group.getName() == null || group.getName().isEmpty()).count() > 0) {
                    getDataProvider().getCurrentGroupList()
                            .stream()
                            .filter(group -> group.getButtons()
                                    .stream()
                                    .filter(button -> !button.isRetired())
                                    .count() == 0)
                            .forEach(group -> error(new FIDLabelModel("label.group_could_not_save", group.getName()).getObject()));

                    getDataProvider().getCurrentGroupList()
                            .stream()
                            .filter(group -> group.getName() == null || group.getName().isEmpty())
                            .forEach(group -> error(new FIDLabelModel("label.group_has_no_name").getObject()));
                }
            }

            @Override
            protected void onSubmit() {
                doSave();
                setResponsePage(ButtonGroupPage.class);
            }
        };

        WebMarkupContainer listContainer = new WebMarkupContainer("buttonGroupsListPanel");

        listContainer.setOutputMarkupId(true);

        listContainer.add(new SimpleDefaultDataTable<ButtonGroup>("buttonGroupsList",
                                                                  getColumns(),
                                                                  getDataProvider(),
                                                                  2000));

        add(listContainer);

        add(new AjaxLink<Void>("addGroupLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                logger.info("The add group button has been clicked!!");
                doAdd();
                target.focusComponent(saveButton);
                target.add(this, listContainer, feedbackPanel);
            }

        });

        buttonGroupForm.add(saveButton = new AjaxSubmitLink("saveButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                logger.info("The Save button has been clicked!!");
                target.add(this, listContainer, feedbackPanel);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        });

        buttonGroupForm.add(new Link<Void>("cancelLink") {
            @Override
            public void onClick() {
                dataProvider.refreshGroupList();
            }
        });

        add(buttonGroupForm);
    }

    private List<IColumn<ButtonGroup>> getColumns() {
        List<IColumn<ButtonGroup>> returnMe = new ArrayList<>();

        returnMe.add(new AbstractColumn<ButtonGroup>(new FIDLabelModel("")) {
            @Override
            public void populateItem(Item<ICellPopulator<ButtonGroup>> item, String id, IModel<ButtonGroup> model) {
                item.add(new ButtonGroupListPanel(id, model));
            }
        });

        return returnMe;
    }

    private ButtonGroupDataProvider getDataProvider() {
        if(dataProvider == null) {
            this.dataProvider = new ButtonGroupDataProvider();
        }

        return dataProvider;
    }

    private void doSave() {


        //Saving should be interesting... we really want two things, only one of which I'm currently certain of how to
        //figure out:
        //1) ButtonGroup entities which have changed - "changed" must be true and the entity must have an ID.
        getDataProvider().getCurrentGroupList()
                         .stream()
                         .filter(group -> !group.isNew() &&
                                 group.isChanged())
                         .forEach(buttonGroupService::update);

        //2) New ButtonGroup entities - this is easy... they'll have no ID.
        getDataProvider().getCurrentGroupList()
                         .stream()
                         .filter(group -> group.getId() == null)
                         //Pretty sure these will be okay, since they're obviously detached... they're new!!
                         .forEach(buttonGroupService::save);

        FieldIDSession.get().info(new FIDLabelModel("label.group_saved").getObject());
    }

    private void doAdd() {
        ButtonGroup newGroup = new ButtonGroup();
        newGroup.setTenant(getCurrentUser().getTenant());

        getDataProvider().getCurrentGroupList().add(newGroup);
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                NavigationItemBuilder.aNavItem().page("eventTypes.action").label("nav.view_all").build(),
                NavigationItemBuilder.aNavItem().page(ButtonGroupPage.class).label("nav.button_groups").build(),
                NavigationItemBuilder.aNavItem().page(ObservationCountGroupPage.class).label("nav.observation_groups").build(),
                NavigationItemBuilder.aNavItem().page(ScoreGroupsPage.class).label("nav.score_groups").build()));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<>(linkId, AssetsAndEventsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("nav.button_groups"));
    }

    @Override
    protected String getMainCss() {
        return "button-groups-page";
    }
}
