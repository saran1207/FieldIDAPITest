package com.n4systems.fieldid.service.remover;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.service.search.SavedSearchRemoveFilter;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.handlers.remover.summary.EventTypeArchiveSummary;
import com.n4systems.handlers.remover.summary.SavedReportDeleteSummary;
import com.n4systems.model.*;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.mail.MailMessage;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Callable;

public class EventTypeRemovalService extends FieldIdPersistenceService {

private static final Logger logger = Logger.getLogger(EventTypeRemovalService.class);

    @Autowired private AsyncService asyncService;
    @Autowired private AssociatedEventTypeRemovalService associatedEventTypeRemovalService;
    @Autowired private SavedReportService savedReportService;
    @Autowired private AllEventsOfTypeRemovalService allEventsOfTypeRemovalService;
    @Autowired private CatalogItemRemovalService catalogItemRemovalService;
    @Autowired private NotificationSettingRemovalService notificationSettingRemovalService;
    @Autowired private PlaceEventsOfTypeRemovalService placeEventsOfTypeRemovalService;
    @Autowired private AssociatedPlaceEventTypeRemovalService associatedPlaceEventTypeRemovalService;
    @Autowired private ProcedureAuditEventTypeRemovalService procedureAuditEventTypeRemovalService;

    @Transactional
	public void remove(EventType eventType) {
        if(eventType.isThingEventType()) {
            breakConnectionsToAssetType(eventType);
            archiveEventsOfType(eventType);
            removeEventTypeFromCatalog(eventType);
            deleteNotificationSettingsUsing(eventType);
            deleteSavedReportsWithEventTypeCriteria(eventType);
        } else if (eventType.isActionEventType()) {
            archiveEventsOfType(eventType);
            archivePlaceEventsOfType(eventType);
            deleteSavedReportsWithEventTypeCriteria(eventType);
            removeEventTypeFromCatalog(eventType);
        } else if (eventType.isPlaceEventType()) {
            breakConnectionsToPlace(eventType);
            archivePlaceEventsOfType(eventType);
            removeRecurringPlaceEvents(eventType);
        } else if (eventType.isProcedureAuditEventType()) {
            removeProcedureAuditEvents(eventType);
            removeRecurringProcedureAudits(eventType);
        }
        archiveEventType(eventType);
	}

    private void removeProcedureAuditEvents(EventType eventType) {
        procedureAuditEventTypeRemovalService.remove((ProcedureAuditEventType) eventType);
    }

    private void removeRecurringProcedureAudits(EventType eventType) {
        procedureAuditEventTypeRemovalService.removeRecurrence((ProcedureAuditEventType) eventType);
    }

    private void removeRecurringPlaceEvents(EventType eventType) {
        associatedPlaceEventTypeRemovalService.removeRecurringEvents(eventType);
    }

    private void breakConnectionsToPlace(EventType eventType) {
        associatedPlaceEventTypeRemovalService.removeOrgConnections(eventType);
    }

    private void archivePlaceEventsOfType(EventType eventType) {
         placeEventsOfTypeRemovalService.remove(eventType);
    }

    private void deleteSavedReportsWithEventTypeCriteria(final EventType eventType) {
        savedReportService.deleteAllSavedSearchesMatching(new SavedSearchRemoveFilter() {
            @Override
            public boolean removeThisSearch(SearchCriteria searchCriteria) {
                if (searchCriteria instanceof EventReportCriteria) {
                    EventReportCriteria searchCriteria1 = (EventReportCriteria) searchCriteria;
                    return searchCriteria1.getEventType() != null && ((EventReportCriteria) searchCriteria).getEventType().getId().equals(eventType.getId());
                }
                return false;
            }
        });
	}

	private void breakConnectionsToAssetType(EventType eventType) {
		associatedEventTypeRemovalService.remove(eventType);
	}

	private void archiveEventsOfType(EventType eventType) {
		allEventsOfTypeRemovalService.remove(eventType);
	}

	private void removeEventTypeFromCatalog(EventType eventType) {
        catalogItemRemovalService.cleanUp(null, eventType);
	}

	private void deleteNotificationSettingsUsing(EventType eventType) {
		notificationSettingRemovalService.cleanUp(eventType);
	}

	private void archiveEventType(EventType eventType) {
		eventType.archiveEntity();
		persistenceService.update(eventType);
	}

    @Transactional
	public EventTypeArchiveSummary summary(ThingEventType eventType) {
		EventTypeArchiveSummary summary = new EventTypeArchiveSummary(eventType);

		summary.setAssociatedEventTypeDeleteSummary(associatedEventTypeRemovalService.summary(eventType));
		summary.setEventArchiveSummary(allEventsOfTypeRemovalService.summary(eventType));
		summary.setNotificationSettingDeleteSummary(notificationSettingRemovalService.summary(eventType));
		summary.setSavedReportDeleteSummary(countSavedReportsToBeDeleted(eventType));

		return summary;
	}

    @Transactional
    public EventTypeArchiveSummary summary(ActionEventType eventType) {
        EventTypeArchiveSummary summary = new EventTypeArchiveSummary(eventType);

        summary.setEventArchiveSummary(allEventsOfTypeRemovalService.summary(eventType));
        summary.setSavedReportDeleteSummary(countSavedReportsToBeDeleted(eventType));

        return summary;
    }

    @Transactional
    public EventTypeArchiveSummary summary(PlaceEventType eventType) {
        EventTypeArchiveSummary summary = new EventTypeArchiveSummary(eventType);

        summary.setEventArchiveSummary(placeEventsOfTypeRemovalService.summary(eventType));
        summary.setAssociatedPlaceEventTypeSummary(associatedPlaceEventTypeRemovalService.summary(eventType));

        return summary;
    }

    public EventTypeArchiveSummary summary(ProcedureAuditEventType eventType) {
        EventTypeArchiveSummary summary = new EventTypeArchiveSummary(eventType);

        summary.setEventArchiveSummary(procedureAuditEventTypeRemovalService.summary(eventType));
        summary.setRecurringProcedureAuditEvents(procedureAuditEventTypeRemovalService.countRecurringEvents(eventType));

        return summary;
    }

    private SavedReportDeleteSummary countSavedReportsToBeDeleted(EventType eventType) {
        QueryBuilder<EventReportCriteria> query = createTenantSecurityBuilder(EventReportCriteria.class);
        query.addSimpleWhere("eventType", eventType);

        SavedReportDeleteSummary summary = new SavedReportDeleteSummary();
        summary.setSavedReportsToRemove(persistenceService.count(query));

        return summary;
    }

    public void startRemovalTask(final EventType eventType) {
        final AsyncService.AsyncTask<Void> task = asyncService.createTask(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                removeAsync(eventType);
                return null;
            }
        });
        asyncService.run(task);
    }

    @Transactional
	private void removeAsync(EventType eventType) throws Exception {
		try {
			remove(eventType);
			sendSuccessEmailResponse(eventType);
		} catch (Exception e) {
			logFailureAndSendFailureEmail(e, eventType);
            throw new RuntimeException(e);
		}
	}

	private void logFailureAndSendFailureEmail(Exception e, EventType eventType) {
		logger.error("failed to archive event type", e);

		sendFailureEmailResponse(eventType);

		revertEventTypeToNonArchivedState(eventType);
	}

	private void revertEventTypeToNonArchivedState(EventType eventType) {
		eventType.activateEntity();
        persistenceService.update(eventType);
	}

	private void sendFailureEmailResponse(EventType eventType) {
		String subject = "Event Type Removal Failed To Complete";
		String body = "<h2>Event Type Removed - " + eventType.getDisplayName() + "</h2>" +
				"The event type and all other associated elements have been restored.  Please contact support for more information.";

		logger.info("Sending logFailureAndSendFailureEmail email [" + getCurrentUser().getEmailAddress() + "]");
		try {
	        ServiceLocator.getMailManager().sendMessage(new MailMessage(subject, body, getCurrentUser().getEmailAddress()));
        } catch (Exception e) {
	        logger.error("Unable to send Event Type removal email", e);
        }
	}

	private void sendSuccessEmailResponse(EventType eventType) {
		String subject = "Event Type Removal Completed";
		String body = "<h2>Event Type Removed - " +  eventType.getDisplayName() + "</h2>";

		logger.info("Sending email [" + getCurrentUser().getEmailAddress() + "]");
		try {
	        ServiceLocator.getMailManager().sendMessage(new MailMessage(subject, body, getCurrentUser().getEmailAddress()));
        } catch (Exception e) {
	        logger.error("Unable to send Event Type removal email", e);
        }
	}
}
