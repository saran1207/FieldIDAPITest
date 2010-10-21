package com.n4systems.ejb.legacy.wrapper;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.ejb.legacy.AssetCodeMappingService;
import rfid.ejb.entity.AssetCodeMapping;

import com.n4systems.ejb.legacy.impl.ProductCodeMappingManager;
import com.n4systems.ejb.wrapper.EJBTransactionEmulator;
import com.n4systems.model.AssetType;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;

public class ProductCodeMappingEJBContainer extends EJBTransactionEmulator<AssetCodeMappingService> implements AssetCodeMappingService {

	protected AssetCodeMappingService createManager(EntityManager em) {
		return new ProductCodeMappingManager(em);
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

	

	public List<AssetCodeMapping> getAllProductCodesByTenant(Long manufacturer) {
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

	public AssetCodeMapping getProductCodeByProductCodeAndTenant(String productCode, Long manufacturer) {
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

	
	public AssetCodeMapping getProductCodeByUniqueIdAndTenant(Long id, Long manufacturer) {
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

	public void update(AssetCodeMapping assetCodeMapping) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).update(assetCodeMapping);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}
}
