package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.SubAsset;
import com.n4systems.model.SubEvent;
import rfid.ejb.entity.AssetStatus;

import com.n4systems.fieldid.actions.inspection.WebInspectionSchedule;
import com.n4systems.fieldid.utils.StrutsListHelper;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.inspection.AssignedToUpdate;
import com.n4systems.model.user.User;
import com.n4systems.tools.FileDataContainer;

public class MasterInspection {
	private String token;

	private Asset masterAsset;

	private Event event;

	private FileDataContainer proofTestFile;

	private AssetStatus assetStatus;

	private List<SubEvent> subEvents = new ArrayList<SubEvent>();

	private Long currentId = -1L;

	private Long inspectionGroupId;

	private Map<SubEvent, List<FileAttachment>> subInspectionUploadedFiles;

	private List<FileAttachment> uploadedFiles;

	private EventSchedule schedule;
	
	private Long scheduleId;

	
	private List<WebInspectionSchedule> nextSchedules = new ArrayList<WebInspectionSchedule>();

	private User assignedTo;

	private boolean assignToSomeone;
	
	public MasterInspection() {
		token = String.valueOf(Math.abs(new Random().nextLong()));
		subInspectionUploadedFiles = new HashMap<SubEvent, List<FileAttachment>>();
	}

	public MasterInspection(Event event) {
		this();

		currentId = event.getId();
		inspectionGroupId = event.getGroup().getId();
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

	public Event getInspection() {
		return event;
	}

	public void setInspection(Event event) {
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

	public void addSubInspection(SubEvent event) {
		event.setId(nextId());
		subEvents.add(event);
	}

	public void replaceSubInspection(SubEvent updatedEvent) {
		SubEvent oldInspeciton = getSubInspection(updatedEvent.getId());
		subEvents.set(subEvents.indexOf(oldInspeciton), updatedEvent);
	}

	public SubEvent getSubInspection(Long id) {
		SubEvent i = new SubEvent();

		i.setId(id); // this doesn't seem good.

		if (subEvents.contains(i)) {
			return subEvents.get(subEvents.indexOf(i));
		}

		return null;
	}

	public Event createInspectionFromSubInspection(SubEvent subEvent) {
		Event event = new Event();

		event.setFormVersion(subEvent.getFormVersion());
		event.setId(subEvent.getId());
		event.setAsset(subEvent.getAsset());
		event.setTenant(subEvent.getTenant());
		event.setType(subEvent.getType());
		event.setComments(subEvent.getComments());
		event.setResults(subEvent.getResults());
		event.setInfoOptionMap(subEvent.getInfoOptionMap());
		event.setAttachments(subEvent.getAttachments());
		event.setCreated(subEvent.getCreated());
		event.setModified(subEvent.getModified());
		event.setModifiedBy(subEvent.getModifiedBy());

		return event;
	}

	public SubEvent createSubInspectionFromInspection(Event event) {
		SubEvent subEvent = new SubEvent();

		subEvent.setFormVersion(event.getFormVersion());
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

	
	public Event getCompletedInspection() {
		applyAssignToUpdateToInspection();
		processSubInspections();

		return event;
	}

	private void processSubInspections() {
		event.getSubEvents().clear();
		StrutsListHelper.clearNulls(subEvents);
		for (SubEvent subEvent : subEvents) {
			SubEvent s = createSubInspectionFromInspection(createInspectionFromSubInspection(subEvent));
			s.setId((subEvent.getId() < 0) ? null : subEvent.getId());
			s.setResults(subEvent.getResults());
			processResults(s);
			s.setFormVersion(subEvent.getFormVersion());
			event.getSubEvents().add(s);

		}
	}

	private void processResults(SubEvent s) {
		for (CriteriaResult criteria : s.getResults()) {
			criteria.setInspection(s);
		}
	}

	public static boolean matchingMasterInspection(MasterInspection masterInspection, String token) {
		if (masterInspection == null) {
			return false;
		}

		return masterInspection.matchingToken(token);
	}

	public boolean isMainInspectionStored() {
		return (event.getDate() != null);
	}

	public Long getInspectionGroupId() {
		return inspectionGroupId;
	}

	public void setInspectionGroupId(Long inspectionGroupId) {
		this.inspectionGroupId = inspectionGroupId;
	}

	public List<SubEvent> getSubInspectionFor(Asset asset) {
		List<SubEvent> eventTypes = new ArrayList<SubEvent>();
		for (SubEvent subEvent : subEvents) {
			if (subEvent.getAsset().getId().equals(asset.getId())) {
				eventTypes.add(subEvent);
			}
		}
		return eventTypes;
	}

	public void removeInspectionsForAsset(Asset subAsset) {
		for (int i = 0; i < subEvents.size(); i++) {
			if (subEvents.get(i).getAsset().equals(subAsset)) {
				subEvents.set(i, null);
			}
		}
	}

	public void cleanSubInspectionsForNonValidSubAssets(Asset upToDateProduct) {
		List<SubEvent> subInspectionsToKeep = new ArrayList<SubEvent>();
		
		/*
		 * this checks that each sub inspection is for an asset that is still
		 * attached to our updated master asset.
		 */
		for (SubEvent subEvent : subEvents) {
			for (SubAsset subAsset : upToDateProduct.getSubAssets()) {
				if (subAsset.getAsset().equals(subEvent.getAsset())) {
					subInspectionsToKeep.add(subEvent);
					break;
				}
			}
		}
		subEvents.retainAll(subInspectionsToKeep);
	}

	public Map<SubEvent, List<FileAttachment>> getSubInspectionUploadedFiles() {
		return subInspectionUploadedFiles;
	}

	public void setSubInspectionUploadedFiles(Map<SubEvent, List<FileAttachment>> subInspectionUploadedFiles) {
		this.subInspectionUploadedFiles = subInspectionUploadedFiles;
	}

	public List<FileAttachment> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(List<FileAttachment> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}

	public List<SubEvent> getSubInspections() {
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
	

	public List<WebInspectionSchedule> getNextSchedules() {
		return nextSchedules;
	}

	public void setNextSchedules(List<WebInspectionSchedule> nextSchedules) {
		this.nextSchedules = nextSchedules;
	}

	public void setAssignToUpdate(User assignedTo, boolean assignToSomeone) {
		this.assignedTo = assignedTo;
		this.assignToSomeone = assignToSomeone;
	}
	
	
	public void applyAssignToUpdateToInspection() {
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
	
	
}
