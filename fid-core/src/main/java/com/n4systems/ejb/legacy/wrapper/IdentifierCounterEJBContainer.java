package com.n4systems.ejb.legacy.wrapper;

import java.util.Collection;

import javax.persistence.EntityManager;

import com.n4systems.ejb.legacy.IdentifierCounter;
import com.n4systems.ejb.legacy.impl.IdentifierCounterManager;
import com.n4systems.model.AssetType;
import rfid.ejb.entity.IdentifierCounterBean;

import com.n4systems.ejb.wrapper.EJBTransactionEmulator;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;

public class IdentifierCounterEJBContainer extends EJBTransactionEmulator<IdentifierCounter> implements IdentifierCounter {

	@Override
	protected IdentifierCounter createManager(EntityManager em) {
		return new IdentifierCounterManager(em);
	}

	public String generateIdentifier(PrimaryOrg primaryOrg, AssetType assetType) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).generateIdentifier(primaryOrg, assetType);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public String getNextCounterValue(Long tenantId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getNextCounterValue(tenantId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public IdentifierCounterBean getIdentifierCounter(Long tenantId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getIdentifierCounter(tenantId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Collection<IdentifierCounterBean> getIdentifierCounters() {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getIdentifierCounters();

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}


	public void updateIdentifierCounter(IdentifierCounterBean identifierCounter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).updateIdentifierCounter(identifierCounter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

}
