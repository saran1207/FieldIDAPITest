package com.n4systems.fieldid.wicket.pages.user;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.fieldid.service.user.UserListFilterCriteria;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.org.people.table.UserGroupColumn;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.components.user.columns.UserNameLinkColumn;
import com.n4systems.fieldid.wicket.components.user.columns.UsersListActionColumn;
import com.n4systems.fieldid.wicket.data.UsersDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.fieldid.wicket.util.DisplayEnumChoiceRenderer;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.security.UserType;
import com.n4systems.util.UserBelongsToFilter;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.param;

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

    public UsersListPage() {
       filterCriteriaModel = Model.of(new UserListFilterCriteria());
       dataProvider = new UsersDataProvider(filterCriteriaModel.getObject());

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

       filterForm.add(new AjaxSubmitLink("filter") {
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
    }


    public List<IColumn<User>> getUserTableColumns() {
        List<IColumn<User>> columns = Lists.newArrayList();

        columns.add(new UserNameLinkColumn(new FIDLabelModel("label.name_first_last"), "firstName, lastName"));
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.username"), "userID", "userID"));
        columns.add(new UserGroupColumn(new FIDLabelModel("label.user_group")));
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.organization"), "owner", "owner.name"));
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.customer"), "owner.customerOrg", "owner.customerOrg.name"));
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.division"), "owner.divisionOrg", "owner.divisionOrg.name"));
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.emailaddress"), "emailAddress", "emailAddress"));
        columns.add(new PropertyColumn<User>(new FIDLabelModel("label.lastlogin"), "created"));
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
        add(new NavigationBar(navBarId,
                aNavItem().label("nav.view_all").page(UsersListPage.class).build(),
                aNavItem().label("nav.view_all_archived").page("archivedUserList.action").params(param("currentPage", 1)).build(),
                aNavItem().label("nav.add").page("addUser.action").onRight().build(),
                aNavItem().label("nav.import_export").page("userImportExport.action").onRight().build()
        ));
    }

}
