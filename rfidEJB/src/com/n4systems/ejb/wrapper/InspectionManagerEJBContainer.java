package com.n4systems.ejb.wrapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.impl.CreateInspectionParameter;
import com.n4systems.ejb.impl.InspectionManagerImpl;
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
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.security.AuditLogger;
import com.n4systems.security.CreateInspectionAuditHandler;
import com.n4systems.security.UpdateInspectionAuditHandler;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.tools.Pager;
import com.n4systems.webservice.dto.WSJobSearchCriteria;
import com.n4systems.webservice.dto.WSSearchCritiera;

public class InspectionManagerEJBContainer extends EJBTransactionEmulator<InspectionManager> implements InspectionManager {

	protected InspectionManager createManager(EntityManager em) {
		return new InspectionManagerImpl(em);
	}

	public Inspection attachFilesToSubInspection(Inspection inspection, SubInspection subInspection, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).attachFilesToSubInspection(inspection, subInspection, uploadedFiles);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	
	public Inspection createInspection(CreateInspectionParameter parameterObject) throws ProcessingProofTestException, FileAttachmentException, UnknownSubProduct {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		AuditLogger auditLogger = new AuditLogger(new CreateInspectionAuditHandler());

		Throwable t = null;

		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).createInspection(parameterObject);

		} catch (RuntimeException re) {
			transactionManager.rollbackTransaction(transaction);
			t = re;
			throw re;
		} finally {
			transactionManager.finishTransaction(transaction);
			auditLogger.audit("createInspection", parameterObject.inspection, t);
		}
	}
	

	public List<Inspection> createInspections(String transactionGUID, List<Inspection> inspections, Map<Inspection, Date> nextInspectionDates) throws ProcessingProofTestException,
			FileAttachmentException, TransactionAlreadyProcessedException, UnknownSubProduct {
		AuditLogger auditLogger = new AuditLogger(new CreateInspectionAuditHandler());

		Throwable t = null;
		TransactionManager transactionManager = new FieldIdTransactionManager();
		
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).createInspections(transactionGUID, inspections, nextInspectionDates);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);
			t = e;
			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
			for (Inspection inspection : inspections) {
				auditLogger.audit("createInspections", inspection, t);
			}
		}
	}

	public Inspection findAllFields(Long id, SecurityFilter filter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findAllFields(id, filter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public List<InspectionGroup> findAllInspectionGroups(SecurityFilter filter, Long productId, String... postFetchFields) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findAllInspectionGroups(filter, productId, postFetchFields);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public InspectionGroup findInspectionGroupByMobileGuid(String mobileGuid, SecurityFilter filter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findInspectionGroupByMobileGuid(mobileGuid, filter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public List<Inspection> findInspectionsByDateAndProduct(Date inspectionDateRangeStart, Date inspectionDateRangeEnd, Product product, SecurityFilter filter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findInspectionsByDateAndProduct(inspectionDateRangeStart, inspectionDateRangeEnd, product, filter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Inspection findInspectionThroughSubInspection(Long subInspectionId, SecurityFilter filter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findInspectionThroughSubInspection(subInspectionId, filter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public InspectionType findInspectionTypeByLegacyEventId(Long eventId, Long tenantId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findInspectionTypeByLegacyEventId(eventId, tenantId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Date findLastInspectionDate(InspectionSchedule schedule) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findLastInspectionDate(schedule);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Date findLastInspectionDate(Long scheduleId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findLastInspectionDate(scheduleId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Date findLastInspectionDate(Product product, InspectionType inspectionType) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findLastInspectionDate(product, inspectionType);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Date findLastInspectionDate(Product product) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findLastInspectionDate(product);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Pager<Inspection> findNewestInspections(WSJobSearchCriteria searchCriteria, SecurityFilter securityFilter, int page, int pageSize) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findNewestInspections(searchCriteria, securityFilter, page, pageSize);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Pager<Inspection> findNewestInspections(WSSearchCritiera searchCriteria, SecurityFilter securityFilter, int page, int pageSize) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findNewestInspections(searchCriteria, securityFilter, page, pageSize);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public SubInspection findSubInspection(Long subInspectionId, SecurityFilter filter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findSubInspection(subInspectionId, filter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public boolean isMasterInspection(Long id) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).isMasterInspection(id);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Inspection retireInspection(Inspection inspection, Long userId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).retireInspection(inspection, userId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Inspection updateInspection(Inspection inspection, Long userId, FileDataContainer fileData, List<FileAttachment> uploadedFiles) throws ProcessingProofTestException, FileAttachmentException {
		AuditLogger auditLogger = new AuditLogger(new UpdateInspectionAuditHandler());

		Throwable t = null;
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).updateInspection(inspection, userId, fileData, uploadedFiles);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);
			t = e;
			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
			auditLogger.audit("updateInspection", inspection, t);
		}
	}


	public InspectionType updateInspectionForm(InspectionType inspectionType, Long modifyingUserId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).updateInspectionForm(inspectionType, modifyingUserId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}
}
