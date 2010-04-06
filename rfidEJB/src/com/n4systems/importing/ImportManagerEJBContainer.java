package com.n4systems.importing;

import java.io.File;

import javax.persistence.EntityManager;

import com.n4systems.ejb.wrapper.EJBTransactionEmulator;
import com.n4systems.exceptions.FileImportException;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;

public class ImportManagerEJBContainer extends EJBTransactionEmulator<ImportManager> implements ImportManager {
	@Override
	protected ImportManager createManager(EntityManager em) {
		return new ImportManagerImpl(em);
	}

	public long importObservations(Long tenantId, File observationsFile) throws FileImportException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).importObservations(tenantId, observationsFile);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}
}
