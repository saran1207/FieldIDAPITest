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

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
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
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public void saveEventAttachment(ApiEventAttachment apiAttachment) {
		QueryBuilder<Event> query = createTenantSecurityBuilder(Event.class);
        query.addWhere(WhereClauseFactory.create("mobileGUID", apiAttachment.getEventSid()));
        Event event = persistenceService.find(query);
        
        if(event != null) {        	
        	// Logic borrowed from ManagerBackedEventSaver.attachUploadedFiles
        	// TODO we should refactor ManagerBackedEventSaver.attachUploadedFiles to expose that logic.
        	FileAttachment attachment = convert(apiAttachment, event.getTenant(), event.getCreatedBy());        	
        	File tmpDirectory = PathHandler.getTempRoot();        	
        	File attachmentDirectory = PathHandler.getAttachmentFile(event);
        	File tmpFile = new File(tmpDirectory, attachment.getFileName());
			try {
				FileUtils.copyFileToDirectory(tmpFile, attachmentDirectory);
			} catch (IOException e) {
				logger.error("Failed Saving Event Attachment. Unable to find Event: " + apiAttachment.getEventSid());
				e.printStackTrace();
				return;
			}

			// clean up the temp file
			tmpFile.delete();

			// now we need to set the correct file name for the
			// attachment and set the modifiedBy
			attachment.setFileName(tmpFile.getName());
			attachment.setTenant(event.getTenant());
			attachment.setModifiedBy(event.getModifiedBy());        	
        	event.getAttachments().add(attachment);
        	persistenceService.save(event);
        	logger.info("Saved Event Attachment for Event: " + apiAttachment.getEventSid());
        } else {
        	logger.error("Failed Saving Event Attachment. Unable to find Event: " + apiAttachment.getEventSid());
        }
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
