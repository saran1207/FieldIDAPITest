package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.search.SaveSearchPage;
import com.n4systems.model.saveditem.SavedReportItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class SaveReportPage extends SaveSearchPage<SavedReportItem> {

    public static final String NEW_REPORT_NAME = "New Report";

    @SpringBean
    private SavedReportService savedReportService;

    public SaveReportPage(SavedReportItem savedReportItem, boolean overwrite) {
        super(savedReportItem, overwrite);
    }

    @Override
    protected String getDefaultName() {
        return NEW_REPORT_NAME;
    }

    @Override
    protected IModel<String> createSavedConfirmationModel() {
        return new FIDLabelModel("message.savedreportsaved");
    }

    @Override
    protected IModel<String> createSavedItemDescriptionModel() {
        return new FIDLabelModel("label.savedreportdetails");
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.reporting"));
    }

    @Override
    protected SavedReportItem saveSearch(SavedReportItem item, boolean overwrite, String name, String description) {
        return savedReportService.saveReport(item, overwrite, name, description);
    }

}
