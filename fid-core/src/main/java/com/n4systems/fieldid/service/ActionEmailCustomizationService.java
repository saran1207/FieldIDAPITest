package com.n4systems.fieldid.service;

import com.n4systems.model.Tenant;
import com.n4systems.model.notificationsettings.ActionEmailCustomization;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;

import java.util.List;


/**
 * This Service class handles saving, updating and reading ActionEmailCustomization entities from the
 * action_email_customization table.  Keep in mind that we should only have at most one row per tenant.  Unless I've
 * totally misunderstood the requirements for this.
 *
 * Created by Jordan Heath on 15-03-13.
 */
public class ActionEmailCustomizationService extends FieldIdPersistenceService {
    private static final Logger logger = Logger.getLogger(ActionEmailCustomizationService.class);

    public static final String DEFAULT_EMAIL_SUBJECT = "Work Items Assigned";

    public static final String DEFAULT_SUB_HEADING = "This is an automated message to notify you that the following actions have been assigned to you or a group you are a member of.";

    /**
     * This is a method to allow you to read the ActionEmailCustomization for a specified tenant.  This was
     * built because I was having difficulty with the Email Notification service, given that it runs without a security
     * context.
     *
     * @param tenant - A valid Tenant that exists in the system.  No cheating!
     * @return The ActionEmailCustomization entity for that Tenant.
     */
    public ActionEmailCustomization readForTennant(Tenant tenant) {
        securityContext.setTenantSecurityFilter(new TenantOnlySecurityFilter(tenant));
        ActionEmailCustomization actionEmailCustomization = read();
        securityContext.reset();
        return actionEmailCustomization;
    }

    /**
     * This method retrieves what should be the only ActionEmailCustomization entity for the Tenant.  Just in case
     * it's not the only one, we grab "all" ActionEmailCustomization rows for the tenant and return the "first" row
     * from the collection.  Those two words are in quotes, because they should be read as "only."
     *
     * If no rows exist yet, a fresh ActionEmailCustomization entity will be created and returned.  It will be
     * populated with the default values.
     *
     * @return The first (and likely ONLY) ActionEmailCustomization entity for the current tenant.
     */
    public ActionEmailCustomization read() {
        QueryBuilder<ActionEmailCustomization> query = createTenantSecurityBuilder(ActionEmailCustomization.class);
        //We order by ID here, so that - in the off chance that we somehow end up with two rows - we're going to always
        //deal with the first one.
        query.addSimpleWhere("tenant.id", getCurrentTenant().getId());
        query.addOrder("id", true);

        //Realistically, this should return only one record... but this is a safer bet.  We're not actively doing
        //much to prevent duplicate rows, since we had to include an ID that we're actually not going to care about.
        List<ActionEmailCustomization> results = persistenceService.findAll(query);

        ActionEmailCustomization returnMe;

        if(results == null || results.size() < 1) {
            //If we got an empty or null list, that means there is currently not an ActionEmailCustomization row.
            //The user will only ever need one, so we will create and return it, populated with default values.
            returnMe = new ActionEmailCustomization();
            returnMe.setTenant(getCurrentTenant());
            returnMe.setEmailSubject(DEFAULT_EMAIL_SUBJECT);
            returnMe.setSubHeading(DEFAULT_SUB_HEADING);

            return returnMe;
        } else {
            if(results.size() > 1) {
                logger.warn("ActionEmailCustomization table holds more than one row for tenant with ID " +
                        getCurrentTenant().getId() + " I'll just return the first row from the List");
            }

            return results.get(0);
        }
    }

    /**
     * This method saves new instances of ActionEmailCustomization entities.  It returns the entity after it has been
     * created, with the DB generated ID attached.
     *
     * @param actionEmailCustomization - An initialized ActionEmailCustomization entity that doesn't already exist in the database.
     * @return An ActionEmailCustomization entity representing the one supplied as a parameter, but with an ID.  It now exists.
     */
    public ActionEmailCustomization save(ActionEmailCustomization actionEmailCustomization) {
        actionEmailCustomization = ensureNoBlankValues(actionEmailCustomization);

        //Save the Entity...
        Long id = persistenceService.save(actionEmailCustomization);

        //...then use the provided ID to retrieve and return the entity.
        return persistenceService.find(ActionEmailCustomization.class, id);
    }

    /**
     * This method updates existing instances of ActionEmailCustomization entities.
     *
     * @param actionEmailCustomization - An initialized ActionEmailCustomization entity that already exists in the database.
     * @return The same ActionEmailCustomization entity after being updated into the database.  Just in case you wanted it back.
     */
    public ActionEmailCustomization update(ActionEmailCustomization actionEmailCustomization) {
        actionEmailCustomization = ensureNoBlankValues(actionEmailCustomization);

        //On an update, we get the entity back, so we can just return that directly.
        return persistenceService.update(actionEmailCustomization);
    }

    /**
     * This method allows us to ensure that default values are applied to any empty ActionEmailCustomization entity.
     *
     * @param actionEmailCustomization - An ActionEmailCustomization entity we want to ensure doesn't contain empty values.
     * @return An ActionEmailCustomizationEntity with default values replacing any empty values.
     */
    private ActionEmailCustomization ensureNoBlankValues(ActionEmailCustomization actionEmailCustomization) {
        actionEmailCustomization.setSubHeading(actionEmailCustomization.getSubHeading() == null ||
                                               actionEmailCustomization.getSubHeading().isEmpty() ?
                                               DEFAULT_SUB_HEADING : actionEmailCustomization.getSubHeading());

        actionEmailCustomization.setEmailSubject(actionEmailCustomization.getEmailSubject() == null ||
                                                 actionEmailCustomization.getEmailSubject().isEmpty() ?
                                                 DEFAULT_EMAIL_SUBJECT : actionEmailCustomization.getEmailSubject());

        return actionEmailCustomization;
    }
}
