package com.n4systems.fieldid.wicket.pages.saveditems;

import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.saveditem.SavedReportItem;
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

public class SavedItemsDropdownPage extends FieldIDAuthenticatedPage {

    private @SpringBean SavedReportService savedReportService;
    private @SpringBean UserService userService;

    public SavedItemsDropdownPage() {
        final LoadableDetachableModel<List<SavedItem>> savedReportModel = createSavedReportModel();
        add(new ListView<SavedItem>("savedItemsList", savedReportModel) {
            @Override
            protected void populateItem(ListItem<SavedItem> item) {
                String linkUrl = null;
                String imageUrl = null;

                if (item.getModelObject() instanceof SavedReportItem) {
                    linkUrl = "w/savedReport?id="+item.getModelObject().getId();
                    imageUrl = "images/saved_items/reporting-small.png";
                }

                // This actually links to a wicket page, but because wicket outputs relative links
                // when using bookmarkable links, and since this page is rendered inside a div in any front end page,
                // with various URLs, the relative link may be wrong. we need an absolute link. Hence the use of non wicket link for now.
                NonWicketLink savedItemLink = new NonWicketLink("savedItemLink", linkUrl);
                savedItemLink.add(new ContextImage("savedItemIcon", ContextAbsolutizer.absolutize(imageUrl)));
                savedItemLink.add(new Label("savedItemName", new PropertyModel<String>(item.getModel(), "name")));

                item.add(savedItemLink);
            }
        });
        add(new WebMarkupContainer("blankSlateContainer").setVisible(savedReportModel.getObject().isEmpty()));
        add(new NonWicketLink("manageSavedItemsLink", "w/manageSavedItems"));
    }

    public LoadableDetachableModel<List<SavedItem>> createSavedReportModel() {
        return new LoadableDetachableModel<List<SavedItem>>() {
            @Override
            protected List<SavedItem> load() {
                return userService.getUser(getSessionUser().getId()).getSavedItems();
            }
        };
    }

}
