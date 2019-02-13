package com.n4systems.fieldid.wicket.pages.useraccount.details;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.user.User;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class UserAccountFormIdentifiersPanel extends Panel {
    @SpringBean private UserService userService;
    private RequiredTextField userID;

    public UserAccountFormIdentifiersPanel(String id, IModel<User> user, IModel<InternalOrg> internalOrgIModel) {
        super(id, user);

        add(userID = new RequiredTextField<String>("userName", new PropertyModel<String>(user, "userID")));
        userID.add(new IValidator<String>() {
            @Override
            public void validate(IValidatable validatable) {
                if(!userService.userIdIsUnique(FieldIDSession.get().getTenant().getId(),
                        (String) validatable.getValue(), user.getObject().getId())) {
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

        add(new Label("primaryOrgName", new PropertyModel<String>(internalOrgIModel, "name")));
        EmailTextField emailTextField = new EmailTextField("email", new PropertyModel<String>(user, "emailAddress"));
        emailTextField.add(new Behavior() {
            @Override public void onComponentTag(Component c, ComponentTag tag) {
                FormComponent fc = (FormComponent) c;
                if (!fc.isValid()) {
                    tag.append("class", "error", " ");
                }
            }
        });
        add(emailTextField.setRequired(true));

        RequiredTextField firstName = new RequiredTextField<String>("firstName", new PropertyModel<String>(user, "firstName"));
        firstName.add(new Behavior() {
            @Override public void onComponentTag(Component c, ComponentTag tag) {
                FormComponent fc = (FormComponent) c;
                if (!fc.isValid()) {
                    tag.append("class", "error", " ");
                }
            }
        });
        add(firstName);

        RequiredTextField lastName = new RequiredTextField<String>("lastName", new PropertyModel<String>(user, "lastName"));
        lastName.add(new Behavior() {
            @Override public void onComponentTag(Component c, ComponentTag tag) {
                FormComponent fc = (FormComponent) c;
                if (!fc.isValid()) {
                    tag.append("class", "error", " ");
                }
            }
        });
        add(lastName);

        add(new TextField<String>("initials", new PropertyModel<String>(user, "initials")));
        add(new TextField<String>("position", new PropertyModel<String>(user, "position")));
    }

    protected void onOwnerPicked(AjaxRequestTarget target) {}
}
