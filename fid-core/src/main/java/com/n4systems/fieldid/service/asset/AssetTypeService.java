package com.n4systems.fieldid.service.asset;

import com.amazonaws.AmazonClientException;
import com.google.common.collect.Lists;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ImageAttachmentException;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.service.procedure.ProcedureService;
import com.n4systems.fieldid.service.schedule.RecurringScheduleService;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.model.*;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.reporting.PathHandler;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.ArchiveAssetTypeTask;
import com.n4systems.util.AssetTypeRemovalSummary;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import rfid.ejb.entity.AssetCodeMapping;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import javax.persistence.Query;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class AssetTypeService extends FieldIdPersistenceService {

    private static final Logger logger= Logger.getLogger(AssetTypeService.class);

    @Autowired private AsyncService asyncService;
    @Autowired private RecurringScheduleService recurringScheduleService;
    @Autowired private AssetCodeMappingService assetCodeMappingService;
    @Autowired private AutoAttributeService autoAttributeService;
    @Autowired private ProcedureService procedureService;
    @Autowired private ProcedureDefinitionService procedureDefinitionService;
    @Autowired private S3Service s3Service;

    public AssetType getAssetType(Long id) {
        return persistenceService.find(AssetType.class, id);
    }

    public boolean isNameExists(String name) {
        QueryBuilder<AssetType> builder = createTenantSecurityBuilder(AssetType.class);
        builder.addWhere(WhereClauseFactory.create("name", name));
        return persistenceService.exists(builder);
    }

    public List<AssetType> getAssetTypes() {
        return getAssetTypes(null, null);
    }

    public List<AssetType> getAssetTypes(Long assetTypeGroupId, String name) {
        QueryBuilder<AssetType> queryBuilder = createUserSecurityBuilder(AssetType.class);

        if(assetTypeGroupId != null) {
            if (assetTypeGroupId == -1)
                queryBuilder.addWhere(WhereClauseFactory.createIsNull("group.id"));
            else {
                queryBuilder.addSimpleWhere("group.id", assetTypeGroupId);
            }
        }

        if (name != null) {
            queryBuilder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "name","name", name, WhereParameter.WILDCARD_BOTH, null));
        }

        queryBuilder.addOrder("name");
        return persistenceService.findAll(queryBuilder);
    }

    public List<AssetTypeGroup> getAssetTypeGroupsByOrder() {
        QueryBuilder<AssetTypeGroup> queryBuilder = createUserSecurityBuilder(AssetTypeGroup.class);
        queryBuilder.addOrder("orderIdx");
        return persistenceService.findAll(queryBuilder);
    }

    public List<AssetTypeGroup> getAssetTypeGroupsForProceduresByOrder() {
        QueryBuilder<AssetType> builder = createUserSecurityBuilder(AssetType.class);

        builder.setSimpleSelect("group");
        builder.addSimpleWhere("hasProcedures", true);
        builder.addOrder("name");
        List<AssetType> listWithProcedures = persistenceService.findAll(builder);

        List<AssetTypeGroup> groupList = (List<AssetTypeGroup>) (List<?>) listWithProcedures;

        return groupList;
    }

    public List<AssetType> getAssetTypesFilteredForProcedures(Long assetTypeGroupId) {
        QueryBuilder<AssetType> builder = createUserSecurityBuilder(AssetType.class);

        builder.addSimpleWhere("hasProcedures", true);

        if(assetTypeGroupId != null) {
            if (assetTypeGroupId == -1)
                builder.addWhere(WhereClauseFactory.createIsNull("group.id"));
            else {
                builder.addSimpleWhere("group.id", assetTypeGroupId);
            }
        }

        builder.addOrder("name");
        return persistenceService.findAll(builder);
    }

    public List<AssetType> getAssetTypes(Long assetTypeGroupId) {
		QueryBuilder<AssetType> builder = createUserSecurityBuilder(AssetType.class);

		if(assetTypeGroupId != null) {
			if (assetTypeGroupId == -1)
				builder.addWhere(WhereClauseFactory.createIsNull("group.id"));
			else {
				builder.addSimpleWhere("group.id", assetTypeGroupId);
			}
		}

		builder.addOrder("name");
		return persistenceService.findAll(builder);
    }

    // NOTE : typically this call is done synchronously and optionally, scheduleInitialEvents is called after. (typically asynchronously because it can be slow).
    public void addRecurringEvent(AssetType assetType, final RecurringAssetTypeEvent recurringEvent) {
        recurringEvent.setTenant(assetType.getTenant());
        persistenceService.save(recurringEvent.getRecurrence());
        persistenceService.save(recurringEvent);
        // NOTE that two things happens here.  a RecurringAssetTypeEvent is saved AND some events are initially created.
        // the first part is quick but the second part is done asynchronously because it can be slow.
        AsyncService.AsyncTask<Void> task = asyncService.createTask(new Callable<Void>() {
            @Override public Void call() throws Exception {
                scheduleInitialEvents(recurringEvent);
                return null;
            }
        });
        asyncService.run(task);
        assetType.touch();
        update(assetType);
    }

    public void purgeRecurringEvent(RecurringAssetTypeEvent recurringEvent) {
        removeScheduledEvents(recurringEvent);
        deleteRecurringEvent(recurringEvent);
    }

    /**
     * @see #addRecurringEvent(com.n4systems.model.AssetType, com.n4systems.model.RecurringAssetTypeEvent)
     * @param recurringEvent
     */
    private void scheduleInitialEvents(RecurringAssetTypeEvent recurringEvent) {
        for (LocalDateTime dateTime : recurringScheduleService.getBoundedScheduledTimesIterator(recurringEvent.getRecurrence())) {
            recurringScheduleService.scheduleAnEventFor(recurringEvent, dateTime);
        }
    }

    private void removeScheduledEvents(RecurringAssetTypeEvent recurringEvent) {
        QueryBuilder<ThingEvent> builder = new QueryBuilder<ThingEvent>(ThingEvent.class, new TenantOnlySecurityFilter(recurringEvent.getAssetType().getTenant().getId()));

        builder.addSimpleWhere("workflowState", WorkflowState.OPEN);
        builder.addSimpleWhere("recurringEvent", recurringEvent);

        List<ThingEvent> events = persistenceService.findAll(builder);
        for (ThingEvent event:events) {
            logger.debug("removing scheduled event for asset " + event.getAsset().getIdentifier() + " on " + event.getDueDate());
            persistenceService.delete(event);
        }
    }

    public List<RecurringAssetTypeEvent> getRecurringEvents(AssetType assetType) {
        QueryBuilder<RecurringAssetTypeEvent> query = new QueryBuilder<RecurringAssetTypeEvent>(RecurringAssetTypeEvent.class, new TenantOnlySecurityFilter(assetType.getTenant().getId()));
        query.addSimpleWhere("assetType", assetType);
        return persistenceService.findAll(query);
    }

    public void deleteRecurringEvent(AssetType assetType, EventType eventType) {
        QueryBuilder<RecurringAssetTypeEvent> query = new QueryBuilder<RecurringAssetTypeEvent>(RecurringAssetTypeEvent.class, new OpenSecurityFilter());
        query.addSimpleWhere("assetType", assetType);
        query.addSimpleWhere("eventType", eventType);
        List<RecurringAssetTypeEvent> recurringEvents = persistenceService.findAll(query);
        for (RecurringAssetTypeEvent recurringEvent:recurringEvents) {
            deleteRecurringEvent(recurringEvent);
        }
    }

    private void deleteRecurringEvent(RecurringAssetTypeEvent recurringEvent) {
        recurringEvent.archiveEntity();
        persistenceService.update(recurringEvent);
    }

    public List<AssetType> getLotoDevices() {
        QueryBuilder<AssetType> query = createTenantSecurityBuilder(AssetType.class);
        query.addSimpleWhere("group.lotoDevice", true);
        return persistenceService.findAll(query);
    }

    public List<AssetType> getLotoLocks() {
        QueryBuilder<AssetType> query = createTenantSecurityBuilder(AssetType.class);
        query.addSimpleWhere("group.lotoLock", true);
        return persistenceService.findAll(query);
    }

    @Cacheable("assetTypes")
    public List<String> getInfoFieldBeans(Tenant tenant) {
        QueryBuilder<String> query = new QueryBuilder(InfoFieldBean.class, new TenantOnlySecurityFilter(tenant.getId()));

        NewObjectSelect nameSelect = new NewObjectSelect(String.class);
        nameSelect.setConstructorArgs(Lists.newArrayList("LOWER(name)"));
        nameSelect.setDistinct(true);
        query.setSelectArgument(nameSelect);

        return persistenceService.findAll(query);
    }

    public List<UnitOfMeasure> getAllUnitOfMeasures() {
        QueryBuilder<UnitOfMeasure> query = createTenantSecurityBuilder(UnitOfMeasure.class);
        return persistenceService.findAll(query);
    }

    public UnitOfMeasure getUnitOfMeasures(Long id) {
        return persistenceService.findNonSecure(UnitOfMeasure.class, id);
    }

    public AssetType update(AssetType assetType) {
        return persistenceService.update(assetType);
    }

    public AssetType saveAssetType(AssetType assetType, List<FileAttachment> uploadedFiles, byte[] imageData ) throws FileAttachmentException, ImageAttachmentException {
        AssetType oldPI = null;
        if( assetType.getId() != null ) {
            oldPI = persistenceService.find( AssetType.class, assetType.getId() );
        }
        if( oldPI != null ) {
            cleanInfoFields(assetType, oldPI );
        }

        assetType.touch();
        assetType = (AssetType) persistenceService.saveOrUpdate(assetType);
        processUploadedFiles(assetType, uploadedFiles );
        processAssetImage(assetType, imageData );
        return assetType;
    }

    private void processAssetImage( AssetType assetType, byte[] imageData ) throws ImageAttachmentException{
        if( imageData != null ) {
            try {
                s3Service.uploadAssetTypeProfileImageData(imageData, assetType);
            } catch (Exception e) {
                throw new ImageAttachmentException( e );
            }
        }
    }

    private void processUploadedFiles( AssetType assetType, List<FileAttachment> uploadedFiles ) throws FileAttachmentException {
        File tmpDirectory = PathHandler.getTempRoot();

        if( uploadedFiles != null ) {
            File tmpFile;
            // move and attach each uploaded file
            for(FileAttachment uploadedFile: uploadedFiles) {

                if(uploadedFile.isNew()) {
                    try {
                        // move the file to it's new location, note that it's location is currently relative to the tmpDirectory
                        tmpFile = new File(tmpDirectory, uploadedFile.getFileName());
                        //FileUtils.copyFileToDirectory(tmpFile, attachmentDirectory);
                        uploadedFile.setTenant(assetType.getTenant());
                        uploadedFile.setModifiedBy(assetType.getModifiedBy());
                        uploadedFile.ensureMobileIdIsSet();
                        uploadedFile.setFileName(s3Service.getFileAttachmentPath(uploadedFile));
                        s3Service.uploadFileAttachment(tmpFile, uploadedFile);

                        // clean up the temp file
                        tmpFile.delete();

                        // attach the attachment
                        assetType.getAttachments().add(uploadedFile);
                    } catch (AmazonClientException  e) {
                        logger.error("failed to copy uploaded file ", e);
                        throw new FileAttachmentException(e);
                    }
                }
            }

            //remove deleted attachments
            List<FileAttachment> removedAttachments = Lists.newArrayList();

            for (FileAttachment attachment: assetType.getAttachments()) {
                if(!uploadedFiles.contains(attachment)) {
                    removedAttachments.add(attachment);
                }
            }
            assetType.getAttachments().removeAll(removedAttachments);

            persistenceService.update(assetType);
        }

    }

    private void cleanInfoFields( AssetType assetType, AssetType oldPI ) {
        assetCodeMappingService.clearRetiredInfoFields(assetType);
        autoAttributeService.clearRetiredInfoFields(assetType);

        // the removal of old infofields needs to be done after the clearing of retired fields otherwise
        // they will get a persit with deleted entity exception.
        for( InfoFieldBean field : oldPI.getInfoFields() ) {
            if (!assetType.getInfoFields().contains( field )) {
                for (InfoOptionBean infoOpiton : field.getUnfilteredInfoOptions() ) {
                    persistenceService.remove(infoOpiton);
                }
                persistenceService.remove(field);
            }
        }
    }

    public UnitOfMeasure getUnitOfMeasure(Long id) {
        return persistenceService.findById(UnitOfMeasure.class, id);
    }

    public AssetTypeRemovalSummary testArchive(AssetType assetType) {
        AssetTypeRemovalSummary summary = new AssetTypeRemovalSummary(assetType);
        try {
            QueryBuilder<Asset> assetCount = createTenantSecurityBuilder(Asset.class);
            assetCount.setCountSelect().addSimpleWhere("type", assetType).addSimpleWhere("state", Archivable.EntityState.ACTIVE);
            summary.setAssetsToDelete(persistenceService.count(assetCount));

            QueryBuilder<Event> eventCount = createTenantSecurityBuilder(Event.class);
            eventCount.setCountSelect().addSimpleWhere("asset.type", assetType).addSimpleWhere("state", Archivable.EntityState.ACTIVE);
            summary.setEventsToDelete(persistenceService.count(eventCount));

            QueryBuilder<Event> scheduleCount = createTenantSecurityBuilder(Event.class);
            scheduleCount.setCountSelect().addSimpleWhere("asset.type", assetType).addSimpleWhere("workflowState", WorkflowState.OPEN);
            summary.setSchedulesToDelete(persistenceService.count(scheduleCount));

            String subEventQuery = "select count(event) From " + Event.class.getName() + " event, IN( event.subEvents ) subEvent WHERE subEvent.asset.type = :assetType AND event.state = :activeState ";
            Query subEventCount = persistenceService.createQuery(subEventQuery);
            subEventCount.setParameter("assetType", assetType).setParameter("activeState", Archivable.EntityState.ACTIVE);
            summary.setAssetsUsedInMasterEvent((Long) subEventCount.getSingleResult());

            String subAssetQuery = "select count(DISTINCT s.masterAsset) From "+SubAsset.class.getName()+" s WHERE s.asset.type = :assetType ";
            Query subAssetCount = persistenceService.createQuery(subAssetQuery);
            subAssetCount.setParameter("assetType", assetType);
            summary.setSubAssetsToDetach((Long) subAssetCount.getSingleResult());

            String subMasterAssetQuery = "select count(s) From "+SubAsset.class.getName()+" s WHERE s.masterAsset.type = :assetType ";
            Query subMasterAssetCount = persistenceService.createQuery(subMasterAssetQuery);
            subMasterAssetCount.setParameter("assetType", assetType);
            summary.setMasterAssetsToDetach((Long) subMasterAssetCount.getSingleResult());

            String partOfProjectQuery = "select count(p) From Project p, IN( p.assets ) s WHERE s.type = :assetType";
            Query partOfProjectCount = persistenceService.createQuery(partOfProjectQuery);
            partOfProjectCount.setParameter("assetType", assetType);
            summary.setAssetsToDetachFromProjects((Long) partOfProjectCount.getSingleResult());

            String subAssetTypeQuery = "select count(a) From "+AssetType.class.getName()+" a, IN( a.subTypes ) s WHERE s = :assetType ";
            Query subAssetTypeCount = persistenceService.createQuery(subAssetTypeQuery);
            subAssetTypeCount.setParameter("assetType", assetType);
            summary.setAssetTypesToDetachFrom((Long) subAssetTypeCount.getSingleResult());

            QueryBuilder<AssetCodeMapping> assetCodeMappingCount = createTenantSecurityBuilder(AssetCodeMapping.class);
            assetCodeMappingCount.setCountSelect().addSimpleWhere("assetInfo", assetType);
            summary.setAssetCodeMappingsToDelete(persistenceService.count(assetCodeMappingCount));

            summary.setProceduresToDelete(procedureService.getAllProceduresForAssetTypeCount(assetType));

            summary.setProcedureDefinitionsToDelete(procedureDefinitionService.getAllProcedureDefinitionsForAssetTypeCount(assetType));



        } catch (InvalidQueryException e) {
            logger.error("bad summary query", e);
            summary = null;
        }
        return summary;
    }

    public AssetType archive(AssetType assetType, Long archivedBy, String deletingPrefix) {
        if (testArchive(assetType).validToDelete()) {

            assetType.archiveEntity();
            assetType.archivedName(deletingPrefix);

            assetType.getSubTypes().clear();
            AssetType type = persistenceService.update(assetType);

            ArchiveAssetTypeTask archiveTask = new ArchiveAssetTypeTask();

            archiveTask.setArchivedById(archivedBy);
            archiveTask.setAssetTypeId(assetType.getId());
            archiveTask.setAssetTypeName(assetType.getArchivedName());

            // WEB-2844  : what happens if this task fails?  we have deleted the asset type but not the assets?
            // why can't we do everything in an atomic task?   DD
            TaskExecutor.getInstance().execute(archiveTask);

            return type;
        } else {
            throw new RuntimeException("asset type can not be validated.");
        }

    }
}
