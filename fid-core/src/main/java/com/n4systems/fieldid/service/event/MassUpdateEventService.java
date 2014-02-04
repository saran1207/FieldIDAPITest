package com.n4systems.fieldid.service.event;

import com.google.common.collect.Sets;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.mail.MailService;
import com.n4systems.model.Event;
import com.n4systems.model.EventAudit;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.user.User;
import com.n4systems.util.mail.MailMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.*;

public class MassUpdateEventService extends FieldIdPersistenceService{

    @Autowired
    private MailService mailService;

    private Logger logger = Logger.getLogger(MassUpdateEventService.class);

    @Transactional
    public Long updateEvents(List<Long> ids, ThingEvent eventChanges, Map<String, Boolean> fieldMap, Long userId) {
        if (ids.isEmpty()) {
            return 0L;
        }

        User user = persistenceService.find(User.class, userId);

        Set<String> updateKeys = getEnabledKeys(fieldMap);

        ThingEvent changeTarget;
        Set<Event> eventsUpdated = Sets.newHashSet();
        EventAudit audit = new EventAudit();
        audit.setModified(new Date());
        audit.setModifiedBy(user);
        audit.setTenant(user.getTenant());

        for (Long id : ids) {
            changeTarget = persistenceService.find(ThingEvent.class, id);
            eventsUpdated.add(changeTarget);

            for (String updateKey : updateKeys) {
                if (updateKey.equals("owner")) {
                    audit.setOwner(eventChanges.getOwner().getDisplayName());
                    changeTarget.setOwner(eventChanges.getOwner());
                }

                if (updateKey.equals("eventBook")) {
                    audit.setEventBook(eventChanges.getBook().getDisplayName());
                    changeTarget.setBook(eventChanges.getBook());
                }

                if (updateKey.equals("location")) {
                    audit.setLocation(eventChanges.getAdvancedLocation().getFullName());
                    changeTarget.setAdvancedLocation(eventChanges.getAdvancedLocation());
                }

                if (updateKey.equals("printable")) {
                    audit.setPrintable(new Boolean(eventChanges.isPrintable()).toString());
                    changeTarget.setPrintable(eventChanges.isPrintable());
                }

                if (updateKey.equals("assetStatus")) {
                    audit.setAssetStatus(eventChanges.getAssetStatus().getDisplayName());
                    changeTarget.setAssetStatus(eventChanges.getAssetStatus());
                }

                if (updateKey.equals("assignedUser")) {
                    audit.setAssignedUser(eventChanges.getAssignedTo().getAssignedUser().getUserID());
                    changeTarget.setAssignedTo(eventChanges.getAssignedTo());
                }

                if (updateKey.equals("performedBy")) {
                    audit.setPerformedBy(eventChanges.getPerformedBy().getUserID());
                    changeTarget.setPerformedBy(eventChanges.getPerformedBy());
                }

                if (updateKey.equals("datePerformed")) {
                    audit.setPerformed(eventChanges.getDate());
                    changeTarget.setDate(eventChanges.getDate());
                }

                if (updateKey.equals("eventResult")) {
                    audit.setResult(eventChanges.getEventResult().getDisplayName());
                    changeTarget.setEventResult(eventChanges.getEventResult());
                }

                if (updateKey.equals("comments")) {
                    audit.setComments(eventChanges.getComments());
                    changeTarget.setComments(eventChanges.getComments());
                }

                if (updateKey.equals("eventStatus")) {
                    audit.setEventStatus(eventChanges.getEventStatus().getDisplayName());
                    changeTarget.setEventStatus(eventChanges.getEventStatus());
                }

                if (updateKey.equals("nextEventDate")) {
                    audit.setNextDate(eventChanges.getDueDate());
                    changeTarget.setDueDate(eventChanges.getDueDate());
                }

                if(updateKey.equals("assignee")) {
                    audit.setAssignee(eventChanges.getAssignee() == null ? null : eventChanges.getAssignee().getUserID());
                    audit.setAssignedGroupId(eventChanges.getAssignedGroup() == null ? null : eventChanges.getAssignedGroup().getId());
                    changeTarget.setAssignedUserOrGroup(eventChanges.getAssignedUserOrGroup());
                }
            }

            persistenceService.update(changeTarget);
        }

        audit.setEvents(eventsUpdated);
        persistenceService.save(audit);

        return new Long(ids.size());
    }

    /** Extracts a set of keys, whose values are True */
    private Set<String> getEnabledKeys(Map<String, Boolean> values) {
        Set<String> keys = new HashSet<String>();
        for (Map.Entry<String, Boolean> entry : values.entrySet()) {
            if (entry.getValue()) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

    public void sendFailureEmailResponse(List<Long> ids, User modifiedBy) {
        String subject="Mass update of events failed";
        String body="Failed to update " + ids.size() + " events";
        sendEmailResponse(subject, body, modifiedBy);
    }

    public void sendSuccessEmailResponse(List<Long> ids, User modifiedBy) {
        String subject="Mass update of events completed";
        String body="Updated " + ids.size() + " events successfully";
        sendEmailResponse(subject, body, modifiedBy);
    }

    private void sendEmailResponse(String subject, String body, User modifiedBy) {
        try {
            mailService.sendMessage(new MailMessage(subject, body, modifiedBy.getEmailAddress()));
        } catch (MessagingException e) {
            logger.error("Unable to send Event Type removal email", e);
        }
    }

}
