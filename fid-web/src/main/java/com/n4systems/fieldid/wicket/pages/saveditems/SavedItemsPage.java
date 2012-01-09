package com.n4systems.fieldid.wicket.pages.saveditems;

import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.fieldid.wicket.pages.reporting.RunSavedReportPage;
import com.n4systems.model.savedreports.SavedReport;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class SavedItemsPage extends FieldIDAuthenticatedPage {

    private @SpringBean SavedReportService savedReportService;

    public SavedItemsPage() {
        final LoadableDetachableModel<List<SavedReport>> savedReportModel = createSavedReportModel();
        add(new ListView<SavedReport>("savedItemsList", savedReportModel) {
            @Override
            protected void populateItem(ListItem<SavedReport> item) {
                // TODO: This is where some logic needs to happen to figure out the type of the item when
                // there's more than one type

                // This actually links to a wicket page, but because wicket outputs relative links
                // when using bookmarkable links, and since this page is rendered inside a div in any front end page,
                // with various URLs, the relative link may be wrong. we need an absolute link. Hence the use of non wicket link for now.
                NonWicketLink savedItemLink = new NonWicketLink("savedItemLink", "w/savedReport?id="+item.getModelObject().getId());
                savedItemLink.add(new ContextImage("savedItemIcon", ContextAbsolutizer.absolutize("images/saved_items/reporting-small.png")));
                savedItemLink.add(new Label("savedItemName", new PropertyModel<String>(item.getModel(), "name")));

                item.add(savedItemLink);
            }
        });
        add(new WebMarkupContainer("blankSlateContainer").setVisible(savedReportModel.getObject().isEmpty()));
    }

    public LoadableDetachableModel<List<SavedReport>> createSavedReportModel() {
        return new LoadableDetachableModel<List<SavedReport>>() {
            @Override
            protected List<SavedReport> load() {
                return savedReportService.listSavedReports();
            }
        };
    }

}
