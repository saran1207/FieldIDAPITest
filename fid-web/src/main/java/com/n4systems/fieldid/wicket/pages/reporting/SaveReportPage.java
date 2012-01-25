package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.wicket.pages.search.SaveSearchPage;
import com.n4systems.model.saveditem.SavedReportItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class SaveReportPage extends SaveSearchPage<SavedReportItem> {

    @SpringBean
    private SavedReportService savedReportService;

    public SaveReportPage(SavedReportItem savedReportItem, WebPage backToPage, boolean overwrite) {
        super(savedReportItem, backToPage, overwrite);
    }

    @Override
    protected void saveSearch(SavedReportItem item, boolean overwrite, String name) {
        savedReportService.saveReport(item, overwrite, name);
    }

}
