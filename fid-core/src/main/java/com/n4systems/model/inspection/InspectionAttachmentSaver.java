package com.n4systems.model.inspection;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.persistence.EntityManager;

import org.apache.commons.io.IOUtils;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.model.AbstractInspection;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.SubInspection;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.reporting.PathHandler;

public class InspectionAttachmentSaver extends Saver<FileAttachment> {

	private Inspection inspection;
	private SubInspection subInspection;
	private byte[] data;
	
	public InspectionAttachmentSaver() {}
	
	@Override
	protected void save(EntityManager em, FileAttachment attachment) {
		writeFileToDisk(attachment);
		
		super.save(em, attachment);
	
		AbstractInspection targetInspection = (subInspection == null) ? inspection : subInspection;
		targetInspection.getAttachments().add(attachment);
		
		em.merge(targetInspection);
	}
	
	private void writeFileToDisk(FileAttachment attachment) {
		File path = resolveAttachmentPath(attachment);
		
		// need to ensure the parent directory exists
		if (!path.getParentFile().exists()) {
			path.getParentFile().mkdirs();
		}
		
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(path));
			out.write(data);
		} catch(IOException e) {
			throw new FileAttachmentException("Failed to write attachment data [" + path + "]", e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}
	
	private File resolveAttachmentPath(FileAttachment attachment) {
		File attachmentFile;
		if (subInspection == null) {
			attachmentFile = PathHandler.getInspectionAttachmentFile(inspection, attachment);
		} else {
			attachmentFile = PathHandler.getInspectionAttachmentFile(inspection, subInspection, attachment);
		}
		return attachmentFile;
	}

	@Override
	protected FileAttachment update(EntityManager em, FileAttachment entity) {
		throw new NotImplementedException();
	}
	
	@Override
	protected void remove(EntityManager em, FileAttachment entity) {
		throw new NotImplementedException();
	}

	public void setInspection(Inspection inspection) {
		this.inspection = inspection;
	}

	public void setSubInspection(SubInspection subInspection) {
		this.subInspection = subInspection;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
}
