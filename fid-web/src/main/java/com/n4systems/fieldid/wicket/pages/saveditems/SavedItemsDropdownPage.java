package com.n4systems.fieldid.wicket.pages.saveditems;

import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.AbstractSearchPage;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.saveditem.SavedReportItem;
import com.n4systems.model.saveditem.SavedSearchItem;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static com.n4systems.fieldid.wicket.pages.assetsearch.version2.AbstractSearchPage.SOURCE_PARAMETER;

public class SavedItemsDropdownPage extends FieldIDAuthenticatedPage {

    private @SpringBean SavedReportService savedReportService;
    private @SpringBean UserService userService;

    private static final String SAVED_SEARCH_IMAGE_URL = "images/saved_items/saved-search.png";
    private static final String SAVED_REPORT_IMAGE_URL = "images/saved_items/saved-report.png";

    public SavedItemsDropdownPage() {
        final User user = userService.getUser(FieldIDSession.get().getSessionUser().getUniqueID());

        final LoadableDetachableModel<List<SavedItem>> savedReportModel = createSavedReportModel();
        add(new ListView<SavedItem>("savedItemsList", savedReportModel) {
            @Override
            protected void populateItem(ListItem<SavedItem> item) {
                String linkUrl = null;
                String imageUrl = null;

                if (item.getModelObject() instanceof SavedReportItem) {
                    linkUrl = "w/savedReport?id="+item.getModelObject().getId()+"&" + SOURCE_PARAMETER +"=" + AbstractSearchPage.SAVED_ITEM_SOURCE;
                    imageUrl = SAVED_REPORT_IMAGE_URL;
                } else if (item.getModelObject() instanceof SavedSearchItem) {
                    linkUrl = "w/savedSearch?id="+item.getModelObject().getId()+"&"+ SOURCE_PARAMETER + "=" + AbstractSearchPage.SAVED_ITEM_SOURCE;
                    imageUrl = SAVED_SEARCH_IMAGE_URL;
                }
                addSavedItemLinkTo(item, new PropertyModel<String>(item.getModel(), "name"), imageUrl, linkUrl);
            }
        });

        WebMarkupContainer lastSearchContainer = new WebMarkupContainer("lastSearchContainer");
        addSavedItemLinkTo(lastSearchContainer, new FIDLabelModel("label.my_last_search"), SAVED_SEARCH_IMAGE_URL, "w/runLastSearch");

        WebMarkupContainer lastReportContainer = new WebMarkupContainer("lastReportContainer");
        addSavedItemLinkTo(lastReportContainer, new FIDLabelModel("label.my_last_report"), SAVED_REPORT_IMAGE_URL, "w/runLastReport");

        lastSearchContainer.setVisible(user.isDisplayLastRunSearches() && user.getLastRunSearch() != null);
        lastReportContainer.setVisible(user.isDisplayLastRunSearches() && user.getLastRunReport() != null);

        add(lastSearchContainer);
        add(lastReportContainer);

        add(new WebMarkupContainer("blankSlateContainer").setVisible(savedReportModel.getObject().isEmpty()));
        add(new NonWicketLink("manageSavedItemsLink", "w/manageSavedItems"));
    }

    private void addSavedItemLinkTo(WebMarkupContainer container, IModel<String> nameModel, String imageUrl, String linkUrl) {
        // These actually links to wicket pages, but because wicket outputs relative links
        // when using bookmarkable links, and since this page is rendered inside a div in any front end page,
        // with various URLs, the relative link may be wrong. we need an absolute link. Hence the use of non wicket link for now.
        NonWicketLink savedItemLink = new NonWicketLink("savedItemLink", linkUrl);
        savedItemLink.add(new ContextImage("savedItemIcon", ContextAbsolutizer.toContextAbsoluteUrl(imageUrl)));
        savedItemLink.add(new Label("savedItemName", nameModel));

        container.add(savedItemLink);
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
