package com.n4systems.fieldid.wicket.pages.massevent;

import com.n4systems.fieldid.service.event.massevent.MassEventService;
import com.n4systems.fieldid.service.event.massevent.SelectedEventTypeCount;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.search.EventReportCriteria;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class SelectMassEventPage extends FieldIDTemplatePage {

    @SpringBean
    private MassEventService massEventService;

    public SelectMassEventPage(IModel<EventReportCriteria> criteriaModel) {
        List<Long> eventIds = criteriaModel.getObject().getSelection().getSelectedIds();

        List<SelectedEventTypeCount> eventTypeCounts = massEventService.countSelectedEventTypes(eventIds);

        add(new ListView<SelectedEventTypeCount>("eventType", eventTypeCounts) {

            @Override
            protected void populateItem(ListItem<SelectedEventTypeCount> item) {
                item.add(new Label("name", new PropertyModel<String>(item.getModel(), "type.displayName")));
                item.add(new Label("count", new PropertyModel<Long>(item.getModel(), "count")));
                item.add(new Link("performEvents") {
                    @Override
                    public void onClick() {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                });
            }
        });
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.select_event_type_to_perform"));    //To change body of overridden methods use File | Settings | File Templates.
    }

}
