package com.n4systems.fieldid.service.escalationrule;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.AssignmentEscalationRule;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.EventSearchType;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import javax.persistence.Query;
import java.util.List;

/**
 * This class provides access to the assignment_escalation_rules table and the associated Events affected by those
 * rules.  This means that methods are included to read the related Events from their respective tables.
 *
 * Created by Jordan Heath on 2015-08-18.
 */
public class AssignmentEscalationRuleService extends FieldIdPersistenceService {
    private static final String RULE_RESET_BY_EVENT_ID = "UPDATE escalation_rule_event_linker SET rule_has_run = 0 WHERE event_id = :eventId";

    private static final Logger logger = Logger.getLogger(AssignmentEscalationRuleService.class);



    /**
     * This method simply grabs the AssignmentEscalationRule object by its provided id.
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

        returnMe.setType(criteria.getEventSearchType().equals(EventSearchType.ACTIONS)? AssignmentEscalationRule.Type.ACTION : AssignmentEscalationRule.Type.EVENT );
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

        return returnMe;
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

        returnMe.setEventSearchType(rule.getType().equals(AssignmentEscalationRule.Type.ACTION) ? EventSearchType.ACTIONS : EventSearchType.EVENTS);
        returnMe.setEventTypeGroup(rule.getEventTypeGroup());
        returnMe.setEventType(rule.getEventType());
        returnMe.setAssetStatus(rule.getAssetStatus());
        returnMe.setAssetTypeGroup(rule.getAssetTypeGroup());
        returnMe.setAssignedTo(rule.getAssignedTo());
        returnMe.setAssignee(rule.getAssignee());
        returnMe.setOwner(rule.getOwner());
        returnMe.setLocation(rule.getLocation());
        returnMe.setRfidNumber(rule.getRfidNumber());
        returnMe.setIdentifier(rule.getSerialNumber());
        returnMe.setReferenceNumber(rule.getReferenceNumber());
        returnMe.setOrderNumber(rule.getOrderNumber());
        returnMe.setPurchaseOrder(rule.getPurchaseOrder());

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
     * @param id - A Long representing the ID of the Event for which you want to reset all rules.
     * @return True if the reset was successful (ie. the Query didn't explode) and False if there was any kind of problem.
     */
    public boolean resetRulesForEvent(Long id) {
        try {
            Query resetQuery = getEntityManager().createNativeQuery(RULE_RESET_BY_EVENT_ID);
            resetQuery.setParameter("eventId", id);
            int resetRules = resetQuery.executeUpdate();
            logger.debug("Reset " + resetRules + " Rules for Event with ID " + id);
            return true;
        } catch (HibernateException e) {
            logger.error("Couldn't reset Escalation Rules for Event with ID " + id, e);
            return false;
        }
    }

    /**
     * This method provides a quick and dirty way to grab every single active rule in the DB.  In order for a rule to
     * be truly considered active, it must pass two tests: 1) The Tenant must NOT be disabled.   2) The RULE must not be
     * ARCHIVED.  If both of those tests pass, you'll see the rule in this resulting list.
     *
     * @return A List populated with all ACTIVE AssignmentEscalationRules from all ACTIVE Tenants.
     */
    public List<AssignmentEscalationRule> getAllActiveRules() {
        //The Open Security Filter should allow us to break out of being restricted by tenant, but should still hide
        //rows in the ARCHIVED state.
        QueryBuilder<AssignmentEscalationRule> query = new QueryBuilder<>(AssignmentEscalationRule.class, new OpenSecurityFilter());

        //Don't grab rows from disabled tenants.  These rules are also effectively disabled.
        query.addSimpleWhere("tenant.disabled", false);

        //Ordering by tenants may not be entirely necessary, but it will allow us to limit how much we're monkeying with
        //the SecurityFilter... in theory, anyways.
        query.addOrder("tenant.id", true);

        return persistenceService.findAll(query);
    }
}
