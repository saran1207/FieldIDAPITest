package com.n4systems.ejb.wrapper;

import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.impl.EventScheduleManagerImpl;
import com.n4systems.model.Asset;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.ThingEventType;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;

import javax.persistence.EntityManager;
import java.util.List;

public class EventScheduleManagerEJBContainer extends EJBTransactionEmulator<EventScheduleManager> implements EventScheduleManager {

	@Override
	protected EventScheduleManager createManager(EntityManager em) {
		return new EventScheduleManagerImpl(em);
	}

	@Override
	public List<ThingEvent> autoSchedule(Asset asset) {
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

	@Override
	public List<ThingEvent> getAvailableSchedulesFor(Asset asset, String... postFetchFields) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getAvailableSchedulesFor(asset, postFetchFields);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	@Override
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

	


	@Override
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

	@Override
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

	

	

	@Override
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



    @Override
	public void restoreScheduleForEvent(ThingEvent event) {
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

	@Override
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

	@Override
	public ThingEvent update(ThingEvent event) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).update(event);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	@Override
	public List<ThingEvent> getAutoEventSchedules(Asset asset) {
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

	@Override
	public List<ThingEvent> getAvailableSchedulesForAssetFilteredByEventType(Asset asset, ThingEventType eventType) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getAvailableSchedulesForAssetFilteredByEventType(asset, eventType);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

    @Override
    public ThingEvent reattach(ThingEvent event) {
        TransactionManager transactionManager = new FieldIdTransactionManager();
        Transaction transaction = transactionManager.startTransaction();
        try {
            return createManager(transaction.getEntityManager()).reattach(event);

        } catch (RuntimeException e) {
            transactionManager.rollbackTransaction(transaction);

            throw e;
        } finally {
            transactionManager.finishTransaction(transaction);
        }
    }

}
