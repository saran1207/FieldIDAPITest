package com.n4systems.fieldid.wicket.pages.admin.printouts;

import com.n4systems.fieldid.service.procedure.LotoReportService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.text.LabelledDropDown;
import com.n4systems.fieldid.wicket.components.text.LabelledRequiredTextField;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.LotoPrintout;
import com.n4systems.model.LotoPrintoutType;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static ch.lambdaj.Lambda.on;


/**
 * Created by jheath on 2014-10-29.
 */
public class AddLotoTemplatePanel extends Panel {

    private FIDFeedbackPanel feedback;
    private Component feedbackPanel;

    private @SpringBean
    LotoReportService lotoReportService;

    private  @SpringBean
    UserService userService;

    public Long tenantId;

    public IModel<LotoPrintout> printout = Model.of(new LotoPrintout());
    public LabelledRequiredTextField textField;
    public LabelledDropDown<String> dropDown;
    public FileUploadField upload = new FileUploadField("fileUploader");

    public AddLotoTemplatePanel(String id, Long tenantId) {
        super(id);
        this.tenantId = tenantId;

    }

    @Override
    public void onInitialize() {
        super.onInitialize();
        feedback = new FIDFeedbackPanel("feedbackPanel");
        feedback.setVisible(true);
        add(feedback);

        //add(feedbackPanel = new FIDFeedbackPanel("feedback").setOutputMarkupId(true));

        setPrintoutInfo();

        Form<Void> form = new Form<Void>("form");


        form.add(feedbackPanel = new FIDFeedbackPanel("feedback").setOutputMarkupId(true));

        form.add(new AjaxSubmitLink("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if(lotoReportService.exists(printout.getObject())) {
                    error(new FIDLabelModel("message.duplicate_loto_name").getObject());
                    //target.add(getTopFeedbackPanel(), feedbackPanel);
                    target.add(getTopFeedbackPanel());
                } else {
                    FileUpload fileUpload = upload.getFileUpload();
                    try {
                        File file = fileUpload.writeToTempFile();
                        //unZipIt(file.getAbsolutePath(), printout.getObject());
                        lotoReportService.saveLotoReport(file, printout.getObject());
                        getTopFeedbackPanel().setVisible(false);
                        info(new FIDLabelModel("message.loto_saved").getObject());
                        setResponsePage(LotoPrintoutListPage.class, PageParametersBuilder.param("id", tenantId));
                        //target.add(getTopFeedbackPanel(), feedbackPanel);
                    } catch (IOException e) {
                        error(new FIDLabelModel("message.loto_not_saved").getObject());
                        target.add(getTopFeedbackPanel());
                    }
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {

            }
        });

        /*  @Override
            public void onSubmit() {

                if(lotoReportService.exists(printout.getObject())) {
                   warn(new FIDLabelModel("message.duplicate_loto_name").getObject());
                } else {
                    FileUpload fileUpload = upload.getFileUpload();
                    try {
                        File file = fileUpload.writeToTempFile();
                        //unZipIt(file.getAbsolutePath(), printout.getObject());
                        lotoReportService.saveLotoReport(file, printout.getObject());
                        info(new FIDLabelModel("message.loto_saved").getObject());
                    } catch (IOException e) {
                        error(new FIDLabelModel("message.loto_not_saved").getObject());
                    }
                }
            }
        };*/

        textField = new LabelledRequiredTextField<String>("name", "label.name", ProxyModel.of(printout, on(LotoPrintout.class).getPrintoutName()));
        form.add(textField);

        dropDown = new LabelledDropDown<String>("typeSelector", "label.loto_type", ProxyModel.of(printout, on(LotoPrintout.class).getPrintoutTypeLabel())) {
                    @Override
                    public List<String> getChoices() {
                        return LotoPrintoutType.LONG.getLotoPrintoutTypeList();
                    }
                };
        form.add(dropDown);

        upload.setOutputMarkupId(true);
        form.add(upload);

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


    public FIDFeedbackPanel getTopFeedbackPanel() {
        return feedback;
    }

    public void setPrintoutInfo(){
        printout.getObject().setTenant(lotoReportService.getTenantById(tenantId));
        //printout.getObject().setCreatedBy(userService.getUser(FieldIDSession.get().getSessionUser().getId()));
        printout.getObject().setCreated(new Date());
    }

}
