package com.n4systems.reporting;

import java.io.File;

import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.SubEvent;
import com.n4systems.util.DateTimeDefinition;

public class SubInspectionReportMapProducer extends AbsractInspectionReportMapProducer {

	private final SubEvent event;
	private final Event masterEvent;
	
	public SubInspectionReportMapProducer(SubEvent event, Event masterEvent, DateTimeDefinition dateTimeDefinition) {
		super(dateTimeDefinition);
		this.event = event;
		this.masterEvent = masterEvent;
	}
	
	@Override
	public void inspectionParameter() {
		SubEvent subEvent = (SubEvent) getInspection();
		add("productLabel", subEvent.getName());
	}

	@Override
	protected File imagePath(FileAttachment imageAttachment) {
		return PathHandler.getInspectionAttachmentFile(masterEvent, event, imageAttachment);
	}

	@Override
	protected AbstractEvent getInspection() {
		return event;
	}



}
