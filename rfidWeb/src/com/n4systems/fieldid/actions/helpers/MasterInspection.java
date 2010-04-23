package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.fieldid.utils.ListHelper;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.SubInspection;
import com.n4systems.model.SubProduct;
import com.n4systems.tools.FileDataContainer;

public class MasterInspection {
	private String token;

	private Product masterProduct;

	private Inspection inspection;

	private FileDataContainer proofTestFile;

	private ProductStatusBean productStatus;

	private List<SubInspection> subInspections = new ArrayList<SubInspection>();

	private Date nextDate;

	private Long currentId = -1L;

	private Long inspectionGroupId;

	private Map<SubInspection, List<FileAttachment>> subInspectionUploadedFiles;

	private List<FileAttachment> uploadedFiles;

	private InspectionSchedule schedule;
	private Long scheduleId;

	private InspectionType nextInspectionType;

	public MasterInspection() {
		token = String.valueOf(Math.abs(new Random().nextLong()));
		subInspectionUploadedFiles = new HashMap<SubInspection, List<FileAttachment>>();
	}

	public MasterInspection(Inspection inspection) {
		this();

		currentId = inspection.getId();
		inspectionGroupId = inspection.getGroup().getId();
		masterProduct = inspection.getProduct();
		subInspections = new ArrayList<SubInspection>(inspection.getSubInspections());
		this.inspection = inspection;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Product getMasterProduct() {
		return masterProduct;
	}

	public void setMasterProduct(Product masterProduct) {
		this.masterProduct = masterProduct;
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

	public ProductStatusBean getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(ProductStatusBean productStatus) {
		this.productStatus = productStatus;
	}

	public Date getNextDate() {
		return nextDate;
	}

	public void setNextDate(Date nextDate) {
		this.nextDate = nextDate;
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
		inspection.setProduct(subInspection.getProduct());
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
		subInspection.setProduct(inspection.getProduct());
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
		Product product = inspection.getProduct();
		if (product == null) {
			return null;
		}
		if (this.inspection.isNew()) {

			for (SubProduct subProduct : this.masterProduct.getSubProducts()) {
				if (subProduct.getProduct().equals(product)) {
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
		inspection.getSubInspections().clear();
		ListHelper.clearNulls(subInspections);
		for (SubInspection subInspection : subInspections) {
			SubInspection s = createSubInspectionFromInspection(createInspectionFromSubInspection(subInspection));
			s.setId((subInspection.getId() < 0) ? null : subInspection.getId());
			s.setResults(subInspection.getResults());
			processResults(s);
			s.setFormVersion(subInspection.getFormVersion());
			inspection.getSubInspections().add(s);

		}

		return inspection;
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

	public List<SubInspection> getSubInspectionFor(Product product) {
		List<SubInspection> inspectionTypes = new ArrayList<SubInspection>();
		for (SubInspection subInspection : subInspections) {
			if (subInspection.getProduct().getId().equals(product.getId())) {
				inspectionTypes.add(subInspection);
			}
		}
		return inspectionTypes;
	}

	public void removeInspectionsForProduct(Product subProduct) {
		for (int i = 0; i < subInspections.size(); i++) {
			if (subInspections.get(i).getProduct().equals(subProduct)) {
				subInspections.set(i, null);
			}
		}
	}

	public void cleanSubInspectionsForNonValidSubProducts(Product upToDateProduct) {
		List<SubInspection> subInspectionsToKeep = new ArrayList<SubInspection>();
		
		/*
		 * this checks that each sub inspection is for a product that is still
		 * attached to our updated master product.   
		 */
		for (SubInspection subInspection : subInspections) {
			for (SubProduct subProduct: upToDateProduct.getSubProducts()) {
				if (subProduct.getProduct().equals(subInspection.getProduct())) {
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

	public void setNextInspectionType(InspectionType nextInspectionType) {
		this.nextInspectionType = nextInspectionType;
		
	}

	public InspectionType getNextInspectionType() {
		return nextInspectionType;
	}
	
}
