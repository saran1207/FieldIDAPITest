package com.n4systems.fieldid.ws.v1.resources.eventattachment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.n4systems.model.FileAttachment;
import com.n4systems.model.Tenant;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;

public class ApiEventAttachmentResource {
	private static Logger logger = Logger.getLogger(ApiEventAttachmentResource.class);
	
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
