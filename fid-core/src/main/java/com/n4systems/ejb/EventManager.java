package com.n4systems.ejb;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.model.*;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.tools.Pager;
import com.n4systems.webservice.dto.WSSearchCritiera;

import java.util.Date;
import java.util.List;

public interface EventManager {

	public SubEvent findSubEvent(Long subEventId, SecurityFilter filter);

	public ThingEvent findEventThroughSubEvent(Long subEventId, SecurityFilter filter);

	public ThingEvent findAllFields(Long id, SecurityFilter filter);

//	public Date findLastEventDate(EventSchedule schedule);

//	public Date findLastEventDate(Long scheduleId);
	public Date findLastEventDate(Long eventId);

    public List<ThingEvent> findEventsByDateAndAsset(Date datePerformedRangeStart, Date datePerformedRangeEnd, Asset asset, SecurityFilter filter);

	public Pager<ThingEvent> findNewestEvents(WSSearchCritiera searchCriteria, SecurityFilter securityFilter, int page, int pageSize);

//	public Pager<Event> findNewestEvents(WSJobSearchCriteria searchCriteria, SecurityFilter securityFilter, int page, int pageSize);

	public boolean isMasterEvent(Long id);

	public ThingEvent updateEvent(ThingEvent event, Long scheduleId, Long userId, FileDataContainer fileData, List<FileAttachment> uploadedFiles) throws ProcessingProofTestException, FileAttachmentException;


	public ThingEvent attachFilesToSubEvent(ThingEvent event, SubEvent subEvent, List<FileAttachment> uploadedFiles) throws FileAttachmentException;

	public ThingEvent retireEvent(ThingEvent event, Long userId);

}
