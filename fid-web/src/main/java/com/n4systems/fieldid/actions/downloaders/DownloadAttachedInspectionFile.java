package com.n4systems.fieldid.actions.downloaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.SubEvent;
import com.n4systems.reporting.PathHandler;

public class DownloadAttachedInspectionFile extends DownloadAction {
	private static final long serialVersionUID = 1L;

	private Event event;
	private EventManager eventManager;
	
	public DownloadAttachedInspectionFile(EventManager eventManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.eventManager = eventManager;
	}

	public String doDownload() {
		
		// load the inspection
		event =  eventManager.findAllFields( uniqueID, getSecurityFilter() );
		
		if( event == null ) {
			addActionError( getText( "error.noevent" ) );
			return MISSING;
		} 
		
		FileAttachment attachment = null;
		
		// make sure our attachment is actually attached to this inspection
		for(FileAttachment attach: event.getAttachments()) {
			if(attach.getId().equals(attachmentID)) {
				attachment = attach;
				break;
			}
		}
		
		// we did not find the attachment
		if(attachment == null) {
			addActionError( getText( "error.noeventattachedfile", fileName ) );
			return MISSING;
		}
		
		// construct a file path to our attachment
		File inspectionDirectory = PathHandler.getAttachmentFile(event);
		File attachedFile = new File( inspectionDirectory.getAbsolutePath(), attachment.getFileName() );
		
		// make sure the file actually exists
		if( !attachedFile.exists() ) {
			addActionError( getText( "error.noeventattachedfile", fileName ) );
			return MISSING;
		}
		
		// stream the file back to the browser
		fileSize = new Long( attachedFile.length() ).intValue();
		InputStream input = null;
		boolean failure = false;
		try {
			input = new FileInputStream( attachedFile );
			return sendFile( input );
		} catch( IOException e ) {
		} finally {
			failure = true;
		}
		
		return (failure) ? ERROR : null;
	}
	
	public String doDownloadSubInspection()  {
		
		// load the inspection
		event = eventManager.findEventThroughSubInspection( uniqueID, getSecurityFilter() );
		
		SubEvent subEvent = eventManager.findSubEvent( uniqueID, getSecurityFilter() );
		
		if( subEvent == null ) {
			addActionError( getText( "error.noevent" ) );
			return MISSING;
		} 
		
		FileAttachment attachment = null;
		
		// make sure our attachment is actually attached to this inspection
		for(FileAttachment attach: subEvent.getAttachments()) {
			if(attach.getId().equals(attachmentID)) {
				attachment = attach;
				break;
			}
		}
		
		// we did not find the attachment
		if(attachment == null) {
			addActionError( getText( "error.noeventattachedfile", fileName ) );
			return MISSING;
		}
		
		// construct a file path to our attachment
		File inspectionDirectory = PathHandler.getAttachmentFile(event, subEvent);
		File attachedFile = new File( inspectionDirectory.getAbsolutePath(), attachment.getFileName() );
		
		// make sure the file actually exists
		if( !attachedFile.exists() ) {
			addActionError( getText( "error.noeventattachedfile", fileName ) );
			return MISSING;
		}
		
		// stream the file back to the browser
		fileSize = new Long( attachedFile.length() ).intValue();
		InputStream input = null;
		boolean failure = false;
		try {
			input = new FileInputStream( attachedFile );
			return sendFile( input );
		} catch( IOException e ) {
		} finally {
			failure = true;
		}
		
		return (failure) ? ERROR : null;
	}
	
	
	public String doDownloadChart() {
		event =  eventManager.findAllFields( uniqueID, getSecurityFilter() );
		if( event == null ) {
			addActionError( getText( "error.noevent" ) );
			return MISSING;
		} 
		
		if( event.getProofTestInfo() == null ) {
			addActionError( getText( "error.nochart", fileName ) );
			return MISSING;
		}
		
		File chartFile = PathHandler.getChartImageFile(event);
		fileName = chartFile.getName();
		if( !chartFile.exists() ) {
			addActionError( getText( "error.nochart", chartFile.getName() ) );
			return MISSING;
		}
		
		fileSize = new Long( chartFile.length() ).intValue();
		InputStream input = null;
		boolean failure = false;
		try {
			input = new FileInputStream( chartFile );
			return sendFile( input );
		} catch( IOException e ) {
		} finally {
			failure = true;
		}
		
		return (failure) ? ERROR : null;
	}
	
}
