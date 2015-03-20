package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.ActionEmailCustomizationService;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.download.SystemUrlUtil;
import com.n4systems.fieldid.service.mail.MailService;
import com.n4systems.fieldid.service.tenant.ExtendedFeatureService;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.model.*;
import com.n4systems.model.notification.AssigneeNotification;
import com.n4systems.model.notificationsettings.ActionEmailCustomization;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
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

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class NotifyEventAssigneeService extends FieldIdPersistenceService {

    private static final Logger logger = Logger.getLogger(NotifyEventAssigneeService.class);
    private static final String ASSIGNEE_TEMPLATE_MULTI = "eventsAssignedMulti";

    @Autowired private MailService mailService;
    @Autowired private S3Service s3Service;
    @Autowired private UserGroupService userGroupService;
    @Autowired private ExtendedFeatureService extendedFeatureService;
    @Autowired private ActionEmailCustomizationService actionEmailCustomizationService;

    @Transactional
    public void sendNotifications() {
        notifyUserAssignees();
        notifyGroupAssignees();

        removeNotificationsWithoutAssignees();
    }

    public boolean notificationExists(Event event) {
        QueryBuilder<AssigneeNotification> query = createTenantSecurityBuilder(AssigneeNotification.class);
        query.addSimpleWhere("event.id", event.getId());
        return persistenceService.exists(query);
    }

    private void notifyGroupAssignees() {
        QueryBuilder<AssigneeNotification> assigneeQuery = new QueryBuilder<>(AssigneeNotification.class, new OpenSecurityFilter());
        assigneeQuery.addWhere(WhereParameter.Comparator.NOTNULL, "event.assignedGroup", "event.assignedGroup", "");
        assigneeQuery.addSimpleWhere("event.tenant.disabled", false);

        List<AssigneeNotification> assigneeRecords = persistenceService.findAll(assigneeQuery);

        Set<UserGroup> groups = aggregateAssignedGroups(assigneeRecords);

        for (UserGroup assignee : groups) {
            sendNotificationsFor(assignee);
        }
    }

    private void notifyUserAssignees() {
        QueryBuilder<AssigneeNotification> assigneeQuery = new QueryBuilder<>(AssigneeNotification.class, new OpenSecurityFilter());
        assigneeQuery.addWhere(WhereParameter.Comparator.NOTNULL, "event.assignee", "event.assignee", "");
        assigneeQuery.addSimpleWhere("event.tenant.disabled", false);

        //So we basically need to add inner joins to the following tables, to make sure that we can pull up
        //tenant information (all may not be necessary... they were only necessary with raw SQL, not accounting
        //for the relationships managed by Hibernate    ):
        //  - org_base
        //  - org_primary
        //  - org_extendedfeatures

        List<AssigneeNotification> assigneeRecords = persistenceService.findAll(assigneeQuery);

        Set<User> assignees = aggregateAssignees(assigneeRecords);

        for (User assignee : assignees) {
            sendNotificationsFor(assignee);
        }
    }

    private Set<UserGroup> aggregateAssignedGroups(List<AssigneeNotification> assigneeRecords) {
        Set<UserGroup> assignees = new HashSet<>();
        for (AssigneeNotification assigneeRecord : assigneeRecords) {
            if (assigneeRecord.getEvent().getWorkflowState() == WorkflowState.OPEN && assigneeRecord.getEvent().getAssignedGroup() != null) {
                assignees.add(assigneeRecord.getEvent().getAssignedGroup());
            }
        }
        return assignees;
    }

    private Set<User> aggregateAssignees(List<AssigneeNotification> assigneeRecords) {
        Set<User> assignees = new HashSet<>();
        for (AssigneeNotification assigneeRecord : assigneeRecords) {
            if (assigneeRecord.getEvent().getWorkflowState() == WorkflowState.OPEN && assigneeRecord.getEvent().getAssignee() != null) {
                assignees.add(assigneeRecord.getEvent().getAssignee());
            }
        }
        return assignees;
    }

    private void sendNotificationsFor(User assignee) {
        QueryBuilder<Event> assigneeQuery = new QueryBuilder<>(AssigneeNotification.class, new OpenSecurityFilter());
        assigneeQuery.setSimpleSelect("event");
        assigneeQuery.addSimpleWhere("event.assignee", assignee);
        List<Event> notificationEvents = persistenceService.findAll(assigneeQuery);
        notifyEventAssignee(notificationEvents, assignee);
        removeNotificationsForAssignee(assignee);
    }

    private void sendNotificationsFor(UserGroup assignedGroup) {
        QueryBuilder<Event> assigneeQuery = new QueryBuilder<>(AssigneeNotification.class, new OpenSecurityFilter());
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

                if(member.getOwner().getPrimaryOrg().getExtendedFeatures().contains(ExtendedFeature.EmailAlerts)) {
                    // Instead of sending a multi message, since users can define their own date
                    // formats and we want our notification to reflect this, we send an individual email to each member.
                    TemplateMailMessage message = createMultiNotifications(events, member);

                    message.getToAddresses().add(member.getEmailAddress());

                    message.getTemplateMap().put("assignedGroup", assignedGroup);
                    mailService.sendMessage(message);
                }
            }


        } catch (Exception e) {
            logger.error("Could not notify assigned group", e);
        } finally {
            securityContext.reset();
        }
    }

    private void notifyEventAssignee(List<Event> events, User assignee) {
        try {
            if(assignee.getOwner().getPrimaryOrg().getExtendedFeatures().contains(ExtendedFeature.EmailAlerts)) {
                TemplateMailMessage message = createMultiNotifications(events, assignee);

                message.getToAddresses().add(assignee.getEmailAddress());
                message.getTemplateMap().put("assignee", assignee);

                mailService.sendMessage(message);
            }
        } catch (Exception e) {
            logger.error("Could not notify assignee", e);
        }
    }

    private TemplateMailMessage createMultiNotifications(List<Event> events, User assignee) {
        return createMailMessage(events, assignee);
    }

    private TemplateMailMessage createMailMessage(List<Event> events, User assignee) {
        //If this hasn't been configured yet, that's okay.  We just pull the default values until the user gets around
        //to customizing them or saving the existing values as a real row in the DB.
        ActionEmailCustomization customizedValues = actionEmailCustomizationService.read();
        String subject = customizedValues.getEmailSubject();
        String subHeading = customizedValues.getSubHeading();
        TemplateMailMessage msg = new TemplateMailMessage(subject, ASSIGNEE_TEMPLATE_MULTI);

        Map<Long, String> dueDateStringMap = createDateStringMap(events, assignee);
        Map<Long, String> criteriaImageMap = createCriteriaImageMap(events);
        Map<Long, String> triggeringEventStringMap = createTriggeringEventStringMap(events);
        Map<Long, List<String>> attachedImageListMap = createAttachedImageListMap(events);

        msg.getTemplateMap().put("systemUrl", SystemUrlUtil.getSystemUrl(events.get(0).getTenant()));
        msg.getTemplateMap().put("events", events);
        msg.getTemplateMap().put("subject", subject);
        msg.getTemplateMap().put("subHeadingMessage", subHeading);
        msg.getTemplateMap().put("dueDateStringMap", dueDateStringMap);
        msg.getTemplateMap().put("criteriaImageMap", criteriaImageMap);
        msg.getTemplateMap().put("attachedImageListMap", attachedImageListMap);
        msg.getTemplateMap().put("triggeringEventStringMap", triggeringEventStringMap);
        return msg;
    }

    private Map<Long, String> createCriteriaImageMap(List<Event> events) {
        s3Service.setExpiryInDays(14);
        Map<Long, String> criteriaImageMap = new HashMap<>();
        for (Event event : events) {
            String query = "SELECT DISTINCT cr FROM " + CriteriaResult.class.getName() + " cr, IN(cr.actions) action WHERE action.id = :eventId";
            Map<String, Object> params = new HashMap<>();
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
        Map<Long, String> dueDateStringMap = new HashMap<>();
        for (Event event : events) {
            Date dueDate = event.getDueDate();
            boolean showTime = !new PlainDate(dueDate).equals(dueDate);
            String dueDateString = new FieldIdDateFormatter(dueDate, assignee, false, showTime).format();
            dueDateStringMap.put(event.getId(), dueDateString);
        }
        return dueDateStringMap;
    }

    /**
     * This method processes the provided List of Events and returns a Map of Strings indexed by Event ID containing
     * a textual representation of the Triggering Event for any of the provided objects.  If an Event doesn't have a
     * triggering event, then there is nothing added to the Map for that Event.
     *
     * @param events - A List populated with Events.
     * @return A Map of Strings indexed by Event ID which roughly represent the Triggering Event for any Event which has one.
     */
    private Map<Long, String> createTriggeringEventStringMap(List<Event> events) {
        return events.stream()
                     .filter(event -> event.getTriggerEvent() != null)
                     .collect(Collectors.toMap(Event::getID, this::createTriggeringEventString));
    }

    /**
     * This method accepts Events, but assumes that they have Triggering Events.  These Events are processed to produce
     * a String which represents the said Triggering Event.
     *
     * @param event - An Event with a Triggering Event.  If you supply an Event without one, the plug comes out of the bottom of the Atlantic Ocean.
     * @return A String representing the Event's Triggering Event.
     */
    private String createTriggeringEventString(Event event) {
        StringBuilder triggeringEventString = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

        triggeringEventString.append(dateFormat.format(event.getRelevantDate())).append(" From ");
        triggeringEventString.append(event.getType().getName()).append(" ");
        triggeringEventString.append(event.getAdvancedLocation().getFullName());

        if(event.getWorkflowState().equals(WorkflowState.COMPLETED)) {
            triggeringEventString.append(" > ").append(event.getTriggerEvent().getEventResult().getDisplayName());
        }

        return triggeringEventString.toString();
    }

    /**
     * This method accepts a List of Events and processes it to produce a Map of Image URLs, indexed by Event ID.  This
     * Map is used by the Email Generator to append images to corresponding Events within the email.
     *
     * @param events - A List populated with Events.
     * @return A Map of Image URLs indexed by Event ID which roughly represent the images associated with Assets associated with the Events.
     */
    private Map<Long, List<String>> createAttachedImageListMap(List<Event> events) {
        return events.stream()
                     .filter(event -> event.getTriggerEvent() != null && event.getTriggerEvent().getImageAttachments().size() > 0)
                     .collect(Collectors.toMap(Event::getId, event -> createAttachedImageUrlList(event.getTriggerEvent())));
    }

    private List<String> createAttachedImageUrlList(Event event) {
        //I wanted to use streams here... I wanted to so badly... but for some weird reason, the compile was kicking up
        //errors due to return types and expected types for some methods... If you see this and can make it work, I will
        //buy you a cookie.
        List<String> urlList = new ArrayList<>();

        for(Object attachment : event.getImageAttachments()) {
            if(attachment instanceof FileAttachment) {
                urlList.add(s3Service.getFileAttachmentUrl((FileAttachment) attachment).toExternalForm());
            }
        }

        return urlList;
    }

    private void removeNotificationsWithoutAssignees() {
        QueryBuilder<AssigneeNotification> builder = new QueryBuilder<>(AssigneeNotification.class, new OpenSecurityFilter());
        builder.addSimpleWhere("event.assignee", null);
        builder.addSimpleWhere("event.assignedGroup", null);
        removeNotifications(builder);
    }

    private void removeNotificationsForAssignee(User assignee) {
        QueryBuilder<AssigneeNotification> builder = new QueryBuilder<>(AssigneeNotification.class, new OpenSecurityFilter());
        builder.addSimpleWhere("event.assignee", assignee);
        removeNotifications(builder);
    }

    private void removeNotificationsForAssignedGroup(UserGroup assignedGroup) {
        QueryBuilder<AssigneeNotification> builder = new QueryBuilder<>(AssigneeNotification.class, new OpenSecurityFilter());
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
