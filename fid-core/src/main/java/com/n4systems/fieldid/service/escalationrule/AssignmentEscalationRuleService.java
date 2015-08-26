package com.n4systems.fieldid.service.escalationrule;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import com.n4systems.util.FieldIdDateFormatter;
import com.n4systems.util.mail.EventUrlUtil;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.search.SortDirection;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import javax.persistence.Query;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class provides access to the assignment_escalation_rules table and the associated Events affected by those
 * rules.  This means that methods are included to read the related Events from their respective tables.
 *
 * Created by Jordan Heath on 2015-08-18.
 */
public class AssignmentEscalationRuleService extends FieldIdPersistenceService {
    private static final String RULE_RESET_BY_EVENT_ID_SQL = "UPDATE escalation_rule_execution_queue SET rule_has_run = 0 WHERE event_id = :eventId";
    private static final String CLEAR_RULES_FOR_EVENT_SQL = "DELETE FROM escalation_rule_execution_queue WHERE event_id = :eventId";

    public List<AssignmentEscalationRule> getAllActiveRules() {
        QueryBuilder<AssignmentEscalationRule> builder = createUserSecurityBuilder(AssignmentEscalationRule.class);
        return persistenceService.findAll(builder);
    }

    public boolean isNameUnique(String name) {
        QueryBuilder<AssignmentEscalationRule> builder = createUserSecurityBuilder(AssignmentEscalationRule.class);
        builder.addSimpleWhere("ruleName", name);
        return persistenceService.exists(builder);
    }

    public void archiveRule(AssignmentEscalationRule rule) {
        rule.setState(Archivable.EntityState.ARCHIVED);
        updateRule(rule);
    }

    public void updateRule(AssignmentEscalationRule rule, Long oldDateRange) {
        if(oldDateRange == rule.getOverdueQuantity()) {
            persistenceService.update(rule);
        } else {
            //We need to version it now because the date range has changed
            AssignmentEscalationRule newRule = copyRule(rule);
            saveRule(newRule);

            //We need to archive this one
            rule.setOverdueQuantity(oldDateRange);
            archiveRule(rule);
        }
    }

    public AssignmentEscalationRule copyRule(AssignmentEscalationRule oldRule) {
        AssignmentEscalationRule returnMe = new AssignmentEscalationRule();

        returnMe.setType(oldRule.getType());
        returnMe.setEventTypeGroup(oldRule.getEventTypeGroup());
        returnMe.setEventType(oldRule.getEventType());
        returnMe.setAssetStatus(oldRule.getAssetStatus());
        returnMe.setAssetTypeGroup(oldRule.getAssetTypeGroup());
        returnMe.setAssignedTo(oldRule.getAssignedTo());
        returnMe.setAssignee(oldRule.getAssignee());
        returnMe.setOwner(oldRule.getOwner());
        returnMe.setLocation(oldRule.getLocation());
        returnMe.setRfidNumber(oldRule.getRfidNumber());

        returnMe.setSerialNumber(oldRule.getSerialNumber());

        returnMe.setReferenceNumber(oldRule.getReferenceNumber());
        returnMe.setOrderNumber(oldRule.getOrderNumber());
        returnMe.setPurchaseOrder(oldRule.getPurchaseOrder());

        returnMe.setTenant(getCurrentTenant());
        returnMe.setOwner(getCurrentUser().getOwner());
        returnMe.setCreatedBy(getCurrentUser());

        //Rule info
        returnMe.setRuleName(oldRule.getRuleName());
        returnMe.setOverdueQuantity(oldRule.getOverdueQuantity());
        returnMe.setEscalateToUser(oldRule.getEscalateToUser());
        returnMe.setReassignUser(oldRule.getReassignUser());
        returnMe.setNotifyAssignee(oldRule.getNotifyAssignee());
        returnMe.setAdditionalEmails(oldRule.getAdditionalEmails());
        returnMe.setSubjectText(oldRule.getSubjectText());
        returnMe.setCustomMessageText(oldRule.getCustomMessageText());

        return returnMe;
    }
    private static final Logger logger = Logger.getLogger(AssignmentEscalationRuleService.class);

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
    public void initializeRule(List<Long> eventIdList, AssignmentEscalationRule rule) {
        eventIdList.forEach(eventId -> writeQueueItem(eventId, rule));
    }

    /**
     * This method takes an Event ID and an AssignmentEscalationRule and will generate the appropriate Queue row for the
     * Escalation Rule.  If the event passes its due date without being completed, then these Queue rows will be
     * processed and acted upon.
     *
     * @param eventId - A Long representing the ID of the Event.
     * @param rule - An AssignmentEscalationRule entity, representing the rule which applies to the Event.
     */
    private void writeQueueItem(Long eventId, AssignmentEscalationRule rule) {
        EscalationRuleExecutionQueueItem queueItem = new EscalationRuleExecutionQueueItem();

        Event event = persistenceService.find(Event.class, eventId);

        Long timeChanger = event.getDueDate().getTime();
        timeChanger += rule.getOverdueQuantity();

        queueItem.setNotifyDate(new Date(timeChanger));

        queueItem.setEventId(eventId);
        queueItem.setEventModDate(event.getModified());
        queueItem.setRuleHasRun(false);

        queueItem.setMapJson(generateEventMapJSON(event));

        persistenceService.save(queueItem);
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
        String json = null;

        try {
            json = jsonMaker.writeValueAsString(eventMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
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
        returnMe.put("performEventURL", event.getType().isActionEventType() ? null : EventUrlUtil.createPerformEventUrl(event));
        returnMe.put("eventSummaryURL", EventUrlUtil.createEventSummaryUrl(event));
        returnMe.put("isThingEvent", event.getType().isThingEventType());
        returnMe.put("isPlaceEvent", event.getType().isPlaceEventType());
        returnMe.put("isAuditEvent", event.getType().isProcedureAuditEventType());
        returnMe.put("isAction", event.getType().isActionEventType());
        returnMe.put("assigneeEmail", event.getAssignee() != null ? event.getAssignee().getEmailAddress() : null);
        returnMe.put("assigneeName", event.getAssignee().getDisplayName());
        returnMe.put("assignedGroupEmails", event.getAssignedGroup() != null ? createEmailList(event.getAssignedGroup()) : null);
        returnMe.put("assignedGroupName", event.getAssignedGroup() != null ? event.getAssignedGroup().getDisplayName() : null);
        returnMe.put("eventType", event.getType().getDisplayName());
        returnMe.put("showLinks", !event.getAssignee().isPerson()); //Not sure if this one is entirely necessary, but it might be.




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

    private List<String> createEmailList(UserGroup assignedGroup) {
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
        return new FieldIdDateFormatter(dueDate, event.getAssignee(), false, showTime).format();
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
     * This method accepts the ID of an event and resets all of the Event's rules.  This is achieved through raw SQL to
     * limit joins to other tables and decrease database load.  It is up to you to ensure you provide the right ID.
     *
     * Supplying the wrong ID may reset rules on a different event... or it may not reset anything.  This method will
     * return true if the query ran successfully, which does not necessarily reflect that rules were reset for a given
     * Event.  It only reflects that the query ran successfully, which can also mean that there were no rows in the
     * linker entity for your Event.  As long as your event meets the criteria of the Escalation Rule itself, rows
     * will be created in this table when your Event blows past the related due date... so no rows simply means that
     * the given event has not had any escalation rules act on it yet.  In other words, the person these events are
     * assigned to is DOING THEIR JOB!!
     *
     * You will typically want to call this method when you have edited the due date of the Event in question.  This
     * can impact rule execution in such a way that one or more escalation rules may need to be reset, so they can be
     * executed the next time the Event isn't completed in time.
     *
     * @param eventId - A Long representing the ID of the Event for which you want to reset all rules.
     * @return True if the reset was successful (ie. the Query didn't explode) and False if there was any kind of problem.
     */
    public boolean resetRulesForEvent(Long eventId) {
        try {
            Query resetQuery = getEntityManager().createNativeQuery(RULE_RESET_BY_EVENT_ID_SQL);
            resetQuery.setParameter("eventId", eventId);
            int resetRules = resetQuery.executeUpdate();
            logger.debug("Reset " + resetRules + " Rules for Event with ID " + eventId);
            return true;
        } catch (HibernateException e) {
            logger.error("Couldn't reset Escalation Rules for Event with ID " + eventId, e);
            return false;
        }
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
        QueryBuilder<AssignmentEscalationRule> ruleQuery = createTenantSecurityBuilder(AssignmentEscalationRule.class);

        persistenceService.findAll(ruleQuery)
                          .stream()
                          .filter(rule -> doesRuleApply(rule, event))
                          .forEach(rule -> writeQueueItem(event.getId(), rule));
    }

    /**
     * This ugly bit of logic determines whether a given AssignmentEscalationRule is applicable to a given Event.  This
     * is used to determine which Queue items for rule execution (if any) need to be created for this Event.
     *
     * This method is used during stream processing of a query for all active Rules for a given tenant.
     *
     * @param rule - An AssignmentEscalationRule entity representing an ACTIVE Rule.
     * @param event - An Event entity representing the Event for which we want to determine if the Rule applies.
     * @return A boolean response indicating whether (true) or not (false) the Rule applies to the Event.
     */
    private boolean doesRuleApply(AssignmentEscalationRule rule, Event event) {
        //Make sure we have fail if the Rule is for an Action and we have an Event, or vice versa.

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

            if(rule.getOrderNumber() != null && !rule.getOrderNumber().equalsIgnoreCase(((ThingEvent)event).getAsset().getOrderNumber())) {
                return false;
            }

            if(rule.getAssignedTo() != null && !rule.getAssignedTo().equals(((ThingEvent) event).getAsset().getAssignedUser())) {
                return false;
            }

            if((rule.getFreeformLocation() != null || rule.getLocation() != null) && event.getAdvancedLocation() == null) {
                return false;
            } else {
                if(rule.getFreeformLocation() != null && !rule.getFreeformLocation().equalsIgnoreCase(((ThingEvent) event).getAsset().getAdvancedLocation().getFreeformLocation())) {
                    return false;
                }

                if(rule.getLocation() != null && !rule.getLocation().getPredefinedLocation().equals(((ThingEvent)event).getAsset().getAdvancedLocation().getPredefinedLocation()))
                {
                    return false;
                }
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
    public void clearEscalationRulesForEvent(Long eventId) {
        Query deleteQuery = getEntityManager().createNativeQuery(CLEAR_RULES_FOR_EVENT_SQL);
        deleteQuery.setParameter("eventId", eventId);

        int queueItemsDeleted = deleteQuery.executeUpdate();

        logger.debug("A total of " + queueItemsDeleted + " Queue Items for Event with ID " + eventId);
    }
}
