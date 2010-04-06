package com.n4systems.ejb.legacy.wrapper;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import rfid.ejb.PopulatorCriteria;
import rfid.ejb.entity.PopulatorLogBean;

import com.n4systems.ejb.legacy.PopulatorLog;
import com.n4systems.ejb.legacy.impl.PopulatorLogManager;
import com.n4systems.ejb.wrapper.EJBTransactionEmulator;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.tools.Pager;

public class PopulatorLogEJBContainer extends EJBTransactionEmulator<PopulatorLog> implements PopulatorLog {

	protected PopulatorLog createManager(EntityManager em) {
		return new PopulatorLogManager(em);
	}

	public Long createPopulatorLog(PopulatorLogBean populatorLogBean) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).createPopulatorLog(populatorLogBean);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public ArrayList<String> findAllLogStatuses(long tenantId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findAllLogStatuses(tenantId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public ArrayList<String> findAllLogTypes(long tenantId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findAllLogTypes(tenantId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public List<PopulatorLogBean> findAllPopulatorLog() {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findAllPopulatorLog();

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Pager<PopulatorLogBean> findPopulatorLogBySearch(Long tenantId, PopulatorCriteria criteria, int pageNumber, int pageSize) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findPopulatorLogBySearch(tenantId, criteria, pageNumber, pageSize);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public void removeAllLogs() {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).removeAllLogs();

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	@SuppressWarnings("deprecation")
	public void removeAllMessages() {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).removeAllMessages();

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public void removePopulatorLog(Long uniqueID) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).removePopulatorLog(uniqueID);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}
}
