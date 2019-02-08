package com.n4systems.fieldid.wicket.pages.useraccount.details;

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

public class UserAccountFormIdentifiersPanel extends Panel {

    public UserAccountFormIdentifiersPanel(String id, IModel<User> user, IModel<InternalOrg> internalOrgIModel) {
        super(id, user);

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
        add(new TextField<String>("identifier", new PropertyModel<String>(user, "identifier")));
        add(new TextField<String>("position", new PropertyModel<String>(user, "position")));
    }

    protected void onOwnerPicked(AjaxRequestTarget target) {}
}
