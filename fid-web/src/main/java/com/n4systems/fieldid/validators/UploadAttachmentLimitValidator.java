package com.n4systems.fieldid.validators;

import com.n4systems.fieldid.actions.helpers.UploadAttachmentSupport;
import com.n4systems.fieldid.utils.StrutsListHelper;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class UploadAttachmentLimitValidator extends FieldValidatorSupport {

	public void validate(Object object) throws ValidationException {
		UploadAttachmentSupport uploadFileAction = (UploadAttachmentSupport)object;

		// remove nulls from the uploaded file list
		StrutsListHelper.clearNulls(uploadFileAction.getUploadedFiles());
		StrutsListHelper.clearNulls(uploadFileAction.getAttachments());
		
		
		if((uploadFileAction.getUploadedFiles().size() + uploadFileAction.getAttachments().size()) > uploadFileAction.getFileUploadMax()) {
			addFieldError(getFieldName(), object);
		}
	}
}
