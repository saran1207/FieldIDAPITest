package com.n4systems.ejb.impl;

import java.util.List;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.SubEvent;

public interface EventSaver {

	public Event createEvent(CreateEventParameter parameterObject) throws ProcessingProofTestException, FileAttachmentException, UnknownSubAsset;

	/**
	 * This must be called AFTER the event and subevent have been persisted
	 */
	public Event attachFilesToSubEvent(Event event, SubEvent subEvent, List<FileAttachment> uploadedFiles) throws FileAttachmentException;

}