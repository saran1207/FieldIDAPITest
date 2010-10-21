package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.n4systems.model.Asset;
import rfid.ejb.entity.AssetStatus;

import com.n4systems.fieldid.actions.inspection.WebInspectionSchedule;
import com.n4systems.fieldid.utils.StrutsListHelper;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.SubInspection;
import com.n4systems.model.SubProduct;
import com.n4systems.model.inspection.AssignedToUpdate;
import com.n4systems.model.user.User;
import com.n4systems.tools.FileDataContainer;

public class MasterInspection {
	private String token;

	private Asset masterAsset;

	private Inspection inspection;

	private FileDataContainer proofTestFile;

	private AssetStatus assetStatus;

	private List<SubInspection> subInspections = new ArrayList<SubInspection>();

	private Long currentId = -1L;

	private Long inspectionGroupId;

	private Map<SubInspection, List<FileAttachment>> subInspectionUploadedFiles;

	private List<FileAttachment> uploadedFiles;

	private InspectionSchedule schedule;
	
	private Long scheduleId;

	
	private List<WebInspectionSchedule> nextSchedules = new ArrayList<WebInspectionSchedule>();

	private User assignedTo;

	private boolean assignToSomeone;
	
	public MasterInspection() {
		token = String.valueOf(Math.abs(new Random().nextLong()));
		subInspectionUploadedFiles = new HashMap<SubInspection, List<FileAttachment>>();
	}

	public MasterInspection(Inspection inspection) {
		this();

		currentId = inspection.getId();
		inspectionGroupId = inspection.getGroup().getId();
		masterAsset = inspection.getAsset();
		subInspections = new ArrayList<SubInspection>(inspection.getSubInspections());
		this.inspection = inspection;
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

	public Inspection getInspection() {
		return inspection;
	}

	public void setInspection(Inspection inspection) {
		this.inspection = inspection;
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

	public void addSubInspection(SubInspection inspection) {
		inspection.setId(nextId());
		subInspections.add(inspection);
	}

	public void replaceSubInspection(SubInspection updatedInspection) {
		SubInspection oldInspeciton = getSubInspection(updatedInspection.getId());
		subInspections.set(subInspections.indexOf(oldInspeciton), updatedInspection);
	}

	public SubInspection getSubInspection(Long id) {
		SubInspection i = new SubInspection();

		i.setId(id); // this doesn't seem good.

		if (subInspections.contains(i)) {
			return subInspections.get(subInspections.indexOf(i));
		}

		return null;
	}

	public Inspection createInspectionFromSubInspection(SubInspection subInspection) {
		Inspection inspection = new Inspection();

		inspection.setFormVersion(subInspection.getFormVersion());
		inspection.setId(subInspection.getId());
		inspection.setAsset(subInspection.getAsset());
		inspection.setTenant(subInspection.getTenant());
		inspection.setType(subInspection.getType());
		inspection.setComments(subInspection.getComments());
		inspection.setResults(subInspection.getResults());
		inspection.setInfoOptionMap(subInspection.getInfoOptionMap());
		inspection.setAttachments(subInspection.getAttachments());
		inspection.setCreated(subInspection.getCreated());
		inspection.setModified(subInspection.getModified());
		inspection.setModifiedBy(subInspection.getModifiedBy());

		return inspection;
	}

	public SubInspection createSubInspectionFromInspection(Inspection inspection) {
		SubInspection subInspection = new SubInspection();

		subInspection.setFormVersion(inspection.getFormVersion());
		subInspection.setId(inspection.getId());
		subInspection.setAsset(inspection.getAsset());
		subInspection.setName(findLabelOfSubProduct(inspection));
		subInspection.setTenant(inspection.getTenant());
		subInspection.setType(inspection.getType());
		subInspection.setComments(inspection.getComments());
		subInspection.setInfoOptionMap(inspection.getInfoOptionMap());
		subInspection.setAttachments(inspection.getAttachments());
		subInspection.setCreated(inspection.getCreated());
		subInspection.setModified(inspection.getModified());
		subInspection.setModifiedBy(inspection.getModifiedBy());

		return subInspection;
	}

	private String findLabelOfSubProduct(Inspection inspection) {
		Asset asset = inspection.getAsset();
		if (asset == null) {
			return null;
		}
		if (this.inspection.isNew()) {

			for (SubProduct subProduct : this.masterAsset.getSubProducts()) {
				if (subProduct.getAsset().equals(asset)) {
					return subProduct.getLabel();
				}
			}
		} else {
			for (SubInspection subInspection : subInspections) {
				if (subInspection.getId().equals(inspection.getId())) {
					return subInspection.getName();
				}
			}
		}
		return null;
	}

	
	public Inspection getCompletedInspection() {
		applyAssignToUpdateToInspection();
		processSubInspections();

		return inspection;
	}

	private void processSubInspections() {
		inspection.getSubInspections().clear();
		StrutsListHelper.clearNulls(subInspections);
		for (SubInspection subInspection : subInspections) {
			SubInspection s = createSubInspectionFromInspection(createInspectionFromSubInspection(subInspection));
			s.setId((subInspection.getId() < 0) ? null : subInspection.getId());
			s.setResults(subInspection.getResults());
			processResults(s);
			s.setFormVersion(subInspection.getFormVersion());
			inspection.getSubInspections().add(s);

		}
	}

	private void processResults(SubInspection s) {
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
		return (inspection.getDate() != null);
	}

	public Long getInspectionGroupId() {
		return inspectionGroupId;
	}

	public void setInspectionGroupId(Long inspectionGroupId) {
		this.inspectionGroupId = inspectionGroupId;
	}

	public List<SubInspection> getSubInspectionFor(Asset asset) {
		List<SubInspection> inspectionTypes = new ArrayList<SubInspection>();
		for (SubInspection subInspection : subInspections) {
			if (subInspection.getAsset().getId().equals(asset.getId())) {
				inspectionTypes.add(subInspection);
			}
		}
		return inspectionTypes;
	}

	public void removeInspectionsForProduct(Asset subProduct) {
		for (int i = 0; i < subInspections.size(); i++) {
			if (subInspections.get(i).getAsset().equals(subProduct)) {
				subInspections.set(i, null);
			}
		}
	}

	public void cleanSubInspectionsForNonValidSubProducts(Asset upToDateProduct) {
		List<SubInspection> subInspectionsToKeep = new ArrayList<SubInspection>();
		
		/*
		 * this checks that each sub inspection is for a asset that is still
		 * attached to our updated master asset.
		 */
		for (SubInspection subInspection : subInspections) {
			for (SubProduct subProduct: upToDateProduct.getSubProducts()) {
				if (subProduct.getAsset().equals(subInspection.getAsset())) {
					subInspectionsToKeep.add(subInspection);
					break;
				}
			}
		}
		subInspections.retainAll(subInspectionsToKeep);
	}

	public Map<SubInspection, List<FileAttachment>> getSubInspectionUploadedFiles() {
		return subInspectionUploadedFiles;
	}

	public void setSubInspectionUploadedFiles(Map<SubInspection, List<FileAttachment>> subInspectionUploadedFiles) {
		this.subInspectionUploadedFiles = subInspectionUploadedFiles;
	}

	public List<FileAttachment> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(List<FileAttachment> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}

	public List<SubInspection> getSubInspections() {
		return subInspections;
	}

	public InspectionSchedule getSchedule() {
		return schedule;
	}

	public void setSchedule(InspectionSchedule schedule) {
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
		if (inspection.isNew()) {
			if (assignToSomeone) {
				inspection.setAssignedTo(AssignedToUpdate.assignAssetToUser(assignedTo));
			} else {
				inspection.removeAssignTo();
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
