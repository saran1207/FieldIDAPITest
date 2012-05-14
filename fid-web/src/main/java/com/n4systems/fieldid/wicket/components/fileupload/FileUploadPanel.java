package com.n4systems.fieldid.wicket.components.fileupload;

import com.n4systems.model.FileAttachment;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
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
import org.apache.wicket.util.lang.Bytes;

import java.util.List;

public class FileUploadPanel extends Panel {

    public FileUploadPanel(String id, IModel<List<FileAttachment>>  attachments) {
        super(id);
        add(new UploadForm("uploadForm", attachments));
    }

    class UploadForm extends Form<List<FileAttachment>> {

        FileUploadField uploadField;

        public UploadForm(String id, IModel<List<FileAttachment>> attachments) {
            super(id, attachments);
            setOutputMarkupId(true);
            add(uploadField = new FileUploadField("file"));
            final WebMarkupContainer attachedFilesContainer = new WebMarkupContainer("attachedFilesContainer");
            attachedFilesContainer.setOutputMarkupPlaceholderTag(true);
            attachedFilesContainer.add(new ListView<FileAttachment>("attachedFiles", attachments) {
                @Override
                protected void populateItem(ListItem<FileAttachment> item) {
                    item.add(new Label("fileName", new PropertyModel<String>(item.getModel(), "fileName")));
                    item.add(new TextArea<String>("comments", new PropertyModel<String>(item.getModel(), "comments")));
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
                System.out.println("We got: "+ fileUpload.getClientFileName() + " size: " + Bytes.bytes(fileUpload.getSize()));
                FileAttachment attachment = new FileAttachment();
                attachment.setFileName(fileUpload.getClientFileName());
                getModelObject().add(attachment);
            } else {
                System.out.println("Nothing was uploaded TT");
            }
        }

    }


}
