package com.n4systems.fieldid.validators;

import com.n4systems.fieldid.actions.helpers.UploadFileSupport;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class UploadFileLimitValidator extends FieldValidatorSupport {
	
	public void validate(Object object) throws ValidationException {
		UploadFileSupport uploadFileAction = (UploadFileSupport)object;

		// remove nulls from the uploaded file list
		while(uploadFileAction.getUploadedFiles().remove(null)) {}
		while(uploadFileAction.getAttachments().remove(null)) {}
		
		if((uploadFileAction.getUploadedFiles().size() + uploadFileAction.getAttachments().size()) > uploadFileAction.getFileUploadMax()) {
			addFieldError(getFieldName(), object);
		}
		
	}
}
