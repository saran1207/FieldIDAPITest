package com.n4systems.ejb.legacy.wrapper;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

import rfid.ejb.entity.FindProductOptionBean;
import rfid.ejb.entity.FindProductOptionManufactureBean;

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

	public Collection<FindProductOptionManufactureBean> getAllFindProductOptionManufacture() {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getAllFindProductOptionManufacture();

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Collection<FindProductOptionBean> getAllFindProductOptions() {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getAllFindProductOptions();

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public FindProductOptionBean getFindProductOption(Long uniqueID) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getFindProductOption(uniqueID);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public FindProductOptionBean getFindProductOption(String key) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getFindProductOption(key);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public FindProductOptionManufactureBean getFindProductOptionManufacture(Long uniqueID) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getFindProductOptionManufacture(uniqueID);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	@SuppressWarnings("deprecation")
	public Collection<FindProductOptionManufactureBean> getFindProductOptionsForManufacture(Long tenantId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getFindProductOptionsForManufacture(tenantId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Collection<FindProductOptionManufactureBean> getFindProductOptionsForTenant(Long tenantId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getFindProductOptionsForTenant(tenantId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Long persistFindProductOptionManufacture(FindProductOptionManufactureBean findProductOptionManufacturer) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).persistFindProductOptionManufacture(findProductOptionManufacturer);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public void updateFindProductOptionManufacture(FindProductOptionManufactureBean findProductOptionManufacturer) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).updateFindProductOptionManufacture(findProductOptionManufacturer);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;

		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

}
