package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.ActionEventType;
import com.n4systems.model.Event;
import com.n4systems.model.notification.AssigneeNotification;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ActionService extends FieldIdPersistenceService {

    @Autowired
    private NotifyEventAssigneeService notifyEventAssigneeService;

    public List<ActionEventType> getActionTypes() {
        QueryBuilder<ActionEventType> query = createUserSecurityBuilder(ActionEventType.class);
        query.addOrder("name");
        return persistenceService.findAll(query);
    }

    public <T extends Event> T update(T actionEvent) {
        if(actionEvent.isSendEmailOnUpdate() && actionEvent.getAssigneeOrDateUpdated()) {
            if(!notifyEventAssigneeService.notificationExists(actionEvent)) {
                AssigneeNotification assigneeNotification = new AssigneeNotification();
                assigneeNotification.setEvent(actionEvent);
                persistenceService.save(assigneeNotification);
                actionEvent.setAssigneeNotification(assigneeNotification);
            }
        }
        return persistenceService.update(actionEvent);
    }
}
