package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.asset.events.EventListPanel;
import com.n4systems.model.Event;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;


public class ActionsColumn extends PropertyColumn<Event> {

    private EventListPanel eventListPanel;

    public ActionsColumn(IModel<String> displayModel, String propertyExpression, EventListPanel eventListPanel) {
        super(displayModel, propertyExpression);
        this.eventListPanel = eventListPanel;
    }

    @Override
    public void populateItem(Item<ICellPopulator<Event>> item, String id, IModel<Event> eventModel) {
        WorkflowState state = eventModel.getObject().getWorkflowState();

        if (isFromSafetyNetwork(eventModel)) {
            item.add(new SafetyNetworkActionsCell(id, eventModel));
        } else if (state.equals(WorkflowState.COMPLETED)) {
            item.add(new EventActionsCell(id, eventModel));
        } else if (state.equals(WorkflowState.OPEN)) {
            item.add(new OpenActionsCell(id, eventModel, eventListPanel));
        } else {
            item.add(new ClosedActionsCell(id, eventModel, eventListPanel));
        }
    }

    private boolean isFromSafetyNetwork(IModel<Event> eventModel) {
        BaseOrg userOrg = FieldIDSession.get().getSessionUser().getOrganization();
        return !eventModel.getObject().getSecurityLevel(userOrg).isLocal();
    }

}
