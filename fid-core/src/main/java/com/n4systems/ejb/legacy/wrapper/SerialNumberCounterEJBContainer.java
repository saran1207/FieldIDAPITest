package com.n4systems.ejb.legacy.wrapper;

import java.util.Collection;

import javax.persistence.EntityManager;

import com.n4systems.model.AssetType;
import rfid.ejb.entity.SerialNumberCounterBean;

import com.n4systems.ejb.legacy.SerialNumberCounter;
import com.n4systems.ejb.legacy.impl.SerialNumberCounterManager;
import com.n4systems.ejb.wrapper.EJBTransactionEmulator;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;

public class SerialNumberCounterEJBContainer extends EJBTransactionEmulator<SerialNumberCounter> implements SerialNumberCounter {

	@Override
	protected SerialNumberCounter createManager(EntityManager em) {
		return new SerialNumberCounterManager(em);
	}

	public String generateSerialNumber(PrimaryOrg primaryOrg, AssetType assetType) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).generateSerialNumber(primaryOrg, assetType);

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

	public SerialNumberCounterBean getSerialNumberCounter(Long tenantId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getSerialNumberCounter(tenantId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Collection<SerialNumberCounterBean> getSerialNumberCounters() {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getSerialNumberCounters();

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}


	public void updateSerialNumberCounter(SerialNumberCounterBean serialNumberCounter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).updateSerialNumberCounter(serialNumberCounter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

}
