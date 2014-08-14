package com.n4systems.fieldid.wicket.pages.setup.eventbook;

import com.n4systems.model.api.Archivable;
import org.apache.wicket.model.Model;

/**
 * Created by jheath on 06/08/14.
 */
public class EventBooksListArchivedPage extends EventBooksListPage {


    public EventBooksListArchivedPage() {
        super(Model.of(Archivable.EntityState.ARCHIVED));
    }
}
