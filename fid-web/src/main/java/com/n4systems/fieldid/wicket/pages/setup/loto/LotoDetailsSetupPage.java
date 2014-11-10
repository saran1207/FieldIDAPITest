package com.n4systems.fieldid.wicket.pages.setup.loto;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.procedure.CustomLotoDetails;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class LotoDetailsSetupPage extends FieldIDTemplatePage {

    @SpringBean
    ProcedureDefinitionService procedureDefinitionService;

    private IModel<CustomLotoDetails> customLotoDetailsModel;

    public LotoDetailsSetupPage() {

        customLotoDetailsModel = Model.of(getCustomLotoDetails());

        Form form = new Form("form") {
            @Override
            protected void onSubmit() {
                CustomLotoDetails customLotoDetails = customLotoDetailsModel.getObject();

                if(customLotoDetails.getApplicationProcess() == null &&
                        customLotoDetails.getRemovalProcess() == null &&
                        customLotoDetails.getTestingAndVerification() == null ) {
                    procedureDefinitionService.deleteCustomLotoDetails(customLotoDetails);
                } else {
                    procedureDefinitionService.saveOrUpdateCustomLotoDetails(customLotoDetails);
                }
                setResponsePage(LotoSetupPage.class);
            }
        };

        form.add(new TextArea<String>("applicationProcess", new PropertyModel<>(customLotoDetailsModel, "applicationProcess")));
        form.add(new TextArea<String>("removalProcess", new PropertyModel<>(customLotoDetailsModel, "removalProcess")));
        form.add(new TextArea<String>("testingAndVerification", new PropertyModel<>(customLotoDetailsModel, "testingAndVerification")));


        form.add(new SubmitLink("saveLink"));
        form.add(new BookmarkablePageLink<LotoSetupPage>("cancelLink", LotoSetupPage.class));
        add(form);

    }

    private CustomLotoDetails getCustomLotoDetails() {
        CustomLotoDetails customLotoDetails =  procedureDefinitionService.getCustomLotoDetails();
        return customLotoDetails != null ? customLotoDetails : new CustomLotoDetails(getTenant());
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.loto_detail_fields"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        return new BookmarkablePageLink<LotoSetupPage>(linkId, LotoSetupPage.class)
                .add(new Label(linkLabelId, new FIDLabelModel("label.back_to_setup")));
    }
}
