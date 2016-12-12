package com.n4systems.fieldid.actions.downloaders;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProjectManager;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Project;
import com.n4systems.reporting.PathHandler;
import org.apache.log4j.Logger;
import rfid.web.helper.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DownloadProjectNoteFile extends DownloadAction {

	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(DownloadProjectNoteFile.class);

	private Project project;
	private ProjectManager projectManager;
	
	public DownloadProjectNoteFile(ProjectManager projectManager, PersistenceManager persistenceManager ) {
		super(persistenceManager);
		this.projectManager = projectManager;
	}

	public String doDownload() {
		
		if( project == null ) {
			addActionError(getText("error.noproject"));
			logger.error("Project is null");
			return MISSING;
		} 
		
		FileAttachment attachment = null;
		
		// make sure our attachment is actually attached to this assettype
		for(FileAttachment attach: getProjectNotes()) {
			if(attach.getId().equals(attachmentID)) {
				attachment = attach;
				break;
			}
		}
		
		// we did not find the attachment
		if(attachment == null || attachment.getFileName() == null ) {
			addActionError(getText("error.noprojectnoteattachedfile", fileName));
			logger.error("Attachment is null for Project: " + project.getId());
			return MISSING;
		}

        File attachedFile;
        if(attachment.isRemote()){
            attachedFile = s3Service.downloadFileAttachment(attachment);
            if(attachedFile == null) {
                addActionError(getText("error.noprojectnoteattachedfile", fileName));
				logger.error("Attachment missing on S3 for Project: " + project.getId());
                return MISSING;
            }
        }
        else {
            // construct a file path to our attachment
            File projectNoteDirectory = PathHandler.getAttachmentFile(project, attachment);
            attachedFile = new File(projectNoteDirectory.getAbsolutePath(), attachment.getFileName());

            // make sure the file actually exists
            if( !attachedFile.exists() ) {
                addActionError(getText("error.noprojectnoteattachedfile", fileName));
				logger.error("Attachment missing on filesystem for Project: " + project.getId());
                return MISSING;
            }
        }
		
		fileName = attachedFile.getName();
		// stream the file back to the browser
		//fileSize = new Long( attachedFile.length() ).intValue();
		InputStream input = null;
		boolean failure = false;
		try {

			input = new FileInputStream( attachedFile );
			return sendFile( input );
		} catch( IOException e ) {
			logger.error(e.getStackTrace().toString());
			failure = true;
		} finally {
			
		}
		
		return (failure) ? ERROR : null;
	}

	public Project getProject() {
		return project;
	}

	private List<FileAttachment> getProjectNotes() {
		return projectManager.getNotesPaged(project, 1, Constants.PAGE_SIZE).getList();
	}

	public void setProjectId( Long projectId ) {
		if( projectId == null ) {
			project = null;
		} else if( project == null || !projectId.equals( project.getId() ) ) {
			project = persistenceManager.find( Project.class, projectId, getSecurityFilter(), "notes" );
		}
	}

}
