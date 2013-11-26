package com.n4systems.fieldid.actions.downloaders;

import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.wicket.util.ZipFileUtil;
import com.n4systems.model.*;
import com.n4systems.reporting.PathHandler;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.zip.ZipOutputStream;

public class DownloadAttachedEventFile extends DownloadAction {
	private static final long serialVersionUID = 1L;

	private ThingEvent event;
	private EventManager eventManager;
	
	public DownloadAttachedEventFile(EventManager eventManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.eventManager = eventManager;
	}

	public String doDownload() {
		
		// load the event
		event =  eventManager.findAllFields( uniqueID, getSecurityFilter() );
		
		if( event == null ) {
			addActionError( getText( "error.noevent" ) );
			return MISSING;
		} 
		
		FileAttachment attachment = null;
		
		// make sure our attachment is actually attached to this event
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
		File eventDirectory = PathHandler.getAttachmentFile(event);
		File attachedFile = new File( eventDirectory.getAbsolutePath(), attachment.getFileName() );
		
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
	
	public String doDownloadSubEvent()  {
		
		// load the event
		event = eventManager.findEventThroughSubEvent( uniqueID, getSecurityFilter() );
		
		SubEvent subEvent = eventManager.findSubEvent( uniqueID, getSecurityFilter() );
		
		if( subEvent == null ) {
			addActionError( getText( "error.noevent" ) );
			return MISSING;
		} 
		
		FileAttachment attachment = null;
		
		// make sure our attachment is actually attached to this event
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
		File eventDirectory = PathHandler.getAttachmentFile(event, subEvent);
		File attachedFile = new File( eventDirectory.getAbsolutePath(), attachment.getFileName() );
		
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


    public String doDownloadAll() {
        // load the event
        event =  eventManager.findAllFields( uniqueID, getSecurityFilter() );

        if( event == null ) {
            addActionError( getText( "error.noevent" ) );
            return MISSING;
        }

        String filename = event.getAsset().getIdentifier() + "-" + new SimpleDateFormat("MM-dd-yy").format(event.getCompletedDate()) + "-Attachments.zip";
        boolean failure = false;

        try {
            setFileName(URLEncoder.encode(filename, "UTF-8"));
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(getFile(fileName)));
            for(FileAttachment attachment: event.getAttachments()) {

                File attachedFile = new File( PathHandler.getAttachmentFile(event), attachment.getFileName());
                ZipFileUtil.addToZipFile(attachedFile, zipOut);
            }
            IOUtils.closeQuietly(zipOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            failure = true;
        } catch (IOException e) {
            e.printStackTrace();
            failure = true;
        }
        File downloadFile = getFile(fileName);
        // stream the file back to the browser
        fileSize = new Long( downloadFile.length() ).intValue();
        InputStream input = null;
        try {
            input = new FileInputStream( downloadFile );
            return sendFile( input );
        } catch( IOException e ) {
        } finally {
            failure = true;
        }

        return (failure) ? ERROR : null;
    }

    private File getFile(String filename) {
        return new File(getCurrentUser().getPrivateDir(), filename);
    }

    @Override
    public void postSendActions() {
        getFile(fileName).delete();
    }
}
