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

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class NotifyEventAssigneeService extends FieldIdPersistenceService {

    private static final Logger logger = Logger.getLogger(NotifyEventAssigneeService.class);
    private static final String ASSIGNEE_TEMPLATE_MULTI = "eventsAssignedMulti";
    private static final String ASSET_URL_FRAGMENT = "/fieldid/w/assetSummary?4&uniqueID=";

    //Event Summary Fragments
    private static final String THING_EVENT_SUMMARY_URL_FRAGMENT = "/fieldid/w/thingEventSummary?0&id=";
    private static final String PLACE_EVENT_SUMMARY_URL_FRAGMENT = "/fieldid/w/placeEventSummary?0&id=";
    private static final String PROC_AUDIT_EVENT_SUMMARY_URL_FRAGMENT = "/fieldid/w/procAuditEventSummary?0&id=";

    //Main Perform Event Fragment
    private static final String PERFORM_EVENT_URL_FRAGMENT = "/fieldid/w/{0}?0&scheduleId={1}&type={2}";

    //Event ID Fragments...
    private static final String THING_EVENT_ID_FRAGMENT = "&assetId={3}";
    private static final String PLACE_EVENT_ID_FRAGMENT = "&placeId={3}";
    private static final String PROCEDURE_AUDIT_ID_FRAGMENT = "&procedureDefinitionId={3}";

    //Perform Event Fragments...
    private static final String PERFORM_PROCEDURE_AUDIT_FRAGMENT = "performProcedureAuditEvent";
    private static final String PERFORM_THING_EVENT_FRAGMENT = "performEvent";
    private static final String PERFORM_PLACE_EVENT_FRAGMENT = "performPlaceEvent";


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
        ActionEmailCustomization customizedValues = actionEmailCustomizationService.readForTennant(assignee.getTenant());
        String subject = events.size() + " " + customizedValues.getEmailSubject();
        String subHeading = customizedValues.getSubHeading();
        TemplateMailMessage msg = new TemplateMailMessage(subject, ASSIGNEE_TEMPLATE_MULTI) {
            /**
             * We override this method because we don't care about the configurations for header and footer.  The
             * footer is now static and the "header" is replaced by a customized "subHeading" which the Tenant can
             * change on their own without calling to Support.
             *
             * We also know that TempalteMaileMessages are ALWAYS HTML, so we can trim all of the logic out of this
             * method without encountering any problems.
             *
             * @return A String representation of the body of the email without those extra header and footer things.
             */
            @Override
            public String getFullBody() {
                return getBody();
            }
        };

        Map<Long, String> dueDateStringMap = createDateStringMap(events, assignee);
        Map<Long, String> criteriaImageMap = createCriteriaImageMap(events);
        Map<Long, String> triggeringEventStringMap = createTriggeringEventStringMap(events);
        Map<Long, String> assetUrlMap = createAssetUrlMap(events);
        Map<Long, String> eventSummaryUrlMap = createEventSummaryUrlMap(events);
        Map<Long, List<String>> attachedImageListMap = createAttachedImageListMap(events);
        Map<Long, String> performEventUrlMap = createPerformEventUrlMap(events);

        msg.getTemplateMap().put("systemUrl", SystemUrlUtil.getSystemUrl(events.get(0).getTenant()));
        msg.getTemplateMap().put("events", events);
        msg.getTemplateMap().put("subject", subject);
        msg.getTemplateMap().put("subHeadingMessage", subHeading);
        msg.getTemplateMap().put("dueDateStringMap", dueDateStringMap);
        msg.getTemplateMap().put("criteriaImageMap", criteriaImageMap);
        msg.getTemplateMap().put("attachedImageListMap", attachedImageListMap);
        msg.getTemplateMap().put("triggeringEventStringMap", triggeringEventStringMap);
        msg.getTemplateMap().put("assetUrlMap", assetUrlMap);
        msg.getTemplateMap().put("eventSummaryUrlMap", eventSummaryUrlMap);
        msg.getTemplateMap().put("performEventUrlMap", performEventUrlMap);
        msg.getTemplateMap().put("userEmail", assignee.getEmailAddress());
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
     * This method creates a Map of the String representation of URLs to Assets associated with supplied Events.  These
     * URLs are used in the Work Notification email to allow links to Assets to be opened directly.  They Map is keyed
     * by Asset ID so that we're not wasting memory on duplicate entries.
     *
     * @param events - A List of Events that a user is to be notified about.  This method will find the ones with Assets.
     * @return A Map of Asset URLs keyed by Asset ID.
     */
    private Map<Long,String> createAssetUrlMap(List<Event> events) {
        //First, lets grab all of the assets from any ThingEvents...
        //Warning: this might cause an NPE if the resulting list is empty... but I think it'll be fine.
        List<Asset> associatedAssets = events.stream()
                                             //So we filter for ThingEvents...
                                             .filter(event -> event instanceof ThingEvent)
                                             //then re-map the list to Assets and pull the Assets off of casted Events...
                                             .map(event -> ((ThingEvent)event).getAsset())
                                             //...then collect those Assets back to a Stream.
                                             .collect(Collectors.toList());

        //Next we're going to pull the Assets from any ActionEvents.  These don't have an asset themselves, but have
        //Trigger Events that might...
        associatedAssets.addAll(events.stream()
                                      //...so we'll filter for any ActionEvents with ThingEvent Trigger Events... EVENT!!!
                                      .filter(event -> event.isAction() && event.getTriggerEvent() instanceof ThingEvent)
                                      //We then pull off those assets and - again - re-map to an Asset List.
                                      .map(event -> ((ThingEvent) event).getAsset())
                                      .collect(Collectors.toList()));

        //Our implementation of equals should be enough for this to work.  We could have done this earlier, but
        //would still have wanted to call .distinct() yet again right here.  Now we just grind down the list of
        //Assets into a map of Asset URLs keyed by Asset ID.  Theoretically, we should be left with unique Assets at
        //this point, so this should just work.
        return associatedAssets.stream().distinct().collect(Collectors.toMap(BaseEntity::getId, this::createAssetUrl));
    }

    /**
     * This method builds a String representation of a URL to an asset using only the provided Asset itself.
     *
     * @param asset - An Asset entity representing an Asset in the system.
     * @return A String representation of the URL that should allow the user to open the Asset in the app.
     */
    private String createAssetUrl(Asset asset) {
        return SystemUrlUtil.getSystemUrl(asset.getTenant()) + ASSET_URL_FRAGMENT + asset.getId().toString();
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

        if(event.getTriggerEvent().getWorkflowState().equals(WorkflowState.COMPLETED)) {
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

    /**
     * This method creates a list of the String representation of URLs for all images attached to the Trigger Event of
     * an Action Event.  These URLs are used by the Freemarker template as values for "img" tags.
     *
     * @param event - An existing Event from the Database which Triggered an Action and has Image Attachments.
     * @return A List of Strings representing the URLs of all Image Attachments for a Trigger Event.
     */
    private List<String> createAttachedImageUrlList(Event event) {
        //I wanted to use streams here... I wanted to so badly... but for some weird reason, the compile was kicking up
        //errors due to return types and expected types for some methods... If you see this and can make it work, I will
        //buy you a cookie.
        List<String> urlList = new ArrayList<>();

        for(Object attachment : event.getImageAttachments()) {
            if(attachment instanceof FileAttachment) {
                urlList.add(s3Service.getFileAttachmentUrlForImpliedTenant((FileAttachment) attachment).toExternalForm());
            }
        }

        return urlList;
    }

    /**
     * This method creates a list of URLs to open the Event Summary page for various kinds of Events.  These Events are
     * the Trigger Event of any Action Events stored within the supplied List.  These URLs are added to the List as
     * Strings and keyed by the ID of the Trigger Event, so that we avoid wasting space.
     *
     * @param events - A List of Events to generate the URL Map from.
     * @return A Map of String URLs keyed by Trigger Event ID.
     */
    private Map<Long, String> createEventSummaryUrlMap(List<Event> events) {
        return events.stream()
                     //Poor naming, but we only care about events with trigger events...
                     .filter(event -> event.getTriggerEvent() != null)
                     .collect(Collectors.toMap(event -> event.getTriggerEvent().getId(), this::createEventSummaryUrl));
    }

    /**
     * This method creates the appropriate URL to view the summary of any kind of event that has a summary page... I
     * think.  It might also just release the Kraken.
     *
     * @param event - An event that you want to get the Event Summary URL for... or maybe you just want to feed it to the Kraken.
     * @return A String representation of the Event Summary URL... definitely not the Kraken.
     */
    private String createEventSummaryUrl(Event event) {
        String returnMe = null;
        if(event instanceof ThingEvent) {
            returnMe = SystemUrlUtil.getSystemUrl(event.getTenant()) + THING_EVENT_SUMMARY_URL_FRAGMENT + event.getId().toString();
        } else
        if(event instanceof PlaceEvent) {
            returnMe = SystemUrlUtil.getSystemUrl(event.getTenant()) + PLACE_EVENT_SUMMARY_URL_FRAGMENT + event.getId().toString();
        } else
        if(event instanceof ProcedureAuditEvent) {
            returnMe = SystemUrlUtil.getSystemUrl(event.getTenant()) + PROC_AUDIT_EVENT_SUMMARY_URL_FRAGMENT + event.getId().toString();
        }
        return returnMe;
    }

    /**
     * This method compiles a Map of URLs to allow the user to perform assigned actions.  These URLs are tailored to the
     * type of event and keyed by the ID of that event.
     *
     * @param events - A List of Events for which "Perform" URLs are required.
     * @return A Map of Perform Event URLs keyed by Event ID.
     */
    private Map<Long, String> createPerformEventUrlMap(List<Event> events) {
        //This one is simple... we're just collecting the list into a Map and using the function below to make the URLs.
        return events.stream()
                     .collect(Collectors.toMap(Event::getId, this::createPerformEventUrl));
    }

    /**
     * This method creates a Perform Event URL for a provided event.  This URL is tailored to the type of event and is
     * used in the Work Notification email to allow users to sign in directly to the mentioned event and perform it.
     *
     * @param event - An Event entity that already exists in the Database.
     * @return A String representation of the Perform Event URL for the provided Event.
     */
    private String createPerformEventUrl(Event event) {
        String performEventUrl = SystemUrlUtil.getSystemUrl(event.getTenant()) + PERFORM_EVENT_URL_FRAGMENT;
        //FIXME This is probably going to break for Master Events...
        if(event instanceof ThingEvent) {
            performEventUrl += THING_EVENT_ID_FRAGMENT;
            performEventUrl = MessageFormat.format(performEventUrl,
                                                   PERFORM_THING_EVENT_FRAGMENT,
                                                   event.getId().toString(),
                                                   event.getType().getId().toString(),
                                                   ((ThingEvent)event).getAsset().getId().toString());
        } else
        if(event instanceof PlaceEvent) {
            performEventUrl += PLACE_EVENT_ID_FRAGMENT;
            performEventUrl = MessageFormat.format(performEventUrl,
                                                   PERFORM_PLACE_EVENT_FRAGMENT,
                                                   event.getId().toString(),
                                                   event.getType().getId().toString(),
                                                   ((PlaceEvent)event).getPlace().getId());
        } else
        if(event instanceof ProcedureAuditEvent) {
            performEventUrl += PROCEDURE_AUDIT_ID_FRAGMENT;
            performEventUrl = MessageFormat.format(performEventUrl,
                                                   PERFORM_PROCEDURE_AUDIT_FRAGMENT,
                                                   event.getId().toString(),
                                                   event.getType().getId().toString(),
                                                   ((ProcedureAuditEvent)event).getProcedureDefinition().getId().toString());
        }

        return performEventUrl;
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
