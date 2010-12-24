package com.n4systems.ejb.wrapper;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.n4systems.ejb.OrderManager;
import com.n4systems.ejb.impl.OrderManagerImpl;
import com.n4systems.exceptions.OrderProcessingException;
import com.n4systems.model.LineItem;
import com.n4systems.model.Order;
import com.n4systems.model.OrderKey;
import com.n4systems.model.Tenant;
import com.n4systems.model.Order.OrderType;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.plugins.integration.OrderResolver;

public class OrderManagerEJBContainer extends EJBTransactionEmulator<OrderManager> implements OrderManager {

	protected OrderManager createManager(EntityManager em) {
		return new OrderManagerImpl(em);
	}

	public int countLineItems(Order order) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).countLineItems(order);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public int countAssetsTagged(LineItem lineItem) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).countAssetsTagged(lineItem);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public LineItem createNonIntegrationShopOrder(String orderNumber, Long tenantId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).createNonIntegrationShopOrder(orderNumber, tenantId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public LineItem findLineItem(Order order, String lineId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findLineItem(order, lineId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public List<LineItem> findLineItems(Order order) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findLineItems(order);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Order findOrder(OrderType type, String orderNumber, Long tenantId, SecurityFilter filter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findOrder(type, orderNumber, tenantId, filter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public LineItem processLineItem(Order order, String sourceId, Map<String, Object> rawLineItemData, Map<String, OrderKey> keyMappings) throws OrderProcessingException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).processLineItem(order, sourceId, rawLineItemData, keyMappings);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public LineItem processLineItem(Order order, String sourceId, Map<String, Object> rawLineItemData) throws OrderProcessingException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).processLineItem(order, sourceId, rawLineItemData);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Order processOrder(Tenant tenant, String sourceId, Map<String, Object> rawOrderData, Map<String, OrderKey> keyMappings) throws OrderProcessingException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).processOrder(tenant, sourceId, rawOrderData, keyMappings);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Order processOrder(Tenant tenant, String sourceId, Map<String, Object> rawOrderData) throws OrderProcessingException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).processOrder(tenant, sourceId, rawOrderData);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Order processOrderFromPlugin(OrderResolver resolver, String orderNumber, OrderType type, Long tenantId) throws OrderProcessingException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).processOrderFromPlugin(resolver, orderNumber, type, tenantId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public List<Order> processOrders(Map<String, Object> unmappedOrders) throws OrderProcessingException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).processOrders(unmappedOrders);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public LineItem findLineItemById(Long lineItemId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
				try {
					return createManager(transaction.getEntityManager()).findLineItemById(lineItemId);

				} catch (RuntimeException e) {
					transactionManager.rollbackTransaction(transaction);

					throw e;
				} finally {
					transactionManager.finishTransaction(transaction);
				}
	}
}
