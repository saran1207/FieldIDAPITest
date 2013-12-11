package com.n4systems.fieldid.wicket.pages.useraccount;

import com.n4systems.fieldid.service.user.SendForgotUserEmailService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDWicketPage;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import java.net.URI;
import java.util.List;

public class UserAccountSearchPage extends FieldIDWicketPage {

    @SpringBean
    protected UserService userService;
    @SpringBean
    protected SendForgotUserEmailService sendForgotUserEmailService;

    public UserAccountSearchPage() {
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        add(new Label("title", "UserAccountSearch"));
        add(new FeedbackPanel("feedbackPanel"));
        add(new GetUsernameForm("GetUsernameForm"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.renderCSSReference("pageStyles/login.css");
        response.renderJavaScriptReference("jquery/watermark/jquery.watermark.js");

    }

    protected void sendUsername(String emailAddress) {
        List<User> users = userService.findUsersByEmailAddress(emailAddress);

//        if (users.size() == 1) {
            sendForgotUserEmailService.sendForgotUserEmail(emailAddress, users.get(0), getBaseURI());
            info("Your user name has been sent to the following email address: " + emailAddress);
//        }

    }

    public URI getBaseURI() {
        return URI.create(getServletRequest().getRequestURL().toString()).resolve(getServletRequest().getContextPath() + "/");
    }

    class GetUsernameForm extends Form {

        private String userEmail;

        public GetUsernameForm(String id) {
            super(id);

            RequiredTextField rField = new RequiredTextField<String>("userEmail", new PropertyModel<String>(this, "userEmail"));
            rField.setRequired(true);
            rField.setLabel(new FIDLabelModel("label.useremail"));
            rField.add(EmailAddressValidator.getInstance());
            add(rField);

            add(new Button("sendUsername"));

            add(new Link<Void>("cancel") {
                @Override
                public void onClick() {
                    redirect("/login.action");
                }
            });
        }

        @Override
        protected void onSubmit() {
            String email = getUserEmail();

            if (null != email && email.length() > 0) {
                sendUsername(email);
            }

        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }
    }

}
