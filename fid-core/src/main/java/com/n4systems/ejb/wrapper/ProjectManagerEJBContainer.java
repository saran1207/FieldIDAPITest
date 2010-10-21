package com.n4systems.ejb.wrapper;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.ejb.ProjectManager;
import com.n4systems.ejb.impl.ProjectManagerImpl;
import com.n4systems.exceptions.AssetAlreadyAttachedException;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.model.Asset;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Project;
import com.n4systems.model.InspectionSchedule.ScheduleStatus;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.tools.Pager;

public class ProjectManagerEJBContainer extends EJBTransactionEmulator<ProjectManager> implements ProjectManager {

	protected ProjectManager createManager(EntityManager em) {
		return new ProjectManagerImpl(em);
	}

	public int attachAsset(Asset asset, Project project, Long modifiedBy) throws AssetAlreadyAttachedException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).attachAsset(asset, project, modifiedBy);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}

	}

	public FileAttachment attachNote(FileAttachment note, Project project, Long modifiedBy) throws FileAttachmentException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).attachNote(note, project, modifiedBy);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}

	}

	public int detachAsset(Asset asset, Project project, Long modifiedBy) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).detachAsset(asset, project, modifiedBy);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}

	}

	public int detachNote(FileAttachment note, Project project, Long modifiedBy) throws FileAttachmentException {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).detachNote(note, project, modifiedBy);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}

	}

	public Pager<Asset> getAssetsPaged(Project project, SecurityFilter filter, int page, int pageSize) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getAssetsPaged(project, filter, page, pageSize);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}

	}

	public Long getCompleteSchedules(Project project, SecurityFilter filter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getCompleteSchedules(project, filter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}

	}

	public Long getIncompleteSchedules(Project project, SecurityFilter filter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getIncompleteSchedules(project, filter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}

	}

	public Pager<FileAttachment> getNotesPaged(Project project, int page, int pageSize) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getNotesPaged(project, page, pageSize);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}

	}

	public List<Project> getProjectsForAsset(Asset asset, SecurityFilter filter) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getProjectsForAsset(asset, filter);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}

	}

	

	public Pager<InspectionSchedule> getSchedulesPaged(Project project, SecurityFilter filter, int page, int pageSize, List<ScheduleStatus> statuses) {
		TransactionManager transactionManager = new FieldIdTransactionManager();
Transaction transaction = transactionManager.startTransaction();
		try {
			return createManager(transaction.getEntityManager()).getSchedulesPaged(project, filter, page, pageSize, statuses);

		} catch (RuntimeException e) {
			transactionManager.rollbackTransaction(transaction);

			throw e;
		} finally {
			transactionManager.finishTransaction(transaction);
		}

	}
}
