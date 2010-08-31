package com.n4systems.fieldid.validators;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.actions.shared.UploadFileAction;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class FileValidator extends FieldValidatorSupport {

	private Long fileSize = 102400L; // 100K default.

	public void validate(Object action) throws ValidationException {
		checkForFileSizeOverRide(action);
		testFileSize(action);
	}

	private void testFileSize(Object object) throws ValidationException {
		AbstractAction action = (AbstractAction)object;
		String fieldName = getFieldName();
		File value = (File) this.getFieldValue(fieldName, action);
		
		if (value != null && value.exists() && value.length() > fileSize) {
			addFieldError(fieldName, action);
			return;
		}
	}

	private void checkForFileSizeOverRide(Object action) {
		if (action instanceof UploadFileAction) {
			UploadFileAction uploadFileAction = (UploadFileAction) action;
			
			fileSize = uploadFileAction.fileSizeLimitInKB() * 1024L;
		}
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	
	
	
	public String getHumanReadableFileLimit() {
		String byteCountToDisplaySize = FileUtils.byteCountToDisplaySize(fileSize);
		return byteCountToDisplaySize;
	}
	
}
