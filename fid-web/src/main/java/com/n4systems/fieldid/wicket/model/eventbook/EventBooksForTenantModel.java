package com.n4systems.fieldid.wicket.model.eventbook;

import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.EventBook;
import com.n4systems.model.eventbook.EventBookListLoader;

import java.util.List;

public class EventBooksForTenantModel extends FieldIDSpringModel<List<EventBook>> {

    private boolean addNullOption;

    @Override
    protected List<EventBook> load() {
        List<EventBook> eventBooks = new EventBookListLoader(getSecurityFilter()).load();
        if (addNullOption) {
            EventBook eventBook = new EventBook();
            eventBook.setId(0L);
            eventBook.setName("Events not in a book");
            eventBooks.add(eventBook);
        }
        return eventBooks;
    }

    public EventBooksForTenantModel addNullOption(boolean addNullOption) {
        this.addNullOption = addNullOption;
        return this;
    }

}
