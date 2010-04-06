package com.n4systems.ejb.legacy.wrapper;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import rfid.ejb.entity.OrderMappingBean;

import com.n4systems.ejb.legacy.OrderMapping;
import com.n4systems.ejb.legacy.impl.OrderMappingManager;
import com.n4systems.ejb.wrapper.EJBTransactionEmulator;
import com.n4systems.model.OrderKey;
import com.n4systems.model.Tenant;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;

public class OrderMappingEJBContainer extends EJBTransactionEmulator<OrderMapping> implements OrderMapping {

	@Override
	protected OrderMapping createManager(EntityManager em) {
		return new OrderMappingManager(em);
	}

	public void delete(Long uniqueID) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).delete(uniqueID);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Map<String, OrderKey> getKeyMappings(Tenant tenant, String externalSourceID) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getKeyMappings(tenant, externalSourceID);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public OrderMappingBean getOrderMapping(Long uniqueID) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getOrderMapping(uniqueID);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public List<OrderMappingBean> getOrganizationMappings() {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getOrganizationMappings();

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public void importXmlOrderMappings(String Xml) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).importXmlOrderMappings(Xml);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public void save(OrderMappingBean orderMapping) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).save(orderMapping);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public void update(OrderMappingBean orderMapping) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).update(orderMapping);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);
			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

}
