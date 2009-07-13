package com.n4systems.fieldid.validators;

import com.n4systems.fieldid.actions.helpers.UploadAttachmentSupport;
import com.n4systems.model.product.ProductAttachment;
import com.opensymphony.xwork2.validator.ValidationException;

public class UploadAttachmentSecurityValidator extends FileSecurityValidator {

	public void validate(Object object) throws ValidationException {
		UploadAttachmentSupport uploadFileAction = (UploadAttachmentSupport)object;
		 
		for(ProductAttachment uploadedFile: uploadFileAction.getUploadedFiles()) {
			if(uploadedFile != null) {
				if( !fileNamePasses( uploadedFile.getNote().getFileName(), object ) ) {
					return;
				}
			}
		}
	}
}
