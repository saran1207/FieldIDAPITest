package com.n4systems.fieldid.wicket.pages.setup.user;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.TextFieldHintBehavior;
import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.MattBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.user.UserGroup;
import com.n4systems.util.FieldIdDateFormatter;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.PatternValidator;

import java.util.List;

import static ch.lambdaj.Lambda.on;

public class UserGroupsPage extends FieldIDFrontEndPage {

    @SpringBean private UserGroupService userGroupService;
    @SpringBean private PersistenceService persistenceService;

    private WebMarkupContainer userGroupsTable;
    private IModel<List<UserGroup>> groupsModel;
    private AddGroupForm addGroupForm;
    private FilterForm filterForm;

    private String nameFilter = null;
    private Archivable.EntityState entityStateFilter = Archivable.EntityState.ACTIVE;

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(filterForm = new FilterForm("filterForm"));
        add(addGroupForm = new AddGroupForm("addGroupForm"));
        add(createActiveOrArchivedSelector("activeOrArchivedSelector"));

        add(new AjaxLink<Void>("addGroupLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                addGroupForm.setVisible(true);
                target.add(addGroupForm);
            }
        });

        add(userGroupsTable = new WebMarkupContainer("userGroupsTable"));
        userGroupsTable.setOutputMarkupId(true);

        groupsModel = createUserGroupsModel();
        userGroupsTable.add(new ListView<UserGroup>("userGroups", groupsModel) {
            @Override
            protected void populateItem(final ListItem<UserGroup> item) {
                item.setOutputMarkupPlaceholderTag(true);
                final EditNameForm editNameForm = new EditNameForm("nameEditForm", item.getModel());
                item.add(editNameForm);

                final Label nameLabel = new Label("name", ProxyModel.of(item.getModel(), on(UserGroup.class).getName()));
                item.add(nameLabel.setOutputMarkupPlaceholderTag(true));
                item.add(new Label("groupId", ProxyModel.of(item.getModel(), on(UserGroup.class).getGroupId())));

                NonWicketLink linkToUsersList = new NonWicketLink("linkToUsersListPage", "userList.action?userGroupFilter="+item.getModelObject().getId());
                linkToUsersList.add(new Label("members", String.valueOf(userGroupService.getUsersInGroup(item.getModelObject().getId()).size())));
                item.add(linkToUsersList);

                String created = new FieldIdDateFormatter(item.getModelObject().getCreated(), FieldIDSession.get().getSessionUser(), true, true).format();
                String modified = new FieldIdDateFormatter(item.getModelObject().getModified(), FieldIDSession.get().getSessionUser(), true, true).format();

                item.add(new Label("created", created));
                item.add(new Label("modified", modified));

                final WebMarkupContainer actionsForActiveGroups = new WebMarkupContainer("actionsForActiveGroups");
                final WebMarkupContainer actionsForArchivedGroups = new WebMarkupContainer("actionsForArchivedGroups");

                Component archiveLink = createArchiveLink(item.getModel());
                archiveLink.add(new TipsyBehavior(getString("label.archive")).hideTooltipsOnLinkClick());
                actionsForActiveGroups.add(archiveLink);
                actionsForActiveGroups.add(new AjaxLink<Void>("editLink") {
                    { add(new TipsyBehavior(getString("label.edit")).hideTooltipsOnLinkClick()); }
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        actionsForActiveGroups.setVisible(false);
                        editNameForm.setVisible(true);
                        nameLabel.setVisible(false);
                        target.add(item);
                    }
                });
                actionsForArchivedGroups.add(createUnarchiveLink(item.getModel()));
                item.add(actionsForActiveGroups.setVisible(item.getModelObject().isActive()).setOutputMarkupPlaceholderTag(true));
                item.add(actionsForArchivedGroups.setVisible(!item.getModelObject().isActive()).setOutputMarkupPlaceholderTag(true));
            }
        });
    }


    class EditNameForm extends Form<UserGroup> {
        public EditNameForm(String id, final IModel<UserGroup> model) {
            super(id, model);
            setVisible(false);
            setOutputMarkupPlaceholderTag(true);
            final FIDFeedbackPanel feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
            add(feedbackPanel);
            add(new RequiredTextField<String>("groupNameField", ProxyModel.of(model, on(UserGroup.class).getName()))
                    .add(new UserGroupUniqueNameValidator(model.getObject().getId())));
            add(new TextField<String>("groupIdField", ProxyModel.of(model, on(UserGroup.class).getGroupId())));
            add(new AjaxSubmitLink("saveButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    EditNameForm.this.setVisible(false);
                    persistenceService.update(model.getObject());
                    redrawTable(target);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }
            });
        }
    }

    class AddGroupForm extends Form<UserGroup> {
        private String groupId;
        private String groupName;
        public AddGroupForm(String id) {
            super(id);
            setVisible(false);
            setOutputMarkupPlaceholderTag(true);
            final FIDFeedbackPanel feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
            add(feedbackPanel);
            add(new RequiredTextField<String>("name", new PropertyModel<String>(this, "groupName"))
                .add(new UserGroupUniqueNameValidator())
                .add(new PatternValidator(".*,.*").setReverse(true)));
            add(new TextField<String>("id", new PropertyModel<String>(this, "groupId")));
            add(new AjaxLink<Void>("cancelLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    AddGroupForm.this.setVisible(false);
                    target.add(AddGroupForm.this);
                }
            });
            add(new AjaxSubmitLink("saveButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    AddGroupForm.this.setVisible(false);
                    UserGroup userGroup = new UserGroup();
                    userGroup.setGroupId(groupId);
                    userGroup.setName(groupName);
                    userGroup.setTenant(getCurrentUser().getTenant());
                    persistenceService.save(userGroup);
                    groupId = groupName = nameFilter = null;
                    target.add(AddGroupForm.this);
                    redrawTable(target);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }
            });
        }
    }

    private Component createArchiveLink(final IModel<UserGroup> model) {
            return new AjaxLink<Void>("archiveLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    if (model.getObject().getMembers().size() > 0) {
                        setResponsePage(ArchiveUserGroupPage.class, PageParametersBuilder.id(model.getObject().getId()));
                    } else {
                        userGroupService.archiveGroup(model.getObject());
                        redrawTable(target);
                    }
                }
            };
    }

    private Component createUnarchiveLink(final IModel<UserGroup> model) {
        return new AjaxLink<Void>("unArchiveLink") {
            { add(new TipsyBehavior(getString("label.unarchive")).hideTooltipsOnLinkClick()); }
            @Override
            public void onClick(AjaxRequestTarget target) {
                userGroupService.unArchiveGroup(model.getObject());
                redrawTable(target);
            }
        };
    }

    private Component createActiveOrArchivedSelector(String id) {
        return new MattBar(id) {
            {
                addLink(new FIDLabelModel("label.active"), Archivable.EntityState.ACTIVE);
                addLink(new FIDLabelModel("label.archived"), Archivable.EntityState.ARCHIVED);
                setCurrentState(entityStateFilter);
            }
            @Override
            protected void onEnterState(AjaxRequestTarget target, Object state) {
                entityStateFilter = (Archivable.EntityState) state;
                redrawTable(target);
            }
        };
    }

    class FilterForm extends Form {
        public FilterForm(String id) {
            super(id);
            setOutputMarkupId(true);
            TextField<String> filterField = new TextField<String>("nameFilter", new PropertyModel<String>(UserGroupsPage.this, "nameFilter"));
            filterField.add(new AjaxFormComponentUpdatingBehavior("onkeyup") {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    target.add(userGroupsTable);
                }
            });
            filterField.add(new TextFieldHintBehavior(new FIDLabelModel("message.start_typing_group_name_or_id")));
            add(filterField);
        }
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new UserGroupTitleLabel(labelId);
    }

    @Override
    protected Component createTopTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.user_groups"));
    }

    @Override
    protected boolean useTopTitleLabel() {
        return true;
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, OwnersUsersLocationsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    private IModel<List<UserGroup>> createUserGroupsModel() {
        return new LoadableDetachableModel<List<UserGroup>>() {
            @Override
            protected List<UserGroup> load() {
                return userGroupService.findUserGroupsLike(nameFilter, entityStateFilter);
            }
        };
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/setup/prettyItemList.css");
    }

    private void redrawTable(AjaxRequestTarget target) {
        groupsModel.detach();
        target.add(userGroupsTable, filterForm);
    }
}
