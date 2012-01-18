package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.search.EventReportCriteriaModel;

public class SaveReportPage extends FieldIDFrontEndPage {

    @SpringBean
    private SavedReportService savedReportService;

    private EventReportCriteriaModel criteriaModel;
    private WebPage backToPage;
    private boolean overwrite;

    private String name;

    public SaveReportPage(EventReportCriteriaModel criteriaModel, WebPage backToPage, boolean overwrite) {
        this.criteriaModel = criteriaModel;
        this.backToPage = backToPage;
        this.overwrite = overwrite;
        if (overwrite && criteriaModel.getSavedReportItem() != null) {
            name = criteriaModel.getSavedReportItem().getName();
        }
        add(new SaveReportForm("saveReportForm"));
    }

    class SaveReportForm extends Form {

        public SaveReportForm(String id) {
            super(id);

            add(new FIDFeedbackPanel("feedbackPanel"));
            add(new RequiredTextField<String>("name", new PropertyModel<String>(SaveReportPage.this, "name")));
            add(new Link("cancelLink") {
                @Override
                public void onClick() {
                    setResponsePage(backToPage);
                }
            });
            add(new Button("submitButton"));
        }

        @Override
        protected void onSubmit() {
            saveReport(name);
            FieldIDSession.get().info(new FIDLabelModel("message.savedreportsaved").getObject());
            setResponsePage(backToPage);
        }

    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.reporting"));
    }

    protected void saveReport(String name) {
        savedReportService.saveReport(criteriaModel, overwrite, name);
    }

}
