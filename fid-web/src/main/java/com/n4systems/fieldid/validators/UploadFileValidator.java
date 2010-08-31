package com.n4systems.fieldid.validators;

import com.n4systems.fieldid.actions.helpers.UploadFileSupport;
import com.n4systems.model.FileAttachment;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class UploadFileValidator extends FieldValidatorSupport {

	public void validate(Object object) throws ValidationException {
		UploadFileSupport uploadFileAction = (UploadFileSupport)object;
		AvailableFileNameValidator availableFileNameValidator = new AvailableFileNameValidator(uploadFileAction.getAttachments(), uploadFileAction.getUploadedFiles());
		
		for(FileAttachment uploadedFile: uploadFileAction.getUploadedFiles()) {
			if( uploadedFile != null ) {
				if(!availableFileNameValidator.isFileNameAvailable(uploadedFile)) {
					addFieldError(getFieldName(), object);
					return;
				}
			}
		}
	}
}
