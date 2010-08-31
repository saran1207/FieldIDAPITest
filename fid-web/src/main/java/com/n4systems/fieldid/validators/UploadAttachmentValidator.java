package com.n4systems.fieldid.validators;

import java.io.File;
import java.util.List;

import com.n4systems.fieldid.actions.helpers.UploadAttachmentSupport;
import com.n4systems.model.product.ProductAttachment;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class UploadAttachmentValidator extends FieldValidatorSupport {

	public void validate(Object object) throws ValidationException {
		UploadAttachmentSupport uploadAttachmentAction = (UploadAttachmentSupport) object;

		for (ProductAttachment uploadedFile : uploadAttachmentAction.getUploadedFiles()) {
			if (uploadedFile != null) {
				if (!testFileNameAvailability((new File(uploadedFile.getNote().getFileName())), uploadAttachmentAction.getAttachments(), uploadAttachmentAction.getUploadedFiles())) {
					addFieldError(getFieldName(), object);
					return;
				}
			}
		}
	}

	public boolean testFileNameAvailability(File file, List<ProductAttachment> attachments, List<ProductAttachment> uploadedFiles) {
		int nameFound = 0;
		for (ProductAttachment attachment : attachments) {
			if (attachment.getNote().getFileName().equals(file.getName())) {
				nameFound++;
			}
		}

		for (ProductAttachment attachment : uploadedFiles) {
			if (attachment != null) {
				if (file.getName().equals(new File(attachment.getNote().getFileName()).getName())) {
					nameFound++;
				}
			}
		}

		return (nameFound == 1);
	}

}
