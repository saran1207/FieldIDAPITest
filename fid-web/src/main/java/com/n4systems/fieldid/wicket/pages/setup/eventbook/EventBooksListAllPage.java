package com.n4systems.fieldid.wicket.pages.setup.eventbook;

import com.n4systems.model.api.Archivable;
import org.apache.wicket.model.Model;

/**
 * This page lists all Active Event Books.  From this page, users may open or close a book, edit its name and owner or
 * archive it.
 *
 * Created by Jordan Heath on 06/08/14.
 */
public class EventBooksListAllPage extends EventBooksListPage {


    public EventBooksListAllPage() {
        super(Model.of(Archivable.EntityState.ACTIVE));
    }
}
