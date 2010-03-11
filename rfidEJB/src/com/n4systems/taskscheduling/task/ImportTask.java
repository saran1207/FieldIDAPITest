package com.n4systems.taskscheduling.task;


import java.util.UUID;

import org.apache.log4j.Logger;

import com.n4systems.exporting.Importer;
import com.n4systems.mail.MailManagerFactory;
import com.n4systems.notifiers.EmailNotifier;
import com.n4systems.notifiers.Notifier;
import com.n4systems.notifiers.notifications.ImportFailureNotification;
import com.n4systems.notifiers.notifications.ImportSuccessNotification;
import com.n4systems.persistence.FieldIdTransactionManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.TransactionManager;
import com.n4systems.util.ConfigContext;


public class ImportTask extends TransactionalTask {
	private Logger logger = Logger.getLogger(ImportTask.class);
	
	public enum Status { 
		PENDING("label.pending"), RUNNING("label.running"), SUCCESSFUL("label.successful"), FAILED("error.import_failed");
	
		String label;
		
		Status(String label) {
			this.label = label;
		}
		
		public String getLabel() {
			return this.label;
		}
	}
	
	private final String id;
	private final Notifier notifier;
	private final ImportSuccessNotification successNotification;
	private final ImportFailureNotification failureNotification;
	private Importer importer;
	private Status status = Status.PENDING;
	
	public ImportTask(TransactionManager transactionManager, Notifier notifier, Importer importer, ImportSuccessNotification successNotification, ImportFailureNotification failureNotification) {
		super(transactionManager);
		this.id = UUID.randomUUID().toString();
		this.notifier = notifier;
		this.importer = importer;
		this.successNotification = successNotification;
		this.failureNotification = failureNotification;
	}
	
	public ImportTask(Importer importer, ImportSuccessNotification successNotification, ImportFailureNotification failueNotification) {
		this(new FieldIdTransactionManager(), new EmailNotifier(MailManagerFactory.defaultMailManager(ConfigContext.getCurrentContext())), importer, successNotification, failueNotification);
	}

	@Override
	protected void run(Transaction transaction) throws Exception {
		setStatus(Status.RUNNING);
		
		int totalImported = importer.runImport(transaction);
		
		setStatus(Status.SUCCESSFUL);
		successNotification.setTotalImported(totalImported);
		notifier.notify(successNotification);
	}
	
	@Override
	protected void onException(Exception e) {
		logger.error("Failed import", e);
		
		setStatus(Status.FAILED);
		notifier.notify(failureNotification);
	}

	public String getId() {
		return id;
	}

	private synchronized void setStatus(Status status) {
		this.status = status;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public boolean isCompleted() {
		return (status == Status.SUCCESSFUL || status == Status.FAILED);
	}
	
	public int getCurrentRow() {
		return importer.getCurrentRow();
	}
	
	public int getTotalRows() {
		return importer.getTotalRows();
	}

}
