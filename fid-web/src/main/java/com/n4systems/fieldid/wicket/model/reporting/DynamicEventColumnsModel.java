package com.n4systems.fieldid.wicket.model.reporting;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.helpers.EventAttributeDynamicGroupGenerator;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroupView;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.EventType;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DynamicEventColumnsModel extends FieldIDSpringModel<List<ColumnMappingGroupView>> {

    @SpringBean
    private PersistenceManager persistenceManager;

    private IModel<EventType> selectedEventTypeModel;
    private IModel<List<EventType>> currentlyAvailableEventTypesModel;

    public DynamicEventColumnsModel(IModel<EventType> selectedEventTypeModel, IModel<List<EventType>> currentlyAvailableEventTypesModel) {
        this.selectedEventTypeModel = selectedEventTypeModel;
        this.currentlyAvailableEventTypesModel = currentlyAvailableEventTypesModel;
    }

    @Override
    protected List<ColumnMappingGroupView> load() {
        EventAttributeDynamicGroupGenerator dynamicGenerator = new EventAttributeDynamicGroupGenerator(persistenceManager);
        EventType selectedEventType = selectedEventTypeModel.getObject();
        Long selectedEventTypeId = selectedEventType == null ? null : selectedEventType.getId();
        return dynamicGenerator.getDynamicGroups(selectedEventTypeId, idList(currentlyAvailableEventTypesModel.getObject()), FieldIDSession.get().getTenant().getId(), "event_search", getSecurityFilter());
    }

    private Set<Long> idList(List<EventType> eventTypes) {
        Set<Long> availableEventTypeIds = new HashSet<Long>();
        for (EventType eventType : eventTypes) {
            availableEventTypeIds.add(eventType.getId());

        }

        return availableEventTypeIds;
    }

}
