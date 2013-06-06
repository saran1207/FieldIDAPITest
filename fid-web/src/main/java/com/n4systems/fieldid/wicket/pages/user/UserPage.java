package com.n4systems.fieldid.wicket.pages.user;

import com.n4systems.fieldid.actions.users.FileSystemUserSignatureFileProcessor;
import com.n4systems.fieldid.actions.users.UploadedImage;
import com.n4systems.fieldid.actions.users.WelcomeMessage;
import com.n4systems.fieldid.service.user.SendWelcomeEmailService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.user.UserFormIdentifiersPanel;
import com.n4systems.fieldid.wicket.components.user.UserFormLocalizationPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.CountryList;
import com.n4systems.util.timezone.Region;
import com.n4systems.util.uri.ActionURLBuilder;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.net.URI;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;
import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.param;

public abstract class UserPage extends FieldIDFrontEndPage{

    @SpringBean
    protected UserService userService;

    @SpringBean
    private SendWelcomeEmailService sendWelcomeEmailService;

    protected IModel<User> user;
    protected Country country;
    protected Region region;

    protected abstract User doSave();

    protected abstract Component createAccountPanel(String id, Form form);

    protected abstract Component createPermissionsPanel(String id);

    protected UserFormIdentifiersPanel identifiersPanel;
    protected Component accountPanel;
    protected Component permissionsPanel;

    protected void saveSignatureFile(UploadedImage signature) {
        new FileSystemUserSignatureFileProcessor(PathHandler.getSignatureImage(user.getObject())).process(signature);
    }

    protected void sendWelcomeEmail(User user, WelcomeMessage welcomeMessage, boolean passwordAssigned) {
        sendWelcomeEmailService.sendUserWelcomeEmail(!passwordAssigned, user, welcomeMessage.getPersonalMessage(), createActionUrlBuilder());
    }

    protected ActionURLBuilder createActionUrlBuilder() {
        return new ActionURLBuilder(getBaseURI(), getConfigurationProvider());
    }

    public URI getBaseURI() {
        // creates a URI based on the current url, and resolved against the context path which should be /fieldid.  We add on the extra / since we currently need it.
        return URI.create(getServletRequest().getRequestURL().toString()).resolve(getServletRequest().getContextPath() + "/");
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        country = CountryList.getInstance().getCountryByFullName(user.getObject().getTimeZoneID());
        region = CountryList.getInstance().getRegionByFullId(user.getObject().getTimeZoneID());
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(new AddUserForm("addUserForm"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/pageStyles/user.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
        response.renderCSSReference("style/newCss/layout/feedback_errors.css");
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

            add(identifiersPanel = new UserFormIdentifiersPanel("identifiersPanel", user));

            add(new UserFormLocalizationPanel("localizationPanel", user));

            add(accountPanel = createAccountPanel("accountPanel", this));

            add(permissionsPanel = createPermissionsPanel("permissionsPanel"));

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


