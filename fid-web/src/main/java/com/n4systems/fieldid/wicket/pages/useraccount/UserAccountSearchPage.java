package com.n4systems.fieldid.wicket.pages.useraccount;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.user.SendForgotUserEmailService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.feedback.TopFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDWicketPage;
import com.n4systems.model.user.User;
import com.n4systems.services.reporting.CriteriaTrendsResultCountRecord;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.List;

public class UserAccountSearchPage extends FieldIDWicketPage {

    @SpringBean
    protected UserService userService;

    @SpringBean
    protected SendForgotUserEmailService sendForgotUserEmailService;

    @SpringBean
    private S3Service s3Service;

    private FIDFeedbackPanel feedbackPanel;
    private TopFeedbackPanel topFeedbackPanel;



    public UserAccountSearchPage() {
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
        feedbackPanel.setOutputMarkupId(true);

        topFeedbackPanel = new TopFeedbackPanel("topFeedbackPanel");
        topFeedbackPanel.setOutputMarkupId(true);

        add(feedbackPanel);
        add(topFeedbackPanel);

        add(new GetUsernameForm("GetUsernameForm"));
        add(new StaticImage("tenantLogo", new Model<String>(s3Service.getBrandingLogoURL().toString())).setEscapeModelStrings(false));
        add(new ContextImage("password-icon", "/fieldid/images/password-icon.png"));
        add(new ContextImage("logo-icon", "/fieldid/images/logo-icon.png"));

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.renderCSSReference("style/pageStyles/login.css");
        response.renderCSSReference("newCss/layout/layout.css");
        response.renderCSSReference("style/branding/default.css");
        response.renderJavaScriptReference("jquery/watermark/jquery.watermark.js");

        response.renderCSSReference("style/colorbox.css");
        response.renderCSSReference("style/fieldid.css");
        response.renderCSSReference("style/public.css");
        response.renderCSSReference("style/reset.css");
        response.renderCSSReference("style/site_wide.css");

        response.renderCSSReference("style/jquery-redmond/jquery-ui-1.8.13.custom.css");

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
            rField.add(EmailAddressValidator.getInstance());
            add(rField);

            add(new AjaxSubmitLink("sendUsername") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    String email = getUserEmail();

                    if (null != email && email.length() > 0) {
                        sendUsername(email);
                    }
                    target.add(feedbackPanel, topFeedbackPanel);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel, topFeedbackPanel);
                }
            });



            add(new Link<Void>("cancel") {
                @Override
                public void onClick() {
                    redirect("/login.action");
                }
            });
        }



        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }
    }
    static class StaticImage extends WebComponent {
        public StaticImage(String id, IModel<String> urlModel) {
            super( id, urlModel );
        }

        @Override
        protected void onComponentTag(ComponentTag tag) {
            super.onComponentTag( tag );
            checkComponentTag( tag, "img" );
            tag.put( "src", getDefaultModelObjectAsString() );
        }
    }

}
