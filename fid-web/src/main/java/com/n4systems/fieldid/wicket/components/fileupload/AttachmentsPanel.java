package com.n4systems.fieldid.wicket.components.fileupload;

import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.model.FileNameModel;
import com.n4systems.model.FileAttachment;
import com.n4systems.reporting.PathHandler;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class AttachmentsPanel extends Panel {

    private static final Logger logger = Logger.getLogger(AttachmentsPanel.class);

    public AttachmentsPanel(String id, IModel<List<FileAttachment>> attachments) {
        super(id);
        add(new UploadForm("uploadForm", attachments));
    }

    class UploadForm extends Form<List<FileAttachment>> {

        FileUploadField uploadField;

        public UploadForm(String id, final IModel<List<FileAttachment>> attachments) {
            super(id, attachments);
            setOutputMarkupId(true);

            add(uploadField = new FileUploadField("file"));

            final WebMarkupContainer attachedFilesContainer = new WebMarkupContainer("attachedFilesContainer");
            attachedFilesContainer.setOutputMarkupPlaceholderTag(true);
            attachedFilesContainer.add(new ListView<FileAttachment>("attachedFiles", attachments) {
                @Override
                protected void populateItem(final ListItem<FileAttachment> item) {
                    item.add(new Label("fileName", new FileNameModel(new PropertyModel<String>(item.getModel(), "fileName"))));
                    item.add(new TextArea<String>("comments", new PropertyModel<String>(item.getModel(), "comments"))
                            .add(new UpdateComponentOnChange()));
                    item.add(new AjaxLink<Void>("removeLink") {
                        @Override
                        public void onClick(AjaxRequestTarget target) {
                            attachments.getObject().remove(item.getIndex());
                            target.add(UploadForm.this);
                        }
                    });
                }
            });
            add(new UploadProgressBar("progress", this, uploadField));
            add(attachedFilesContainer);

            uploadField.add(new AjaxFormSubmitBehavior("onchange") {
                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    target.add(UploadForm.this);
                }

                @Override
                protected void onError(AjaxRequestTarget target) {
                }
            });
        }

        @Override
        protected void onSubmit() {
            FileUpload fileUpload = uploadField.getFileUpload();

            if (fileUpload != null) {
                File tempFilePath = PathHandler.getTempFile(fileUpload.getClientFileName());
                String uploadedFilePath = new File( tempFilePath.getParent() ).getName() + '/' + tempFilePath.getName();
                try {
                    IOUtils.copy(fileUpload.getInputStream(), new FileOutputStream(tempFilePath));

                    FileAttachment attachment = new FileAttachment();
                    attachment.setFileName(uploadedFilePath);

                    getModelObject().add(attachment);

                } catch(IOException e) {
                    logger.error("error with attaching file", e);
                }
            }
        }

    }


}
