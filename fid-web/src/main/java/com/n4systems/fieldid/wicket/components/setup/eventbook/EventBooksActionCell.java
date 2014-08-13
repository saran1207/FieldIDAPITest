package com.n4systems.fieldid.wicket.components.setup.eventbook;

import com.n4systems.fieldid.service.event.EventBookService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
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
                                IModel<EventBook> model,
                                final EventBooksListPanel listPanel) {

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
                eventBookService.openEventBook(thisBook);

                info(new FIDLabelModel("message.open_event_book",
                                       thisBook.getDisplayName()).getObject());
                target.add(listPanel.getFeedbackPanel());
                target.add(((FieldIDFrontEndPage) getPage()).getTopFeedbackPanel());
            }
        });

        add(closeLink = new AjaxLink("closeLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                eventBookService.closeEventBook(thisBook);

                info(new FIDLabelModel("message.close_event_book",
                                       thisBook.getDisplayName()).getObject());
                target.add(listPanel.getFeedbackPanel());
                target.add(((FieldIDFrontEndPage) getPage()).getTopFeedbackPanel());
            }
        });

        add(archiveLink = new AjaxLink("archiveLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                eventBookService.archiveEventBook(thisBook);

                info(new FIDLabelModel("message.archive_event_book",
                                       thisBook.getDisplayName()).getObject());
                target.add(listPanel.getFeedbackPanel());
                target.add(((FieldIDFrontEndPage) getPage()).getTopFeedbackPanel());
            }
        });

        add(unarchiveLink = new AjaxLink("unarchiveLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                eventBookService.unarchiveStatus(thisBook);

                info(new FIDLabelModel("message.unarchive_event_book",
                                       thisBook.getDisplayName()).getObject());
                target.add(listPanel.getFeedbackPanel());
                target.add(((FieldIDFrontEndPage) getPage()).getTopFeedbackPanel());
            }
        });

        //Manage Visibility...
        archiveLink.setVisible(!thisBook.isArchived());
        unarchiveLink.setVisible(thisBook.isArchived());
        openLink.setVisible(!thisBook.isOpen());
        closeLink.setVisible(thisBook.isOpen());
    }
}
