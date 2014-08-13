package com.n4systems.fieldid.wicket.pages.setup.eventbook;

import com.n4systems.fieldid.service.event.EventBookService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.setup.eventbook.EventBooksActionColumn;
import com.n4systems.fieldid.wicket.components.setup.eventbook.EventBooksListPanel;
import com.n4systems.fieldid.wicket.data.EventBookDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.model.EventBook;
import com.n4systems.model.api.Archivable;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * This is the shared back-end for <b>EventBooksListAllPage</b> and <b>EventBooksListArchivedPage</b>.
 *
 * This handles building all shared page components and sets up any shared logic.
 *
 * Created by Jordan Heath on 06/08/14.
 */
public abstract class EventBooksListPage extends FieldIDTemplatePage {

    protected FIDFeedbackPanel feedbackPanel;

    protected IModel<Archivable.EntityState> archivableState;

    @SpringBean
    protected EventBookService eventBookService;

    public EventBooksListPage(IModel<Archivable.EntityState> model) {
        archivableState = model;
    }

    @Override
    public void onInitialize() {
        super.onInitialize();


        feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        //Yep... that's right, champ!  Just one piece!!
        add(new EventBooksListPanel("eventBooksListPanel", getDataProvider()) {
            @Override
            protected void addActionColumn(List<IColumn<EventBook>> columns) {
                columns.add(new EventBooksActionColumn(this));
            }

            @Override
            protected FIDFeedbackPanel getFeedbackPanel() {
                return feedbackPanel;
            }
        });
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.manage_event_books"));
    }


    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, AssetsAndEventsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }


    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                        aNavItem().label(new FIDLabelModel("nav.view_all.count",
                                         eventBookService.getActiveEventBookCount()))
                                  .page(EventBooksListAllPage.class)
                                  .build(),

                        aNavItem().label(new FIDLabelModel("nav.view_all_archived.count",
                                         eventBookService.getArchivedEventBookCount()))
                                  .page(EventBooksListArchivedPage.class)
                                  .build(),

                        aNavItem().label(new FIDLabelModel("nav.add"))
                                  .page(AddEventBookPage.class)
                                  .onRight()
                                  .build()
                )
        );
    }

    /**
     * This method sets the Data Provider with the required Archivable.EntityState.
     *
     * @return An <b>EventBookDataProvider</b> tied to the appropriate state..
     */
    protected EventBookDataProvider getDataProvider() {
        return new EventBookDataProvider("name", SortOrder.ASCENDING, archivableState.getObject());
    }
}
