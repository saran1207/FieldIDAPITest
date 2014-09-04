package com.n4systems.fieldid.wicket.pages.setup.userregistration;

import com.n4systems.fieldid.actions.users.WelcomeMessage;
import com.n4systems.fieldid.service.user.SendWelcomeEmailService;
import com.n4systems.fieldid.service.user.UserRequestService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.org.OrgLocationPicker;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.OwnersUsersLocationsPage;
import com.n4systems.model.UserRequest;
import com.n4systems.model.user.User;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class ViewUserRequestPage extends FieldIDTemplatePage {

    @SpringBean
    UserRequestService userRequestService;

    @SpringBean
    private SendWelcomeEmailService sendWelcomeEmailService;

    private Long uniqueId;
    private IModel<UserRequest> userRequestModel;

    private FIDFeedbackPanel feedbackPanel;
    private WebMarkupContainer infoPanel;
    private WebMarkupContainer setOwnerPanel;

    public ViewUserRequestPage(PageParameters params) {
        super(params);
        uniqueId = params.get("uniqueID").toLong();
        userRequestModel = Model.of(userRequestService.getUserRequest(uniqueId));

        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

        add(infoPanel = new WebMarkupContainer("infoPanel"));
        infoPanel.setOutputMarkupPlaceholderTag(true);

        infoPanel.add(new Label("created", new DayDisplayModel(new PropertyModel<Date>(userRequestModel, "created")).includeTime().withTimeZone(getSessionUser().getTimeZone())));
        infoPanel.add(new Label("userid", new PropertyModel<String>(userRequestModel, "userAccount.userID")));
        infoPanel.add(new Label("email", new PropertyModel<String>(userRequestModel, "userAccount.emailAddress")));
        infoPanel.add(new Label("firstName", new PropertyModel<String>(userRequestModel, "userAccount.firstName")));
        infoPanel.add(new Label("lastName", new PropertyModel<String>(userRequestModel, "userAccount.lastName")));
        infoPanel.add(new Label("position", new PropertyModel<String>(userRequestModel, "userAccount.position")));
        infoPanel.add(new Label("city", new PropertyModel<String>(userRequestModel, "city")));
        infoPanel.add(new Label("timezone", new PropertyModel<String>(userRequestModel, "userAccount.timeZone.displayName")));
        infoPanel.add(new Label("companyName", new PropertyModel<String>(userRequestModel, "companyName")));
        infoPanel.add(new Label("phoneNumber", new PropertyModel<String>(userRequestModel, "phoneNumber")));
        infoPanel.add(new Label("comments", new PropertyModel<String>(userRequestModel, "comment")));

        infoPanel.add(new AjaxLink("acceptLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                infoPanel.setVisible(false);
                setOwnerPanel.setVisible(true);
                target.add(infoPanel, setOwnerPanel);
            }
        });

        infoPanel.add(new Link<Void>("rejectLink") {
            @Override
            public void onClick() {
                userRequestService.denyRequest(userRequestModel.getObject());
                setResponsePage(UserRequestListPage.class);
            }
        });

        add(setOwnerPanel = new WebMarkupContainer("setOwnerPanel"));
        setOwnerPanel.setOutputMarkupPlaceholderTag(true);
        setOwnerPanel.setVisible(false);
        Form form;

        setOwnerPanel.add(form = new Form<Void>("form") {
            @Override
            protected void onSubmit() {
                userRequestService.acceptRequest(userRequestModel.getObject());
                User userAccount = userRequestModel.getObject().getUserAccount();
                sendWelcomeEmail(userAccount, new WelcomeMessage(), userAccount.getHashPassword() != null);
                setResponsePage(UserRequestListPage.class);
            }
        });

        form.add(new Label("companyName", new PropertyModel<String>(userRequestModel, "companyName")));
        OrgLocationPicker ownerPicker = new OrgLocationPicker("ownerPicker", new PropertyModel(userRequestModel,"userAccount.owner")) {
            @Override protected void onChanged(AjaxRequestTarget target) { }

            @Override protected void onError(AjaxRequestTarget target, RuntimeException e) { }
        }.withAutoUpdate();
        form.add(ownerPicker.setRequired(true).setLabel(new FIDLabelModel("label.owner")));

        form.add(new SubmitLink("submitLink"));
        form.add(new AjaxLink<Void>("cancelLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                infoPanel.setVisible(true);
                setOwnerPanel.setVisible(false);
                target.add(infoPanel, setOwnerPanel);
            }
        });
    }

    protected void sendWelcomeEmail(User user, WelcomeMessage welcomeMessage, boolean passwordAssigned) {
        sendWelcomeEmailService.sendUserWelcomeEmail(!passwordAssigned, user, welcomeMessage.getPersonalMessage(), createActionUrlBuilder());
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("nav.view_all.count", userRequestService.countAllUserRequests())).page(UserRequestListPage.class).build(),
                aNavItem().label("nav.view").page(ViewUserRequestPage.class).params(PageParametersBuilder.uniqueId(uniqueId)).build()
        ));
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_user_registration_request"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, OwnersUsersLocationsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }
}
