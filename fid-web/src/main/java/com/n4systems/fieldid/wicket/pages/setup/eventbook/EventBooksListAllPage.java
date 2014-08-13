package com.n4systems.fieldid.wicket.pages.setup.eventbook;

import com.n4systems.model.api.Archivable;
import org.apache.wicket.model.IModel;

/**
 * This page lists all Active Event Books.  From this page, users may open or close a book, edit its name and owner or
 * archive it.
 *
 * Created by Jordan Heath on 06/08/14.
 */
public class EventBooksListAllPage extends EventBooksListPage {


    public EventBooksListAllPage() {
        super(new IModel<Archivable.EntityState>() {
            @Override
            public Archivable.EntityState getObject() {
                return Archivable.EntityState.ACTIVE;
            }

            @Override
            public void setObject(Archivable.EntityState object) {

            }

            @Override
            public void detach() {

            }
        });
    }
//
//
//    @Override
//    public void onInitialize() {
//        super.onInitialize();
//
//
//        feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
//        feedbackPanel.setOutputMarkupId(true);
//        add(feedbackPanel);
//
//        //Yep... that's right, champ!  Just one piece!!
//        add(new EventBooksListPanel("eventBooksListPanel", getDataProvider()) {
//            @Override
//            protected void addActionColumn(List<IColumn<EventBook>> columns) {
//                columns.add(new EventBooksActionColumn(this));
//            }
//
//            @Override
//            protected FIDFeedbackPanel getFeedbackPanel() {
//                return feedbackPanel;
//            }
//        });
//    }
}
