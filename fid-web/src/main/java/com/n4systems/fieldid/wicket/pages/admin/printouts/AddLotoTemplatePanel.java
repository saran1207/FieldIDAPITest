package com.n4systems.fieldid.wicket.pages.admin.printouts;

import com.n4systems.fieldid.wicket.components.text.LabelledDropDown;
import com.n4systems.model.LotoPrintout;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;


/**
 * Created by jheath on 2014-10-29.
 */
public class AddLotoTemplatePanel extends Panel {

    public AddLotoTemplatePanel(String id) {
        super(id);
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        Form<Void> form = new Form<Void>("form") {
            @Override
            public void onSubmit() {
                //Do whatever needs to be done when they submit what they want to upload.
            }
        };

        form.add(new LabelledDropDown<>("typeSelector", "", Model.of(new LotoPrintout())));

        form.add(new FileUploadField("fileUploader"));

        Button submitButton;
        form.add(submitButton = new Button("saveButton"));
        submitButton.setOutputMarkupId(true);

        form.add(new Link("cancelLink") {
            @Override
            public void onClick() {
                doCancel();
            }
        });

        add(form);
    }

    protected void doCancel() {

    }
}
