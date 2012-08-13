package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.download.SystemUrlUtil;
import com.n4systems.fieldid.service.mail.MailService;
import com.n4systems.model.Event;
import com.n4systems.model.notification.AssigneeNotification;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.FieldIdDateFormatter;
import com.n4systems.util.mail.TemplateMailMessage;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class NotifyEventAssigneeService extends FieldIdPersistenceService {

    private static final Logger logger = Logger.getLogger(NotifyEventAssigneeService.class);
    private static final String ASSIGNEE_TEMPLATE_SINGLE = "eventsAssignedSingle";
    private static final String ASSIGNEE_TEMPLATE_MULTI = "eventsAssignedMulti";

    @Autowired private MailService mailService;

    @Transactional
    public void sendNotifications() {
        QueryBuilder<AssigneeNotification> assigneeQuery = new QueryBuilder<AssigneeNotification>(AssigneeNotification.class, new OpenSecurityFilter());
        assigneeQuery.addWhere(WhereParameter.Comparator.NOTNULL, "event.assignee", "event.assignee", "");

        List<AssigneeNotification> assigneeRecords = persistenceService.findAll(assigneeQuery);

        Set<User> assignees = aggregateAssignees(assigneeRecords);

        for (User assignee : assignees) {
            sendNotificationsFor(assignee);
        }

        removeNotificationsWithoutAssignees();
    }

    private Set<User> aggregateAssignees(List<AssigneeNotification> assigneeRecords) {
        Set<User> assignees = new HashSet<User>();
        for (AssigneeNotification assigneeRecord : assigneeRecords) {
            if (assigneeRecord.getEvent().getEventState() == Event.EventState.OPEN && assigneeRecord.getEvent().getAssignee() != null) {
                assignees.add(assigneeRecord.getEvent().getAssignee());
            }
        }
        return assignees;
    }

    private void sendNotificationsFor(User assignee) {
        QueryBuilder<Event> assigneeQuery = new QueryBuilder<Event>(AssigneeNotification.class, new OpenSecurityFilter());
        assigneeQuery.setSimpleSelect("event");
        assigneeQuery.addSimpleWhere("event.assignee", assignee);
        List<Event> notificationEvents = persistenceService.findAll(assigneeQuery);
        notifyEventAssignee(notificationEvents);
        removeNotificationsForAssignee(assignee);
    }

    private void notifyEventAssignee(List<Event> events) {
        try {
            TemplateMailMessage message;
            if (events.size() == 1) {
                message = createSingleNotification(events.get(0));
            } else {
                message = createMultiNotifications(events);
            }

            mailService.sendMessage(message);

        } catch (Exception e) {
            logger.error("Could not notify assignee", e);
        }
    }

    private TemplateMailMessage createMultiNotifications(List<Event> events) {
        String subject = "Work Assigned: " + events.size() + " Events";
        Map<Long, String> dueDateStringMap = createDateStringMap(events);

        TemplateMailMessage msg = new TemplateMailMessage(subject, ASSIGNEE_TEMPLATE_MULTI);
        msg.getToAddresses().add(events.get(0).getAssignee().getEmailAddress());

        msg.getTemplateMap().put("systemUrl", SystemUrlUtil.getSystemUrl(events.get(0).getTenant()));
        msg.getTemplateMap().put("events", events);
        msg.getTemplateMap().put("subject", subject);
        msg.getTemplateMap().put("dueDateStringMap", dueDateStringMap);

        return msg;
    }

    private TemplateMailMessage createSingleNotification(Event event) {
        String subject = "Work Assigned: " + event.getEventType().getName();
        Map<Long, String> dueDateStringMap = createDateStringMap(Arrays.asList(event));
        TemplateMailMessage msg = new TemplateMailMessage(subject, ASSIGNEE_TEMPLATE_SINGLE);
        msg.getToAddresses().add(event.getAssignee().getEmailAddress());

        msg.getTemplateMap().put("systemUrl", SystemUrlUtil.getSystemUrl(event.getTenant()));
        msg.getTemplateMap().put("event", event);
        msg.getTemplateMap().put("subject", subject);
        msg.getTemplateMap().put("dueDateStringMap", dueDateStringMap);

        return msg;
    }


    private Map<Long, String> createDateStringMap(List<Event> events) {
        Map<Long, String> dueDateStringMap = new HashMap<Long,String>();
        for (Event event : events) {
            Date dueDate = event.getNextDate();
            boolean showTime = !new PlainDate(dueDate).equals(dueDate);
            String dueDateString = new FieldIdDateFormatter(dueDate, event.getAssignee(), false, showTime).format();
            dueDateStringMap.put(event.getId(), dueDateString);
        }
        return dueDateStringMap;
    }


    private void removeNotificationsWithoutAssignees() {
        removeNotificationsForAssignee(null);
    }

    private void removeNotificationsForAssignee(User assignee) {
        QueryBuilder<AssigneeNotification> builder = new QueryBuilder<AssigneeNotification>(AssigneeNotification.class, new OpenSecurityFilter());
        builder.addSimpleWhere("event.assignee", assignee);
        List<AssigneeNotification> resultList = persistenceService.findAll(builder);
        for (AssigneeNotification o : resultList) {
            o.getEvent().setAssigneeNotification(null);
            persistenceService.remove(o);
        }
    }

}
