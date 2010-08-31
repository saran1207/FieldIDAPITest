package com.n4systems.ejb.legacy.wrapper;

import javax.persistence.EntityManager;

import com.n4systems.ejb.legacy.UnitOfMeasureManager;
import com.n4systems.ejb.legacy.impl.UnitOfMeasureManagerImpl;
import com.n4systems.ejb.wrapper.EJBTransactionEmulator;
import com.n4systems.model.UnitOfMeasure;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;

public class UnitOfMeasureManagerEJBContainer extends EJBTransactionEmulator<UnitOfMeasureManager> implements UnitOfMeasureManager {

	protected UnitOfMeasureManager createManager(EntityManager em) {
		return new UnitOfMeasureManagerImpl(em);
	}

	public UnitOfMeasure getUnitOfMeasureForInfoField(Long infoFieldId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getUnitOfMeasureForInfoField(infoFieldId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}
}
