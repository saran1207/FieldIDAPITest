package com.n4systems.ejb.wrapper;

import java.io.File;
import java.util.Map;

import javax.persistence.EntityManager;


import com.n4systems.ejb.ProofTestHandler;
import com.n4systems.ejb.impl.ProofTestHandlerImpl;
import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.Event;
import com.n4systems.model.user.User;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.tools.FileDataContainer;

public class ProofTestHandlerEJBContainer extends EJBTransactionEmulator<ProofTestHandler> implements ProofTestHandler {

	protected ProofTestHandler createManager(EntityManager em) {
		return new ProofTestHandlerImpl(em);
	}


	public Map<String, Event> eventServiceUpload(FileDataContainer fileData, User performedBy) throws FileProcessingException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).eventServiceUpload(fileData, performedBy);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Map<String, Event> multiProofTestUpload(File proofTestFile, ProofTestType type, Long tenantId, Long userId, Long ownerId, Long eventBookId) throws FileProcessingException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).multiProofTestUpload(proofTestFile, type, tenantId, userId, ownerId, eventBookId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}
}
