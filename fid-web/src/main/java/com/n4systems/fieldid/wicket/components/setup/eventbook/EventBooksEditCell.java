package com.n4systems.fieldid.wicket.components.setup.eventbook;

import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.setup.eventbook.EditEventBookPage;
import com.n4systems.model.EventBook;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * This is the "Edit" cell for <b>EventBooksListPanel</b>.  This allows the user to edit the EventBook that they have
 * selected.
 *
 * Created by Jordan Heath on 07/08/14.
 */
public class EventBooksEditCell extends Panel {
    public EventBooksEditCell(String id,
                              IModel<EventBook> model) {

        super(id, model);

        BookmarkablePageLink editLink;

        add(editLink = new BookmarkablePageLink("editLink",
                                                EditEventBookPage.class,
                                                PageParametersBuilder.param("eventBookId",
                                                                            model.getObject().getId())));

        editLink.add(new FlatLabel("nameField",
                               new PropertyModel<String>(model.getObject(),
                                                         "displayName")));
    }
}
