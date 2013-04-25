package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.download.SystemUrlUtil;
import com.n4systems.fieldid.service.mail.MailService;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Event;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.notification.AssigneeNotification;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
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
    private static final String ASSIGNEE_TEMPLATE_MULTI = "eventsAssignedMulti";

    @Autowired private MailService mailService;
    @Autowired private S3Service s3Service;
    @Autowired private UserGroupService userGroupService;

    @Transactional
    public void sendNotifications() {
        notifyUserAssignees();
        notifyGroupAssignees();

        removeNotificationsWithoutAssignees();
    }

    private void notifyGroupAssignees() {
        QueryBuilder<AssigneeNotification> assigneeQuery = new QueryBuilder<AssigneeNotification>(AssigneeNotification.class, new OpenSecurityFilter());
        assigneeQuery.addWhere(WhereParameter.Comparator.NOTNULL, "event.assignedGroup", "event.assignedGroup", "");

        List<AssigneeNotification> assigneeRecords = persistenceService.findAll(assigneeQuery);

        Set<UserGroup> groups = aggregateAssignedGroups(assigneeRecords);

        for (UserGroup assignee : groups) {
            sendNotificationsFor(assignee);
        }
    }

    private void notifyUserAssignees() {
        QueryBuilder<AssigneeNotification> assigneeQuery = new QueryBuilder<AssigneeNotification>(AssigneeNotification.class, new OpenSecurityFilter());
        assigneeQuery.addWhere(WhereParameter.Comparator.NOTNULL, "event.assignee", "event.assignee", "");

        List<AssigneeNotification> assigneeRecords = persistenceService.findAll(assigneeQuery);

        Set<User> assignees = aggregateAssignees(assigneeRecords);

        for (User assignee : assignees) {
            sendNotificationsFor(assignee);
        }
    }

    private Set<UserGroup> aggregateAssignedGroups(List<AssigneeNotification> assigneeRecords) {
        Set<UserGroup> assignees = new HashSet<UserGroup>();
        for (AssigneeNotification assigneeRecord : assigneeRecords) {
            if (assigneeRecord.getEvent().getWorkflowState() == WorkflowState.OPEN && assigneeRecord.getEvent().getAssignedGroup() != null) {
                assignees.add(assigneeRecord.getEvent().getAssignedGroup());
            }
        }
        return assignees;
    }

    private Set<User> aggregateAssignees(List<AssigneeNotification> assigneeRecords) {
        Set<User> assignees = new HashSet<User>();
        for (AssigneeNotification assigneeRecord : assigneeRecords) {
            if (assigneeRecord.getEvent().getWorkflowState() == WorkflowState.OPEN && assigneeRecord.getEvent().getAssignee() != null) {
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
        notifyEventAssignee(notificationEvents, assignee);
        removeNotificationsForAssignee(assignee);
    }

    private void sendNotificationsFor(UserGroup assignedGroup) {
        QueryBuilder<Event> assigneeQuery = new QueryBuilder<Event>(AssigneeNotification.class, new OpenSecurityFilter());
        assigneeQuery.setSimpleSelect("event");
        assigneeQuery.addSimpleWhere("event.assignedGroup", assignedGroup);
        List<Event> notificationEvents = persistenceService.findAll(assigneeQuery);
        notifyEventAssignee(notificationEvents, assignedGroup);
        removeNotificationsForAssignedGroup(assignedGroup);
    }

    private void notifyEventAssignee(List<Event> events, UserGroup assignedGroup) {
        try {

            securityContext.setTenantSecurityFilter(new TenantOnlySecurityFilter(assignedGroup.getTenant()));

            for (User member : userGroupService.getUsersInGroup(assignedGroup.getId())) {
                // Instead of sending a multi message, since users can define their own date
                // formats and we want our notification to reflect this, we send an individual email to each member.
                TemplateMailMessage message = createMultiNotifications(events, member);

                message.getToAddresses().add(member.getEmailAddress());

                message.getTemplateMap().put("assignedGroup", assignedGroup);
                mailService.sendMessage(message);
            }


        } catch (Exception e) {
            logger.error("Could not notify assigned group", e);
        } finally {
            securityContext.reset();
        }
    }

    private void notifyEventAssignee(List<Event> events, User assignee) {
        try {

            TemplateMailMessage message = createMultiNotifications(events, assignee);

            message.getToAddresses().add(assignee.getEmailAddress());
            message.getTemplateMap().put("assignee", assignee);

            mailService.sendMessage(message);

        } catch (Exception e) {
            logger.error("Could not notify assignee", e);
        }
    }

    private TemplateMailMessage createMultiNotifications(List<Event> events, User assignee) {
        TemplateMailMessage msg = createMailMessage(events, assignee);

        return msg;
    }

    private TemplateMailMessage createMailMessage(List<Event> events, User assignee) {
        String subject = "Work Assigned: " + events.size() + " Events";
        TemplateMailMessage msg = new TemplateMailMessage(subject, ASSIGNEE_TEMPLATE_MULTI);

        Map<Long, String> dueDateStringMap = createDateStringMap(events, assignee);
        Map<Long, String> criteriaImageMap = createCriteriaImageMap(events);

        msg.getTemplateMap().put("systemUrl", SystemUrlUtil.getSystemUrl(events.get(0).getTenant()));
        msg.getTemplateMap().put("events", events);
        msg.getTemplateMap().put("subject", subject);
        msg.getTemplateMap().put("dueDateStringMap", dueDateStringMap);
        msg.getTemplateMap().put("criteriaImageMap", criteriaImageMap);
        return msg;
    }

    private Map<Long, String> createCriteriaImageMap(List<Event> events) {
        s3Service.setExpiryInDays(14);
        Map<Long, String> criteriaImageMap = new HashMap<Long,String>();
        for (Event event : events) {
            String query = "SELECT DISTINCT cr FROM " + CriteriaResult.class.getName() + " cr, IN(cr.actions) action WHERE action.id = :eventId";
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("eventId", event.getId());
            List<CriteriaResult> criteriaResults = (List<CriteriaResult>) persistenceService.runQuery(query, params);
            if(criteriaResults.size() > 0 && criteriaResults.get(0).getCriteriaImages().size() > 0) {
                CriteriaResult criteriaResult = criteriaResults.get(0);
                criteriaImageMap.put(event.getId(), s3Service.getCriteriaResultImageThumbnailURL(criteriaResult.getTenant().getId(), criteriaResult.getCriteriaImages().get(0)).toString());
            }
        }
        s3Service.resetExpiryInDays();
        return criteriaImageMap;
    }

    private Map<Long, String> createDateStringMap(List<Event> events, User assignee) {
        Map<Long, String> dueDateStringMap = new HashMap<Long,String>();
        for (Event event : events) {
            Date dueDate = event.getDueDate();
            boolean showTime = !new PlainDate(dueDate).equals(dueDate);
            String dueDateString = new FieldIdDateFormatter(dueDate, assignee, false, showTime).format();
            dueDateStringMap.put(event.getId(), dueDateString);
        }
        return dueDateStringMap;
    }


    private void removeNotificationsWithoutAssignees() {
        QueryBuilder<AssigneeNotification> builder = new QueryBuilder<AssigneeNotification>(AssigneeNotification.class, new OpenSecurityFilter());
        builder.addSimpleWhere("event.assignee", null);
        builder.addSimpleWhere("event.assignedGroup", null);
        removeNotifications(builder);
    }

    private void removeNotificationsForAssignee(User assignee) {
        QueryBuilder<AssigneeNotification> builder = new QueryBuilder<AssigneeNotification>(AssigneeNotification.class, new OpenSecurityFilter());
        builder.addSimpleWhere("event.assignee", assignee);
        removeNotifications(builder);
    }

    private void removeNotificationsForAssignedGroup(UserGroup assignedGroup) {
        QueryBuilder<AssigneeNotification> builder = new QueryBuilder<AssigneeNotification>(AssigneeNotification.class, new OpenSecurityFilter());
        builder.addSimpleWhere("event.assignedGroup", assignedGroup);
        removeNotifications(builder);
    }

    private void removeNotifications(QueryBuilder<AssigneeNotification> builder) {
        List<AssigneeNotification> resultList = persistenceService.findAll(builder);
        for (AssigneeNotification o : resultList) {
            o.getEvent().setAssigneeNotification(null);
            persistenceService.remove(o);
        }
    }

}
