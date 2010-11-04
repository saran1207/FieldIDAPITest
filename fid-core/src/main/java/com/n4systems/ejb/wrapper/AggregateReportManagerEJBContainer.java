package com.n4systems.ejb.wrapper;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.ejb.AggregateReportManager;
import com.n4systems.ejb.impl.AggregateReportManagerImpl;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.util.AggregateReport;

public class AggregateReportManagerEJBContainer extends EJBTransactionEmulator<AggregateReportManager> implements AggregateReportManager {

	protected AggregateReportManager createManager(EntityManager em) {
		return new AggregateReportManagerImpl(em);
	}

	public AggregateReport createAggregateReport(List<Long> eventIds) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).createAggregateReport(eventIds);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}
}
