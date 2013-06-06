package com.n4systems.fieldid.wicket.components.user;

import com.n4systems.fieldid.actions.users.UploadedImage;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.fieldid.wicket.components.MultiSelectDropDownChoice;
import com.n4systems.fieldid.wicket.components.org.OrgPicker;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.reporting.PathHandler;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class UserFormIdentifiersPanel extends Panel {

    private static final Logger logger = Logger.getLogger(UserFormIdentifiersPanel.class);

    @SpringBean
    private UserGroupService userGroupService;

    private UploadedImage uploadedImage;

    public UserFormIdentifiersPanel(String id, IModel<User> user) {
        super(id, user);

        add(new OrgPicker("orgPicker", new PropertyModel<BaseOrg>(user, "owner")));
        add(new MultiSelectDropDownChoice<UserGroup>("group",
                new PropertyModel<List<UserGroup>>(user, "groups"),
                userGroupService.getActiveUserGroups(),
                new ListableChoiceRenderer<UserGroup>()).setVisible(!user.getObject().isPerson()));
        add(new RequiredTextField<String>("email", new PropertyModel<String>(user, "emailAddress")));
        add(new RequiredTextField<String>("firstname", new PropertyModel<String>(user, "firstName")));
        add(new RequiredTextField<String>("lastname", new PropertyModel<String>(user, "lastName")));
        add(new TextField<String>("initials", new PropertyModel<String>(user, "initials")));
        add(new TextField<String>("identifier", new PropertyModel<String>(user, "identifier")));
        add(new TextField<String>("position", new PropertyModel<String>(user, "position")));

        uploadedImage = new UploadedImage();
        add(new UploadForm("uploadForm").setVisible(!user.getObject().isPerson()));

    }

    class UploadForm extends Form<FileAttachment> {
        FileUploadField uploadField;
        Label fileName;

        public UploadForm(String id) {
            super(id);
            setOutputMarkupId(true);

            final WebMarkupContainer fileDisplay = new WebMarkupContainer("fileDisplay");

            add(uploadField = new FileUploadField("digitalSignature"));
            uploadField.add(new AjaxFormSubmitBehavior("onchange") {
                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    FileUpload fileUpload = uploadField.getFileUpload();
                    fileName.setDefaultModelObject(fileUpload.getClientFileName());
                    saveFileAttachment(fileUpload);
                    fileDisplay.setVisible(true);
                    uploadField.setVisible(false);
                    target.add(UploadForm.this);
                }

                @Override
                protected void onError(AjaxRequestTarget target) {
                }
            });

            fileDisplay.add(fileName = new Label("fileName", Model.of(new String())));
            fileDisplay.add(new AjaxLink<Void>("removeLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    fileDisplay.setVisible(false);
                    uploadField.setVisible(true);
                    target.add(UploadForm.this);
                }
            });
            fileDisplay.setOutputMarkupId(true);
            fileDisplay.setVisible(false);
            add(fileDisplay);
        }
    }

    public void saveFileAttachment(FileUpload fileUpload) {
        if (fileUpload != null) {
            File tempFilePath = PathHandler.getTempFile(fileUpload.getClientFileName());
            String uploadedFilePath = new File( tempFilePath.getParent() ).getName() + '/' + tempFilePath.getName();

            UploadedImage newImage = new UploadedImage();
            try {
                IOUtils.copy(fileUpload.getInputStream(), new FileOutputStream(tempFilePath));
                newImage.setUploadDirectory(uploadedFilePath);
                newImage.setImage(new File(uploadedFilePath));
                newImage.setNewImage(true);
                uploadedImage = newImage;
            } catch (IOException e) {
                logger.error("error with attaching file", e);
            }
        }
    }

    public UploadedImage getUploadedImage() {
        return uploadedImage;
    }

}
