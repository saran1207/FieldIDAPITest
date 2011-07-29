package com.n4systems.model.event;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.persistence.EntityManager;

import com.n4systems.model.SubEvent;
import org.apache.commons.io.IOUtils;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Event;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.reporting.PathHandler;

public class EventAttachmentSaver extends Saver<FileAttachment> {

	private Event event;
	private SubEvent subEvent;
	private byte[] data;
	
	public EventAttachmentSaver() {}
	
	@Override
	public void save(EntityManager em, FileAttachment attachment) {
		writeFileToDisk(attachment);
		
		super.save(em, attachment);
	
		AbstractEvent targetEvent = (subEvent == null) ? event : subEvent;
		targetEvent.getAttachments().add(attachment);
		
		em.merge(targetEvent);
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
		if (subEvent == null) {
			attachmentFile = PathHandler.getEventAttachmentFile(event, attachment);
		} else {
			attachmentFile = PathHandler.getEventAttachmentFile(event, subEvent, attachment);
		}
		return attachmentFile;
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
