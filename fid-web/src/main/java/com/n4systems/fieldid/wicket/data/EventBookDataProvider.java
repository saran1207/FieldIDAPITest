package com.n4systems.fieldid.wicket.data;

import com.n4systems.fieldid.service.event.EventBookService;
import com.n4systems.model.EventBook;
import com.n4systems.model.api.Archivable;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;
import java.util.List;

/**
 * Created by jheath on 06/08/14.
 */
public class EventBookDataProvider extends FieldIDDataProvider<EventBook> {

    @SpringBean
    private EventBookService eventBookService;

    private Archivable.EntityState state;

    private List<EventBook> results;

    private Long size;

    public EventBookDataProvider(String order,
                                 SortOrder sortOrder,
                                 Archivable.EntityState state) {

        setSort(order, sortOrder);
        this.state = state;
    }


    @Override
    public Iterator<? extends EventBook> iterator(int first,
                                                  int count) {

        List<EventBook> eventBookList = eventBookService.getPagedEventBooksListByState(state,
                                                                                       getSort().getProperty(),
                                                                                       getSort().isAscending(),
                                                                                       first,
                                                                                       count);

        return eventBookList.iterator();
    }

    @Override
    public int size() {
        return eventBookService.getEventBooksCountByState(state).intValue();
    }

    @Override
    public IModel<EventBook> model(final EventBook eventBook) {
        return new AbstractReadOnlyModel<EventBook>() {
            @Override
            public EventBook getObject() {
                return eventBook;
            }
        };
    }

    @Override
    public void detach() {
        results = null;
        size = null;
    }
}
