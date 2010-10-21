package com.n4systems.ejb.legacy.wrapper;

import java.io.File;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

import rfid.ejb.entity.InfoFieldBean;

import com.n4systems.ejb.legacy.LegacyProductType;
import com.n4systems.ejb.legacy.impl.LegacyProductTypeManager;
import com.n4systems.ejb.wrapper.EJBTransactionEmulator;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ImageAttachmentException;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.AssetType;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.util.ListingPair;

public class LegacyProductTypeEJBContainer extends EJBTransactionEmulator<LegacyProductType> implements LegacyProductType {

	protected LegacyProductType createManager(EntityManager em) {
		return new LegacyProductTypeManager(em);
	}

	public boolean cleanInfoOptions(int pageNumber, int pageSize) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).cleanInfoOptions(pageNumber, pageSize);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public AssetType findDefaultProductType(Long tenantId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findDefaultProductType(tenantId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}


	public AssetType findProductTypeForProduct(Long productId) throws InvalidQueryException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findProductTypeForProduct(productId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public List<ListingPair> getProductTypeListForTenant(Long tenantId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getProductTypeListForTenant(tenantId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public List<AssetType> getProductTypesForTenant(Long tenantId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getProductTypesForTenant(tenantId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Collection<Long> infoFieldsInUse(Collection<InfoFieldBean> infoFields) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).infoFieldsInUse(infoFields);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}


	

	public AssetType updateProductType(AssetType assetType, List<FileAttachment> uploadedFiles, File productImage) throws FileAttachmentException, ImageAttachmentException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).updateProductType(assetType, uploadedFiles, productImage);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public AssetType updateProductType(AssetType assetType) throws FileAttachmentException, ImageAttachmentException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).updateProductType(assetType);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}
}
