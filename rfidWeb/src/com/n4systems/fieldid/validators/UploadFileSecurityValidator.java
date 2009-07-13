package com.n4systems.fieldid.validators;

import com.n4systems.fieldid.actions.helpers.UploadFileSupport;
import com.n4systems.model.FileAttachment;
import com.opensymphony.xwork2.validator.ValidationException;

/**
 * The following validator checks to make sure the Canonical path is equal to the absolute path.
 * This ensures that the uploaded file could not be a path like "tmpdir/../../../etc/passwd" as 
 * the canonical path of this would be "/etc/passwd" (depending on where the application is rooted).
 * This validation DOES NOT ensure that the file is being requested is within the applications 
 * defined upload directory, only that the path does not use any relative tricks.
 */
public class UploadFileSecurityValidator extends FileSecurityValidator {
	
	
	
	public void validate(Object object) throws ValidationException {
		UploadFileSupport uploadFileAction = (UploadFileSupport)object;
		 
		for(FileAttachment uploadedFile: uploadFileAction.getUploadedFiles()) {
			if(uploadedFile != null) {
				if( !fileNamePasses( uploadedFile.getFileName(), object ) ) {
					return;
				}
			}
		}
	}

}
