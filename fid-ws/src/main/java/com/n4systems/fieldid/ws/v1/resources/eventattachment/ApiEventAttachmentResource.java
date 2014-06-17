package com.n4systems.fieldid.ws.v1.resources.eventattachment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.amazonaws.AmazonClientException;
import com.n4systems.fieldid.service.amazon.S3Service;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Tenant;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

@Component
@Path("eventAttachment")
public class ApiEventAttachmentResource extends FieldIdPersistenceService {
	private static Logger logger = Logger.getLogger(ApiEventAttachmentResource.class);

    @Autowired
    protected S3Service s3Service;

    @PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void saveEventAttachment(ApiEventAttachment apiAttachment) throws IOException {
		QueryBuilder<Event> query = createTenantSecurityBuilder(Event.class, true);
        query.addWhere(WhereClauseFactory.create("mobileGUID", apiAttachment.getEventSid()));
        Event event = persistenceService.find(query);
        
        if(event != null) {        	
        	// Logic borrowed from ManagerBackedEventSaver.attachUploadedFiles
        	// TODO we should refactor ManagerBackedEventSaver.attachUploadedFiles to expose that logic.
        	FileAttachment attachment = convert(apiAttachment, event.getTenant(), event.getCreatedBy());        	
        	File tmpDirectory = PathHandler.getTempRoot();        	
        	File tmpFile = new File(tmpDirectory, attachment.getFileName());

			try {
				//FileUtils.copyFileToDirectory(tmpFile, attachmentDirectory);
                attachment.setTenant(event.getTenant());
                attachment.setModifiedBy(event.getModifiedBy());
                attachment.ensureMobileIdIsSet();
                attachment.setFileName(s3Service.getFileAttachmentPath(attachment));
                s3Service.uploadFileAttachment(tmpFile, attachment);

                // clean up the temp file
                tmpFile.delete();

                event.getAttachments().add(attachment);
                persistenceService.save(event);

			} catch (AmazonClientException e) {
				logger.error("Failed Saving Event Attachment for Event: " + apiAttachment.getEventSid());
				e.printStackTrace();
				throw e;
			}
        	logger.info("Saved Event Attachment for Event: " + apiAttachment.getEventSid());
        } else {
        	logger.error("Failed Saving Event Attachment. Unable to find Event: " + apiAttachment.getEventSid());
        	throw new NotFoundException("Event", apiAttachment.getEventSid());
        }
	}

	@PUT
	@Path("multi")
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void multiAddEventAttachment(ApiMultiEventAttachment multiEventAttachment) throws IOException {
		ApiEventAttachment apiEventAttachment = multiEventAttachment.getEventAttachmentTemplate();
		for(String eventId : multiEventAttachment.getEventIds()) {
			apiEventAttachment.setEventSid(eventId);
			saveEventAttachment(apiEventAttachment);
		}
		logger.info("Saved Multi Event Attachment for Events: " + multiEventAttachment.getEventIds().size());
	}
	
	public List<FileAttachment> convert(List<ApiEventAttachment> apiAttachments, Tenant tenant, User user) {
		List<FileAttachment> attachments = new ArrayList<FileAttachment>();
		if(apiAttachments != null && apiAttachments.size() > 0) {
			for(ApiEventAttachment apiAttachment : apiAttachments) {
				FileAttachment attachment = convert(apiAttachment, tenant, user);
				attachments.add(attachment);
			}
		}
		
		logger.info("Got " + attachments.size() + " event images");
		
		return attachments;
	}
	
	// Logic borrowed from ServiceDTOBeanConverterImpl.
	private FileAttachment convert(ApiEventAttachment apiAttachment, Tenant tenant, User user) {
		try {			
			File tempImageFile = PathHandler.getTempFile(apiAttachment.getFileName());
			tempImageFile.getParentFile().mkdirs();
			
			FileOutputStream fileOut = new FileOutputStream(tempImageFile);
			fileOut.write(apiAttachment.getImage());
			
			// Must get the full path name and then remove the temporary root
			// path to conform to how the processor accepts it
			String fileName = tempImageFile.getPath();
			fileName = fileName.substring(fileName.indexOf(PathHandler.getTempRoot().getPath()) + PathHandler.getTempRoot().getPath().length());

			FileAttachment attachment = new FileAttachment();
			
			attachment.setFileName(fileName);
			attachment.setComments(apiAttachment.getComments());
			attachment.setTenant(tenant);
			attachment.setModifiedBy(user);
			
			return attachment;		
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
