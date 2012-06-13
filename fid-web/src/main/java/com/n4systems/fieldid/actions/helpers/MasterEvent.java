package com.n4systems.fieldid.actions.helpers;

import com.n4systems.fieldid.actions.event.WebEventSchedule;
import com.n4systems.fieldid.actions.event.viewmodel.CriteriaResultWebModel;
import com.n4systems.fieldid.utils.StrutsListHelper;
import com.n4systems.model.*;
import com.n4systems.model.event.AssignedToUpdate;
import com.n4systems.model.user.User;
import com.n4systems.tools.FileDataContainer;

import java.util.*;

public class MasterEvent {

	private String token;
	private Asset masterAsset;
	private Event event;
	private FileDataContainer proofTestFile;
	private AssetStatus assetStatus;
	private List<SubEvent> subEvents = new ArrayList<SubEvent>();
    private Map<Long, Map<Long, String>> subEventTemporarySignatureFileIdMap = new HashMap<Long, Map<Long,String>>();
	private Long currentId = -1L;
	private Long eventGroupId;
	private Map<SubEvent, List<FileAttachment>> subEventUploadedFiles;
	private List<FileAttachment> uploadedFiles;
	private EventSchedule schedule;
	private Long scheduleId;
	private List<WebEventSchedule> nextSchedules = new ArrayList<WebEventSchedule>();
	private User assignedTo;
	private boolean assignToSomeone;
    private String overrideResult;
    private boolean cleanToEventsToMatchConfiguration = false;
    
	public MasterEvent() {
		token = String.valueOf(Math.abs(new Random().nextLong()));
		subEventUploadedFiles = new HashMap<SubEvent, List<FileAttachment>>();
	}

	public MasterEvent(Event event) {
		this();

		currentId = event.getId();
		eventGroupId = event.getGroup().getId();
		masterAsset = event.getAsset();
		subEvents = new ArrayList<SubEvent>(event.getSubEvents());
		this.event = event;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Asset getMasterAsset() {
		return masterAsset;
	}

	public void setMasterAsset(Asset masterAsset) {
		this.masterAsset = masterAsset;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public FileDataContainer getProofTestFile() {
		return proofTestFile;
	}

	public void setProofTestFile(FileDataContainer proofTestFile) {
		this.proofTestFile = proofTestFile;
	}

	public AssetStatus getAssetStatus() {
		return assetStatus;
	}

	public void setAssetStatus(AssetStatus assetStatus) {
		this.assetStatus = assetStatus;
	}

	public boolean matchingToken(String token) {
		return (token != null && token.equalsIgnoreCase(this.token));
	}

	private Long nextId() {
		return currentId--;
	}

	public void addSubEvent(SubEvent event) {
		event.setId(nextId());
		subEvents.add(event);
	}

	public void replaceSubEvent(SubEvent updatedEvent) {
		SubEvent oldEvent = getSubEvent(updatedEvent.getId());
		subEvents.set(subEvents.indexOf(oldEvent), updatedEvent);
	}

	public SubEvent getSubEvent(Long id) {
		SubEvent i = new SubEvent();

		i.setId(id); // this doesn't seem good.

		if (subEvents.contains(i)) {
			return subEvents.get(subEvents.indexOf(i));
		}

		return null;
	}

	public Event createEventFromSubEvent(SubEvent subEvent) {
		Event event = new Event();

		event.setId(subEvent.getId());
		event.setAsset(subEvent.getAsset());
		event.setTenant(subEvent.getTenant());
		event.setType(subEvent.getType());
		event.setComments(subEvent.getComments());
		event.setCriteriaResults(subEvent.getResults());
		event.setInfoOptionMap(subEvent.getInfoOptionMap());
		event.setAttachments(subEvent.getAttachments());
		event.setCreated(subEvent.getCreated());
		event.setModified(subEvent.getModified());
		event.setModifiedBy(subEvent.getModifiedBy());
        event.setEventForm(subEvent.getEventForm());
        event.setEditable(subEvent.isEditable());
        event.setEventStatus(subEvent.getEventStatus());

		return event;
	}

	public SubEvent createSubEventFromEvent(Event event) {
		SubEvent subEvent = new SubEvent();

		subEvent.setId(event.getId());
		subEvent.setAsset(event.getAsset());
		subEvent.setName(findLabelOfSubAsset(event));
		subEvent.setTenant(event.getTenant());
		subEvent.setType(event.getType());
		subEvent.setComments(event.getComments());
		subEvent.setInfoOptionMap(event.getInfoOptionMap());
		subEvent.setAttachments(event.getAttachments());
		subEvent.setCreated(event.getCreated());
		subEvent.setModified(event.getModified());
		subEvent.setModifiedBy(event.getModifiedBy());
        subEvent.setEventForm(event.getEventForm());
        subEvent.setEditable(event.isEditable());
        subEvent.setEventStatus(event.getEventStatus());

		return subEvent;
	}

	private String findLabelOfSubAsset(Event event) {
		Asset asset = event.getAsset();
		if (asset == null) {
			return null;
		}
		if (this.event.isNew()) {

			for (SubAsset subAsset : this.masterAsset.getSubAssets()) {
				if (subAsset.getAsset().equals(asset)) {
					return subAsset.getLabel();
				}
			}
		} else {
			for (SubEvent subEvent : subEvents) {
				if (subEvent.getId().equals(event.getId())) {
					return subEvent.getName();
				}
			}
		}
		return null;
	}

	public Event getCompletedEvent() {
		applyAssignToUpdateToEvent();
		processSubEvents();
        if (overrideResult != null) {
            if ("auto".equals(overrideResult)) {
                event.setStatus(null);
            } else {
                event.setStatus(Status.valueOf(overrideResult));
            }
        }

		return event;
	}

	private void processSubEvents() {
		event.getSubEvents().clear();
		StrutsListHelper.clearNulls(subEvents);
		for (SubEvent subEvent : subEvents) {
			SubEvent s = createSubEventFromEvent(createEventFromSubEvent(subEvent));
            storeTemporarySignatureFilesInMemory(s.getResults());
			s.setId((subEvent.getId() < 0) ? null : subEvent.getId());
			s.setCriteriaResults(subEvent.getResults());
			processResults(s);
			event.getSubEvents().add(s);
		}
	}

    private void storeTemporarySignatureFilesInMemory(Set<CriteriaResult> results) {
        for (CriteriaResult result : results) {
            if (result instanceof SignatureCriteriaResult) {

            }
        }
    }

	private void processResults(SubEvent s) {
		for (CriteriaResult criteria : s.getResults()) {
			criteria.setEvent(s);
		}
	}

	public static boolean matchingMasterEvent(MasterEvent masterEvent, String token) {
		if (masterEvent == null) {
			return false;
		}

		return masterEvent.matchingToken(token);
	}

	public boolean isMainEventStored() {
		return (event.getDate() != null);
	}

	public Long getEventGroupId() {
		return eventGroupId;
	}

	public void setEventGroupId(Long eventGroupId) {
		this.eventGroupId = eventGroupId;
	}

	public List<SubEvent> getSubEventFor(Asset asset) {
		List<SubEvent> eventTypes = new ArrayList<SubEvent>();
		for (SubEvent subEvent : subEvents) {
			if (subEvent.getAsset().getId().equals(asset.getId())) {
				eventTypes.add(subEvent);
			}
		}
		return eventTypes;
	}

	public void removeEventsForAsset(Asset subAsset) {
		for (int i = 0; i < subEvents.size(); i++) {
			if (subEvents.get(i).getAsset().equals(subAsset)) {
				subEvents.set(i, null);
			}
		}
	}

	public void cleanSubEventsForNonValidSubAssets(Asset upToDateAsset) {
		List<SubEvent> subEventsToKeep = new ArrayList<SubEvent>();
		
		/*
		 * this checks that each sub event is for an asset that is still
		 * attached to our updated master asset.
		 */
		for (SubEvent subEvent : subEvents) {
			for (SubAsset subAsset : upToDateAsset.getSubAssets()) {
				if (subAsset.getAsset().equals(subEvent.getAsset())) {
					subEventsToKeep.add(subEvent);
					break;
				}
			}
		}
		subEvents.retainAll(subEventsToKeep);
	}

	public Map<SubEvent, List<FileAttachment>> getSubEventUploadedFiles() {
		return subEventUploadedFiles;
	}

	public void setSubEventUploadedFiles(Map<SubEvent, List<FileAttachment>> subEventUploadedFiles) {
		this.subEventUploadedFiles = subEventUploadedFiles;
	}

	public List<FileAttachment> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(List<FileAttachment> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}

	public List<SubEvent> getSubEvents() {
		return subEvents;
	}

	public EventSchedule getSchedule() {
		return schedule;
	}

	public void setSchedule(EventSchedule schedule) {
		this.schedule = schedule;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}
	

	public List<WebEventSchedule> getNextSchedules() {
		return nextSchedules;
	}

	public void setNextSchedules(List<WebEventSchedule> nextSchedules) {
		this.nextSchedules = nextSchedules;
	}

	public void setAssignToUpdate(User assignedTo, boolean assignToSomeone) {
		this.assignedTo = assignedTo;
		this.assignToSomeone = assignToSomeone;
	}
	
	
	public void applyAssignToUpdateToEvent() {
		if (event.isNew()) {
			if (assignToSomeone) {
				event.setAssignedTo(AssignedToUpdate.assignAssetToUser(assignedTo));
			} else {
				event.removeAssignTo();
			}
		}
	}

	public Long getAssignedToId() {
		return assignedTo != null ? assignedTo.getId() : null;
	}

	public boolean isAssignToSomeone() {
		return assignToSomeone;
	}

    public String getOverrideResult() {
        return overrideResult;
    }

    public void setOverrideResult(String overrideResult) {
        this.overrideResult = overrideResult;
    }

	public boolean isCleanToEventsToMatchConfiguration() {
		return cleanToEventsToMatchConfiguration;
	}

	public void setCleanToEventsToMatchConfiguration(boolean cleanToEventsToMatchConfiguration) {
		this.cleanToEventsToMatchConfiguration = cleanToEventsToMatchConfiguration;
	}

    public void storeTemporaryFileIds(List<CriteriaResultWebModel> criteriaResults) {
        storeTemporaryFileIds(currentId, criteriaResults);
    }

    public void storeTemporaryFileIds(Long eventId, List<CriteriaResultWebModel> criteriaResults) {
    	if(criteriaResults != null) {
	        for (CriteriaResultWebModel criteriaResult : criteriaResults) {
	            storeTemporaryFileId(eventId, criteriaResult.getCriteriaId(), criteriaResult.isSigned(), criteriaResult.getSignatureFileId());
	        }
        }
    }

    private void storeTemporaryFileId(Long eventId, Long criteriaId, boolean signed, String fileId) {
        if (subEventTemporarySignatureFileIdMap.get(eventId) == null) {
            subEventTemporarySignatureFileIdMap.put(eventId, new HashMap<Long, String>());
        }
        subEventTemporarySignatureFileIdMap.get(eventId).put(criteriaId, signed ? fileId : null);
    }

    public String getTemporarySignatureFileId(Long eventId, Long criteriaId) {
        if (subEventTemporarySignatureFileIdMap.get(eventId) == null) {
            return null;
        }
        return subEventTemporarySignatureFileIdMap.get(eventId).get(criteriaId);
    }

}
