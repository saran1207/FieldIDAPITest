package com.n4systems.ejb.wrapper;

import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.impl.EventManagerImpl;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.SubEvent;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.security.Log4JAuditLogger;
import com.n4systems.security.UpdateEventAuditHandler;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.tools.Pager;
import com.n4systems.webservice.dto.WSSearchCritiera;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

public class EventManagerEJBContainer extends EJBTransactionEmulator<EventManager> implements EventManager {

	protected EventManager createManager(EntityManager em) {
		return new EventManagerImpl(em);
	}

	public Event attachFilesToSubEvent(Event event, SubEvent subEvent, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).attachFilesToSubEvent(event, subEvent, uploadedFiles);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

    public Date findLastEventDate(Long eventId) {
        TransactionManager transactionManager = new FieldIdTransactionManager();
        Transaction transaction = transactionManager.startTransaction();
        try {
            return createManager(transaction.getEntityManager()).findLastEventDate(eventId);

        } catch (RuntimeException e) {
            transactionManager.rollbackTransaction(transaction);

            throw e;
        } finally {
            transactionManager.finishTransaction(transaction);
        }
    }

    public Event findAllFields(Long id, SecurityFilter filter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findAllFields(id, filter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}


    public List<Event> findEventsByDateAndAsset(Date datePerformedRangeStart, Date datePerformedRangeEnd, Asset asset, SecurityFilter filter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findEventsByDateAndAsset(datePerformedRangeStart, datePerformedRangeEnd, asset, filter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Event findEventThroughSubEvent(Long subEventId, SecurityFilter filter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findEventThroughSubEvent(subEventId, filter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}


	public Pager<Event> findNewestEvents(WSSearchCritiera searchCriteria, SecurityFilter securityFilter, int page, int pageSize) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findNewestEvents(searchCriteria, securityFilter, page, pageSize);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public SubEvent findSubEvent(Long subEventId, SecurityFilter filter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).findSubEvent(subEventId, filter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public boolean isMasterEvent(Long id) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).isMasterEvent(id);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Event retireEvent(Event event, Long userId) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).retireEvent(event, userId);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}
	}

	public Event updateEvent(Event event, Long scheduleId, Long userId, FileDataContainer fileData, List<FileAttachment> uploadedFiles) throws ProcessingProofTestException, FileAttachmentException {
		Log4JAuditLogger auditLogger = new Log4JAuditLogger(new UpdateEventAuditHandler());

		Throwable t = null;
		TransactionManager transactionManager = new FieldIdTransactionManager();
		Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).updateEvent(event, scheduleId, userId, fileData, uploadedFiles);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);
			t = e;
			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
			auditLogger.audit("updateEvent", event, t);
		}
	}

}
