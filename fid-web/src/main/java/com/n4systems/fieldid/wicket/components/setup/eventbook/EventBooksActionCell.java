package com.n4systems.fieldid.wicket.components.setup.eventbook;

import com.n4systems.fieldid.service.event.EventBookService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.EventBook;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
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
        AjaxLink openLink;
        AjaxLink closeLink;
        AjaxLink archiveLink;
        AjaxLink unarchiveLink;

        add(openLink = new AjaxLink("openLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                EventBook bookToOpen = eventBookService.getEventBookById(thisBook.getId());
                eventBookService.openEventBook(bookToOpen);

                FieldIDSession.get()
                              .info(new FIDLabelModel("message.open_event_book",
                                      thisBook.getDisplayName()).getObject());

                onAction(target);
            }
        });

        add(closeLink = new AjaxLink("closeLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                EventBook bookToClose = eventBookService.getEventBookById(thisBook.getId());
                eventBookService.closeEventBook(bookToClose);

                FieldIDSession.get()
                              .info(new FIDLabelModel("message.close_event_book",
                                      thisBook.getDisplayName()).getObject());

                onAction(target);
            }
        });

        add(archiveLink = new AjaxLink("archiveLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                EventBook bookToArchive = eventBookService.getEventBookById(thisBook.getId());
                eventBookService.archiveEventBook(bookToArchive);

                FieldIDSession.get()
                              .info(new FIDLabelModel("message.archive_event_book",
                                      thisBook.getDisplayName()).getObject());

                onAction(target);
            }
        });

        add(unarchiveLink = new AjaxLink("unarchiveLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                EventBook bookToUnarchive = eventBookService.getEventBookById(thisBook.getId());
                eventBookService.unarchiveStatus(bookToUnarchive);

                FieldIDSession.get()
                              .info(new FIDLabelModel("message.unarchive_event_book",
                                      thisBook.getDisplayName()).getObject());

                onAction(target);
            }
        });

        //Manage Visibility...
        archiveLink.setVisible(!thisBook.isArchived());
        unarchiveLink.setVisible(thisBook.isArchived());
        openLink.setVisible(!thisBook.isOpen());
        closeLink.setVisible(thisBook.isOpen());
    }

    protected void onAction(AjaxRequestTarget target) {}
}
