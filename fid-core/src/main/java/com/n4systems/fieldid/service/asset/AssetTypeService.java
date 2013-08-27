package com.n4systems.fieldid.service.asset;

import com.google.common.collect.Lists;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ImageAttachmentException;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.schedule.RecurringScheduleService;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.model.*;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class AssetTypeService extends FieldIdPersistenceService {

    private static final Logger logger= Logger.getLogger(AssetTypeService.class);

    private @Autowired AsyncService asyncService;
    private @Autowired RecurringScheduleService recurringScheduleService;
    private @Autowired AssetCodeMappingService assetCodeMappingService;
    private @Autowired AutoAttributeService autoAttributeService;

    public AssetType getAssetType(Long id) {
        return persistenceService.find(AssetType.class, id);
    }

    public boolean isNameExists(String name) {
        QueryBuilder<AssetType> builder = createTenantSecurityBuilder(AssetType.class);
        builder.addWhere(WhereClauseFactory.create("name", name));
        return persistenceService.exists(builder);
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
        QueryBuilder<Event> builder = new QueryBuilder<Event>(Event.class, new TenantOnlySecurityFilter(recurringEvent.getAssetType().getTenant().getId()));

        builder.addSimpleWhere("workflowState", WorkflowState.OPEN);
        builder.addSimpleWhere("recurringEvent", recurringEvent);

        List<Event> events = persistenceService.findAll(builder);
        for (Event event:events) {
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
        File imageDirectory = PathHandler.getAssetTypeImageFile(assetType);
        // clear the old file if we have a new one uploaded or the image has been removed.
        if( assetType.getImageName() == null || imageData != null ) {
            if( imageDirectory.exists() && imageDirectory.isDirectory() ) {
                try {
                    FileUtils.cleanDirectory(imageDirectory);
                } catch (Exception e) {
                    throw new ImageAttachmentException( e );
                }
            }
        }

        if( imageData != null ) {
            try {
                File imageFile = new File( imageDirectory, assetType.getImageName() );
                FileUtils.writeByteArrayToFile( imageFile, imageData );
            } catch (Exception e) {
                throw new ImageAttachmentException( e );
            }
        }
    }

    private void processUploadedFiles( AssetType assetType, List<FileAttachment> uploadedFiles ) throws FileAttachmentException {
        File attachmentDirectory = PathHandler.getAssetTypeAttachmentFile(assetType);
        File tmpDirectory = PathHandler.getTempRoot();

        if( uploadedFiles != null ) {
            File tmpFile;
            // move and attach each uploaded file
            for(FileAttachment uploadedFile: uploadedFiles) {

                if(uploadedFile.isNew()) {
                    try {
                        // move the file to it's new location, note that it's location is currently relative to the tmpDirectory
                        tmpFile = new File(tmpDirectory, uploadedFile.getFileName());
                        FileUtils.copyFileToDirectory(tmpFile, attachmentDirectory);

                        // clean up the temp file
                        tmpFile.delete();

                        // now we need to set the correct file name for the attachment and set the modifiedBy
                        uploadedFile.setFileName(tmpFile.getName());
                        uploadedFile.setTenant(assetType.getTenant());
                        uploadedFile.setModifiedBy(assetType.getModifiedBy());

                        // attach the attachment
                        assetType.getAttachments().add(uploadedFile);
                    } catch (IOException e) {
                        logger.error("failed to copy uploaded file ", e);
                        throw new FileAttachmentException(e);
                    }
                }
            }

            persistenceService.update(assetType);
        }

        // Now we need to cleanup any files that are no longer attached to the assettype
        if(attachmentDirectory.exists()) {

			/*
			 * We'll start by constructing a list of attached file names which will be used in
			 * a directory filter
			 */
            final List<String> attachedFiles = new ArrayList<String>();
            for(FileAttachment file: assetType.getAttachments()) {
                attachedFiles.add(file.getFileName());
            }

			/*
			 * This lists all files in the attachment directory
			 */
            for(File detachedFile: attachmentDirectory.listFiles(
                    new FilenameFilter() {
                        public boolean accept(File dir, String name) {
                            // accept only files that are not in our attachedFiles list
                            return !attachedFiles.contains(name);
                        }
                    }
            )) {
				/*
				 * any file returned from our fileNotAttachedFilter, is not in our attached file list
				 * and should be removed
				 */
                detachedFile.delete();
            }
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
}