package com.n4systems.fieldid.actions.downloaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Project;
import com.n4systems.reporting.PathHandler;

public class DownloadProjectNoteFile extends DownloadAction {

	private static final long serialVersionUID = 1L;

	private Project project;

	
	
	public DownloadProjectNoteFile( PersistenceManager persistenceManager ) {
		super(persistenceManager);
	}

	

	@Override
	public String doDownload() {
		
		if( project == null ) {
			addActionError( getText( "error.noproject" ) );
			return MISSING;
		} 
		
		FileAttachment attachment = null;
		
		// make sure our attachment is actually attached to this assettype
		for(FileAttachment attach: project.getNotes()) {
			if(attach.getId().equals(attachmentID)) {
				attachment = attach;
				break;
			}
		}
		
		// we did not find the attachment
		if(attachment == null || attachment.getFileName() == null ) {
			addActionError(getText("error.noprojectnoteattachedfile", fileName));
			return MISSING;
		}
		
		// construct a file path to our attachment
		File projectNoteDirectory = PathHandler.getAttachmentFile(project, attachment);
		File attachedFile = new File(projectNoteDirectory.getAbsolutePath(), attachment.getFileName());
		
		// make sure the file actually exists
		if( !attachedFile.exists() ) {
			addActionError( getText( "error.noprojectnoteattachedfile", fileName ) );
			return MISSING;
		}
		
		fileName = attachment.getFileName();
		// stream the file back to the browser
		fileSize = new Long( attachedFile.length() ).intValue();
		InputStream input = null;
		boolean failure = false;
		try {
			input = new FileInputStream( attachedFile );
			return sendFile( input );
		} catch( IOException e ) {
			failure = true;
		} finally {
			
		}
		
		return (failure) ? ERROR : null;
	}



	public Project getProject() {
		return project;
	}



	public void setProjectId( Long projectId ) {
		if( projectId == null ) {
			project = null;
		} else if( project == null || !projectId.equals( project.getId() ) ) {
			project = persistenceManager.find( Project.class, projectId, getSecurityFilter(), "notes" );
			project.setNotes( persistenceManager.reattchAndFetch( project.getNotes(), "modifiedBy.userID" ) );
		}
	}

}
