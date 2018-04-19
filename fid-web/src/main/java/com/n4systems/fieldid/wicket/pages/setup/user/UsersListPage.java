package com.n4systems.fieldid.wicket.pages.setup.user;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.fieldid.service.user.UserListFilterCriteria;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.org.people.table.UserGroupColumn;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.components.user.columns.LastLoginColumn;
import com.n4systems.fieldid.wicket.components.user.columns.UserNameLinkCell;
import com.n4systems.fieldid.wicket.components.user.columns.UserNameLinkColumn;
import com.n4systems.fieldid.wicket.components.user.columns.UsersListActionColumn;
import com.n4systems.fieldid.wicket.data.UsersDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.fieldid.wicket.components.renderer.DisplayEnumChoiceRenderer;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.security.UserType;
import com.n4systems.util.UserBelongsToFilter;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class UsersListPage extends FieldIDTemplatePage {

    private static int PAGE_SIZE = 20;

    @SpringBean
    private UserService userService;

    @SpringBean
    private UserGroupService userGroupService;

    @SpringBean
    private OrgService orgService;

    private UsersDataProvider dataProvider;
    private WebMarkupContainer usersListContainer;
    private IModel<UserListFilterCriteria> filterCriteriaModel;
    private Form filterForm;

    protected FIDFeedbackPanel feedbackPanel;

    public UsersListPage() {
       filterCriteriaModel = getUserListFilterCriteria();
       dataProvider = new UsersDataProvider(filterCriteriaModel.getObject());
    }

    public UsersListPage(PageParameters params) {
        filterCriteriaModel = getUserListFilterCriteria();

        Long userGroupId = params.get("userGroupFilter").toLong();
        UserGroup userGroupFilter = userGroupService.getUserGroup(userGroupId);
        dataProvider = new UsersDataProvider(filterCriteriaModel.getObject().withUserGroup(userGroupFilter));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));
        feedbackPanel.setOutputMarkupId(true);

        add(filterForm = new Form<UserListFilterCriteria>("filterForm", filterCriteriaModel));
        filterForm.add(new TextField<String>("nameFilter", new PropertyModel<String>(filterCriteriaModel, "nameFilter")));
        filterForm.add(new FidDropDownChoice<UserBelongsToFilter>("userBelongsToFilter",
                new PropertyModel<UserBelongsToFilter>(filterCriteriaModel, "userBelongsToFilter"),
                Lists.newArrayList(UserBelongsToFilter.values()),
                new DisplayEnumChoiceRenderer()));
        filterForm.add(new FidDropDownChoice<UserType>("userTypeFilter",
                new PropertyModel<UserType>(filterCriteriaModel, "userType"),
                Lists.newArrayList(UserType.VISIBLE_USER_TYPES),
                new DisplayEnumChoiceRenderer())
                .setNullValid(true));
        filterForm.add(new FidDropDownChoice<UserGroup>("userGroupFilter",
                new PropertyModel<UserGroup>(filterCriteriaModel, "groupFilter"),
                userGroupService.getActiveUserGroups(),
                new ListableChoiceRenderer<UserGroup>())
                .setNullValid(true));
        filterForm.add(new FidDropDownChoice<InternalOrg>("orgFilter",
                new PropertyModel<InternalOrg>(filterCriteriaModel, "orgFilter"),
                orgService.getInternalOrgs(),
                new ListableChoiceRenderer<InternalOrg>())
                .setNullValid(true));

        filterForm.add(new AjaxButton("filter") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.add(usersListContainer);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {}
        });

        filterForm.add(new AjaxSubmitLink("clear") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                filterCriteriaModel.getObject().reset();
                target.add(filterForm, usersListContainer);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {}
        });

        add(usersListContainer = new WebMarkupContainer("usersListContainer"));
        usersListContainer.setOutputMarkupId(true);
        usersListContainer.add(new SimpleDefaultDataTable<User>("users", getUserTableColumns(), dataProvider, PAGE_SIZE));

        usersListContainer.add(new Label("total", getTotalCountLabel()));
    }

    protected Model<UserListFilterCriteria> getUserListFilterCriteria() {
        return Model.of(new UserListFilterCriteria(false));
    }

    protected List<IColumn<User>> getUserTableColumns() {
        List<IColumn<User>> columns = Lists.newArrayList();

        columns.add(new UserNameLinkColumn(new FIDLabelModel("label.name_first_last"), "firstName, lastName") {
            @Override
            public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
                cellItem.add(new UserNameLinkCell(componentId, rowModel)).
                        add(new AttributeAppender("class", new Model<String>("notranslate"), " "));
            }
        });
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.username"), "userID", "userID") {
            @Override
            public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
                cellItem.add(new Label(componentId, createLabelModel(rowModel))).
                        add(new AttributeAppender("class", new Model<String>("notranslate"), " "));
            }
        });
        columns.add(new UserGroupColumn(new FIDLabelModel("label.user_group")));
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.organization"), "owner", "owner.rootOrgName"));
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.customer"), "owner.customerOrg", "owner.customerOrg.name"));
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.division"), "owner.divisionOrg", "owner.divisionOrg.name"));
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.emailaddress"), "emailAddress", "emailAddress") {
            @Override
            public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
                cellItem.add(new Label(componentId, createLabelModel(rowModel))).
                        add(new AttributeAppender("class", new Model<String>("notranslate"), " "));
            }
        });
        columns.add(new LastLoginColumn(new FIDLabelModel("label.lastlogin")));
        columns.add(new UsersListActionColumn());
        return columns;
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_users.plural"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, OwnersUsersLocationsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    @Override
    protected void addNavBar(String navBarId) {
        UserListFilterCriteria criteria = new UserListFilterCriteria(filterCriteriaModel.getObject());
        Long activeUserCount = userService.countUsers(criteria.withArchivedOnly(false));
        Long archivedUserCount = userService.countUsers(criteria.withArchivedOnly());
        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("nav.view_all.count", activeUserCount)).page(UsersListPage.class).build(),
                aNavItem().label(new FIDLabelModel("nav.view_all_archived.count", archivedUserCount)).page(ArchivedUsersListPage.class).build(),
                aNavItem().label("nav.add").page(SelectUserTypePage.class).onRight().build(),
                aNavItem().label("nav.import_export").page("userImportExport.action").onRight().build()
        ));
    }

    private LoadableDetachableModel<String> getTotalCountLabel() {
        return new LoadableDetachableModel<String>() {
            @Override
            protected String load() {
                return new FIDLabelModel("label.total_x", userService.countUsers(filterCriteriaModel.getObject())).getObject();
            }
        };
    }

}
