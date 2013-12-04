package com.n4systems.ejb.wrapper;

import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.ejb.impl.MassUpdateManagerImpl;
import com.n4systems.exceptions.UpdateConatraintViolationException;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.Project;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.user.User;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

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

    @Override
    public Long closeEvents(List<Long> ids, ThingEvent eventChanges, User modifiedBy) throws UpdateFailureException {
        TransactionManager transactionManager = new FieldIdTransactionManager();
        Transaction transaction = transactionManager.startTransaction();
        try {
            return createManager(transaction.getEntityManager()).closeEvents(ids, eventChanges, modifiedBy);

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

	public Long updateEvents(List<Long> ids, ThingEvent event, Map<String, Boolean> values, Long userId) throws UpdateFailureException {
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

	public Long updateAssets(List<Long> ids, Asset asset, Map<String, Boolean> values, User modifiedBy, String orderNumber) throws UpdateFailureException, UpdateConatraintViolationException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).updateAssets(ids, asset, values, modifiedBy, orderNumber);

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
