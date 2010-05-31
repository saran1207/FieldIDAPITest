package com.n4systems.ejb.legacy.wrapper;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import rfid.ejb.entity.AddProductHistoryBean;
import rfid.ejb.entity.ProductSerialExtensionBean;
import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.ejb.legacy.impl.LegacyProductSerialManager;
import com.n4systems.ejb.wrapper.EJBTransactionEmulator;
import com.n4systems.exceptions.SubProductUniquenessException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.model.Inspection;
import com.n4systems.model.Product;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;

public class LegacyProductSerialEJBContainer extends EJBTransactionEmulator<LegacyProductSerial> implements LegacyProductSerial {

	protected LegacyProductSerial createManager(EntityManager em) {
		return new LegacyProductSerialManager(em);
	}

	public Long countAllInspections(Product product, SecurityFilter securityFilter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).countAllInspections(product, securityFilter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Long countAllLocalInspections(Product product, SecurityFilter securityFilter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).countAllLocalInspections(product, securityFilter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}



	public Product create(Product product, User modifiedBy) throws SubProductUniquenessException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).create(product, modifiedBy);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Long createProductStatus(ProductStatusBean productStatus) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).createProductStatus(productStatus);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Product createProductWithServiceTransaction(String transactionGUID, Product product, User modifiedBy) throws TransactionAlreadyProcessedException, SubProductUniquenessException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).createProductWithServiceTransaction(transactionGUID, product, modifiedBy);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Product createWithHistory(Product product, User modifiedBy) throws SubProductUniquenessException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).createWithHistory(product, modifiedBy);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public boolean duplicateSerialNumber(String serialNumber, Long uniqueID, Tenant tenant) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).duplicateSerialNumber(serialNumber, uniqueID, tenant);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	

	public Inspection findLastInspections(Product product, SecurityFilter securityFilter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findLastInspections(product, securityFilter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	

	public List<ProductStatusBean> findProductStatus(Long tenantId, Date beginDate) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findProductStatus(tenantId, beginDate);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public ProductStatusBean findProductStatus(Long uniqueID, Long tenantId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findProductStatus(uniqueID, tenantId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}


	
	public AddProductHistoryBean getAddProductHistory(Long rFieldidUser) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getAddProductHistory(rFieldidUser);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	@SuppressWarnings("deprecation")
	public List<ProductStatusBean> getAllProductStatus(Long tenantId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getAllProductStatus(tenantId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	@SuppressWarnings("deprecation")
	public Collection<ProductSerialExtensionBean> getProductSerialExtensions(Long tenantId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getProductSerialExtensions(tenantId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public void removeProductStatus(ProductStatusBean obj) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).removeProductStatus(obj);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public boolean rfidExists(String rfidNumber, Long tenantId, Long uniqueID) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).rfidExists(rfidNumber, tenantId, uniqueID);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public boolean rfidExists(String rfidNumber, Long tenantId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).rfidExists(rfidNumber, tenantId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Product update(Product productSerial, User modifiedBy) throws SubProductUniquenessException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).update(productSerial, modifiedBy);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}



	public Long updateProductStatus(ProductStatusBean productStatus) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).updateProductStatus(productStatus);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}
}
