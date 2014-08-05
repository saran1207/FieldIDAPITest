package com.n4systems.fieldid.wicket.pages.setup.eventtypegroup;

import com.n4systems.fieldid.service.event.EventTypeGroupService;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;
import java.util.List;

/**
 * Created by rrana on 2014-08-04.
 */
public class EventTypeGroupDataProvider extends FieldIDDataProvider<EventTypeGroup> {


    @SpringBean
    private EventTypeGroupService eventTypeGroupService;

    private Archivable.EntityState state;

    private List<? extends ProcedureDefinition> results;
    private Long size;


    public EventTypeGroupDataProvider(String order, SortOrder sortOrder, Archivable.EntityState state) {
        setSort(order, sortOrder);
        this.state = state;
    }


    @Override
    public Iterator<? extends EventTypeGroup> iterator(int first, int count) {
        List<? extends EventTypeGroup> eventtypeGroupsList = null;
        eventtypeGroupsList = eventTypeGroupService.getEventTypeGroupsByState(state, getSort().getProperty(), getSort().isAscending(), first, count);
        return eventtypeGroupsList.iterator();
    }

    @Override
    public int size() {
        int size = 0;
        List<? extends EventTypeGroup> eventtypeGroupsList = null;
        size = eventTypeGroupService.getEventTypeGroupsByStateCount(state).intValue();
        return size;
    }

    @Override
    public IModel<EventTypeGroup> model(final EventTypeGroup object) {
        return new AbstractReadOnlyModel<EventTypeGroup>() {
            @Override
            public EventTypeGroup getObject() {
                return object;
            }
        };
    }

    @Override
    public void detach() {
        results = null;
        size = null;
    }

}
