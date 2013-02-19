package com.n4systems.fieldid.wicket.pages.user;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.org.OrgPicker;
import com.n4systems.fieldid.wicket.components.timezone.TimeZoneSelectorPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.CountryList;
import com.n4systems.util.timezone.Region;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.param;

public abstract class UserPage extends FieldIDFrontEndPage{

    @SpringBean
    protected UserService userService;

    protected IModel<User> user;
    protected Country country;
    protected Region region;

    protected abstract User doSave();

    @Override
    protected void onInitialize() {
        super.onInitialize();
        country = CountryList.getInstance().getCountryByFullName(user.getObject().getTimeZoneID());
        region = CountryList.getInstance().getRegionByFullId(user.getObject().getTimeZoneID());
        add(new FeedbackPanel("feedbackPanel"));
        add(new AddUserForm("addUserForm"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/pageStyles/user.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
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
                aNavItem().label("nav.view_all").page("userList.action").build(),
                aNavItem().label("nav.view_all_archived").page("archivedUserList.action").params(param("currentPage", 1)).build(),
                aNavItem().label("nav.add").page("addUser.action").onRight().build(),
                aNavItem().label("nav.import_export").page("userImportExport.action").onRight().build()

        ));
    }

    class AddUserForm extends Form {

        public AddUserForm(String id) {
            super(id);

            add(new OrgPicker("orgPicker", new PropertyModel<BaseOrg>(user, "owner")));
            add(new RequiredTextField<String>("email", new PropertyModel<String>(user, "emailAddress")));
            add(new RequiredTextField<String>("firstname", new PropertyModel<String>(user, "firstName")));
            add(new RequiredTextField<String>("lastname", new PropertyModel<String>(user, "lastName")));
            add(new TextField<String>("initials", new PropertyModel<String>(user, "initials")));
            add(new TextField<String>("identifier", new PropertyModel<String>(user, "identifier")));
            add(new TextField<String>("position", new PropertyModel<String>(user, "position")));
            add(new TimeZoneSelectorPanel("timeZoneContainer", new PropertyModel<String>(user, "timeZoneID")));

            add(new Button("save"));

            add(new Link<Void>("cancel") {
                @Override
                public void onClick() {
                    redirect("/userList.action?currentPage=1&listFilter=&userType=ALL");
                }
            });
        }

        @Override
        protected void onSubmit() {
            doSave();
            redirect("/userList.action?currentPage=1&listFilter=&userType=ALL");
        }
    }
}


