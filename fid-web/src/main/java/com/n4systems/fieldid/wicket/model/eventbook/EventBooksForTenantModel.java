package com.n4systems.fieldid.wicket.model.eventbook;

import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.EventBook;
import com.n4systems.model.eventbook.EventBookListLoader;
import com.n4systems.model.orgs.BaseOrg;

import java.util.List;

public class EventBooksForTenantModel extends FieldIDSpringModel<List<EventBook>> {

    private boolean addNullOption;
    
    private boolean openBooksOnly;
    
    private BaseOrg owner;

    @Override
    protected List<EventBook> load() {
        EventBookListLoader loader = new EventBookListLoader(getSecurityFilter());
        loader.setOpenBooksOnly(openBooksOnly);        
        if(owner != null) {
        	loader.setOwner(owner);
        }
        
		List<EventBook> eventBooks = loader.load();
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

	public EventBooksForTenantModel setOpenBooksOnly(boolean openBooksOnly) {
		this.openBooksOnly = openBooksOnly;
		return this;
	}

	public EventBooksForTenantModel setOwner(BaseOrg owner) {
		this.owner = owner;
		return this;
	}

}
