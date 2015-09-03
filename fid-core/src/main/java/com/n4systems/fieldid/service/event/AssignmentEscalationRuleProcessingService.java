package com.n4systems.fieldid.service.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.escalationrule.AssignmentEscalationRuleService;
import com.n4systems.fieldid.service.mail.MailService;
import com.n4systems.model.EscalationRuleExecutionQueueItem;
import com.n4systems.model.Event;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.util.mail.TemplateMailMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class is fired off by the AssignmentEscalationRuleTask and is responsible for notifying users referenced in any
 * AssignmentEscalationRules when any associated events exceed their due date by the prescribed time without having
 * been completed.
 *
 * This class will search for such events, send the notification and then flag the related row in the linker entity to
 * confirm that the notification has been sent.  For now, this service piggybacks on the ReportService, utilizing the
 * search mechanics to determine which events require notifications to be sent and recorded.
 *
 * Created by Jordan Heath on 2015-08-19.
 */
public class AssignmentEscalationRuleProcessingService extends FieldIdPersistenceService {
    private static final Logger logger = Logger.getLogger(AssignmentEscalationRuleProcessingService.class);
    private static final String ESCALATION_NOTIFICATION_TEMPLATE = "escalationNotification";

    private static final String JSON_PROCESSING_ERROR = "There was an exception while processing JSON for Rule with " +
                                                        "ID {0}.";

    private static final String MAIL_SERVICE_ERROR = "There was an exception while sending an Escalation Rule " +
                                                    "Notification for Rule with ID {0}.";

    @Autowired
    private AssignmentEscalationRuleService ruleService;

    @Autowired
    private EventService eventService;

    @Autowired
    private MailService mailService;

    /**
     * Calling this method kicks off the processing of all valid rules within FieldID.  Each of these jobs queries the
     * Events table for events which match the criteria set out in the rule.  When executing, rules send out emails,
     * reassign work or notify the existing assignee of their overdue tasks.
     */
    @Transactional
    public void processQueue() {
        //Get a list of ALL expired Queue items.  These have already been sorted by Tenant ID to make things
        //easier and potentially stop us from skipping all over the place with the Security Context.  I just
        //find that silly.
        List<EscalationRuleExecutionQueueItem> queueItems = ruleService.getQueueItemsForProcessing();

        processItems(queueItems);
    }

    /**
     * This is just necessary to avoid hopping around with the security context.  If this isn't actually a concern, we
     * will be using a stream to process this.
     *
     * @param queueItemsForProcessing - A List of EscalationRuleExecutionQueueItems which need to be processed.
     */
    private void processItems(List<EscalationRuleExecutionQueueItem> queueItemsForProcessing) {
        Tenant lastTenant = null;

        for(EscalationRuleExecutionQueueItem item : queueItemsForProcessing) {
            if(lastTenant == null || !lastTenant.equals(item.getRule().getTenant())) {
                lastTenant = item.getRule().getTenant();
                securityContext.reset();
                securityContext.setTenantSecurityFilter(new TenantOnlySecurityFilter(item.getRule().getTenant()));
                //Do we need to set user?
            }

            item = handleReassignmentAndStaleCheck(item);

            try {
                //Now we send the mail... yet another place something could go wrong.  We could bail out form here, as
                //well and - again - will not end up marking that Item as having processed.
                mailService.sendMessage(createMailMessage(item));

                item.setRuleHasRun(true);

                ruleService.updateQueueItem(item);
            } catch (IOException e) {
                logger.error(MessageFormat.format(JSON_PROCESSING_ERROR, item.getRule().getId()), e);
            } catch (MessagingException e) {
                logger.error(MessageFormat.format(MAIL_SERVICE_ERROR, item.getRule().getId()), e);
                e.printStackTrace();
            }
        }
    }

    /**
     * This method handles any reassignment logic from the rule and also performs a stale check.  If the stale check
     * determines that the Event has been updated since we created the Queue Item, we need to read up the Event and
     * regenerate the JSON that we use to generate the email.  This means we didn't save any work on that one.
     *
     * @param item - The Queue Item that you want to handle reassignment and stale check for.
     * @return The EscalationRuleExecutionQueueItem with potentially updated JSON.
     */
    private EscalationRuleExecutionQueueItem handleReassignmentAndStaleCheck(EscalationRuleExecutionQueueItem item) {
        Event event = null;

        if(item.getRule().getReassignUser() != null) {
            event = persistenceService.find(Event.class, item.getEventId());
            event.setAssignee(item.getRule().getReassignUser());
            event = persistenceService.update(event);
        } else if(item.getRule().getReassignToUserGroup() != null) {
            event = persistenceService.find(Event.class, item.getEventId());
            event.setAssignedGroup(item.getRule().getReassignToUserGroup());
            event = persistenceService.update(event);
        }

        //Do this later after we make any other necessary changes to the event that may change the display, such
        //as assignee name.  If we've made those kind of changes, we won't bother with the stale check or reading
        //up the event again (since we'll already have done that).  We'll just recreate the map from the new data.
        if(event != null || !eventService.isEventDataCurrent(item.getEventId(), item.getEventModDate())) {
            if(event == null) {
                event = persistenceService.find(Event.class, item.getEventId());
            }
            item.setMapJson(ruleService.generateEventMapJSON(event));
        }

        return item;
    }

    /**
     * Create a TemplateMailMessage to notify of Escalation of an overdue event.  JSON from the Queue Item is used to
     * fill the email with any necessary data.
     *
     * @param item - An EscalationRuleExecutionQueueItem to be used to populate the Notificaiton email.
     * @return A TemplateMailMessage populated with values from the Queue Item.
     * @throws IOException if the JSON couldn't be properly unmarshalled into a list or if Skynet has gone live.
     */
    @SuppressWarnings("unchecked") //'cuz I know better than Java
    private TemplateMailMessage createMailMessage(EscalationRuleExecutionQueueItem item) throws IOException {
        //We'll need to convert that JSON to a list.  This could potentially cause an IO Exception and we'll
        //have to bail out, never marking that item as processed.
        ObjectMapper unmarshaller = new ObjectMapper();
        Map<String, Object> mailContents =
                unmarshaller.readValue(item.getMapJson(), Map.class);

        mailContents.put("messageBody", item.getRule().getCustomMessageText() == null ? "" : item.getRule().getCustomMessageText());

        TemplateMailMessage msg = new TemplateMailMessage(item.getRule().getSubjectText(),
                                                          ESCALATION_NOTIFICATION_TEMPLATE) {

            /**
             * We override this because we're only interested in planting our own body in here.  No other headers or
             * footers.  Those are for people who wear pants... and pants are for suckers.
             *
             * @return A String representing just the body that we made... no extra junk.
             */
            @Override
            public String getFullBody() {
                return getBody();
            }
        };

        if(item.getRule().getEscalateToUser() != null) {
            msg.getToAddresses().add(item.getRule().getEscalateToUser().getEmailAddress());
        }

        if(item.getRule().getEscalateToUserGroup() != null) {
            msg.getToAddresses().addAll(item.getRule().getEscalateToUserGroup()
                                .getMembers()
                                .stream()
                                .map(User::getEmailAddress)
                                .collect(Collectors.toList()));
        }

        if(item.getRule().getAdditionalEmails() != null && !item.getRule().getAdditionalEmails().isEmpty()) {
            msg.getCcAddresses().addAll(item.getRule().getAdditionalEmailsList());
        }

        if(item.getRule().isNotifyAssignee()) {
            if(mailContents.get("assigneeEmail") != null) {
                msg.getToAddresses().add((String)mailContents.get("assigneeEmail"));
            } else if(mailContents.get("assignedGroupEmails") != null) {
                msg.getToAddresses().addAll((List<String>)mailContents.get("assignedGroupEmails"));
            }
        }

        msg.getTemplateMap().putAll(mailContents);

        return msg;
    }

}
