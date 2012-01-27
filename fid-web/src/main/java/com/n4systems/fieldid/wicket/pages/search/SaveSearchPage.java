package com.n4systems.fieldid.wicket.pages.search;

import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.saveditem.SavedItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public abstract class SaveSearchPage<T extends SavedItem> extends FieldIDFrontEndPage {

    @SpringBean
    private SavedReportService savedReportService;

    private T savedItem;
    private WebPage backToPage;
    private boolean overwrite;

    private String name;

    public SaveSearchPage(T savedItem, WebPage backToPage, boolean overwrite) {
        this.savedItem = savedItem;
        this.backToPage = backToPage;
        this.overwrite = overwrite;
        if (overwrite && savedItem != null) {
            name = savedItem.getName();
        }
        add(new SaveReportForm("saveReportForm"));
    }

    class SaveReportForm extends Form {

        public SaveReportForm(String id) {
            super(id);

            add(new Label("savedItemLabel", createSavedItemDescriptionModel()));

            add(new FIDFeedbackPanel("feedbackPanel"));
            add(new RequiredTextField<String>("name", new PropertyModel<String>(SaveSearchPage.this, "name")));
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
            saveSearch(savedItem, overwrite, name);
            FieldIDSession.get().info(createSavedConfirmationModel().getObject());
            setResponsePage(backToPage);
        }

    }

    protected abstract IModel<String> createSavedConfirmationModel();
    protected abstract IModel<String> createSavedItemDescriptionModel();

    protected abstract void saveSearch(T item, boolean overwrite, String name);

}
