package com.n4systems.ejb.legacy.wrapper;

import java.util.List;

import javax.persistence.EntityManager;

import rfid.ejb.entity.ProductCodeMappingBean;

import com.n4systems.ejb.legacy.ProductCodeMapping;
import com.n4systems.ejb.legacy.impl.ProductCodeMappingManager;
import com.n4systems.ejb.wrapper.EJBTransactionEmulator;
import com.n4systems.model.ProductType;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;

public class ProductCodeMappingEJBContainer extends EJBTransactionEmulator<ProductCodeMapping> implements ProductCodeMapping {

	protected ProductCodeMapping createManager(EntityManager em) {
		return new ProductCodeMappingManager(em);
	}

	public void clearRetiredInfoFields(ProductType productType) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).clearRetiredInfoFields(productType);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public void deleteById(Long uniqueID) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).deleteById(uniqueID);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public void deleteByIdAndTenant(Long id, Long manufacturer) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).deleteByIdAndTenant(id, manufacturer);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public List<ProductCodeMappingBean> getAllProductCodes() {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getAllProductCodes();

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public List<ProductCodeMappingBean> getAllProductCodesByTenant(Long manufacturer) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getAllProductCodesByTenant(manufacturer);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public ProductCodeMappingBean getProductCodeByProductCodeAndTenant(String productCode, Long manufacturer) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getProductCodeByProductCodeAndTenant(productCode, manufacturer);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public ProductCodeMappingBean getProductCodeByUniqueId(Long id) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getProductCodeByUniqueId(id);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public ProductCodeMappingBean getProductCodeByUniqueIdAndTenant(Long id, Long manufacturer) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getProductCodeByUniqueIdAndTenant(id, manufacturer);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public void save(ProductCodeMappingBean bean) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).save(bean);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public void update(ProductCodeMappingBean productCodeMapping) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).update(productCodeMapping);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}
}
