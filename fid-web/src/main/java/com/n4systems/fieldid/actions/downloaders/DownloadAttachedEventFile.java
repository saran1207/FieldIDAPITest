package com.n4systems.fieldid.actions.downloaders;

import com.google.common.collect.Maps;
import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.wicket.util.ZipFileUtil;
import com.n4systems.model.*;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

public class DownloadAttachedEventFile extends DownloadAction {
	private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(DownloadAttachedEventFile.class);

	//private ThingEvent event;
	private EventManager eventManager;
	
	public DownloadAttachedEventFile(EventManager eventManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.eventManager = eventManager;
	}

	public String doDownload() {
		
		// load the event
        ThingEvent event =  eventManager.findAllFields(uniqueID, getSecurityFilter());
		
		if( event == null ) {
			addActionError( getText( "error.noevent" ) );
			return MISSING;
		} 
		
		FileAttachment attachment = null;
		
		// make sure our attachment is actually attached to this event
        List<FileAttachment> fileAttachments = event.getAttachments();
		for(FileAttachment attach: fileAttachments) {
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
        ThingEvent event = eventManager.findEventThroughSubEvent( uniqueID, getSecurityFilter() );
		
		SubEvent subEvent = eventManager.findSubEvent(uniqueID, getSecurityFilter());
		
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
		ThingEvent thingEvent =  eventManager.findAllFields( uniqueID, getSecurityFilter() );
		if( thingEvent == null ) {
			addActionError( getText( "error.noevent" ) );
			return MISSING;
		}

        ThingEventProofTest proofTest = thingEvent.getProofTestInfo();
        if (proofTest == null) {
			addActionError( getText( "error.nochart", fileName ) );
			return MISSING;
		}

        File chartFile = null;

        if(s3Service.assetProofTestChartExists(thingEvent.getAsset().getMobileGUID(), thingEvent.getMobileGUID())){
            chartFile = s3service.downloadAssetProofTestChart(proofTest);
        }
        else {
            chartFile = PathHandler.getChartImageFile(thingEvent);
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
        ThingEvent event =  eventManager.findAllFields(uniqueID, getSecurityFilter());
        String name = event.getAsset().getIdentifier();

        return doDownloadAll(event, name);
    }

    public String doDownloadAllSub() {

        // load the event
        SubEvent event =  eventManager.findSubEvent(uniqueID, getSecurityFilter());
        String name = event.getAsset().getIdentifier();

        return doDownloadAll(event, name);
    }

    public String doDownloadAllPlace() {

        // load the event
        PlaceEvent event =  eventManager.findPlaceEvent(uniqueID, getSecurityFilter());
        String name = event.getPlace().getDisplayName();

        return doDownloadAll(event, name);
    }

    private String doDownloadAll(AbstractEvent event, String name) {

        if( event == null ) {
            addActionError( getText( "error.noevent" ) );
            return MISSING;
        }

        name = name.replaceAll("[^a-zA-Z0-9.-]", "_");
        String filename = name + "-" + new SimpleDateFormat("MM-dd-yy").format(event.getCreated()) + "-Attachments.zip";
        boolean failure = false;

        try {
            setFileName(URLEncoder.encode(filename, "UTF-8"));

            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(getFile(fileName)));
            List<FileAttachment> fileAttachments = event.getAttachments();

            Map<String, Integer> duplicateFilenames = Maps.newHashMap();

            for(FileAttachment attachment: fileAttachments) {

                File attachedFile = null;

                if(attachment.isRemote()){
                    attachedFile = s3Service.downloadFileAttachment(attachment);
                }
                else {
                    attachedFile = new File( PathHandler.getAttachmentFile(event), attachment.getFileName());
                }

                String attachmentName = attachedFile.getName();
                Boolean isRenamed = false;
                if (duplicateFilenames.containsKey(attachmentName)) {
                    Integer count = duplicateFilenames.get(attachmentName) + 1;
                    duplicateFilenames.put(attachmentName, count);
                    String newName = attachedFile.getParent() + "/" + attachmentName.replaceFirst("\\.", "-" + count + "\\.");
                    File renamedFile;
                    if(attachedFile.renameTo(renamedFile = new File(newName))) {
                        attachedFile = renamedFile;
                        isRenamed = true;
                    }

                } else {
                    duplicateFilenames.put(attachmentName, 0);
                }
                ZipFileUtil.addToZipFile(attachedFile, zipOut);
                if (isRenamed) {
                    attachedFile.delete();
                }
            }
            IOUtils.closeQuietly(zipOut);
        } catch (Exception e) {
            logger.error(e);
            failure = true;
        }

        File downloadFile = getFile(fileName);
        // stream the file back to the browser
        fileSize = new Long( downloadFile.length() ).intValue();
        InputStream input = null;
        try {
            input = new FileInputStream( downloadFile );
            return sendFile( input );
        } catch( Exception e ) {
            logger.error(e);
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
