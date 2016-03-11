package com.n4systems.model.event;

import com.amazonaws.AmazonServiceException;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.SubEvent;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.util.ServiceLocator;

import javax.persistence.EntityManager;

public class EventAttachmentSaver extends Saver<FileAttachment> {

	private Event event;
	private SubEvent subEvent;
	private byte[] data;
    protected S3Service s3Service;

    //added get/set for S3Service so that it can be mocked by EasyMock
    public void setS3Service(S3Service _s3Service){
        s3Service = _s3Service;
    }

    private S3Service getS3Service(){
        if(s3Service == null){
            s3Service = ServiceLocator.getS3Service();
        }
        return s3Service;
    }
	
	@Override
	public void save(EntityManager em, FileAttachment attachment) {
        saveFileAttachment(attachment);
		
		super.save(em, attachment);
	
		AbstractEvent targetEvent = (subEvent == null) ? event : subEvent;
		targetEvent.getAttachments().add(attachment);
		
		em.merge(targetEvent);
	}
	
	private void saveFileAttachment(FileAttachment attachment) {
		try {
            attachment.ensureMobileIdIsSet();
            attachment.setFileName(getS3Service().getFileAttachmentPath(attachment));
            getS3Service().uploadFileAttachmentData(data, attachment);
		} catch(AmazonServiceException e) {
			throw new FileAttachmentException("Failed to write attachment data to S3", e);
		}
	}

	@Override
	public FileAttachment update(EntityManager em, FileAttachment entity) {
		throw new NotImplementedException();
	}
	
	@Override
	public void remove(EntityManager em, FileAttachment entity) {
		throw new NotImplementedException();
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public void setSubEvent(SubEvent subEvent) {
		this.subEvent = subEvent;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
}
