package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.EventBook;

import java.util.List;

public class EventBookService extends FieldIdPersistenceService {

    public List<EventBook> getAllEventBooks() {
        return persistenceService.findAll(EventBook.class);
    }
}
