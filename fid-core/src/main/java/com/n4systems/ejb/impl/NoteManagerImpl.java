package com.n4systems.ejb.impl;

import com.amazonaws.AmazonClientException;
import com.n4systems.ejb.NoteManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Project;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.ServiceLocator;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import java.io.File;


public class NoteManagerImpl implements NoteManager {

	private static final Logger logger = Logger.getLogger(NoteManagerImpl.class);
	
	private PersistenceManager persistenceManager;

	public NoteManagerImpl(EntityManager em) {
		this.persistenceManager = new PersistenceManagerImpl(em);
	}

	public FileAttachment attachNote(FileAttachment note, Project project, Long modifiedBy) throws FileAttachmentException {

		try {
			note.setTenant(project.getTenant());
			persistenceManager.save(note, modifiedBy);

			//File attachmentDirectory = PathHandler.getAttachmentFile(project, note);
			File tmpDirectory = PathHandler.getTempRoot();
			File tmpFile = null;

			if (note.getFileName() != null) {
				// move the file to it's new location, note that it's
				// location is currently relative to the tmpDirectory
				tmpFile = new File(tmpDirectory, note.getFileName());
				//FileUtils.copyFileToDirectory(tmpFile, attachmentDirectory);
                note.ensureMobileIdIsSet();
                S3Service s3Service = ServiceLocator.getS3Service();
                note.setFileName(s3Service.getFileAttachmentPath(note));
                s3Service.uploadFileAttachment(tmpFile, note);

				// clean up the temp file
				tmpFile.delete();
			}

			persistenceManager.update(note, modifiedBy);

			project.getNotes().add(0, note);
			persistenceManager.update(project, modifiedBy);

		} catch (AmazonClientException e) {
			logger.error("failed to copy uploaded file ", e);
			throw new FileAttachmentException(e);
		}
		return note;

	}

	public int detachNote(FileAttachment note, Project project, Long modifiedBy) throws FileAttachmentException {

		note = persistenceManager.find(FileAttachment.class, note.getId());
		try {
			if (note != null && project.getNotes().remove(note) ) {
				project = persistenceManager.update(project, modifiedBy);
				persistenceManager.delete(note);
			} else {
				return project.getNotes().size();
			}
		} catch (Exception e) {
			logger.error("could not remove a note", e);
			throw new FileAttachmentException(e);
		}

		try {
			File attachmentDirectory = PathHandler.getAttachmentFile(project, note);

			if (attachmentDirectory.exists() && note.getFileName() != null) {
				File attachedFile = new File(attachmentDirectory, note.getFileName());
				if (attachedFile.exists()) {
					attachedFile.delete();
				}
			}
		} catch (Exception e) {
			logger.warn("could not delete the attached file correctly", e);
		}

		return project.getNotes().size();
	}
}
