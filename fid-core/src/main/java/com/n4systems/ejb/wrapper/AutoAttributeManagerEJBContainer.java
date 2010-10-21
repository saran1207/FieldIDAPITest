package com.n4systems.ejb.wrapper;

import java.util.Collection;

import javax.persistence.EntityManager;

import com.n4systems.model.AssetType;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.ejb.AutoAttributeManager;
import com.n4systems.ejb.impl.AutoAttributeManagerImpl;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.Tenant;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.tools.Pager;

public class AutoAttributeManagerEJBContainer extends EJBTransactionEmulator<AutoAttributeManager> implements AutoAttributeManager {

	protected AutoAttributeManager createManager(EntityManager em) {
		return new AutoAttributeManagerImpl(em);
	}

	public void clearRetiredInfoFields(AssetType assetType) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).clearRetiredInfoFields(assetType);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public void delete(AutoAttributeCriteria criteria) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).delete(criteria);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Pager<AutoAttributeDefinition> findAllPage(AutoAttributeCriteria criteria, Tenant tenant, Integer pageNumber, Integer pageSize) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findAllPage(criteria, tenant, pageNumber, pageSize);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public AutoAttributeDefinition findTemplateToApply(AutoAttributeCriteria criteria, Collection<InfoOptionBean> selectedInfoOptions) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findTemplateToApply(criteria, selectedInfoOptions);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public AutoAttributeDefinition findTemplateToApply(AssetType assetType, Collection<InfoOptionBean> selectedInfoOptions) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findTemplateToApply(assetType, selectedInfoOptions);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public void removeDefinition(AutoAttributeDefinition definition) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).removeDefinition(definition);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public AutoAttributeDefinition saveDefinition(AutoAttributeDefinition definition) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).saveDefinition(definition);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public AutoAttributeCriteria update(AutoAttributeCriteria criteria) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).update(criteria);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}
}
