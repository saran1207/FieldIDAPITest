package com.n4systems.ejb;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.exceptions.UnknownSubProduct;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionGroup;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.SubInspection;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.tools.Pager;
import com.n4systems.webservice.dto.WSJobSearchCriteria;
import com.n4systems.webservice.dto.WSSearchCritiera;

@Local
public interface InspectionManager {


	public List<InspectionGroup> findAllInspectionGroups(SecurityFilter filter, Long productId, String... postFetchFields);

	public InspectionGroup findInspectionGroupByMobileGuid(String mobileGuid, SecurityFilter filter);

	public SubInspection findSubInspection(Long subInspectionId, SecurityFilter filter);

	public Inspection findInspectionThroughSubInspection(Long subInspectionId, SecurityFilter filter);

	public Inspection findAllFields(Long id, SecurityFilter filter);

	public Date findLastInspectionDate(Product product);

	public Date findLastInspectionDate(InspectionSchedule schedule);

	public Date findLastInspectionDate(Long scheduleId);

	public Date findLastInspectionDate(Product product, InspectionType inspectionType);

	public List<Inspection> findInspectionsByDateAndProduct(Date inspectionDateRangeStart, Date inspectionDateRangeEnd, Product product, SecurityFilter filter);

	public FileDataContainer createFileDataContainer(Inspection inspection, File proofTestFile) throws ProcessingProofTestException;

	public List<Inspection> createInspections(String transactionGUID, List<Inspection> inspections, Map<Inspection, ProductStatusBean> productStatus, Map<Inspection, Date> nextInspectionDates)
			throws ProcessingProofTestException, FileAttachmentException, TransactionAlreadyProcessedException, UnknownSubProduct;

	public Inspection createInspection(Inspection inspection, ProductStatusBean productStatus, Date nextInspectionDate, Long userId) throws ProcessingProofTestException, FileAttachmentException,
			UnknownSubProduct;

	public Inspection createInspection(Inspection inspection, ProductStatusBean productStatus, Date nextInspectionDate, Long userId, File proofTestFile, List<FileAttachment> uploadedFiles)
			throws ProcessingProofTestException, FileAttachmentException, UnknownSubProduct;

	public Inspection createInspection(Inspection inspection, ProductStatusBean productStatus, Date nextInspectionDate, Long userId, FileDataContainer fileData, List<FileAttachment> uploadedFiles)
			throws ProcessingProofTestException, FileAttachmentException, UnknownSubProduct;

	public Inspection updateInspection(Inspection inspection, Long userId) throws ProcessingProofTestException, FileAttachmentException;

	public Inspection updateInspection(Inspection inspection, Long userId, File proofTestFile, List<FileAttachment> uploadedFiles) throws ProcessingProofTestException, FileAttachmentException;

	public Inspection updateInspection(Inspection inspection, Long userId, FileDataContainer fileData, List<FileAttachment> uploadedFiles) throws ProcessingProofTestException, FileAttachmentException;

	public Inspection attachFilesToSubInspection(Inspection inspection, SubInspection subInspection, List<FileAttachment> uploadedFiles) throws FileAttachmentException;

	public Inspection retireInspection(Inspection inspection, Long userId);

	public InspectionType findInspectionTypeByLegacyEventId(Long eventId, Long tenantId);

	public InspectionType updateInspectionForm(InspectionType inspectionType, Long modifyingUserId);

	public Pager<Inspection> findNewestInspections(WSSearchCritiera searchCriteria, SecurityFilter securityFilter, int page, int pageSize);
	
	public Pager<Inspection> findNewestInspections(WSJobSearchCriteria searchCriteria, SecurityFilter securityFilter, int page, int pageSize);

	public boolean isMasterInspection(Long id);

}
