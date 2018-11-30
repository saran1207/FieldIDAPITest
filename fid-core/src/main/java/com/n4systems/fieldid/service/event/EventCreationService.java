package com.n4systems.fieldid.service.event;

import com.amazonaws.AmazonClientException;
import com.n4systems.ejb.impl.EventResultCalculator;
import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.escalationrule.AssignmentEscalationRuleService;
import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.model.*;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.api.HasOwner;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import com.n4systems.model.notification.AssigneeNotification;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.reporting.PathHandler;
import com.n4systems.services.signature.SignatureService;
import com.n4systems.tools.FileDataContainer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class EventCreationService<T extends Event<?,?,?>, V extends EntityWithTenant & HasOwner> extends FieldIdPersistenceService {

    protected static final Logger logger = Logger.getLogger(EventCreationService.class);

    @Autowired protected AssetService assetService;
    @Autowired protected LastEventDateService lastEventDateService;
    @Autowired protected NextEventScheduleService nextEventScheduleService;
    @Autowired protected S3Service s3Service;
    @Autowired protected TenantSettingsService tenantSettingsService;
    @Autowired protected EventScheduleService eventScheduleService;
    @Autowired protected EventService eventService;
    @Autowired protected SignatureService signatureService;
    @Autowired protected NotifyEventAssigneeService notifyEventAssigneeService;
    @Autowired protected AssignmentEscalationRuleService ruleService;

    @Transactional
    public T createEventWithSchedules(T event, Long scheduleId, FileDataContainer fileData, List<FileAttachment> uploadedFiles, List<EventScheduleBundle<V>> schedules) {
        return createEventWithSchedules(event, scheduleId, fileData, uploadedFiles, schedules, true);
    }

    @Transactional
    public T createEventWithSchedules(T event, Long scheduleId, FileDataContainer fileData, List<FileAttachment> uploadedFiles, List<EventScheduleBundle<V>> schedules, Boolean cleanUpCriteriaImages) {
        T savedEvent = createEvent(event, scheduleId, fileData, uploadedFiles, cleanUpCriteriaImages);
        for (EventScheduleBundle<V> eventScheduleBundle : schedules) {
            T openEvent = createEvent();
            openEvent.setTenant(eventScheduleBundle.getTarget().getTenant());
            setTargetFromScheduleBundle(openEvent, eventScheduleBundle);
            openEvent.setType(eventScheduleBundle.getType());
            openEvent.setOwner(eventScheduleBundle.getTarget().getOwner());
            openEvent.setProject(eventScheduleBundle.getJob());
            openEvent.setDueDate(eventScheduleBundle.getScheduledDate());
            openEvent.setAssignedUserOrGroup(eventScheduleBundle.getAssginee());
            doSaveSchedule(openEvent);
        }
        return savedEvent;
    }

    protected abstract T createEvent();
    protected abstract void setTargetFromScheduleBundle(T event, EventScheduleBundle<V> bundle);
    protected void doSaveSchedule(T openEvent) {}

    @Transactional
    public T createEvent(T event, Long scheduleId, FileDataContainer fileData, List<FileAttachment> uploadedFiles) {
        return createEvent(event, scheduleId, fileData, uploadedFiles, true);
    }


        @Transactional
    public T createEvent(T event, Long scheduleId, FileDataContainer fileData, List<FileAttachment> uploadedFiles, Boolean cleanUpCriteriaImages) {
        defaultOneClickResultsWithNullState(event.getResults());
        if(event.getSubEvents() != null) {
            event.getSubEvents().forEach(subEvent -> defaultOneClickResultsWithNullState(subEvent.getResults()));
        }

        EventResult calculatedEventResult = calculateEventResultAndScore(event);

        if (event.getEventResult() == null || event.getEventResult() == EventResult.VOID) {
            event.setEventResult(calculatedEventResult);
        }

        event.setWorkflowState(WorkflowState.COMPLETED);

        Date completedDate = event.getDate();

        event.setDate(completedDate);

        preSaveEvent(event, fileData);

        if (event.getId() == null) {
            //If the event was unscheduled, then it's going to immediately be written as a COMPLETE, ACTIVE event.
            //When this happens, we're going to bypass the painful logic of an AFTER INSERT trigger and simply dump
            //what we know to be the correct value into the Asset.  I'm under the impression that, from here, it'll
            //actually get saved.
            if(event.getWorkflowState().equals(WorkflowState.COMPLETED)
                    && event.getState().equals(Archivable.EntityState.ACTIVE)
                    && event.getType().isThingEventType()) {
                //If the current Last Event Date on the Asset is NOT AFTER the Completed Date on the Event, set the
                //Asset's Last Event Date to the Event's Completed Date... if there is a Last Event Date at all.
                if(((ThingEvent) event).getAsset().getLastEventDate() == null
                        || !((ThingEvent) event).getAsset().getLastEventDate().after(event.getCompletedDate())) {
                    ((ThingEvent) event).getAsset().setLastEventDate(event.getCompletedDate());
                }

                //I'm sure I'm violating the rules by doing this... but this is WAY easier than a trigger.
                Asset asset = assetService.update(((ThingEvent) event).getAsset());
                ((ThingEvent) event).setAsset(asset);
            }
            persistenceService.save(event);
        } else {
            // Because the update drops the transient data on the signature criteria result, we
            // must remember the file names in a map before we call update. We must call update before saving
            // the file, because we have to get IDs for our signature criteria results so we know the path to save them at.
            // Perhaps it would be better to pass transient signature data in a separate parameter
            Map<Long, String> rememberedSignatureMap = rememberTemporarySignatureFiles(event);
            Map<Long, List<String>> rememberedCriteriaImages = rememberCriteriaImages(event);

            event.setTriggersIntoResultingActions(event);

            for (CriteriaResult result : event.getResults()) {
                for (Event action : result.getActions()) {
                    //Make sure that we clear and create rules for any open Actions.
                    if(action.getWorkflowState().equals(WorkflowState.OPEN)) {
                        ruleService.clearEscalationRulesForEvent(action.getId());
                        ruleService.createApplicableQueueItems(action);
                    }
                }
            }

            restoreTemporarySignatureFiles(event, rememberedSignatureMap);
            restoreCriteriaImages(event, rememberedCriteriaImages);

            event = persistenceService.update(event);
        }

        setAllTriggersForActions(event);

        postSaveEvent(event, fileData);

        // writeSignatureImagesToDisk MUST be called after persistenceManager.save(parameterObject.event, parameterObject.userId) as an
        // event id is required to build the save path
        writeSignatureImagesToDisk(event);
        saveCriteriaResultImages(event, cleanUpCriteriaImages);

        processUploadedFiles(event, uploadedFiles);

        if(getCurrentUser().isUsageBasedUser()) {
            int eventCount = event.getSubEvents().size() + 1;
            tenantSettingsService.decrementUsageBasedEventCount(eventCount);
        }

        return event;
    }

    protected void preSaveEvent(T event, FileDataContainer fileData) {}
    protected void postSaveEvent(T event, FileDataContainer fileData) {}

    private void setAllTriggersForActions(T event) {
        event.setTriggersIntoResultingActions(event);
        for (SubEvent subEvent : event.getSubEvents()) {
            subEvent.setTriggersIntoResultingActions(event);
        }
    }

    private void restoreCriteriaImages(T event, Map<Long, List<String>> rememberedCriteriaImages) {
        for (CriteriaResult criteriaResult : event.getResults()) {
            int index = 0;
            for (CriteriaResultImage criteriaResultImage : criteriaResult.getCriteriaImages()) {
                String tempFileName  = rememberedCriteriaImages.get(criteriaResult.getCriteria().getId()).get(index);
                criteriaResultImage.setTempFileName(tempFileName);
                index++;
            }
        }
    }

    private Map<Long, List<String>> rememberCriteriaImages(T event) {
        Map<Long, List<String>> criteriaImageFiles = new HashMap<Long, List<String>>();
        for (CriteriaResult criteriaResult : event.getResults()) {
            if (!criteriaResult.getCriteriaImages().isEmpty()) {
                criteriaImageFiles.put(criteriaResult.getCriteria().getId(), new ArrayList<String>());
            }
            for (CriteriaResultImage criteriaResultImage : criteriaResult.getCriteriaImages()) {
                criteriaImageFiles.get(criteriaResult.getCriteria().getId()).add(criteriaResultImage.getTempFileName());
            }
        }
        return criteriaImageFiles;
    }

    private void restoreTemporarySignatureFiles(T event, Map<Long, String> rememberedSignatureFiles) {
        for (CriteriaResult criteriaResult : event.getResults()) {
            if (criteriaResult instanceof SignatureCriteriaResult && rememberedSignatureFiles.containsKey(criteriaResult.getCriteria().getId())) {
                ((SignatureCriteriaResult) criteriaResult).setTemporaryFileId(rememberedSignatureFiles.get(criteriaResult.getCriteria().getId()));
            }
        }
    }

    private Map<Long, String> rememberTemporarySignatureFiles(T event) {
        Map<Long,String> rememberedSignatureFiles = new HashMap<Long, String>();
        for (CriteriaResult criteriaResult : event.getResults()) {
            if (criteriaResult instanceof  SignatureCriteriaResult) {
                rememberedSignatureFiles.put(criteriaResult.getCriteria().getId(), ((SignatureCriteriaResult) criteriaResult).getTemporaryFileId());
            }
        }
        return rememberedSignatureFiles;
    }

    private Event processUploadedFiles(T event, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
        attachUploadedFiles(event, null, uploadedFiles);

        for (SubEvent subEvent : event.getSubEvents()) {
            attachUploadedFiles(event, subEvent, null);
        }

        return null;
    }

    private Event attachUploadedFiles(Event event, SubEvent subEvent, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
        AbstractEvent<ThingEventType,Asset> targetEvent;
        if (subEvent == null) {
            targetEvent = event;
        } else {
            targetEvent = subEvent;
        }
        File tmpDirectory = PathHandler.getTempRoot();

        if (uploadedFiles != null) {

            File tmpFile;
            // move and attach each uploaded file
            for (FileAttachment uploadedFile : uploadedFiles) {

                try {

                    if (!uploadedFile.isNew()) {
                        // File attachments are stored transiently, so we need to re find pre-existing ones and
                        // set the comment to the value that was possibly edited in while the event was edited.
                        FileAttachment editedFile = persistenceService.find(FileAttachment.class, uploadedFile.getId());
                        editedFile.setComments(uploadedFile.getComments());
                        targetEvent.getAttachments().add(editedFile);
                        continue;
                    }

                    // attach the attachment
                    targetEvent.getAttachments().add(uploadedFile);

                    // move the file to it's new location, note that it's
                    // location is currently relative to the tmpDirectory
                    tmpFile = new File(tmpDirectory, uploadedFile.getFileName());
                    //FileUtils.copyFileToDirectory(tmpFile, attachmentDirectory);
                    uploadedFile.setTenant(targetEvent.getTenant());
                    uploadedFile.setModifiedBy(targetEvent.getModifiedBy());
                    uploadedFile.ensureMobileIdIsSet();
                    uploadedFile.setFileName(s3Service.getFileAttachmentPath(uploadedFile));
                    s3Service.uploadFileAttachment(tmpFile, uploadedFile);

                    // clean up the temp file
                    tmpFile.delete();
                } catch (AmazonClientException e) {
                    logger.error("failed to copy uploaded file ", e);
                    throw new FileAttachmentException(e);
                }

            }
        }

        return event;
    }

    private EventResult calculateEventResultAndScore(T event) {
        EventResultCalculator resultCalculator = new EventResultCalculator();
        EventResult eventResult = resultCalculator.findEventResult(event);

        return adjustEventResult(event, eventResult, resultCalculator);
    }

    protected EventResult adjustEventResult(T event, EventResult initialResult, EventResultCalculator resultCalculator) {
        return initialResult;
    }

    private void writeSignatureImagesToDisk(T event) {

        writeSignatureImagesFor(signatureService, event.getResults());
        for (SubEvent subEvent : event.getSubEvents()) {
            writeSignatureImagesFor(signatureService, subEvent.getResults());
        }
    }

    private void writeSignatureImagesFor(SignatureService sigService, Set<CriteriaResult> results) {
        for (CriteriaResult result : results) {
            if (result.getCriteria().getCriteriaType() == CriteriaType.SIGNATURE && ((SignatureCriteriaResult)result).hasImageInMemoryOrTemporaryFile()) {
                try {
                    sigService.storeSignatureFileFor((SignatureCriteriaResult)result);
                } catch (IOException e) {
                    throw new FileAttachmentException("Unable to store signature image for result [" + result + "]", e);
                }
            }
        }
    }

    @Transactional
    public T updateEvent(T event, FileDataContainer fileData, List<FileAttachment> attachments) {

        EventResult calculatedEventResult = calculateEventResultAndScore(event);

        if (event.getEventResult() == null || event.getEventResult() == EventResult.VOID) {
            // Mobile sets the result to void to indicate that it should be recalculated.
            event.setEventResult(calculatedEventResult);
        }

        preUpdateEvent(event, fileData);

        writeSignatureImagesToDisk(event);
        saveCriteriaResultImages(event, true);
        setAllTriggersForActions(event);

        event.getAttachments().clear();;

        //Save any new actions that have been added on edit.
        for (CriteriaResult result : event.getResults()) {
            for (Event action : result.getActions()) {
                if(action.isNew()) {
                    Long id = persistenceService.save(action);

                    if(action.getWorkflowState().equals(WorkflowState.OPEN)) {
                        //Once the Action has been successfully saved, we want to create any necessary Queue Items if it
                        //is OPEN.
                        Event newAction = persistenceService.find(Event.class, id);
                        ruleService.createApplicableQueueItems(newAction);
                    }
                } else {
                    //Since Actions may have been changed since they were added, we'll make sure that we update the
                    //queue for them... just to be safe.
                    ruleService.clearEscalationRulesForEvent(action.getId());
                    ruleService.createApplicableQueueItems(action);
                }
            }
        }

        for (SubEvent subEvent: event.getSubEvents()) {
            for (CriteriaResult result: subEvent.getResults()) {
                for (Event action : result.getActions()) {
                    if(action.isNew()) {
                        persistenceService.save(action);
                    }
                }

            }
        }

        addActionNotifications(event);

        event = persistenceService.update(event);

        //Since things have changed in the Event, we may have invalidated one or more rules. Either way, the chances
        //that the JSON stored in the Queue for this event being out of date is pretty high, so we're going to want
        //to update the JSON, anyways.  Best way to do that is to delete everything and start again.  We'll do this
        //after the event saves successfully... otherwise there's not much point in doing this work... it would be for
        //an event not found in the DB.

        //FIXME This is where the problem is and it's ONLY in Java 8u20.
        //When we move away from 8u20, you can remove the updateEvent overrides from the following classes and
        //uncomment the two lines below:
        // - PlaceEventCreationService
        // - ThingEventCreationService
        // - ProcedureAuditEventCreationService

//        ruleService.clearEscalationRulesForEvent(trainingWheels.getId());
//        ruleService.createApplicableQueueItems(trainingWheels);

        postUpdateEvent(event, fileData);
        processUploadedFiles(event, attachments);

        return event;
    }

    protected abstract void preUpdateEvent(T event, FileDataContainer fileData);
    protected abstract void postUpdateEvent(T event, FileDataContainer fileData);

    private void saveCriteriaResultImages(T event, Boolean cleanUpCriteriaImages) {
        saveCriteriaResultImages(event.getResults(), cleanUpCriteriaImages);
        for (SubEvent subEvent: event.getSubEvents()) {
            saveCriteriaResultImages(subEvent.getResults(), cleanUpCriteriaImages);
        }
    }

    private void saveCriteriaResultImages(Collection<CriteriaResult> results, Boolean cleanUpCriteriaImages) {

        for (CriteriaResult result: results) {
            for (CriteriaResultImage criteriaResultImage: result.getCriteriaImages()) {
                if (criteriaResultImage.getTempFileName() != null) {
                    if (cleanUpCriteriaImages) {
                        s3Service.finalizeCriteriaResultImageUpload(criteriaResultImage);
                    } else {
                        s3Service.finalizeMultiCriteriaResultImageUpload(criteriaResultImage);
                    }
                }
            }
        }
    }

    public void cleanUpMultiEventCriteriaImages(Collection<CriteriaResult> results) {
        for (CriteriaResult result: results) {
            for (CriteriaResultImage criteriaResultImage: result.getCriteriaImages()) {
                if (criteriaResultImage.getTempFileName() != null) {
                    s3Service.removeTempAfterMassUpload(criteriaResultImage);
                }
            }
        }
    }

    private void defaultOneClickResultsWithNullState(Collection<CriteriaResult> results) {
        for (CriteriaResult result: results) {
            OneClickCriteriaResult oneClickResult;
            if (result instanceof OneClickCriteriaResult) {
                oneClickResult = (OneClickCriteriaResult)result;
                if (oneClickResult.getButton() == null) {
                    oneClickResult.setButton(((OneClickCriteria) oneClickResult.getCriteria()).getButtonGroup().getAvailableButtons().get(0));
                }
            }
        }
    }

    private void addActionNotifications(T event) {
        for (CriteriaResult result : event.getResults()) {
            for (Event action : result.getActions()) {
                if(action.isSendEmailOnUpdate() && action.getAssigneeOrDateUpdated()) {
                    if(!notifyEventAssigneeService.notificationExists(action)) {
                        AssigneeNotification assigneeNotification = new AssigneeNotification();
                        assigneeNotification.setEvent(action);
                        persistenceService.save(assigneeNotification);
                        action.setAssigneeNotification(assigneeNotification);
                        persistenceService.update(action);
                    }
                }
            }
        }

        for (SubEvent subEvent: event.getSubEvents()) {
            for (CriteriaResult result : subEvent.getResults()) {
                for (Event action : result.getActions()) {
                    if(action.isSendEmailOnUpdate() && action.getAssigneeOrDateUpdated()) {
                        if(!notifyEventAssigneeService.notificationExists(action)) {
                            AssigneeNotification assigneeNotification = new AssigneeNotification();
                            assigneeNotification.setEvent(action);
                            persistenceService.save(assigneeNotification);
                            action.setAssigneeNotification(assigneeNotification);
                            persistenceService.update(action);
                        }
                    }
                }
            }
        }
    }

}
