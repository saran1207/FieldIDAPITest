package com.n4systems.ejb.legacy.wrapper;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

import rfid.ejb.entity.FindAssetOptionManufacture;

import com.n4systems.ejb.legacy.Option;
import com.n4systems.ejb.legacy.impl.OptionManager;
import com.n4systems.ejb.wrapper.EJBTransactionEmulator;
import com.n4systems.model.TagOption;
import com.n4systems.model.TagOption.OptionKey;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;

public class OptionEJBContainer extends EJBTransactionEmulator<Option> implements Option {

	@Override
	protected Option createManager(EntityManager em) {
		return new OptionManager(em);
	}

	public TagOption findTagOption(Long id, SecurityFilter filter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findTagOption(id, filter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public TagOption findTagOption(OptionKey key, SecurityFilter filter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findTagOption(key, filter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public List<TagOption> findTagOptions(SecurityFilter filter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findTagOptions(filter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Collection<FindAssetOptionManufacture> getAllFindAssetOptionManufacture() {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getAllFindAssetOptionManufacture();

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	

	

	public FindAssetOptionManufacture getFindAssetOptionManufacture(Long uniqueID) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getFindAssetOptionManufacture(uniqueID);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	

	public Collection<FindAssetOptionManufacture> getFindAssetOptionsForTenant(Long tenantId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getFindAssetOptionsForTenant(tenantId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	

	public void updateFindAssetOptionManufacture(FindAssetOptionManufacture findAssetOptionManufacturer) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).updateFindAssetOptionManufacture(findAssetOptionManufacturer);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

}
