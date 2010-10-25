/**
 * 
 */
package com.n4systems.handlers.creator;

import java.util.List;

import com.n4systems.ejb.impl.CreateInspectionParameter;
import com.n4systems.ejb.impl.InspectionSaver;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.SubInspection;

public class NullInspectionSaver implements InspectionSaver {
	@Override
	public Inspection createInspection(CreateInspectionParameter parameterObject) throws ProcessingProofTestException, FileAttachmentException, UnknownSubAsset {
		return parameterObject.inspection;
	}

	public Inspection attachFilesToSubInspection(Inspection inspection, SubInspection subInspection, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
		return null;
	}
}