package com.n4systems.fieldid.wicket.pages.useraccount.details;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.useraccount.AccountSetupPage;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.user.User;
import com.n4systems.security.UserType;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class UserDetailsPage extends AccountSetupPage {

    protected static final Logger logger = Logger.getLogger(UserDetailsPage.class);

    @SpringBean private UserService userService;
    @SpringBean private OrgService orgService;

    private UserType userType;
    private Long uniqueId;
    private IModel<User> userModel;
    private IModel<InternalOrg> internalOrgIModel;
    private RequiredTextField userID;

    public UserDetailsPage(IModel<User> userModel, IModel<InternalOrg> primaryOrgModel) {
        super();
        this.uniqueId = userModel.getObject().getId();
        this.userModel = userModel;
        this.internalOrgIModel = primaryOrgModel;
        addComponents();
    }

    public UserDetailsPage(PageParameters parameters) {
        super(parameters);
        uniqueId = parameters.get("uniqueID").toLong();
        userModel = Model.of(loadExistingUser());
        internalOrgIModel = Model.of(getPrimaryOrg());
        addComponents();
    }

    public UserDetailsPage() {
        super();
        uniqueId = getSessionUser().getId();
        userModel = Model.of(loadExistingUser());
        internalOrgIModel = Model.of(getPrimaryOrg());
        addComponents();
    }

    private void addComponents() {

        Form form = new Form("form") {
            @Override
            protected void onSubmit() {
                updateUser();
                setResponsePage(UserDetailsPage.class);
            }
        };
        add(form);

        form.add(new Link("cancel") {
            @Override
            public void onClick() {
                setResponsePage(UserDetailsPage.class);
            }
        });

        form.add(userID = new RequiredTextField<String>("userName", new PropertyModel<String>(userModel, "userID")));
        userID.add(new IValidator<String>() {
            @Override
            public void validate(IValidatable validatable) {
                if(userService != null && !userService.userIdIsUnique(FieldIDSession.get().getTenant().getId(),
                        (String) validatable.getValue(), userModel.getObject().getId())) {
                    ValidationError error = new ValidationError();
                    error.addMessageKey("errors.data.userduplicate");
                    validatable.error(error);
                }
            }
        });
        userID.add(new Behavior() {
            @Override public void onComponentTag(Component c, ComponentTag tag) {
                FormComponent fc = (FormComponent) c;
                if (!fc.isValid()) {
                    tag.append("class", "error", " ");
                }
            }
        });

        form.add(new Label("primaryOrgName", new PropertyModel<String>(internalOrgIModel, "name")));
        EmailTextField emailTextField = new EmailTextField("email", new PropertyModel<String>(userModel, "emailAddress"));
        emailTextField.add(new Behavior() {
            @Override public void onComponentTag(Component c, ComponentTag tag) {
                FormComponent fc = (FormComponent) c;
                if (!fc.isValid()) {
                    tag.append("class", "error", " ");
                }
            }
        });
        form.add(emailTextField.setRequired(true));

        RequiredTextField firstName = new RequiredTextField<String>("firstName", new PropertyModel<String>(userModel, "firstName"));
        firstName.add(new Behavior() {
            @Override public void onComponentTag(Component c, ComponentTag tag) {
                FormComponent fc = (FormComponent) c;
                if (!fc.isValid()) {
                    tag.append("class", "error", " ");
                }
            }
        });
        form.add(firstName);

        RequiredTextField lastName = new RequiredTextField<String>("lastName", new PropertyModel<String>(userModel, "lastName"));
        lastName.add(new Behavior() {
            @Override public void onComponentTag(Component c, ComponentTag tag) {
                FormComponent fc = (FormComponent) c;
                if (!fc.isValid()) {
                    tag.append("class", "error", " ");
                }
            }
        });
        form.add(lastName);

        form.add(new TextField<String>("initials", new PropertyModel<String>(userModel, "initials")));
        form.add(new TextField<String>("position", new PropertyModel<String>(userModel, "position")));
    }

    protected User loadExistingUser() {
        return getCurrentUser();
    }
    protected InternalOrg getPrimaryOrg() {
        return FieldIDSession.get().getPrimaryOrg();
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.myaccount"));
    }

    protected void updateUser() {
        User user = userModel.getObject();

        userService.update(user);
        // Next line updates SessionUser with updated user account
        FieldIDSession.get().setUser(user);
        logger.info("account profile updated for " + getSessionUser().getUserID());
        FieldIDSession.get().info(new FIDLabelModel("message.user_saved").getObject());

        return ;
    }

}


