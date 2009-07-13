package com.n4systems.fieldid.validators;

import java.io.File;
import java.util.List;

import com.n4systems.fieldid.actions.helpers.UploadFileSupport;
import com.n4systems.model.FileAttachment;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class UploadFileValidator extends FieldValidatorSupport {

	public void validate(Object object) throws ValidationException {
		UploadFileSupport uploadFileAction = (UploadFileSupport)object;
		
		for(FileAttachment uploadedFile: uploadFileAction.getUploadedFiles()) {
			if( uploadedFile != null ) {
				if(!testFileNameAvailability((new File(uploadedFile.getFileName())), uploadFileAction.getAttachments(), uploadFileAction.getUploadedFiles())) {
					addFieldError(getFieldName(), object);
					return;
				}
			}
		}
	}
	
	public boolean testFileNameAvailability(File file, List<FileAttachment> attachments, List<FileAttachment> uploadedFiles) {
		int nameFound = 0;
		for(FileAttachment attachment: attachments) {
			if(attachment.getFileName().equals(file.getName())) {
				nameFound++;
			}
		}
		
		for(FileAttachment attachment: uploadedFiles) {
			if( attachment != null ) {
				if(file.getName().equals(new File(attachment.getFileName()).getName() ) ) {
					nameFound++;
				}
			}
		}
		
		return ( nameFound == 1 );
	}

}
