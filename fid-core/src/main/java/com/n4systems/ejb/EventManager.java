package com.n4systems.ejb;

import java.util.Date;
import java.util.List;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.model.Event;
import com.n4systems.model.EventGroup;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.EventType;
import com.n4systems.model.Asset;
import com.n4systems.model.SubEvent;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.tools.Pager;
import com.n4systems.webservice.dto.WSJobSearchCriteria;
import com.n4systems.webservice.dto.WSSearchCritiera;

public interface EventManager {


	public List<EventGroup> findAllEventGroups(SecurityFilter filter, Long assetId, String... postFetchFields);

	public SubEvent findSubEvent(Long subEventId, SecurityFilter filter);

	public Event findEventThroughSubEvent(Long subEventId, SecurityFilter filter);

	public Event findAllFields(Long id, SecurityFilter filter);

	public Date findLastEventDate(EventSchedule schedule);

	public Date findLastEventDate(Long scheduleId);

	public List<Event> findEventsByDateAndAsset(Date datePerformedRangeStart, Date datePerformedRangeEnd, Asset asset, SecurityFilter filter);

	public Pager<Event> findNewestEvents(WSSearchCritiera searchCriteria, SecurityFilter securityFilter, int page, int pageSize);
	
	public Pager<Event> findNewestEvents(WSJobSearchCriteria searchCriteria, SecurityFilter securityFilter, int page, int pageSize);
	
	public boolean isMasterEvent(Long id);
	
	public Event updateEvent(Event event, Long userId, FileDataContainer fileData, List<FileAttachment> uploadedFiles) throws ProcessingProofTestException, FileAttachmentException;

	
	public Event attachFilesToSubEvent(Event event, SubEvent subEvent, List<FileAttachment> uploadedFiles) throws FileAttachmentException;

	public Event retireEvent(Event event, Long userId);

	

	public EventType updateEventForm(EventType eventType, Long modifyingUserId);

}
