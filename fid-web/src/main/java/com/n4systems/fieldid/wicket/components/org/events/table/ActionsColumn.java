package com.n4systems.fieldid.wicket.components.org.events.table;

import com.n4systems.fieldid.wicket.components.asset.events.EventListPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Event;
import com.n4systems.model.WorkflowState;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

public class ActionsColumn extends AbstractColumn<Event> {

    private EventListPanel eventListPanel;

    public ActionsColumn(EventListPanel eventListPanel) {
        super(new FIDLabelModel(""));
        this.eventListPanel = eventListPanel;
    }

    @Override
    public void populateItem(Item<ICellPopulator<Event>> item, String componentId, IModel<Event> eventModel) {
        WorkflowState state = eventModel.getObject().getWorkflowState();

        if (state.equals(WorkflowState.COMPLETED)) {
            item.add(new EventActionsCell(componentId, eventModel));
        } else if (state.equals(WorkflowState.OPEN)) {
            item.add(new OpenActionsCell(componentId, eventModel, eventListPanel));
        } else {
            item.add(new ClosedActionsCell(componentId, eventModel, eventListPanel));
        }

    }
}
