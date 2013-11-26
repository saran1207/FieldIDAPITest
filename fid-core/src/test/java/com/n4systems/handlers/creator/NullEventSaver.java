/**
 * 
 */
package com.n4systems.handlers.creator;

import com.n4systems.ejb.impl.CreateEventParameter;
import com.n4systems.ejb.impl.EventSaver;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.UnknownSubAsset;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.SubEvent;
import com.n4systems.model.ThingEvent;

import java.util.List;

public class NullEventSaver implements EventSaver {
	@Override
	public ThingEvent createEvent(CreateEventParameter parameterObject) throws ProcessingProofTestException, FileAttachmentException, UnknownSubAsset {
		return parameterObject.event;
	}

	public ThingEvent attachFilesToSubEvent(ThingEvent event, SubEvent subEvent, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
		return null;
	}
}