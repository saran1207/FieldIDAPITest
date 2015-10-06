package com.n4systems.fieldid.service.escalationrule;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.download.SystemUrlUtil;
import com.n4systems.model.*;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.location.Location;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.EventSearchType;
import com.n4systems.model.search.WorkflowStateCriteria;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.model.utils.PlainDate;
import com.n4systems.util.DateHelper;
import com.n4systems.util.mail.EventUrlUtil;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.search.SortDirection;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class provides access to the assignment_escalation_rules table and the associated Events affected by those
 * rules.  This means that methods are included to read the related Events from their respective tables.
 *
 * Created by Jordan Heath on 2015-08-18.
 */
public class AssignmentEscalationRuleService extends FieldIdPersistenceService {
    private static final Logger logger = Logger.getLogger(AssignmentEscalationRuleService.class);
    private static final String CLEAR_RULES_FOR_EVENT_SQL = "DELETE FROM escalation_rule_execution_queue WHERE event_id = :eventId";
    private static final String CLEAR_QUEUE_ITEMS_FOR_RULE_SQL = "DELETE FROM escalation_rule_execution_queue WHERE rule_id = :ruleId";

    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    private static final SimpleDateFormat ALL_DAY_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");

    private static final SimpleDateFormat ZONE_AGNOSTIC_DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private static final long easternOffset = (long) TimeZone.getTimeZone("Canada/Eastern").getRawOffset();

    /**
     * This method returns all of the ACTIVE rules for the current user.
     *
     * @return A list of AssignmentEscalationRule objects that are active for the current user.
     */
    public List<AssignmentEscalationRule> getAllActiveRules() {
        QueryBuilder<AssignmentEscalationRule> builder = createTenantSecurityBuilder(AssignmentEscalationRule.class);
        builder.addSimpleWhere("createdBy", getCurrentUser());
        return persistenceService.findAll(builder);
    }

    /**
     * This method checks the uniqueness of the rule name for the current user.
     *
     * @param name - A string representing the name of the rule that we are checking uniqueness of.
     * @return A boolean determining whether or not the name exists (true if it exists, false otherwise).
     */
    public boolean isNameUnique(String name) {
        QueryBuilder<AssignmentEscalationRule> builder = createTenantSecurityBuilder(AssignmentEscalationRule.class);
        builder.addSimpleWhere("createdBy", getCurrentUser());
        builder.addSimpleWhere("ruleName", name);
        return persistenceService.exists(builder);
    }

    /**
     * This method archives the AssignmentEscalationRule object (soft deletes the rules).  It does, however,
     * permanently delete rows in the escalation_rule_processing_queue, because failing to do so would either cause
     * an exception when we try to process those rows, or would lead to rows that simply sit in the table and slow down
     * subsequent queries.
     *
     * @param rule - the AssignmentEscalationRule object that will be updated
     *
     */
    @Transactional
    public void archiveRule(AssignmentEscalationRule rule) {
        rule.setState(Archivable.EntityState.ARCHIVED);
        updateRule(rule);
        clearQueueItemsForRule(rule.getId());
    }

    /**
     * This method updates the AssignmentEscalationRule object and, if necessary, the email jobs that need to run associated with this rule.
     *
     * @param rule - the AssignmentEscalationRule object that will be updated
     * @param oldDateRange - A Long representing the old date that the user had selected in milliseconds.
     *
     */
    public void updateRule(AssignmentEscalationRule rule, Long oldDateRange) {
        //To make things as simple as possible, we will simply update the existing Queue items.  This allows us to
        //avoid running the search that backs the rule, which can take a significant amount of time to run compared to
        //this update which SHOULD be rather quick.
        rule = persistenceService.update(rule);
        if(oldDateRange.longValue() != rule.getOverdueQuantity().longValue()) {
            long difference = oldDateRange - rule.getOverdueQuantity();

            updateNotificationDateInQueue(rule, difference);
        }
    }

    /**
     * This method simply grabs the AssignmentEscalationRule object by its provided eventId.
     *
     * @param id - A Long representing the ID of the assignment_escalation_rule row.
     * @return An AssignmentEscalationRule entity representing the desired rule.
     */
    public AssignmentEscalationRule getRuleById(Long id) {
        return persistenceService.find(AssignmentEscalationRule.class, id);
    }

    /**
     * This method simply saves a provided AssignmentEscalationRule entity to the database.  You could probably
     * use this for updating OR saving... but please try to only use it for saving.  It might cause the plug in the
     * bottom of the Atlantic Ocean to come out if it's used too much.
     *
     * @param rule - An AssignmentEscalationRule entity which doesn't exist in the database yet.
     * @return An AssignmentEscalationRule entity representing the one you just saved!
     */
    public AssignmentEscalationRule saveRule(AssignmentEscalationRule rule) {
        Long id = persistenceService.save(rule);

        return getRuleById(id);
    }

    /**
     * This method updates an existing AssignmentEscalationRule entity in the database.  You could probably use
     * this for saving, as well, but please do try to only use it for updates.  Similar to the method above, using
     * this for the wrong purpose may cause the drain plug in the Pacific Ocean to become dislodged.
     *
     * @param rule - An AssignmentEscalationRule entity which needs to be updated in the database.
     * @return An AssignmentEscalationRule entity representing the one you just updated!
     */
    public AssignmentEscalationRule updateRule(AssignmentEscalationRule rule) {
        //If we're updating the rule, do we also want to ensure we make any necessary updates to the linker entity,
        //such as resetting those "rule has run" flags?

        return persistenceService.update(rule);
    }

    /**
     * In order to make things easier for the creation of Escalation Rules, we're going to create them (CREATE the
     * entity, not it save to the database) using the EventReportCriteria object that spawned the search the rule
     * is based on.
     *
     * @param criteria - The same EventReportCriteria object that created the search/report we're basing the rule on.
     * @return An AssignmentEscalationRule entity the necessary criteria populated from the supplied EventReportCriteria.
     */
    public AssignmentEscalationRule createRule(EventReportCriteria criteria) {
        AssignmentEscalationRule returnMe = new AssignmentEscalationRule();

        returnMe.setType(
                criteria.getEventSearchType().equals(EventSearchType.ACTIONS)?
                        AssignmentEscalationRule.Type.ACTION :
                        AssignmentEscalationRule.Type.EVENT
        );
        returnMe.setEventTypeGroup(criteria.getEventTypeGroup());
        returnMe.setEventType(criteria.getEventType());
        returnMe.setAssetStatus(criteria.getAssetStatus());
        returnMe.setAssetType(criteria.getAssetType());
        returnMe.setAssetTypeGroup(criteria.getAssetTypeGroup());
        returnMe.setAssignedTo(criteria.getAssignedTo());
        returnMe.setAssignee(criteria.getAssignee());
        returnMe.setOwner(criteria.getOwner());
        returnMe.setLocation(criteria.getLocation());
        returnMe.setRfidNumber(criteria.getRfidNumber());
        returnMe.setSerialNumber(criteria.getIdentifier());
        returnMe.setReferenceNumber(criteria.getReferenceNumber());
        returnMe.setOrderNumber(criteria.getOrderNumber());
        returnMe.setPurchaseOrder(criteria.getPurchaseOrder());

        returnMe.setTenant(getCurrentTenant());
        returnMe.setOwner(getCurrentUser().getOwner());
        returnMe.setCreatedBy(getCurrentUser());

        return returnMe;
    }

    /**
     * This method creates all Queue items for a rule.  You should call it after you create a rule and will need to pass
     * both a list of the IDs of the initially affected events and the AssignmentEscalationRule that applies to them.
     *
     * It will then generate all necessary queue items, including JSON Strings representing the likely contents of the
     * Escalation email and the event's modification date.  This date will be used later to determine whether the JSON
     * needs to be regenerated to provide accurate data for the email.
     *
     * You should be able ot grab this list of IDs from the results of a Reporting Search.
     *
     * @param eventIdList - A List populated with Longs, representing the ID of all Events initially affected.
     * @param rule - An AssignmentEscalationRule representing the Rule which affects the provided Events.
     */
    @Transactional
    public void initializeRule(List<Long> eventIdList, AssignmentEscalationRule rule) {
        eventIdList.forEach(eventId ->  {
            Event event = persistenceService.find(Event.class, eventId);
            writeQueueItem(event, rule);
        });
    }

    /**
     * Update the NotificationDate on all Queue Items for the given Rule.  We don't need to know a terrible amount, only
     * if the queue items are related to the rule.  We'll also update ones that have already fired, just in case they
     * get reset later.
     *
     * The updated time is applied by adding the difference between the old and new Overdue Quantities to the Notify
     * Date field on the Queue Item.
     *
     * @param rule - An AssignmentEscalationRule that has had its
     * @param difference - A long value representing the difference between the old and new Overdue amounts.
     */
    private void updateNotificationDateInQueue(AssignmentEscalationRule rule, long difference) {
        QueryBuilder<EscalationRuleExecutionQueueItem> query = new QueryBuilder<>(EscalationRuleExecutionQueueItem.class, new OpenSecurityFilter());
        query.addSimpleWhere("rule", rule);
        query.addSimpleWhere("tenant.id", rule.getTenant().getId());

        List<EscalationRuleExecutionQueueItem> results = persistenceService.findAll(query);

        results.forEach(queueItem -> {
            Date correctedDate = new Date(queueItem.getNotifyDate().getTime() + difference);
            queueItem.setNotifyDate(correctedDate);

            //Set the rule as not having run, so it can run again... this may not be ideal, but we'll see.
            queueItem.setRuleHasRun(false);
            persistenceService.update(queueItem);
        });
    }

    /**
     * This method takes an Event entity and an AssignmentEscalationRule and will generate the appropriate Queue row
     * for the Escalation Rule.  If the event passes its due date without being completed, then these Queue rows will
     * be processed and acted upon.
     *
     * @param event - An Event entity for which we want to write a Queue Item for a particular rule.
     * @param rule - An AssignmentEscalationRule entity, representing the rule which applies to the Event.
     */
    private void writeQueueItem(Event event, AssignmentEscalationRule rule) {
        EscalationRuleExecutionQueueItem queueItem = new EscalationRuleExecutionQueueItem();

        //At this point, we double check that we don't have what amounts to shitty data.
        //Event should:
        // - be in OPEN workflow state
        // - must HAVE a DueDate
        // - DueDate must be after NOW
        // - I think that's it.

        //Convert current system time from UTC to the rule creator's time zone.
        Date currentTime = new Date(System.currentTimeMillis());
        TimeZone userTimeZone = rule.getCreatedBy().getTimeZone();
        Date currentTimeInUsersTimeZone = DateHelper.convertToUserTimeZone(currentTime, userTimeZone);

        if(event.getWorkflowState().equals(WorkflowState.OPEN) &&
                event.getDueDate() != null &&
                event.getDueDate().after(currentTimeInUsersTimeZone)) {

            //Dates and Times are hard.  Thank goodness for Java 8's Time API.

            //1) First, we shift the time by the OverDue amount.  This is going to happen the same no matter what,
            //  because the amount you're overdue by is TimeZone agnostic.  1 hour always equals 1 hour.
            Long timeChanger = event.getDueDate().getTime();
            timeChanger += rule.getOverdueQuantity();
            Date overdueApplied = new Date(timeChanger);

            //2) The user doesn't realize there's no TimeZone attached to the Due Date.  So we need to simulate that
            //  by creating this ZonedDateTime object without actually shifting the time.  No problem.
            ZonedDateTime zonedDateTime = ZonedDateTime.of(
                    LocalDateTime.parse(ZONE_AGNOSTIC_DATE_TIME_FORMAT.format(overdueApplied)),
                    rule.getCreatedBy().getTimeZone().toZoneId()
            );

            //3) Now that we've shifted the time without changing it to the user's TimeZone, we need to shift it to
            //  the server's TimeZone and properly offset the time.  This will cause the server to send it's
            //  notifications and do its work at the time that equates to the desired time in the user's TimeZone.
            zonedDateTime = zonedDateTime.withZoneSameInstant(TimeZone.getDefault().toZoneId());

            //4) We have now shifted the timezone of the provided DateTime data.  It is sufficiently corrected to
            //  write to the DB.  However, to do this, we need to convert it back down to a Date so we can lose
            //  reference to the TZ again.
            overdueApplied = Date.from(zonedDateTime.toInstant());
            queueItem.setNotifyDate(overdueApplied);

            //...and do everything else as normal.
            queueItem.setEventId(event.getId());
            queueItem.setEventModDate(event.getModified());
            queueItem.setRuleHasRun(false);

            queueItem.setRule(rule);

            queueItem.setMapJson(generateEventMapJSON(event));

            persistenceService.save(queueItem);
        }
    }

    /**
     * This method calls out to convert the Event to a HashMap and then uses Jackson to convert that Event into a Map,
     * which then gets converted into a JSON String.  We only want a small portion of the event for that map, so there's
     * no point in just converting the whole thing.
     *
     * @param event - An Event object, representing the Event you're going to eventually send an email about.
     * @return A String containing a JSON representation of the important bits of that Event for the email.
     */
    public String generateEventMapJSON(Event event) {
        Map<String, Object> eventMap = createMap(event);

        ObjectMapper jsonMaker = new ObjectMapper();
        jsonMaker.enableDefaultTyping(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT);
        StringWriter json = new StringWriter();

        try {
            JsonGenerator generator = jsonMaker.getFactory().createGenerator(json);

            jsonMaker.writeValue(generator, eventMap);

            return json.toString();
        } catch (IOException e) {
            logger.error("Unable to create JSON for Event with ID " + event.getId());
        }

        return null;
    }

    /**
     * This method pulls the important fields off of an Event and throws them into a Map so that they can be either
     * converted into JSON or immediately used to produce a map for generating an Escalation email.
     *
     * @param event - An Event object representing the Event you want converted into a Map.
     * @return A Map containing the important fields of the Event keyed by String values.
     */
    private Map<String, Object> createMap(Event event) {
        Map<String, Object> returnMe = new HashMap<>();

        //Essential Fields - No matter the Event Type, we need these.
        returnMe.put("systemUrl", SystemUrlUtil.getSystemUrl(event.getTenant()));
        returnMe.put("dueDate", createDueDateString(event));
        returnMe.put("performEventURL", EventUrlUtil.createPerformEventUrl(event));
        returnMe.put("eventSummaryURL", EventUrlUtil.createEventSummaryUrl(event));
        returnMe.put("isThingEvent", event.getType().isThingEventType());
        returnMe.put("isPlaceEvent", event.getType().isPlaceEventType());
        returnMe.put("isAuditEvent", event.getType().isProcedureAuditEventType());
        returnMe.put("isAction", event.getType().isActionEventType());
        returnMe.put("assigneeEmail", event.getAssignee() != null ? event.getAssignee().getEmailAddress() : null);
        returnMe.put("assigneeName", event.getAssignee() != null ? event.getAssignee().getDisplayName() : null);
        returnMe.put("assignedGroupEmails", event.getAssignedGroup() != null ? createEmailList(event.getAssignedGroup()) : null);
        returnMe.put("assignedGroupName", event.getAssignedGroup() != null ? event.getAssignedGroup().getDisplayName() : null);
        returnMe.put("eventType", event.getType().getDisplayName());
        returnMe.put("showLinks", (event.getAssignee() != null && !event.getAssignee().isPerson()) || event.getAssignedGroup() != null); //Not sure if this one is entirely necessary, but it might be.




        //Thing Event Fields - We only need these values if we're escalating a ThingEvent.
        returnMe.put("assetSummaryURL", event.getType().isThingEventType() ? EventUrlUtil.createAssetSummaryUrl(((ThingEvent) event).getAsset()) : null);
        returnMe.put("assetName", event.getType().isThingEventType() ? EventUrlUtil.createAssetName(((ThingEvent) event).getAsset()) : null);
        returnMe.put("assetOwnerName", event.getType().isThingEventType() ? ((ThingEvent)event).getAsset().getOwner().getDisplayName() : null);




        //Place Event Fields - We only need these values if we're escalating a PlaceEvent.
        returnMe.put("placeSummaryURL", event.getType().isPlaceEventType() ? EventUrlUtil.createPlaceSummaryUrl(((PlaceEvent) event).getPlace()) : null);
        returnMe.put("placeName", event.getType().isPlaceEventType() ? ((PlaceEvent) event).getPlace().getDisplayName() : null);




        //Audit Event Fields - We only need these values if we're escalating a ProcedureAuditEvent.
        //Currently there aren't any, but it's likely we'll get a request for some...



        //Action Fields - We only need these values if we're escalating an Action.  This gets more complex, because we
        //also want to know about the Triggering event, which can be any of the three above types.
        returnMe.put("triggeringEventURL", event.getType().isActionEventType() ? EventUrlUtil.createEventSummaryUrl(event.getTriggerEvent()) : null);
        returnMe.put("triggeringEventName", event.getType().isActionEventType() ? EventUrlUtil.createTriggeringEventString(event, event.getAssignee()) : null);

        returnMe.put("triggeringPlaceName", event.getType().isActionEventType() && event.getTriggerEvent().getType().isPlaceEventType() ? ((PlaceEvent)event.getTriggerEvent()).getPlace().getDisplayName() : null);
        returnMe.put("triggeringPlaceSummaryURL", event.getType().isActionEventType() && event.getTriggerEvent().getType().isPlaceEventType() ? EventUrlUtil.createPlaceSummaryUrl(((PlaceEvent) event.getTriggerEvent()).getPlace()) : null);

        returnMe.put("triggeringAssetName", event.getType().isActionEventType() && event.getTriggerEvent().getType().isThingEventType() ? EventUrlUtil.createAssetName(((ThingEvent) event.getTriggerEvent()).getAsset()) : null);
        returnMe.put("triggeringAssetSummaryURL", event.getType().isActionEventType() && event.getTriggerEvent().getType().isThingEventType() ? EventUrlUtil.createAssetSummaryUrl(((ThingEvent) event.getTriggerEvent()).getAsset()) : null);

        returnMe.put("actionPriority", event.getType().isActionEventType() ? event.getPriority().getDisplayName() : null);

        return returnMe;
    }

    /**
     * This method converts a UserGroup into a list of email addresses for use in creating the Escalation Notification
     * email.
     *
     * @param assignedGroup - A UserGroup representing the group assigned to the Event.
     * @return A List of Strings representing the group members' collective email addresses.
     */
    private List<String> createEmailList(UserGroup assignedGroup) {
        //Ensure we reload this value.
        QueryBuilder<UserGroup> query = new QueryBuilder<>(UserGroup.class, new OpenSecurityFilter());
        query.addSimpleWhere("id", assignedGroup.getId());
        assignedGroup = persistenceService.find(query);

        return assignedGroup.getMembers()
                            .stream()
                            .map(User::getEmailAddress)
                            .collect(Collectors.toList());
    }

    /**
     * This method returns a String representing the Due Date of the Event.  This String will be formatted to the
     * Assignee's desired Date/Time format.
     *
     * @param event - An Event from which we want to generate a Due Date String.
     * @return A String containing a formatted Due Date, conforming to the Assignee's saved date format preference.
     */
    private String createDueDateString(Event event) {
        Date dueDate = event.getDueDate();
        boolean showTime = !new PlainDate(dueDate).equals(dueDate);

        if(showTime) {
            return DATETIME_FORMAT.format(event.getDueDate());
        } else {
            return ALL_DAY_DATE_FORMAT.format(event.getDueDate());
        }
    }

    /**
     * In order to make things easier for running a search from the Assignment Escalation Rule service, we'll provide
     * a convenience method to convert an AssignmentEscalationRule entity into an EventReportCriteria entity.  This
     * allows us to keep this information isolated to one table and facilitates future conversion of this portion of
     * the system into a microservice by minimising dependency on other tables (such as the saved_reports table).
     *
     * This Criteria object is fed to the ReportService to retrieve specific Events to determine whether or not they
     * have exceeded their due dates by an amount which would trigger an Escalation Rule.
     *
     * @param rule - An AssignmentEscalationRule entity representing the Rule for which you would like to perform a search.
     * @return An EventReportCriteria with the necessary fields filled from the Rule to be used to search for Events.
     */
    public EventReportCriteria createCriteria(AssignmentEscalationRule rule) {
        EventReportCriteria returnMe = new EventReportCriteria();

        //We ALWAYS search for open events/actions only.  So this gets statically set.
        returnMe.setWorkflowState(WorkflowStateCriteria.OPEN);
        returnMe.setEventSearchType(
                rule.getType().equals(AssignmentEscalationRule.Type.ACTION) ?
                        EventSearchType.ACTIONS :
                        EventSearchType.EVENTS
        );
        returnMe.setEventTypeGroup(rule.getEventTypeGroup());
        returnMe.setEventType(rule.getEventType());
        returnMe.setAssetStatus(rule.getAssetStatus());
        returnMe.setAssetTypeGroup(rule.getAssetTypeGroup());
        returnMe.setAssignedTo(rule.getAssignedTo());
        returnMe.setAssignee(rule.getAssignee());
        returnMe.setOwner(rule.getOwner());

        //This one is a bit weird.  We can't just let it be null, even if it is null.  We need to make an EMPTY
        //Location, not a NULL one.  If it's not null, then we're fine.
        returnMe.setLocation(rule.getLocation() == null ? Location.onlyFreeformLocation("") : rule.getLocation());

        returnMe.setRfidNumber(rule.getRfidNumber());
        returnMe.setIdentifier(rule.getSerialNumber());
        returnMe.setReferenceNumber(rule.getReferenceNumber());
        returnMe.setOrderNumber(rule.getOrderNumber());
        returnMe.setPurchaseOrder(rule.getPurchaseOrder());

        //We also need to set a "SortDirection" value, even though I'm pretty certain it's never actually used.
        returnMe.setSortDirection(SortDirection.ASC);

        return returnMe;
    }

    /**
     * WARNING: There is no security on this method!  It is used by a backend process so it needs to be Tenant agnostic.
     *
     * This will return a list of all EscalationRuleExecutionQueueItems that are up for processing and - likely - for
     * sending an email.  These should be processed and summarily deleted from the queue only by the appropriate
     * service.  Please don't monkey with these.
     *
     * @return A List populated with all EscalationRuleExecutionQueueItems that need to be processed at the time of calling.
     */
    public List<EscalationRuleExecutionQueueItem> getQueueItemsForProcessing() {
        QueryBuilder<EscalationRuleExecutionQueueItem> queueQuery =
                new QueryBuilder<>(EscalationRuleExecutionQueueItem.class, new OpenSecurityFilter());

        queueQuery.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.LE, "notifyDate", new Date()));
        queueQuery.addSimpleWhere("ruleHasRun", false);
        queueQuery.addSimpleWhere("rule.tenant.disabled", false);
        //I THINK this will have us sorting by tenant.  That'll be a little useful, just to avoid hopping all over with
        //our security context.  It's probably fine, but I don't like doing that.
        queueQuery.addOrder("rule.tenant.id", true);

        return persistenceService.findAll(queueQuery);
    }

    /**
     * This method is simply used to update an EscalationRuleExecutionQueueItem entity.
     *
     * @param queueItem - An EscalationRuleExecutionQueueItem that you want to update.
     * @return The updated EscalationRuleExecutionQueueItem.
     */
    public EscalationRuleExecutionQueueItem updateQueueItem(EscalationRuleExecutionQueueItem queueItem) {
        return persistenceService.update(queueItem);
    }

    /**
     * This method takes an Event as input and determins which ACTIVE rules, under that Tenant, apply to the Event.  For
     * all rules that apply, appropriate EscalationRuleExecutionQueueItems are made and saved to the database.
     *
     * This method should be called upon the creation of an event to ensure that the appropriate Queue Items are
     * immediately in place, instead of being generated at the time that we call the event.
     *
     * @param event - An Event for which QueueItems may need to be created.
     */
    public void createApplicableQueueItems(Event event) {
        QueryBuilder<AssignmentEscalationRule> ruleQuery = new QueryBuilder<>(AssignmentEscalationRule.class, new OpenSecurityFilter());

        persistenceService.findAll(ruleQuery)
                          .stream()
                          .filter(rule -> doesRuleApply(rule, event))
                          .forEach(rule -> writeQueueItem(event, rule));
    }

    /**
     * This ugly bit of logic determines whether a given AssignmentEscalationRule is applicable to a given Event.  This
     * is used to determine which Queue items for rule execution (if any) need to be created for this Event.
     *
     * This method is used during stream processing of a query for all active Rules for a given tenant.
     *
     * If it wasn't for the fact that it's total overkill, DROOLS would be pretty damn useful here.
     *
     * I know... it's hideous.
     *
     * @param rule - An AssignmentEscalationRule entity representing an ACTIVE Rule.
     * @param event - An Event entity representing the Event for which we want to determine if the Rule applies.
     * @return A boolean response indicating whether (true) or not (false) the Rule applies to the Event.
     */
    private boolean doesRuleApply(AssignmentEscalationRule rule, Event event) {
        //Make sure we have fail if the Rule is for an Action and we have an Event, or vice versa.

        if(!rule.getCreatedBy().getOwner().isPrimary() &&
                !rule.getCreatedBy().getOwner().isParentOf(event.getOwner())) {
            return false;
        }

        if(!event.getWorkflowState().equals(WorkflowState.OPEN)) {
            return false;
        }

        AssignmentEscalationRule.Type eventType = event.getType().isActionEventType() ? AssignmentEscalationRule.Type.ACTION : AssignmentEscalationRule.Type.EVENT;

        if(rule.getType() != null && !rule.getType().equals(eventType)) {
            return false;
        }

        if(rule.requiresThingEvent() && !event.getType().isThingEventType()) {
            return false;
        } else {
            if(rule.getAssetStatus() != null && !rule.getAssetStatus().equals(((ThingEvent)event).getAssetStatus())) {
                return false;
            }

            if(rule.getAssetType() != null && !rule.getAssetType().equals(((ThingEvent)event).getAsset().getType())) {
                return false;
            }

            if(rule.getAssetTypeGroup() != null && !rule.getAssetTypeGroup().equals(((ThingEvent)event).getAsset().getType().getGroup())) {
                return false;
            }

            if(rule.getRfidNumber() != null && !rule.getRfidNumber().equalsIgnoreCase(((ThingEvent) event).getAsset().getRfidNumber())) {
                return false;
            }

            if(rule.getSerialNumber() != null && !rule.getSerialNumber().equalsIgnoreCase(((ThingEvent)event).getAsset().getIdentifier())) {
                return false;
            }

            if(rule.getPurchaseOrder() != null && !rule.getPurchaseOrder().equalsIgnoreCase(((ThingEvent)event).getAsset().getPurchaseOrder())) {
                return false;
            }

            if(rule.getReferenceNumber() != null && !rule.getReferenceNumber().equalsIgnoreCase(((ThingEvent)event).getAsset().getCustomerRefNumber())) {
                return false;
            }

            if (rule.getOrderNumber() != null && !rule.getOrderNumber().equalsIgnoreCase(((ThingEvent)event).getAsset().getOrderNumber())) {
                return false;
            }

            if(rule.getAssignedTo() != null && !rule.getAssignedTo().equals(((ThingEvent) event).getAsset().getAssignedUser())) {
                return false;
            }

            if(rule.getFreeformLocation() != null && !((ThingEvent)event).getAsset().getAdvancedLocation().isBlank() && !((ThingEvent)event).getAsset().getAdvancedLocation().getFreeformLocation().equalsIgnoreCase(rule.getFreeformLocation())) {
                return false;
            }

            if(!rule.getLocation().isBlank() && !((ThingEvent)event).getAsset().getAdvancedLocation().isBlank() && !rule.getLocation().getPredefinedLocation().equals(((ThingEvent) event).getAsset().getAdvancedLocation().getPredefinedLocation())) {
                return false;
            }
        }

        if(rule.getAssignee() != null && !rule.getAssignee().equals(event.getAssignee())) {
            return false;
        }

        if(rule.getEventType() != null && !rule.getEventType().equals(event.getType())) {
            return false;
        }

        if(rule.getEventTypeGroup() != null && !rule.getEventTypeGroup().equals(event.getType().getGroup())) {
            return false;
        }

        //Congratulations, if you made it this far without returning false, then your rule must apply to the given event.
        return true;
    }

    /**
     * This method clears away all Queue Items for a given Event ID.  This is used when an Event is completed or
     * otherwise closed.  It may turn out to be more reliable to perform this action inside of a trigger.
     *
     * @param eventId - A Long representing the ID of an Event which has one or more Queue Items for processing.
     */
    @Transactional
    public void clearEscalationRulesForEvent(Long eventId) {
        Query deleteQuery = getEntityManager().createNativeQuery(CLEAR_RULES_FOR_EVENT_SQL);
        deleteQuery.setParameter("eventId", eventId);

        int queueItemsDeleted = deleteQuery.executeUpdate();

        logger.debug("A total of " + queueItemsDeleted + " Queue Items for Event with ID " + eventId + " were deleted.");
    }

    /**
     * This method clears away all Queue Items for a given Rule ID.  This is to be used only when a Rule is being
     * archived, in order to clean up the table and prevent any rows from failing during execution.
     *
     * @param ruleId - A Long representing the ID of a Rule which may have active Queue Items that need to be cleared.
     */
    private void clearQueueItemsForRule(Long ruleId) {
        Query deleteQuery = getEntityManager().createNativeQuery(CLEAR_QUEUE_ITEMS_FOR_RULE_SQL);
        deleteQuery.setParameter("ruleId", ruleId);

        int queueItemsDeleted = deleteQuery.executeUpdate();

        logger.debug("A total of " + queueItemsDeleted + " Queue Items for Rule with ID " + ruleId + " were deleted.");
    }
}
