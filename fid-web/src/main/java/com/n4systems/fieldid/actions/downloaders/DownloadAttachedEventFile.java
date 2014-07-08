package com.n4systems.fieldid.actions.downloaders;

import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.util.ZipFileUtil;
import com.n4systems.model.*;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.ServiceLocator;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Iterator;
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

        File attachedFile;
        if(attachment.isRemote()){
            attachedFile = s3Service.downloadFileAttachment(attachment);
            if(attachedFile == null) {
                addActionError( getText( "error.noeventattachedfile", fileName ) );
                return MISSING;
            }
        }
        else {
            // construct a file path to our attachment
            File eventDirectory = PathHandler.getAttachmentFile(event);
            attachedFile = new File( eventDirectory.getAbsolutePath(), attachment.getFileName() );

            // make sure the file actually exists
            if( !attachedFile.exists() ) {
                addActionError( getText( "error.noeventattachedfile", fileName ) );
                return MISSING;
            }
        }

        // stream the file back to the browser
        //fileSize = new Long( attachedFile.length() ).intValue();
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

        File attachedFile;
        if(attachment.isRemote()){
            attachedFile = s3Service.downloadFileAttachment(attachment);
            if(attachedFile == null) {
                addActionError( getText( "error.noeventattachedfile", fileName ) );
                return MISSING;
            }
        }
        else {
            // construct a file path to our attachment
            File eventDirectory = PathHandler.getAttachmentFile(event, subEvent);
            attachedFile = new File( eventDirectory.getAbsolutePath(), attachment.getFileName() );

            // make sure the file actually exists
            if( !attachedFile.exists() ) {
                addActionError( getText( "error.noeventattachedfile", fileName ) );
                return MISSING;
            }
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

        Iterator<ThingEventProofTest> itr = event.getThingEventProofTests().iterator();
        if (!itr.hasNext()) {
			addActionError( getText( "error.nochart", fileName ) );
			return MISSING;
		}

        S3Service s3Service = ServiceLocator.getS3Service();
        ThingEventProofTest proofTest = itr.next();

        File chartFile = null;

        if(s3Service.assetProofTestExists(event.getAsset().getMobileGUID(), event.getMobileGUID())){
            chartFile = s3service.downloadAssetProofTest(proofTest);
        }
        else {
            chartFile = PathHandler.getChartImageFile(event);
        }

		if( chartFile == null || !chartFile.exists() ) {
			addActionError( getText( "error.nochart", fileName ) );
			return MISSING;
		}

        fileName = chartFile.getName();

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

                if(attachment.isRemote()){
                    ZipFileUtil.addToZipFile(s3Service.downloadFileAttachment(attachment), zipOut);
                }
                else {
                    File attachedFile = new File( PathHandler.getAttachmentFile(event), attachment.getFileName());
                    ZipFileUtil.addToZipFile(attachedFile, zipOut);
                }

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
        File file = new File(getCurrentUser().getPrivateDir(), filename);
        ensureParentDirectoriesExist(file);
        return file;
    }

    private void ensureParentDirectoriesExist(File file) {
        File parentDir = file.getParentFile();

        if (!parentDir.isDirectory() && !parentDir.mkdirs()) {
            throw new SecurityException("Could not create directory [" + parentDir.toString() + "]");
        }
    }

    @Override
    public void postSendActions() {
        getFile(fileName).delete();
    }
}
