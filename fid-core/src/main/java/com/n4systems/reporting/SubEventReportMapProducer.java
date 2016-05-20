package com.n4systems.reporting;

import java.io.File;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.LastEventDateService;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.SubEvent;
import com.n4systems.util.DateTimeDefinition;

public class SubEventReportMapProducer extends AbsractEventReportMapProducer {

	private final SubEvent event;
	private final Event masterEvent;

	public SubEventReportMapProducer(SubEvent event, Event masterEvent, DateTimeDefinition dateTimeDefinition, S3Service s3service, LastEventDateService lastEventDateService, AssetService assetService) {
		super(dateTimeDefinition, s3service, lastEventDateService, assetService);
		this.event = event;
		this.masterEvent = masterEvent;
	}
	
	@Override
	public void eventParameter() {
		SubEvent subEvent = (SubEvent) getEvent();
		add("productLabel", subEvent.getName());
	}

	@Override
	protected File imagePath(FileAttachment imageAttachment) {
        if(imageAttachment.isRemote())
            return s3Service.downloadFileAttachment(imageAttachment);
        else
            return PathHandler.getEventAttachmentFile(masterEvent, event, imageAttachment);
	}

	@Override
	protected AbstractEvent getEvent() {
		return event;
	}



}
