package com.n4systems.fieldid.wicket.components.setup.eventbook;

import com.n4systems.fieldid.service.eventbook.EventBookService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.setup.eventbook.EventBooksListAllPage;
import com.n4systems.fieldid.wicket.pages.setup.eventbook.EventBooksListArchivedPage;
import com.n4systems.model.EventBook;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * This is the Action Cell for <b>EventBooksListPanel</b>.
 *
 * This cell gives the user the option to open or close an EventBook as well as to archive and unarchive it, based on
 * the current state of the particular row.
 *
 * Created by Jordan Heath on 06/08/14.
 */
public class EventBooksActionCell extends Panel {

    @SpringBean
    private EventBookService eventBookService;

    public EventBooksActionCell(String id,
                                IModel<EventBook> model) {

        super(id);

        final EventBook thisBook = model.getObject();

        //Create all of the links...

        AjaxLink openOrCloseLink;

        AjaxLink openLink;
        AjaxLink closeLink;
        AjaxLink archiveLink;
        AjaxLink unarchiveLink;

        if(!thisBook.isOpen()) {
            add(openOrCloseLink = new AjaxLink("openOrCloseLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    EventBook bookToOpen = eventBookService.findById(thisBook.getId());
                    eventBookService.openEventBook(bookToOpen);

                    FieldIDSession.get()
                            .info(new FIDLabelModel("message.open_event_book",
                                    thisBook.getDisplayName()).getObject());

                    onAction(target);
                }
            });

            openOrCloseLink.add(new Label("openOrCloseLabel", new FIDLabelModel("label.open")));
        } else {
            add(openOrCloseLink = new AjaxLink("openOrCloseLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    EventBook bookToClose = eventBookService.findById(thisBook.getId());
                    eventBookService.closeEventBook(bookToClose);

                    FieldIDSession.get()
                            .info(new FIDLabelModel("message.close_event_book",
                                    thisBook.getDisplayName()).getObject());

                    onAction(target);
                }
            });

            openOrCloseLink.add(new Label("openOrCloseLabel", new FIDLabelModel("label.close")));
        }

        WebMarkupContainer optionsContainer = new WebMarkupContainer("optionsContainer");

        optionsContainer.add(archiveLink = new AjaxLink("archiveLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                EventBook bookToArchive = eventBookService.findById(thisBook.getId());
                eventBookService.archiveEventBook(bookToArchive);

                FieldIDSession.get()
                        .info(new FIDLabelModel("message.archive_event_book",
                                thisBook.getDisplayName()).getObject());

                setResponsePage(EventBooksListAllPage.class);
            }
        });

        add(optionsContainer);


        add(unarchiveLink = new AjaxLink("unarchiveLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                EventBook bookToUnarchive = eventBookService.findArchivedEventBookById(thisBook.getId());
                eventBookService.unarchiveStatus(bookToUnarchive);

                FieldIDSession.get()
                              .info(new FIDLabelModel("message.unarchive_event_book",
                                      thisBook.getDisplayName()).getObject());

                setResponsePage(EventBooksListArchivedPage.class);
            }
        });

        //Manage Visibility...
        optionsContainer.setVisible(!thisBook.isArchived());
        openOrCloseLink.setVisible(!thisBook.isArchived());
        unarchiveLink.setVisible(thisBook.isArchived());
    }

    protected void onAction(AjaxRequestTarget target) {}
}
