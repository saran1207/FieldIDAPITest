package com.n4systems.fieldid.service.escalationrule;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.AssignmentEscalationRule;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.EventSearchType;

/**
 * This class provides access to the assignment_escalation_rules table and the associated Events affected by those
 * rules.  This means that methods are included to read the related Events from their respective tables.
 *
 * Created by Jordan Heath on 2015-08-18.
 */
public class AssignmentEscalationRuleService extends FieldIdPersistenceService {

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
}
