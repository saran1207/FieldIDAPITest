package com.n4systems.ejb.wrapper;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.impl.EventScheduleManagerImpl;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.Asset;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;

public class EventScheduleManagerEJBContainer extends EJBTransactionEmulator<EventScheduleManager> implements EventScheduleManager {

	protected EventScheduleManager createManager(EntityManager em) {
		return new EventScheduleManagerImpl(em);
	}

	public List<EventSchedule> autoSchedule(Asset asset) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).autoSchedule(asset);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public List<EventSchedule> getAvailableSchedulesFor(Asset asset) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getAvailableSchedulesFor(asset);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Long getEventIdForSchedule(Long scheduleId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getEventIdForSchedule(scheduleId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	


	public Long getEventTypeIdForSchedule(Long scheduleId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getEventTypeIdForSchedule(scheduleId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Long getAssetIdForSchedule(Long scheduleId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getAssetIdForSchedule(scheduleId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	

	

	public void removeAllSchedulesFor(Asset asset) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).removeAllSchedulesFor(asset);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	

	public void restoreScheduleForEvent(Event event) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			createManager(transaction.getEntityManager()).restoreScheduleForEvent(event);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public boolean schedulePastDue(Long scheduleId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).schedulePastDue(scheduleId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public EventSchedule update(EventSchedule schedule) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).update(schedule);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public List<EventSchedule> getAutoEventSchedules(Asset asset) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
				try {
					return createManager(transaction.getEntityManager()).getAutoEventSchedules(asset);

				} catch (RuntimeException e) {
					transactionManager.rollbackTransaction(transaction);

					throw e;
				} finally {
					transactionManager.finishTransaction(transaction);
				}
	}

}
