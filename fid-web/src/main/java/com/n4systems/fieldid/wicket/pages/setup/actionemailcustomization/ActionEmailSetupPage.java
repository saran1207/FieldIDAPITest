package com.n4systems.fieldid.wicket.pages.setup.actionemailcustomization;

import com.n4systems.fieldid.service.ActionEmailCustomizationService;
import com.n4systems.fieldid.wicket.components.text.LabelledTextArea;
import com.n4systems.fieldid.wicket.components.text.LabelledTextField;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.SettingsPage;
import com.n4systems.model.notificationsettings.ActionEmailCustomization;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * This page allows the user to customize values for the new Action notification email.  Currently, the user is only
 * able to set custom values for the email Subject line and for the "sub-heading" in the email, which is the first
 * line within the email.
 *
 * Created by Jordan Heath on 15-03-13.
 */
public class ActionEmailSetupPage extends FieldIDTemplatePage {
    @SpringBean
    private ActionEmailCustomizationService actionEmailCustomizationService;

    private Model<ActionEmailCustomization> actionEmailCustomizationModel;

    /**
     * Default constructor... it currently does nothing except look pretty.
     */
    public ActionEmailSetupPage() {
        //Do nothing, look pretty.
        ActionEmailCustomization actionEmailCustomization = actionEmailCustomizationService.read();
        actionEmailCustomizationModel = Model.of(actionEmailCustomization);

        Form customizationForm = new Form("customizationForm") {
            @Override
            protected void onSubmit() {
                ActionEmailCustomization entity;
                if(actionEmailCustomizationModel.getObject().getId() == null) {
                    entity = actionEmailCustomizationService.save(actionEmailCustomizationModel.getObject());
                } else {
                    entity = actionEmailCustomizationService.update(actionEmailCustomizationModel.getObject());
                }
                if(entity != null) {
                    info("Action Email Customization successfully saved");
                } else {
                    error("There appears to have been an error when attempting to save your email customization changes.");
                }

                setResponsePage(new ActionEmailSetupPage());
            }
        };

        LabelledTextField<String> emailSubjectField = new LabelledTextField<>("emailSubjectTextBox", "label.email_subject", new PropertyModel<>(actionEmailCustomizationModel, "emailSubject"));
        emailSubjectField.setMaxLength(255);
        customizationForm.add(emailSubjectField);

        LabelledTextArea<String> subHeadingField = new LabelledTextArea<>("subHeadingTextArea", "label.sub_heading", new PropertyModel<>(actionEmailCustomizationModel, "subHeading"));
        subHeadingField.setMaxLength(2048);
        customizationForm.add(subHeadingField);

        customizationForm.add(new SubmitLink("saveLink"));
        customizationForm.add(new BookmarkablePageLink<SettingsPage>("cancelLink", SettingsPage.class));

        add(customizationForm);
    }

//    @Override
//    public void onInitialize() {
//        super.onInitialize();
//
//        Form customizationForm = new Form("customizationForm") {
//            @Override
//            protected void onSubmit() {
//                ActionEmailCustomization entity;
//                if(actionEmailCustomizationModel.getObject().getId() == null) {
//                    entity = actionEmailCustomizationService.save(actionEmailCustomizationModel.getObject());
//                } else {
//                    entity = actionEmailCustomizationService.update(actionEmailCustomizationModel.getObject());
//                }
//                if(entity != null) {
//                    info("Action Email Customization successfully saved");
//                } else {
//                    error("There appears to have been an error when attempting to save your email customization changes.");
//                }
//
//                setResponsePage(new ActionEmailCustomizationSetupPage());
//            }
//        };
//
//        LabelledTextField<String> emailSubjectField = new LabelledTextField<>("emailSubjectTextBox", "label.email_subject", new PropertyModel<>(actionEmailCustomizationModel, "emailSubject"));
//        emailSubjectField.setMaxLength(255);
//        customizationForm.add(emailSubjectField);
//
//        LabelledTextArea<String> subHeadingField = new LabelledTextArea<>("subHeadingTextArea", "label.sub_heading", new PropertyModel<>(actionEmailCustomizationModel, "subHeading"));
//        subHeadingField.setMaxLength(2048);
//        customizationForm.add(subHeadingField);
//
//        customizationForm.add(new SubmitLink("saveLink"));
//        customizationForm.add(new BookmarkablePageLink<SettingsPage>("cancelLink", SettingsPage.class));
//
//        add(customizationForm);
//    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.customize_action_email"));
    }
}
