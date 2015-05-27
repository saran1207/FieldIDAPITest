package com.n4systems.fieldid.service.procedure;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.download.SystemUrlUtil;
import com.n4systems.fieldid.service.mail.MailService;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.model.Asset;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.procedure.Procedure;
import com.n4systems.model.procedure.ProcedureNotification;
import com.n4systems.model.security.OpenSecurityFilter;
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

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This Service sends out notifications to the Assignees and Assigned Groups from Procedure Definitions where the
 * Assigning user has requested that a notification be sent.
 *
 * Created by Jordan Heath on 15-04-28.
 */
public class NotifyProcedureAssigneeService extends FieldIdPersistenceService {

    private static final Logger logger = Logger.getLogger(NotifyProcedureAssigneeService.class);

    private static final String ASSET_SUMMARY_URL_FRAGMENT = "/fieldid/w/assetSummary?4&uniqueID=";

    private static final String PROCEDURE_NOTIFICATION_TEMPLATE = "assignedProcedureNotification";

    @Autowired private MailService mailService;
    @Autowired private UserGroupService userGroupService;
    //TODO You'll eventually want to link to some kind of email customization service as well...


    /**
     * Call this method to trigger the sending of Procedure Assignee notifications.
     */
    @Transactional
    public void sendNotifications() {
        processGroupAssignees();
        processUserAssignees();

        removeNotificationsWithoutAssignees();
    }

    /**
     * A convenience method to check whether or not a Procedure has an existing Notification in queue to be sent.
     *
     * @param procedure - The Procedure you want to check for existing notifications.
     * @return A boolean result, true if there is an existing notification, false if there is not.
     */
    public boolean notificationExists(Procedure procedure) {
        QueryBuilder<ProcedureNotification> query = createTenantSecurityBuilder(ProcedureNotification.class);
        query.addSimpleWhere("procedure.id", procedure.getId());
        return persistenceService.exists(query);
    }

    /**
     * This method processes all UserGroup assignees and sends out notifications to everyone in each group that has
     * been assigned to a Procedure where the users are to be notified of the schedule.
     */
    private void processGroupAssignees() {
        QueryBuilder<UserGroup> groupQuery = new QueryBuilder<>(ProcedureNotification.class, new OpenSecurityFilter());
        groupQuery.setSimpleSelect("procedure.assignedGroup");
        groupQuery.addWhere(WhereParameter.Comparator.NOTNULL, "procedure.assignedGroup", "procedure.assignedGroup", "");
        groupQuery.addSimpleWhere("procedure.tenant.disabled", false);

        persistenceService.findAll(groupQuery).stream().distinct().forEach(this::notifyGroup);
    }

    /**
     * This method processes all User assignees and sends out notifications to them indicating that they have been
     * assigned a LOTO Procedure.
     */
    private void processUserAssignees() {
        QueryBuilder<ProcedureNotification> userQuery = new QueryBuilder<>(ProcedureNotification.class, new OpenSecurityFilter());
        userQuery.addWhere(WhereParameter.Comparator.NOTNULL, "procedure.assignee", "procedure.assignee", "");
        userQuery.addSimpleWhere("procedure.tenant.disabled", false);

        //We'll use the results of this query twice....
        List<ProcedureNotification> notifications = persistenceService.findAll(userQuery);

        //First, we'll get a map of Procedure Lists against users...
        Map<User, List<Procedure>> userProcedureMap =
                notifications.stream()
                             .map(ProcedureNotification::getProcedure)
                             .collect(Collectors.groupingBy(Procedure::getAssignee, Collectors.toList()));

        //...then we use the same list to figure out who all the assigned users are so that we can notify them of their
        //assignments.  We use the user to pull the correct List from the map.  This could probably be done with the
        //keyset from the map, but I'm more confident that this will work.
        notifications.stream()
                     .map(notification -> notification.getProcedure().getAssignee())
                     //This is very important.  The list of users we're using MUST be distinct!!  Otherwise we send
                     //duplicate notifications.
                     .distinct()
                     .forEach(assignee -> {
                         //First we notify the user...
                         notifyAssignee(assignee, userProcedureMap.get(assignee));
                         //...then we remove notifications for them.
                         removeNotificationsForUser(assignee);
                     });
    }

    /**
     * This method notifies all members of a group of any assigned Procedures where notifications have been requested.
     *
     * The procedure_notifications table is queried for all Notifications to UserGroups, the notifications are sent
     * and then - to finish things up - all of the notifications are purged from the table.
     *
     * @param userGroup - A UserGroup for which assignment notifications should be sent out.
     */
    private void notifyGroup(UserGroup userGroup) {
        QueryBuilder<Procedure> userQuery = new QueryBuilder<>(ProcedureNotification.class, new OpenSecurityFilter());
        userQuery.setSimpleSelect("procedure");
        userQuery.addSimpleWhere("procedure.assignedGroup", userGroup);
        List<Procedure> procedures = persistenceService.findAll(userQuery);

        //To make this work, we still need the procedures assigned to the group... it'll be the same across all users
        //in the group as far as assignments for that group go... right????
        userGroupService.getUsersInGroup(userGroup).forEach(user -> notifyAssignee(user, procedures));

        removeNotificationsForGroup(userGroup);
    }

    /**
     * This method notifies a User assignee that they have work to be completed. This method is used by both Group
     * and User notification processes.  This is where the two streams converge again.
     *
     * @param user - An existing User to which notifications are to be sent.
     * @param procedures - A List populated with Procedures representing all scheduled procedures the User is to be notified of.
     */
    private void notifyAssignee(User user, List<Procedure> procedures) {
        try {
            if(user.getOwner().getPrimaryOrg().getExtendedFeatures().contains(ExtendedFeature.EmailAlerts)) {
                TemplateMailMessage message = createNotificationMessage(user, procedures);

                message.getToAddresses().add(user.getEmailAddress());
                message.getTemplateMap().put("assignee", user);

                mailService.sendMessage(message);
            }
        } catch (MessagingException e) {
            logger.error("Could not notify Procedure assignee", e);
        }

    }

    /**
     * This method creates the Notification email to be sent to the User.  It uses the list of assigned Procedures to
     * populate a Freemarker HTML frame and returns a TemplateMailMessage which is populated and ready to roll.
     *
     * @param user - An existing User to which the notifications are to be sent.
     * @param procedures - A List of Procedures to notify the user of their assignment to.
     * @return A TemplateMailMessage object populated with the necessary values for the Freemarker template.
     */
    private TemplateMailMessage createNotificationMessage(User user, List<Procedure> procedures) {
        //TODO Make some kind of Email Customization thing for this, too!!

        String subject = procedures.size() + " Assigned LOTO Procedures";
        String subHeading = "This is an automated email to inform you that you have been assigned a Schedule LOTO Procedure to complete on the associated equipment.";

        TemplateMailMessage message = new TemplateMailMessage(subject, PROCEDURE_NOTIFICATION_TEMPLATE) {
            @Override
            public String getFullBody() {
                return getBody();
            }
        };

        Map<Long, String> assetUrlMap = createAssetUrlMap(procedures);
        Map<Long, String> dueDateStringMap = createDueDateMap(procedures, user);

        message.getTemplateMap().put("dueDateStringMap", dueDateStringMap);
        message.getTemplateMap().put("assetUrlMap", assetUrlMap);
        message.getTemplateMap().put("procedures", procedures);
        message.getTemplateMap().put("userEmail", user.getEmailAddress());
        message.getTemplateMap().put("subject", subject);
        message.getTemplateMap().put("subHeadingMessage", subHeading);
        message.getTemplateMap().put("systemUrl", SystemUrlUtil.getSystemUrl(user.getTenant()));
        message.getTemplateMap().put("showLinks", !user.isPerson());

        return message;
    }

    /**
     * This method processes a List of Procedures to create a Map of Due Dates, indexed by Procedure ID that are
     * formatted to the provided User's desired timezone settings.
     *
     * @param procedures - A List of Procedures for which Due Dates need to be formatted.
     * @param user - A User whose preferences for Time Zone and Date Format must be applied.
     * @return A Map populated with String representations of Due Dates indexed by Procedure ID.
     */
    private Map<Long, String> createDueDateMap(List<Procedure> procedures, User user) {
        //...just trust me, this formats the dates from the entire collection and drops them into a map indexed by
        //Procedure ID... it takes more lines to describe it than to write it!!!
        return procedures.stream().collect(Collectors.toMap(Procedure::getId, procedure -> new FieldIdDateFormatter(procedure.getDueDate(), user, false, !new PlainDate(procedure.getDueDate()).equals(procedure.getDueDate())).format()));
    }

    /**
     * This method creates a map of the String representations of URLs pointing to the Summary Page for Assets
     * associated with the provided List of Procedures.  These URLs are indexed by Asset ID to reduce duplicates.
     *
     * @param procedures - A List of Procedure entities representing real Procedures from the database.
     * @return A Map of Strings representing the URL of Assets,
     */
    private Map<Long, String> createAssetUrlMap(List<Procedure> procedures) {
        return procedures.stream().map(Procedure::getAsset).distinct().collect(Collectors.toMap(Asset::getId, asset -> SystemUrlUtil.getSystemUrl(asset.getTenant()) + ASSET_SUMMARY_URL_FRAGMENT + asset.getId()));
    }

    /**
     * This method removes all notifications associated with the provided Procedure.
     *
     * @param procedure - A Procedure for which all notifications must be removed.
     */
    public void removeNotificationForProcedure(Procedure procedure) {
        QueryBuilder<ProcedureNotification> removeQuery = new QueryBuilder<>(ProcedureNotification.class, new OpenSecurityFilter());
        removeQuery.addSimpleWhere("procedure", procedure);
        removeNotifications(removeQuery);
    }

    /**
     * This method removes all Notifications that somehow didn't have assignees.  This can happen from time to time.
     * These notifications were technically never valid or - if they were at one time - their associated User has since
     * flown the coop.
     */
    private void removeNotificationsWithoutAssignees() {
        QueryBuilder<ProcedureNotification> removeQuery = new QueryBuilder<>(ProcedureNotification.class, new OpenSecurityFilter());
        removeQuery.addSimpleWhere("event.assignee", null);
        removeQuery.addSimpleWhere("event.assignedGroup", null);
        removeNotifications(removeQuery);
    }

    /**
     * This method removes all notifications associated with the provided UserGroup.
     *
     * @param userGroup - A UserGroup for which all notifications must be removed.
     */
    private void removeNotificationsForGroup(UserGroup userGroup) {
        QueryBuilder<ProcedureNotification> removeQuery = new QueryBuilder<>(ProcedureNotification.class, new OpenSecurityFilter());
        removeQuery.addSimpleWhere("procedure.assignedGroup", userGroup);
        removeNotifications(removeQuery);
    }

    /**
     * This method removes all notifications associated with the provided User.
     *
     * @param user - A User for which all notifications must be removed.
     */
    private void removeNotificationsForUser(User user) {
        QueryBuilder<ProcedureNotification> removeQuery = new QueryBuilder<>(ProcedureNotification.class, new OpenSecurityFilter());
        removeQuery.addSimpleWhere("procedure.assignee", user);
        removeNotifications(removeQuery);
    }

    /**
     * This internal convenience method takes a provided QueryBuilder and uses it to remove the desired notifications
     * from the procedure_notifications table.
     *
     * @param removeQuery - A QueryBuilder which is used to removed undesired Notifications.
     */
    private void removeNotifications(QueryBuilder<ProcedureNotification> removeQuery) {
        //Wipe out all evidence of these notifications.  They've been sent or never should have existed.
        persistenceService.findAll(removeQuery).forEach(notification -> {
            notification.getProcedure().setNotification(null);
            persistenceService.update(notification.getProcedure());
            persistenceService.remove(notification);
        });
    }
}
