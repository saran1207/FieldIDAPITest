package com.n4systems.ejb.wrapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.ejb.impl.MassUpdateManagerImpl;
import com.n4systems.exceptions.UpdateConatraintViolationException;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Product;
import com.n4systems.model.Project;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.util.ReportCriteria;
import com.n4systems.util.SearchCriteria;

@SuppressWarnings("deprecation")
public class MassUpdateManagerEJBContainer extends EJBTransactionEmulator<MassUpdateManager> implements MassUpdateManager {

	protected MassUpdateManager createManager(EntityManager em) {
		return new MassUpdateManagerImpl(em);
	}

	public Long assignToJob(List<Long> scheduleIds, Project project, Long userId) throws UpdateFailureException, UpdateConatraintViolationException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).assignToJob(scheduleIds, project, userId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public List<Long> createSchedulesForInspections(List<Long> inspectionIds, Long userId) throws UpdateFailureException, UpdateConatraintViolationException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).createSchedulesForInspections(inspectionIds, userId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Long deleteInspectionSchedules(Set<Long> scheduleIds) throws UpdateFailureException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).deleteInspectionSchedules(scheduleIds);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Long modifyProudcts(List<Long> ids) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).modifyProudcts(ids);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Long updateInspections(List<Long> ids, Inspection inspection, Map<String, Boolean> values, Long userId) throws UpdateFailureException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).updateInspections(ids, inspection, values, userId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Long updateInspections(ReportCriteria reportCriteria, Inspection inspection, Map<String, Boolean> values, Long userId) throws UpdateFailureException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).updateInspections(reportCriteria, inspection, values, userId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Long updateInspectionSchedules(Set<Long> ids, InspectionSchedule inspectionSchedule, Map<String, Boolean> values) throws UpdateFailureException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).updateInspectionSchedules(ids, inspectionSchedule, values);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Long updateProducts(List<Long> ids, Product product, Map<String, Boolean> values, Long userId) throws UpdateFailureException, UpdateConatraintViolationException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).updateProducts(ids, product, values, userId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Long updateProducts(SearchCriteria searchCriteria, Product product, Map<String, Boolean> values, Long userId) throws UpdateFailureException, UpdateConatraintViolationException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).updateProducts(searchCriteria, product, values, userId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}
}
