/**
 * 
 */
package com.n4systems.fieldid.validators;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.utils.StrutsListHelper;
import com.n4systems.model.FileAttachment;

public class AvailableFileNameValidator {
	private final List<FileAttachment> allAttachments;

	public AvailableFileNameValidator(List<FileAttachment> attachments, List<FileAttachment> uploadedFiles) {
		allAttachments = new ArrayList<FileAttachment>();
		allAttachments.addAll(attachments);
		allAttachments.addAll(uploadedFiles);

		StrutsListHelper.clearNulls(allAttachments);
	}

	public boolean isFileNameAvailable(FileAttachment fileAttachment) {
		String file = justFileName(fileAttachment);
		
		for (FileAttachment attachment : allAttachments) {
			if (!fileAttachment.equals(attachment) && file.equals(justFileName(attachment))) {
				return false;
			}
		}

		return true;
	}
	
	private String justFileName(FileAttachment fileAttachment) {
        if(fileAttachment.getFileName() == null){
            return null;
        }
        else {
            return fileAttachment.getFileName().substring(fileAttachment.getFileName().lastIndexOf('/') + 1);
        }
		//return new File(fileAttachment.getFileName()).getName();
	}

	
}