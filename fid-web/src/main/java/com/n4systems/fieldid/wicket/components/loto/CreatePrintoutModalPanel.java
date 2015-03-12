package com.n4systems.fieldid.wicket.components.loto;

import com.n4systems.fieldid.service.procedure.LotoReportService;
import com.n4systems.fieldid.service.task.DownloadLinkService;
import com.n4systems.model.LotoPrintoutType;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * This Panel is used to populate a modal window which gives the user an opportunity to rename the DownloadLink for
 * their desired
 *
 * Created by jheath on 15-03-06.
 */
public abstract class CreatePrintoutModalPanel extends Panel {
    @SpringBean
    private DownloadLinkService downloadLinkService;

    @SpringBean
    private LotoReportService lotoReportService;

    private IModel<ProcedureDefinition> procedureDefinitionModel;
    private Model<DownloadLink> downloadLinkModel;
    private Form<DownloadLink> form;

    /**
     * This is the Constructor for the CreatePrintoutModalPanel.  This panel is used to initiate the printing process
     * of a LOTO Printout and allows the user to change the name of that report and then be navigated to the Downloads
     * page if they so choose.
     *
     * @param id - A String representing the Wicket ID where this component will be placed.
     * @param procedureDefinitionModel - An IModel populated with a ProcedureDefinition.
     * @param type - A LotoPrintoutType enum representing the type of LOTO Printout the user wants created.
     */
    public CreatePrintoutModalPanel(String id,
                                    IModel<ProcedureDefinition> procedureDefinitionModel,
                                    LotoPrintoutType type) {

        super(id, procedureDefinitionModel);

        this.procedureDefinitionModel = procedureDefinitionModel;
        this.downloadLinkModel = Model.of(lotoReportService.generateLotoPrintout(procedureDefinitionModel.getObject(), type));
    }

    /**
     * We take care of decorating the Panel here, just in case we end up using more than one constructor or entry point.
     */
    @Override
    protected void onInitialize() {
        //Huh... I just thought... I hope this thing actually executes before the assignments in the constructor.  That
        //would be disastrous if it didn't.
        super.onInitialize();

        add(form = new Form<>("printForm", downloadLinkModel));

        form.add(new TextField<>("name", new PropertyModel<>(downloadLinkModel, "name")));
        form.add(new AjaxButton("saveAndGoToDownloadsButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                downloadLinkService.update((DownloadLink)form.getModelObject());

                //This is where we want to navigate to the Downloads page.
                target.appendJavaScript("window.parent.location = '/fieldid/showDownloads.action';");
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                //Again, I'm not too sure we can actually experience errors here.  This stuff is happening
                //asynchronously, so I think it's pretty much invisible to the Panel.
            }
        });

        form.add(new AjaxSubmitLink("saveAndCloseLink") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                //We want to save the DownloadLink here... originally, this was in an override of the onSubmit method
                //for the form... but that didn't appear ot be working, so we'll force it to save here.
                downloadLinkService.update((DownloadLink)form.getModelObject());

                //This is also where we want to close the modal.
                closeModal(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                //I don't think an error can happen... lets go with that assumption.
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/pages/loto-printout-modal.css");
    }

    protected abstract void closeModal(AjaxRequestTarget target);
}
