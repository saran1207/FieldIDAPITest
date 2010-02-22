package com.n4systems.taskscheduling.task;


import java.util.UUID;

import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;

import com.n4systems.exporting.Importer;
import com.n4systems.notifiers.EmailNotifier;
import com.n4systems.notifiers.Notifier;
import com.n4systems.notifiers.notifications.CustomerImportFailureNotification;
import com.n4systems.notifiers.notifications.CustomerImportSuccessNotification;
import com.n4systems.notifiers.notifications.Notification;


public class CustomerImportTask implements Runnable {
	private static final Notification failureNotification = new CustomerImportFailureNotification();
	private Logger logger = Logger.getLogger(CustomerImportTask.class);
	
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
	private Status status = Status.PENDING;
	private Importer importer;
	private final Notifier notifier;
	private final UserBean user;
	
	public CustomerImportTask(Importer importer, UserBean user, Notifier notifier) {
		this.id = UUID.randomUUID().toString();
		this.importer = importer;
		this.user = user;
		this.notifier = notifier;
	}
	
	public CustomerImportTask(Importer importer, UserBean user) {
		this(importer, user, new EmailNotifier());
	}
	
	@Override
	public void run() {
		setStatus(Status.RUNNING);
		try {
			int totalImported = importer.runImport();
			
			setStatus(Status.SUCCESSFUL);
			notifier.notify(createSuccessNotification(totalImported));
			
		} catch (Exception e) {
			logger.error("Failed import", e);
			
			setStatus(Status.FAILED);
			notifier.notify(failureNotification);
		}
	}
	
	private Notification createSuccessNotification(int totalImported) {
		Notification note = new CustomerImportSuccessNotification(totalImported);
		note.notifiyUser(user);
		return note;
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
