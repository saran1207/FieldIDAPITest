package com.n4systems.fieldid.wicket.pages.search;

import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.validation.UniquelyNamedSavedItemValidator;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.util.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public abstract class SaveSearchPage<T extends SavedItem> extends FieldIDFrontEndPage {

    public static final String NEW_SEARCH_NAME = "New Search";


    @SpringBean
    private SavedReportService savedReportService;

    private T savedItem;
    private boolean overwrite;

    private String name;
    private String description;

    public SaveSearchPage(T savedItem, boolean overwrite) {
        this.savedItem = savedItem;
        this.overwrite = overwrite;
        Long savedItemId = null;
        name = getName();
        if (overwrite && savedItem != null) {
            savedItemId = savedItem.getId();
        }
        add(new SaveReportForm("saveReportForm", savedItemId));
    }
    
    private String getName() { 
        if (savedItem!=null && StringUtils.isNotEmpty(savedItem.getName())) {
            return overwrite ? savedItem.getName() : StringUtils.getFileCopyName(savedItem.getName());
        }
        return getDefaultName();
    }

    protected String getDefaultName() {
        return NEW_SEARCH_NAME;
    }

    class SaveReportForm extends Form {

        private RequiredTextField<String> nameText;

		public SaveReportForm(String id, Long savedItemId) {
            super(id);

            add(new Label("savedItemLabel", createSavedItemDescriptionModel()));

            add(new FIDFeedbackPanel("feedbackPanel"));
            add(nameText = new RequiredTextField<String>("name", new PropertyModel<String>(SaveSearchPage.this, "name")));
           	nameText.add(new UniquelyNamedSavedItemValidator(savedItemId));
            
            add(new TextArea<String>("description", new PropertyModel<String>(SaveSearchPage.this, "description")));
            
            add(new Link("cancelLink") {
                @Override public void onClick() {
                    setResponsePage(createCancelResponsePage());
                }
            });
            add(new Button("submitButton"));
        }

        @Override
        protected void onSubmit() {
            T newSavedItem = saveSearch(savedItem, overwrite, name, description);
            newSavedItem.getSearchCriteria().setSavedReportName(name); //?? why i don't think this is needed anymore?  confirm with neil.
            FieldIDSession.get().info(createSavedConfirmationModel().getObject());
            setResponsePage(createSaveResponsePage(newSavedItem));
        }

    }

    protected FieldIDFrontEndPage createSaveResponsePage(T newSavedItem) { return null; }; // override these!
    protected FieldIDFrontEndPage createCancelResponsePage() { return null; };

    protected abstract IModel<String> createSavedConfirmationModel();
    protected abstract IModel<String> createSavedItemDescriptionModel();

    protected abstract T saveSearch(T item, boolean overwrite, String name, String description);

}
