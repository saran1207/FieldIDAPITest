package com.n4systems.ejb.wrapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.ejb.impl.MassUpdateManagerImpl;
import com.n4systems.exceptions.UpdateConatraintViolationException;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.Project;
import com.n4systems.model.user.User;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;

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

	public List<Long> createSchedulesForEvents(List<Long> eventIds, Long userId) throws UpdateFailureException, UpdateConatraintViolationException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).createSchedulesForEvents(eventIds, userId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Long deleteEventSchedules(Set<Long> scheduleIds) throws UpdateFailureException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).deleteEventSchedules(scheduleIds);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Long updateAssetModifiedDate(List<Long> ids) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).updateAssetModifiedDate(ids);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Long updateEvents(List<Long> ids, Event event, Map<String, Boolean> values, Long userId) throws UpdateFailureException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).updateEvents(ids, event, values, userId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	

	public Long updateEventSchedules(Set<Long> ids, EventSchedule eventSchedule, Map<String, Boolean> values) throws UpdateFailureException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).updateEventSchedules(ids, eventSchedule, values);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Long updateAssets(List<Long> ids, Asset asset, Map<String, Boolean> values, User modifiedBy) throws UpdateFailureException, UpdateConatraintViolationException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).updateAssets(ids, asset, values, modifiedBy);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	@Override
	public Long deleteAssets(List<Long> ids, User modifiedBy) throws UpdateFailureException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).deleteAssets(ids, modifiedBy);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

}
